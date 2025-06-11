document.getElementById('product-form').addEventListener('submit', function(event) {
    event.preventDefault();
    document.querySelectorAll('.error').forEach(el => el.textContent = '');

    const form = event.target;
    const formData = new FormData(form);

    console.log('Отправка формы с данными:', [...formData.entries()]);

    fetch('/product/add', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            console.log('Получен ответ от сервера:', response);
            if (response.ok) {
                return response.json();
            } else {
                return response.json().then(data => {
                    console.warn('Ошибка формы:', data);
                    throw data;
                }).catch(err => {
                    console.warn('Ошибка при парсинге ответа с ошибкой:', err);
                    throw { error: 'Ошибка сервера или неверный формат ответа' };
                });
            }
        })
        .then(data => {
            console.log('Ответ с данными:', data);
            if (data.success) {
                console.log('Успешное сохранение, перенаправление...');
                window.location.href = '/';
            } else {
                console.warn('Ошибка сохранения:', data.error);
                const formErrorEl = document.getElementById('formError');
                if (formErrorEl) {
                    formErrorEl.textContent = data.error || 'Ошибка сохранения';
                } else {
                    console.warn('Элемент formError не найден для отображения ошибки');
                }
            }
        })
        .catch(error => {
            console.warn('Обработка ошибки:', error);
            if (typeof error === 'object' && error !== null) {
                let handled = false;
                for (const field in error) {
                    let errorElement = document.getElementById(field + 'Error');
                    if (!errorElement && field === 'tagNames') {
                        errorElement = document.getElementById('tagNamesError');
                    }
                    if (errorElement) {
                        errorElement.textContent = error[field];
                        handled = true;
                    } else {
                        console.warn(`Элемент для ошибки поля ${field} не найден`);
                    }
                }
                if (!handled) {
                    const formErrorEl = document.getElementById('formError');
                    if (formErrorEl) {
                        formErrorEl.textContent = 'Произошла ошибка при сохранении формы';
                    }
                }
            } else {
                const formErrorEl = document.getElementById('formError');
                if (formErrorEl) {
                    formErrorEl.textContent = error || 'Ошибка сохранения';
                } else {
                    console.error('Ошибка:', error);
                }
            }
        });
});
