<#macro searchform>
    <form method="get" action="/" id="search-form" autocomplete="off">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
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
</#macro>


<#macro productform>
    <form id="product-form" autocomplete="off" enctype="multipart/form-data" novalidate>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <label>Название:
            <input type="text" name="title" value="${form.title!}">
            <div id="titleError" class="error"></div>
        </label><br>

        <label>Описание:
            <textarea name="description">${form.description!}</textarea>
            <div id="descriptionError" class="error"></div>
        </label><br>

        <label>Цена:
            <input type="number" step="0.01" name="price" value="${form.price!}">
            <div id="priceError" class="error"></div>
        </label><br>

        <label>Теги:</label>
        <div id="tags-list"></div>
        <div style="position: relative; display: inline-block;">
            <label for="tag-input"></label><input type="text" id="tag-input" placeholder="Введите тег" autocomplete="off">
            <div id="autocomplete-list" class="autocomplete-list" style="display: none;"></div>
        </div>
        <button type="button" id="add-tag-btn">Добавить тег</button>
        <div id="tagNamesError" class="error"></div>

        <label for="address">Адрес:</label>
        <input type="text" id="address" name="address" autocomplete="off">
        <div id="address-suggestions" style="border:1px solid #ccc; display:none;"></div>
        <div id="addressError" class="error"></div>

        <input type="hidden" id="lat" name="lat">
        <input type="hidden" id="lon" name="lon">
        <div id="latError" class="error"></div>
        <div id="lonError" class="error"></div>

        <label>Выберите место на карте:</label>
        <div id="map" style="width: 600px; height: 400px; margin-bottom: 20px;"></div>

        <input type="file" id="image-input" name="images" multiple accept="image/*" style="display:none;">
        <button type="button" id="upload-btn">Добавить фото</button>
        <input type="hidden" name="mainImageIndex" id="mainImageIndex" value="0">
        <div id="image-previews"></div>
        <div id="imagesError" class="error"></div>

        <button type="submit">Сохранить</button>
        <div id="formError" class="error"></div>
    </form>
</#macro>