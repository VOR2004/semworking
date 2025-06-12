<!DOCTYPE html>
<html lang="ru">
<#import "editforms.ftl" as forms/>
<head>
    <base href="/"/>
    <link rel="stylesheet" href="css/edit.css"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Audiowide&family=Coral+Pixels&family=DotGothic16&family=Doto:wght,ROND@100..900,100&family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&family=Micro+5&family=Orbitron:wght@400..900&family=Press+Start+2P&family=Rampart+One&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&family=Rubik:ital,wght@0,300..900;1,300..900&family=Silkscreen:wght@400;700&display=swap" rel="stylesheet">
    <meta charset="UTF-8">
    <title>Обновить товар</title>
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
        .error {
            color: red !important;
            font-size: 0.9em;
            margin-top: 2px;
            margin-bottom: 10px;
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
        .star {
            position: absolute;
            top: 5px;
            left: 5px;
            font-size: 20px;
            color: rgb(128, 128, 128);
            cursor: pointer;
            user-select: none;
            z-index: 10; /* чтобы была поверх других элементов */
        }
        .star.active {
            color: gold;
        }

        #existing-images label img {
            pointer-events: none;
        }

    </style>
    <script src="https://api-maps.yandex.ru/2.1/?lang=ru_RU" type="text/javascript"></script>
</head>
<body>
<a href="/">Главная</a>
<h2>Обновить товар</h2>
<@forms.editform/>
<script src="js/form-edit-handle.js"></script>
<script src="js/image-edit.js"></script>
<script src="js/ymaps-edit.js"></script>
<script src="js/tags-edit.js"></script>
</body>
</html>
