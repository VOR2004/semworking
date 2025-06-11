let selectedFiles = [];
let mainImageIndex = 0;

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
        reader.onerror = function(error) {
            console.error("Ошибка при чтении файла:", file.name, error);
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
            console.log("Главное изображение установлено на индекс:", i);
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
            console.log("Изображение удалено, новый индекс главного изображения:", mainImageIndex);
            renderPreviews();
        });

        wrapper.appendChild(img);
        wrapper.appendChild(mainBtn);
        wrapper.appendChild(removeBtn);
        previews.appendChild(wrapper);
    });
}

imageInput.addEventListener('change', function() {
    const files = Array.from(this.files);
    files.forEach(file => {
        if (selectedFiles.some(f => f.name === file.name && f.size === file.size)) {
            console.log("Дубликат файла пропущен:", file.name);
            return;
        }
        selectedFiles.push(file);
        console.log("Файл добавлен:", file.name);
    });
    renderPreviews();
    this.value = '';
});

form.addEventListener('submit', function(e) {
    e.preventDefault();
    console.log("Отправка формы...");

    const formData = new FormData(form);
    formData.delete('images');
    selectedFiles.forEach(file => formData.append('images', file));
    formData.set('mainImageIndex', mainImageIndex);

    console.log("Данные формы перед отправкой:", [...formData.entries()]);

    fetch(form.action, {
        method: 'POST',
        body: formData
    })
        .then(response => {
            console.log("Ответ сервера:", response);
            if (response.redirected) {
                console.log("Перенаправление на:", response.url);
                window.location.href = response.url;
            } else {
                console.warn("Обработка ошибок отсутствует или произошла ошибка");
            }
        })
        .catch(error => {
            console.error("Ошибка при отправке формы:", error);
        });
});
