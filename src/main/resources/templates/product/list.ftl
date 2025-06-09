<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Все товары</title>
    <style>
        .tag-search-container { position: relative; display: inline-block; }
        #tag-dropdown {
            display: none;
            position: absolute;
            background: #fff;
            border: 1px solid #ccc;
            max-height: 120px;
            overflow-y: auto;
            z-index: 1000;
            width: 200px;
        }
        .selected-tag {
            display: inline-block;
            background: #e0e0e0;
            border-radius: 12px;
            padding: 2px 8px;
            margin: 2px 4px 2px 0;
            font-size: 14px;
        }
        .remove-tag {
            color: #b00;
            margin-left: 6px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<h2>Все товары</h2>
<nav>
    <a href="/">Главная</a> |
    <a href="/chats">Мои чаты</a> |
    <a href="/account">Мой аккаунт</a>
</nav>
<a href="/product/add">Добавить товар</a><br><br>

<form method="get" action="/" id="search-form" autocomplete="off">
    <label>
        <input type="text" name="q" placeholder="Поиск по товарам..." value="${q!}" style="width: 250px;">
    </label>

    <div style="margin-top:10px;">
        <label>Поиск по тегам:</label>
        <div class="tag-search-container">
            <label for="tag-search"></label><input type="text" id="tag-search" placeholder="Начните вводить тег..." style="width: 200px;">
            <div id="tag-dropdown"></div>
        </div>
        <div id="selected-tags" style="margin:5px 0;">
            <#if selectedTags??>
                <#list selectedTags as tag>
                    <span class="selected-tag" data-tag="${tag}">${tag} <span class="remove-tag" title="Убрать тег">×</span></span>
                    <input type="hidden" name="tags" value="${tag}">
                </#list>
            </#if>
        </div>
    </div>
    <button type="submit">Найти</button>
</form>

<#list products as p>
    <div style="margin-bottom: 15px;">
        <#if p.mainImageUrl?? && (p.mainImageUrl?length > 0)>
            <img src="${p.mainImageUrl}" alt="Главное фото" style="max-width:120px;max-height:120px;margin:5px 0;">
        </#if>
        <br>
        <strong>${p.title}</strong><br>
        ${p.description}<br>
        Цена: ${p.price} руб.<br>
        Размещено: ${p.createdAt}<br>
        Пользователь: ${p.userEntity.username}<br>
        <#if p.tags?? && (p.tags?size > 0)>
            Теги:
            <#list p.tags as tag>
                <a href="/?tags=${tag.name}<#if q??>&q=${q}</#if>
                    <#if selectedTags??>
                        <#list selectedTags as t>
                            <#if t != tag.name>&tags=${t}</#if>
                        </#list>
                    </#if>
                ">${tag.name}</a><#if tag_has_next>, </#if>
            </#list>
            <br>
        </#if>
        <a href="/product/${p.id}">Открыть</a>
    </div>
</#list>

<#if totalPages?? && (totalPages > 1)>
    <div style="margin-top: 20px;">
        <#list 0..(totalPages-1) as i>
            <#if i == currentPage>
                <b>${i+1}</b>
            <#else>
                <a href="/?page=${i}&size=10<#if q??>&q=${q}</#if>
                <#if selectedTags??>
                    <#list selectedTags as t>&tags=${t}</#list>
                </#if>
                ">${i+1}</a>
            </#if>
        </#list>
    </div>
</#if>

<script>
    const allTags = [
        <#list allTags as tag>
        "${tag.name}"<#if tag_has_next>,</#if>
        </#list>
    ];
    let selectedTags = [
        <#if selectedTags??>
        <#list selectedTags as tag>
        "${tag}"<#if tag_has_next>,</#if>
        </#list>
        </#if>
    ];

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


    tagSearch.addEventListener('focus', () => renderDropdown(tagSearch.value));
    tagSearch.addEventListener('input', () => renderDropdown(tagSearch.value));
    document.addEventListener('click', (e) => {
        if (!tagSearch.contains(e.target) && !tagDropdown.contains(e.target)) {
            tagDropdown.style.display = "none";
        }
    });
    renderSelectedTags();
</script>

</body>
</html>
