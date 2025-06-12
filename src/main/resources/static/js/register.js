const csrfToken = document.querySelector('input[name="_csrf"]').value;
document.getElementById('registrationForm').addEventListener('submit', function(event) {
    event.preventDefault();

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
            'Accept': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify(jsonData)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return response.json().then(data => {
                    throw data;
                });
            }
        })
        .then(data => {
            if (data.success) {
                window.location.href = '/login';
            } else {
                document.getElementById('errorMessage').style.display = 'block';
                document.getElementById('errorMessage').textContent = data.error || 'Ошибка регистрации';
            }
        })
        .catch(error => {
            if (typeof error === 'object') {
                for (let field in error) {
                    const errorElement = document.getElementById(field + 'Error');
                    if (errorElement) {
                        errorElement.textContent = error[field];
                    }
                }
            } else {
                document.getElementById('errorMessage').style.display = 'block';
                document.getElementById('errorMessage').textContent = error || 'Ошибка регистрации';
            }
        });
});
