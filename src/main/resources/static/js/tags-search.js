const tagSearch = document.getElementById('tag-search');
const tagDropdown = document.getElementById('tag-dropdown');
const selectedTagsDiv = document.getElementById('selected-tags');
const searchForm = document.getElementById('search-form');

function renderDropdown(filter = "") {
    tagDropdown.innerHTML = "";
    const filtered = allTags.filter(tag =>
        tag.toLowerCase().includes(filter.toLowerCase()) && !selectedTags.includes(tag)
    );

    filtered.forEach(tag => {
        const div = document.createElement('div');
        div.className = 'tag-option';
        div.dataset.tag = tag;
        div.textContent = tag;
        div.onclick = () => {
            if (selectedTags.length < 3) {
                selectedTags.push(tag);
                renderSelectedTags();
                tagDropdown.style.display = "none";
                tagSearch.value = "";
            } else {
                alert("Можно выбрать не более 3 тегов");
            }
        };
        tagDropdown.appendChild(div);
    });

    tagDropdown.style.display = filtered.length > 0 ? "block" : "none";
}

function renderSelectedTags() {
    selectedTagsDiv.innerHTML = "";
    selectedTags.forEach(function(tag) {
        const span = document.createElement('span');
        span.className = "selected-tag";
        span.dataset.tag = tag;
        span.innerHTML = tag + ' <span class="remove-tag" title="Убрать тег">×</span>';
        span.querySelector(".remove-tag").onclick = function() {
            selectedTags = selectedTags.filter(t => t !== tag);
            renderSelectedTags();
        };
        selectedTagsDiv.appendChild(span);

        const input = document.createElement('input');
        input.type = "hidden";
        input.name = "tags";
        input.value = tag;
        selectedTagsDiv.appendChild(input);
    });
}

// Инициализация
tagSearch.addEventListener('focus', () => renderDropdown(tagSearch.value));
tagSearch.addEventListener('input', () => renderDropdown(tagSearch.value));
document.addEventListener('click', (e) => {
    if (!tagSearch.contains(e.target) && !tagDropdown.contains(e.target)) {
        tagDropdown.style.display = "none";
    }
});

renderSelectedTags();
