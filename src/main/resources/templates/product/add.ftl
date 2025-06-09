<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Добавить товар</title>
    <style>
        #image-previews {
            min-height: 50px;
            border: 1px dashed #ccc;
            padding: 10px;
            border-radius: 5px;
        }
        .tag-chip {
            display: inline-block;
            background: #e0e0e0;
            border-radius: 10px;
            padding: 3px 10px;
            margin: 2px;
        }
        .tag-chip button {
            background: none;
            border: none;
            color: #c00;
            font-weight: bold;
            margin-left: 5px;
            cursor: pointer;
        }
        .autocomplete-list {
            position: absolute;
            background: #fff;
            border: 1px solid #ccc;
            z-index: 100;
            max-height: 120px;
            overflow-y: auto;
            width: 200px;
        }
        .autocomplete-item {
            padding: 5px 10px;
            cursor: pointer;
        }
        .autocomplete-item:hover {
            background: #f0f0f0;
        }
    </style>
    <script src="https://api-maps.yandex.ru/2.1/?lang=ru_RU" type="text/javascript"></script>
</head>
<body>
<h2>Добавить товар</h2>
<form method="post" action="/product/add" id="product-form" autocomplete="off" enctype="multipart/form-data">
    <label>Название:
        <input type="text" name="title" value="${form.title!}">
    </label><br>
    <label>Описание:
        <textarea name="description">${form.description!}</textarea>
    </label><br>
    <label>Цена:
        <input type="number" step="0.01" name="price" value="${form.price!}">
    </label><br>
    <label>Теги:</label>
    <div id="tags-list"></div>
    <div style="position: relative; display: inline-block;">
        <label for="tag-input"></label><input type="text" id="tag-input" placeholder="Введите тег" autocomplete="off">
        <div id="autocomplete-list" class="autocomplete-list" style="display: none;"></div>
    </div>
    <button type="button" id="add-tag-btn">Добавить тег</button>
    <div id="tag-warning" style="color: red"></div>

    <label for="address">Адрес:</label>
    <input type="text" id="address" name="address" autocomplete="off">
    <div id="address-suggestions" style="border:1px solid #ccc; display:none;"></div>
    <input type="hidden" id="lat" name="lat">
    <input type="hidden" id="lon" name="lon">

    <label>Выберите место на карте:</label>
    <div id="map" style="width: 600px; height: 400px; margin-bottom: 20px;"></div>

    <form method="post" action="/product/add" id="product-form" enctype="multipart/form-data">
        <input type="file" id="image-input" name="images" multiple accept="image/*" style="display:none;">
        <button type="button" id="upload-btn">Добавить фото</button>
        <input type="hidden" name="mainImageIndex" id="mainImageIndex" value="0">
        <div id="image-previews"></div>
        <button type="submit">Сохранить</button>
    </form>

</form>

