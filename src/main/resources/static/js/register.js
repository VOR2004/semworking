
document.getElementById('registrationForm').addEventListener('submit', function(event) {
    event.preventDefault();

    // Сброс сообщений об ошибках
    document.querySelectorAll('.error').forEach(el => el.textContent = '');
    document.getElementById('errorMessage').style.display = 'none';

    const formData = new FormData(this);
    const jsonData = {};
    formData.forEach((value, key) => {
        jsonData[key] = value;
    });

    fetch('/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(jsonData)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return response.json().then(data => {
                    throw data; // Пробрасываем ошибки дальше
                });
            }
        })
        .then(data => {
            if (data.success) {
                // Успешная регистрация - перенаправляем на страницу входа
                window.location.href = '/login';
            } else {
                // Обработка других ошибок (например, с бэкенда)
                document.getElementById('errorMessage').style.display = 'block';
                document.getElementById('errorMessage').textContent = data.error || 'Ошибка регистрации';
            }
        })
        .catch(error => {
            if (typeof error === 'object') {
                // Вывод ошибок валидации
                for (let field in error) {
                    const errorElement = document.getElementById(field + 'Error');
                    if (errorElement) {
                        errorElement.textContent = error[field];
                    }
                }
            } else {
                // Общая ошибка
                document.getElementById('errorMessage').style.display = 'block';
                document.getElementById('errorMessage').textContent = error || 'Ошибка регистрации';
            }
        });
});
