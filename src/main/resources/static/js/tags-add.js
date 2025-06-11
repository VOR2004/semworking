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
            console.log(`Тег удалён: ${tag}`);
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
    const warningEl = document.getElementById('tag-warning');
    const tagInput = document.getElementById('tag-input');
    const addBtn = document.getElementById('add-tag-btn');

    if (tags.length >= maxTags) {
        warningEl.textContent = 'Максимум 3 тега';
        tagInput.disabled = true;
        addBtn.disabled = true;
        console.warn('Достигнуто максимальное количество тегов');
    } else {
        if (warningEl) {
            warningEl.textContent = tags.length >= maxTags ? 'Максимум 3 тега' : '';
        }
        tagInput.disabled = false;
        addBtn.disabled = false;
    }
}

function addTag(tag) {
    tag = tag.trim();
    if (!tag) {
        console.warn('Пустой тег не добавлен');
        return;
    }
    if (tags.includes(tag)) {
        console.warn(`Тег "${tag}" уже добавлен`);
        return;
    }
    if (tags.length >= maxTags) {
        console.warn('Превышен лимит тегов');
        return;
    }
    tags.push(tag);
    console.log(`Тег добавлен: ${tag}`);
    updateTagsList();
    document.getElementById('tag-input').value = '';
    hideAutocomplete();
}

document.getElementById('add-tag-btn').onclick = function() {
    const input = document.getElementById('tag-input');
    addTag(input.value);
};

document.getElementById('tag-input').addEventListener('keydown', function(e) {
    const autocompleteList = document.getElementById('autocomplete-list');
    if (e.key === 'Enter') {
        e.preventDefault();
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
        console.log('Подсказки не найдены');
        return;
    }
    suggestions.forEach(tag => {
        const item = document.createElement('div');
        item.className = 'autocomplete-item';
        item.textContent = tag;
        item.onclick = () => {
            addTag(tag);
        };
        autocompleteList.appendChild(item);
    });
    autocompleteList.style.display = 'block';
    const items = autocompleteList.querySelectorAll('.autocomplete-item');
    if (items.length) items[0].classList.add('active');
    console.log(`Показано ${suggestions.length} подсказок`);
}

function hideAutocomplete() {
    document.getElementById('autocomplete-list').style.display = 'none';
    console.log('Подсказки скрыты');
}

document.getElementById('tag-input').addEventListener('input', function() {
    let query = this.value.trim();
    if (query.length > 1) {
        fetch('/tags/search?query=' + encodeURIComponent(query))
            .then(r => {
                if (!r.ok) {
                    throw new Error(`Ошибка сети: ${r.status}`);
                }
                return r.json();
            })
            .then(tagsArr => {
                const filtered = tagsArr.filter(t => !tags.includes(t));
                showAutocomplete(filtered);
            })
            .catch(error => {
                console.error('Ошибка при поиске тегов:', error);
                hideAutocomplete();
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
