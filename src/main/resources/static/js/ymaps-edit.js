ymaps.ready(function () {
    const latInput = document.getElementById('lat');
    const lonInput = document.getElementById('lon');
    const addressInput = document.getElementById('address');
    const suggestionsContainer = document.getElementById('address-suggestions');

    const defaultCoords = [55.76, 37.64];

    const map = new ymaps.Map('map', {
        center: defaultCoords,
        zoom: 10
    });
    let placemark;
    function setPlacemark(coords) {
        if (!placemark) {
            placemark = new ymaps.Placemark(coords, {}, { draggable: true });
            map.geoObjects.add(placemark);

            placemark.events.add('dragend', function (e) {
                const newCoords = e.get('target').geometry.getCoordinates();
                updateAddressAndCoords(newCoords);
            });
        } else {
            placemark.geometry.setCoordinates(coords);
        }
        updateAddressAndCoords(coords);
        map.setCenter(coords, 15);
    }

    function updateAddressAndCoords(coords) {
        latInput.value = coords[0].toFixed(6);
        lonInput.value = coords[1].toFixed(6);

        ymaps.geocode(coords).then(function (res) {
            if (res.geoObjects.getLength()) {
                const firstGeoObject = res.geoObjects.get(0);
                addressInput.value = firstGeoObject.getAddressLine();
            } else {
                addressInput.value = coords[0].toFixed(6) + ', ' + coords[1].toFixed(6);
            }
        }).catch(function (err) {
            addressInput.value = coords[0].toFixed(6) + ', ' + coords[1].toFixed(6);
        });
    }
    map.events.add('click', function (e) {
        const coords = e.get('coords');
        setPlacemark(coords);
    });

    if (latInput.value && lonInput.value) {
        const savedCoords = [parseFloat(latInput.value), parseFloat(lonInput.value)];
        setPlacemark(savedCoords);
    }

    addressInput.addEventListener('input', function () {
        const query = this.value;
        if (query.length < 3) {
            suggestionsContainer.style.display = 'none';
            return;
        }
        fetch('/api/dadata/suggest?query=' + encodeURIComponent(query))
            .then(r => r.json())
            .then(data => {
                const suggestions = data.suggestions || [];
                suggestionsContainer.innerHTML = '';
                suggestions.forEach(item => {
                    const div = document.createElement('div');
                    div.textContent = item.value;
                    div.style.cursor = 'pointer';
                    div.onclick = () => {
                        addressInput.value = item.value;
                        latInput.value = item.data.geo_lat || '';
                        lonInput.value = item.data.geo_lon || '';
                        suggestionsContainer.style.display = 'none';

                        if (latInput.value && lonInput.value) {
                            const coords = [parseFloat(latInput.value), parseFloat(lonInput.value)];
                            setPlacemark(coords);
                        }
                    };
                    suggestionsContainer.appendChild(div);
                });
                suggestionsContainer.style.display = suggestions.length ? 'block' : 'none';
            });
    });

    document.addEventListener('click', function (e) {
        if (suggestionsContainer && !e.target.closest('#address-suggestions') && e.target !== addressInput) {
            suggestionsContainer.style.display = 'none';
        }
    });
});