<!DOCTYPE html>
<html lang="ru">
<head>
    <base href="/"/>
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
        .error {
            color: red;
            font-size: 0.9em;
            margin-top: 2px;
            margin-bottom: 10px;
        }
    </style>
    <script src="https://api-maps.yandex.ru/2.1/?lang=ru_RU" type="text/javascript"></script>
</head>
<body>
<a href="/">Главная</a>
<h2>Добавить товар</h2>

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

<script src="js/image-add.js"></script>
<script src="js/ymaps-script-big.js"></script>
<script src="js/tags-add.js"></script>
<script src="js/add-script-big.js"></script>
</body>
</html>
