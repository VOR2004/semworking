<#macro editform>
<form method="post" action="/product/edit/${productId}" id="product-form" autocomplete="off" enctype="multipart/form-data" novalidate>
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
    <div id="addressError" class="error"></div>

    <input type="hidden" id="lat" name="lat" value="${form.lat}">
    <input type="hidden" id="lon" name="lon" value="${form.lon}">
    <div id="latError" class="error"></div>
    <div id="lonError" class="error"></div>

    <label>Выберите место на карте:</label>
    <div id="map" style="width: 600px; height: 400px; margin-bottom: 20px;"></div>

    <#if existingImages?? && (existingImages?size > 0)>
        <div id="existing-images">
            <strong>Текущие изображения:</strong><br>
            <#list existingImages as url>
                <label style="display:inline-block; margin:5px; position:relative;">
                    <img src="${url}" alt="Фото товара" style="max-width:150px; border:1px solid #ccc; padding:3px;">
                    <br>
                    <input type="checkbox" name="existingImageUrls" value="${url}" checked> Оставить
                    <span class="star" title="Сделать главным">★</span>
                </label>
            </#list>
        </div>
    </#if>

    <input type="file" id="image-input" name="images" multiple accept="image/*" style="display:none;">
    <button type="button" id="upload-btn">Добавить фото</button>
    <input type="hidden" name="mainImageIndex" id="mainImageIndex" value="0">
    <div id="image-previews"></div>
    <div id="imagesError" class="error"></div>
    <button type="submit">Сохранить</button>
    <div id="formError" class="error"></div>
</form>
</#macro>