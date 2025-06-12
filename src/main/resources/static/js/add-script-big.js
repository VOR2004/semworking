form.addEventListener('submit', function(e) {
    e.preventDefault();
    document.querySelectorAll('.error').forEach(el => el.textContent = '');

    const formData = new FormData(form);
    formData.delete('images');
    selectedFiles.forEach(file => formData.append('images', file));
    formData.set('mainImageIndex', mainImageIndex);

    console.log("Отправка формы с данными:", [...formData.entries()]);

    fetch(form.action, {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return response.json().then(data => { throw data; });
            }
        })
        .then(data => {
            if (data.success) {
                window.location.href = '/';
            } else {
                const formErrorEl = document.getElementById('formError');
                if (formErrorEl) formErrorEl.textContent = data.error || 'Ошибка сохранения';
            }
        })
        .catch(error => {
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
                    }
                }
                if (!handled) {
                    const formErrorEl = document.getElementById('formError');
                    if (formErrorEl) formErrorEl.textContent = 'Произошла ошибка при сохранении формы';
                }
            } else {
                const formErrorEl = document.getElementById('formError');
                if (formErrorEl) formErrorEl.textContent = error || 'Ошибка сохранения';
            }
        });
});