<script>
    let selectedFiles = [];
    let mainImageIndex = 0; // по умолчанию главная — первая

    const imageInput = document.getElementById('image-input');
    const uploadBtn = document.getElementById('upload-btn');
    const previews = document.getElementById('image-previews');
    const form = document.getElementById('product-form');
    const mainImageIndexInput = document.getElementById('mainImageIndex');

    uploadBtn.addEventListener('click', () => imageInput.click());

    function renderPreviews() {
        previews.innerHTML = '';
        selectedFiles.forEach((file, i) => {
            const wrapper = document.createElement('div');
            wrapper.style.display = 'inline-block';
            wrapper.style.position = 'relative';
            wrapper.style.margin = '5px';

            const img = document.createElement('img');
            img.style.width = '120px';
            img.style.height = '120px';
            img.style.objectFit = 'cover';
            img.style.display = 'block';

            const reader = new FileReader();
            reader.onload = function(e) {
                img.src = e.target.result;
            };
            reader.readAsDataURL(file);

            // Кнопка "Сделать главной"
            const mainBtn = document.createElement('button');
            mainBtn.type = 'button';
            mainBtn.innerHTML = '★';
            mainBtn.title = 'Сделать главной';
            mainBtn.style.position = 'absolute';
            mainBtn.style.bottom = '0';
            mainBtn.style.left = '0';
            mainBtn.style.background = i === mainImageIndex ? 'gold' : 'rgba(255,255,255,0.7)';
            mainBtn.style.border = 'none';
            mainBtn.style.fontSize = '18px';
            mainBtn.style.cursor = 'pointer';
            mainBtn.style.width = '28px';
            mainBtn.style.height = '28px';
            mainBtn.style.borderRadius = '50%';
            mainBtn.style.opacity = i === mainImageIndex ? '1' : '0.6';

            mainBtn.addEventListener('click', function() {
                mainImageIndex = i;
                mainImageIndexInput.value = i;
                renderPreviews();
            });

            // Кнопка "Удалить"
            const removeBtn = document.createElement('button');
            removeBtn.type = 'button';
            removeBtn.textContent = '×';
            removeBtn.title = 'Убрать фото';
            removeBtn.style.position = 'absolute';
            removeBtn.style.top = '0';
            removeBtn.style.right = '0';
            removeBtn.style.background = 'rgba(255,255,255,0.7)';
            removeBtn.style.border = 'none';
            removeBtn.style.fontSize = '18px';
            removeBtn.style.cursor = 'pointer';
            removeBtn.style.width = '28px';
            removeBtn.style.height = '28px';
            removeBtn.style.borderRadius = '50%';

            removeBtn.addEventListener('click', function() {
                selectedFiles.splice(i, 1);
                // Если удалили главную — сбросить на первую, иначе скорректировать индекс
                if (mainImageIndex === i) {
                    mainImageIndex = 0;
                } else if (mainImageIndex > i) {
                    mainImageIndex--;
                }
                mainImageIndexInput.value = mainImageIndex;
                renderPreviews();
            });

            wrapper.appendChild(img);
            wrapper.appendChild(mainBtn);
            wrapper.appendChild(removeBtn);
            previews.appendChild(wrapper);
        });
    }

    imageInput.addEventListener('change', function() {
        Array.from(this.files).forEach(file => {
            // Не добавлять дубликаты по имени и размеру
            if (selectedFiles.some(f => f.name === file.name && f.size === file.size)) return;
            selectedFiles.push(file);
        });
        renderPreviews();
        this.value = '';
    });

    form.addEventListener('submit', function(e) {
        e.preventDefault();

        const formData = new FormData(form);
        formData.delete('images');
        selectedFiles.forEach(file => formData.append('images', file));
        formData.set('mainImageIndex', mainImageIndex);

        fetch(form.action, {
            method: 'POST',
            body: formData
        }).then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            } else {
                // обработка ошибок, если нужно
            }
        });
    });


    ymaps.ready(function () {
        const latInput = document.getElementById('lat');
        const lonInput = document.getElementById('lon');
        const addressInput = document.getElementById('address');
        const suggestionsContainer = document.getElementById('address-suggestions');

        const defaultCoords = [55.76, 37.64];

        const map = new ymaps.Map('map', {
            center: defaultCoords,
            zoom: 10
        });

        let placemark;

        // Создаём или перемещаем метку
        function setPlacemark(coords) {
            if (!placemark) {
                placemark = new ymaps.Placemark(coords, {}, { draggable: true });
                map.geoObjects.add(placemark);

                // При перетаскивании метки обновляем адрес и координаты
                placemark.events.add('dragend', function (e) {
                    const newCoords = e.get('target').geometry.getCoordinates();
                    updateAddressAndCoords(newCoords);
                });
            } else {
                placemark.geometry.setCoordinates(coords);
            }
            updateAddressAndCoords(coords);
            map.setCenter(coords, 15);
        }

        // Обратное геокодирование: по координатам обновляем адрес и скрытые поля
        function updateAddressAndCoords(coords) {
            latInput.value = coords[0].toFixed(6);
            lonInput.value = coords[1].toFixed(6);

            ymaps.geocode(coords).then(function (res) {
                if (res.geoObjects.getLength()) {
                    const firstGeoObject = res.geoObjects.get(0);
                    addressInput.value = firstGeoObject.getAddressLine();
                } else {
                    // Если геокодирование не дало результатов, можно установить координаты как адрес
                    addressInput.value = coords[0].toFixed(6) + ', ' + coords[1].toFixed(6);
                }
            }).catch(function (err) {
                console.error('Ошибка геокодирования:', err);
                // В случае ошибки также устанавливаем координаты как адрес
                addressInput.value = coords[0].toFixed(6) + ', ' + coords[1].toFixed(6);
            });
        }

        // Обработчик клика по карте — ставим метку и обновляем поля
        map.events.add('click', function (e) {
            const coords = e.get('coords');
            setPlacemark(coords);
        });

        // Если в форме уже есть координаты — ставим метку и центрируем карту
        if (latInput.value && lonInput.value) {
            const savedCoords = [parseFloat(latInput.value), parseFloat(lonInput.value)];
            setPlacemark(savedCoords);
        }

        // --- Работа с подсказками DaData ---

        addressInput.addEventListener('input', function () {
            const query = this.value;
            if (query.length < 3) {
                suggestionsContainer.style.display = 'none';
                return;
            }
            fetch('/api/dadata/suggest?query=' + encodeURIComponent(query))
                .then(r => r.json())
                .then(data => {
                    const suggestions = data.suggestions || [];
                    suggestionsContainer.innerHTML = '';
                    suggestions.forEach(item => {
                        const div = document.createElement('div');
                        div.textContent = item.value;
                        div.style.cursor = 'pointer';
                        div.onclick = () => {
                            // При выборе подсказки обновляем адрес, координаты и карту
                            addressInput.value = item.value;
                            latInput.value = item.data.geo_lat || '';
                            lonInput.value = item.data.geo_lon || '';
                            suggestionsContainer.style.display = 'none';

                            if (latInput.value && lonInput.value) {
                                const coords = [parseFloat(latInput.value), parseFloat(lonInput.value)];
                                setPlacemark(coords);
                            }
                        };
                        suggestionsContainer.appendChild(div);
                    });
                    suggestionsContainer.style.display = suggestions.length ? 'block' : 'none';
                });
        });

        // Скрываем подсказки при клике вне поля и списка
        document.addEventListener('click', function (e) {
            if (!e.target.closest('#address-suggestions') && e.target !== addressInput) {
                suggestionsContainer.style.display = 'none';
            }
        });
    });

    let tags = [];
    const maxTags = 3;

    function updateTagsList() {
        const tagsList = document.getElementById('tags-list');
        tagsList.innerHTML = '';
        tags.forEach((tag, idx) => {
            const chip = document.createElement('span');
            chip.className = 'tag-chip';
            chip.textContent = tag;
            const btn = document.createElement('button');
            btn.type = 'button';
            btn.textContent = '×';
            btn.onclick = () => {
                tags.splice(idx, 1);
                updateTagsList();
            };
            chip.appendChild(btn);

            const hidden = document.createElement('input');
            hidden.type = 'hidden';
            hidden.name = 'tagNames';
            hidden.value = tag;

            tagsList.appendChild(chip);
            tagsList.appendChild(hidden);
        });
        document.getElementById('tag-warning').textContent = tags.length >= maxTags ? 'Максимум 3 тега' : '';
        document.getElementById('tag-input').disabled = tags.length >= maxTags;
        document.getElementById('add-tag-btn').disabled = tags.length >= maxTags;
    }

    function addTag(tag) {
        tag = tag.trim();
        if (tag && !tags.includes(tag) && tags.length < maxTags) {
            tags.push(tag);
            updateTagsList();
            document.getElementById('tag-input').value = '';
            hideAutocomplete();
        }
    }

    document.getElementById('add-tag-btn').onclick = function() {
        const input = document.getElementById('tag-input');
        addTag(input.value);
    };

    document.getElementById('tag-input').addEventListener('keydown', function(e) {
        const autocompleteList = document.getElementById('autocomplete-list');
        if (e.key === 'Enter') {
            e.preventDefault();
            // Если подсказки видны и выбрана подсказка
            const active = autocompleteList.querySelector('.autocomplete-item.active');
            if (autocompleteList.style.display !== 'none' && active) {
                addTag(active.textContent);
            } else {
                addTag(this.value);
            }
        } else if (e.key === 'ArrowDown' || e.key === 'ArrowUp') {
            let items = autocompleteList.querySelectorAll('.autocomplete-item');
            if (items.length === 0) return;
            let idx = Array.from(items).findIndex(i => i.classList.contains('active'));
            if (e.key === 'ArrowDown') {
                if (idx >= 0) items[idx].classList.remove('active');
                idx = (idx + 1) % items.length;
                items[idx].classList.add('active');
            } else if (e.key === 'ArrowUp') {
                if (idx >= 0) items[idx].classList.remove('active');
                idx = (idx - 1 + items.length) % items.length;
                items[idx].classList.add('active');
            }
        }
    });

    function showAutocomplete(suggestions) {
        const autocompleteList = document.getElementById('autocomplete-list');
        autocompleteList.innerHTML = '';
        if (suggestions.length === 0) {
            autocompleteList.style.display = 'none';
            return;
        }
        suggestions.forEach(tag => {
            const item = document.createElement('div');
            item.className = 'autocomplete-item';
            item.textContent = tag;
            item.onclick = () => addTag(tag);
            autocompleteList.appendChild(item);
        });
        autocompleteList.style.display = 'block';
        const items = autocompleteList.querySelectorAll('.autocomplete-item');
        if (items.length) items[0].classList.add('active');
    }

    function hideAutocomplete() {
        document.getElementById('autocomplete-list').style.display = 'none';
    }

    document.getElementById('tag-input').addEventListener('input', function() {
        let query = this.value.trim();
        if (query.length > 1) {
            fetch('/tags/search?query=' + encodeURIComponent(query))
                .then(r => r.json())
                .then(tagsArr => {
                    const filtered = tagsArr.filter(t => !tags.includes(t));
                    showAutocomplete(filtered);
                });
        } else {
            hideAutocomplete();
        }
    });

    document.addEventListener('click', function(e) {
        if (!document.getElementById('autocomplete-list').contains(e.target) &&
            e.target !== document.getElementById('tag-input')) {
            hideAutocomplete();
        }
    });

    updateTagsList();
</script>
</body>
</html>
