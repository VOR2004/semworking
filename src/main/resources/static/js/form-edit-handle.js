document.getElementById('product-form').addEventListener('submit', function(event) {
    event.preventDefault();

    document.querySelectorAll('.error').forEach(el => el.textContent = '');

    const form = event.target;
    const formData = new FormData(form);

    fetch(form.action, {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(data => Promise.reject(data));
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                window.location.href = '/';
            }
        })
        .catch(error => {
            if (typeof error === 'object' && error !== null) {
                let hasFieldErrors = false;
                for (const field in error) {
                    let errorElement = document.getElementById(field + 'Error');
                    if (!errorElement && field === 'tagNames') {
                        errorElement = document.getElementById('tagNamesError');
                    }
                    if (errorElement) {
                        errorElement.textContent = error[field];
                        hasFieldErrors = true;
                    }
                }
                if (!hasFieldErrors && error.error) {
                    const formErrorDiv = document.getElementById('formError');
                    if (formErrorDiv) {
                        formErrorDiv.textContent = error.error;
                    }
                }
            } else {
                console.error('Необработанная ошибка:', error);
            }
        });
});
