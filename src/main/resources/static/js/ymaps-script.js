function initProductMap() {
    var productCoords = window.productCoords || [0, 0];
    var productTitle = window.productTitle || '';
    var map = new ymaps.Map('map', {
        center: productCoords,
        zoom: 14,
        controls: ['zoomControl', 'fullscreenControl']
    });

    var placemark = new ymaps.Placemark(productCoords, {
        hintContent: productTitle,
        balloonContent: productTitle
    });

    map.geoObjects.add(placemark);

    var resetBtn = document.getElementById('reset-center');
    if (resetBtn) {
        resetBtn.addEventListener('click', function() {
            map.setCenter(productCoords, 14, { duration: 500 });
        });
    }
}

if (window.ymaps) {
    ymaps.ready(initProductMap);
}