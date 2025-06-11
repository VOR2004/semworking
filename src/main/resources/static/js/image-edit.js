let selectedFiles = [];
let mainImageIndex = 0

const existingImagesContainer = document.getElementById('existing-images');
const previews = document.getElementById('image-previews');
const imageInput = document.getElementById('image-input');
const uploadBtn = document.getElementById('upload-btn');
const form = document.getElementById('product-form');
const mainImageIndexInput = document.getElementById('mainImageIndex');

const existingImages = existingImagesContainer ?
    Array.from(existingImagesContainer.querySelectorAll('label')).map(label => {
        const checkbox = label.querySelector('input[type="checkbox"]');
        const star = label.querySelector('.star');
        return {
            url: checkbox.value,
            element: label,
            checkbox: checkbox,
            star: star
        };
    }) : [];

existingImages.forEach((img, i) => {
    if (img.star) {
        img.star.addEventListener('click', (e) => {
            e.preventDefault();
            e.stopPropagation();
            setMainImage(i);
        });

        img.star.style.pointerEvents = 'auto';
        img.star.style.zIndex = '10';
    }

    if (img.checkbox) {
        img.checkbox.addEventListener('click', (e) => {
            e.stopPropagation();
        });

        img.checkbox.style.pointerEvents = 'auto';
    }

    if (img.star) img.star.style.pointerEvents = 'auto';
    if (img.checkbox) img.checkbox.style.pointerEvents = 'auto';
});

mainImageIndexInput.value = mainImageIndex;

function setMainImage(index) {
    mainImageIndex = index;
    mainImageIndexInput.value = index;
    updateMainImageVisual();
}

function updateMainImageVisual() {
    existingImages.forEach((img, i) => {
        const star = img.element.querySelector('.star');
        if (star) {
            star.classList.toggle('active', i === mainImageIndex);
            star.style.color = (i === mainImageIndex) ? 'gold' : 'gray';
        }
    });

    const newImageWrappers = previews.querySelectorAll('.image-wrapper');
    newImageWrappers.forEach((wrapper, i) => {
        const globalIndex = existingImages.length + i;
        const starBtn = wrapper.querySelector('.btn-main');
        if (starBtn) {
            starBtn.style.background = (globalIndex === mainImageIndex) ? 'gold' : 'rgba(255,255,255,0.7)';
            starBtn.style.opacity = (globalIndex === mainImageIndex) ? '1' : '0.6';
        }
    });
}

existingImages.forEach((img, i) => {
    const star = img.element.querySelector('.star');
    if (star) {
        star.addEventListener('click', () => setMainImage(i));
    }
});

function renderPreviews() {
    previews.innerHTML = '';
    selectedFiles.forEach((file, i) => {
        const wrapper = document.createElement('div');
        wrapper.className = 'image-wrapper';
        wrapper.style.position = 'relative';
        wrapper.style.display = 'inline-block';
        wrapper.style.margin = '5px';

        const img = document.createElement('img');
        img.style.width = '120px';
        img.style.height = '120px';
        img.style.objectFit = 'cover';
        img.style.display = 'block';

        const reader = new FileReader();
        reader.onload = function(e) {
            img.src = e.target.result;
        };
        reader.readAsDataURL(file);

        wrapper.appendChild(img);

        const mainBtn = document.createElement('button');
        mainBtn.type = 'button';
        mainBtn.className = 'btn-main';
        mainBtn.innerHTML = '★';
        mainBtn.title = 'Сделать главной';
        mainBtn.style.position = 'absolute';
        mainBtn.style.bottom = '0';
        mainBtn.style.left = '0';
        mainBtn.style.border = 'none';
        mainBtn.style.fontSize = '18px';
        mainBtn.style.cursor = 'pointer';
        mainBtn.style.width = '28px';
        mainBtn.style.height = '28px';
        mainBtn.style.borderRadius = '50%';
        mainBtn.style.background = 'rgba(255,255,255,0.7)';
        mainBtn.style.opacity = '0.6';

        const globalIndex = existingImages.length + i;
        if (globalIndex === mainImageIndex) {
            mainBtn.style.background = 'gold';
            mainBtn.style.opacity = '1';
        }

        mainBtn.addEventListener('click', () => setMainImage(globalIndex));

        const removeBtn = document.createElement('button');
        removeBtn.type = 'button';
        removeBtn.textContent = '×';
        removeBtn.title = 'Убрать фото';
        removeBtn.style.position = 'absolute';
        removeBtn.style.top = '0';
        removeBtn.style.right = '0';
        removeBtn.style.background = 'rgba(255,255,255,0.7)';
        removeBtn.style.border = 'none';
        removeBtn.style.fontSize = '18px';
        removeBtn.style.cursor = 'pointer';
        removeBtn.style.width = '28px';
        removeBtn.style.height = '28px';
        removeBtn.style.borderRadius = '50%';

        removeBtn.addEventListener('click', () => {
            selectedFiles.splice(i, 1);
            if (mainImageIndex === globalIndex) {
                setMainImage(0);
            } else if (mainImageIndex > globalIndex) {
                setMainImage(mainImageIndex - 1);
            }
            renderPreviews();
        });

        wrapper.appendChild(mainBtn);
        wrapper.appendChild(removeBtn);
        previews.appendChild(wrapper);
    });
}

imageInput.addEventListener('change', function() {
    Array.from(this.files).forEach(file => {
        if (!selectedFiles.some(f => f.name === file.name && f.size === file.size)) {
            selectedFiles.push(file);
        }
    });
    renderPreviews();
    this.value = '';
});

uploadBtn.addEventListener('click', () => imageInput.click());

form.addEventListener('submit', function(e) {
    e.preventDefault();

    const formData = new FormData(form);
    formData.delete('images');
    selectedFiles.forEach(file => formData.append('images', file));
    formData.set('mainImageIndex', mainImageIndex);

    const checkedExisting = existingImagesContainer ?
        Array.from(existingImagesContainer.querySelectorAll('input[name="existingImageUrls"]:checked'))
            .map(input => input.value) : [];
    formData.delete('existingImageUrls');
    checkedExisting.forEach(url => formData.append('existingImageUrls', url));

    fetch(form.action, {
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.redirected) {
            window.location.href = response.url;
        }
    }).catch(() => alert('Ошибка сети'));
});

updateMainImageVisual();