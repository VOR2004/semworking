<!DOCTYPE html>
<html lang="ru">
<head>
    <base href="/"/>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/add.css"/>
    <title>Добавить товар</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Audiowide&family=Coral+Pixels&family=DotGothic16&family=Doto:wght,ROND@100..900,100&family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&family=Micro+5&family=Orbitron:wght@400..900&family=Press+Start+2P&family=Rampart+One&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&family=Rubik:ital,wght@0,300..900;1,300..900&family=Silkscreen:wght@400;700&display=swap" rel="stylesheet">
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
