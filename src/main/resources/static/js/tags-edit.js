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
    const warningEl = document.getElementById('tag-warning');
    if (warningEl) {
        warningEl.textContent = tags.length >= maxTags ? 'Максимум 3 тега' : '';
    }
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