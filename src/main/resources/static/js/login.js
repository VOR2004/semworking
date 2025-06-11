document.getElementById('ajaxLoginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const errorDiv = document.getElementById('errorMessage');
    errorDiv.style.display = 'none';
    errorDiv.textContent = '';

    const formData = new FormData(this);
    fetch('/login', {  // изменено на единый URL /login
        method: 'POST',
        body: new URLSearchParams(formData),
        headers: {
            'Accept': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'  // добавлен заголовок для AJAX
        }
    }).then(response => {
        if (response.ok) {
            window.location.href = '/';
        } else {
            return response.json().then(data => {
                errorDiv.style.display = 'block';
                errorDiv.textContent = data.error || 'Ошибка аутентификации';
            });
        }
    }).catch(() => {
        errorDiv.style.display = 'block';
        errorDiv.textContent = 'Ошибка соединения с сервером';
    });
});
