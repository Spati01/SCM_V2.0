console.log("admin user");

// ✅ Select Elements
const imageInput = document.getElementById('image_file_input');
const imagePreview = document.getElementById('upload_image_preview');
const imageContainer = document.getElementById('image_container');
const removeImageButton = document.getElementById('remove_image_btn');
const dropArea = document.getElementById('drop_area');
const uploadText = document.getElementById('upload_text');

// ✅ Track If Image Is Added (Fixing Form Submission Error)
let isImageAdded = false;

// ✅ Handle File Input Change (When using File Input)
imageInput.addEventListener('change', function(event) {
    const file = event.target.files[0];
    if (file) {
        readFile(file);
        clearImageError();
        isImageAdded = true;  // ✅ Mark image as added
    }
});

// ✅ Handle Drag and Drop Image Upload (Main Fix Here)
dropArea.addEventListener('dragover', (event) => {
    event.preventDefault();
    dropArea.classList.add('bg-gray-200');
});

dropArea.addEventListener('dragleave', () => {
    dropArea.classList.remove('bg-gray-200');
});

dropArea.addEventListener('drop', (event) => {
    event.preventDefault();
    dropArea.classList.remove('bg-gray-200');

    const file = event.dataTransfer.files[0];
    if (file) {
        // ✅ 💯 Main Fix: Bind File to Input Field
        const dataTransfer = new DataTransfer();
        dataTransfer.items.add(file);
        imageInput.files = dataTransfer.files;

        // ✅ Now Read File and Show Preview
        readFile(file);
        clearImageError();
        isImageAdded = true; // ✅ Track that image is uploaded via drag & drop
    }
});

// ✅ Read File and Preview Image
function readFile(file) {
    const reader = new FileReader();
    reader.onload = function(e) {
        imagePreview.src = e.target.result;
        imageContainer.classList.remove('hidden');
        dropArea.classList.add('hidden');
    };
    reader.readAsDataURL(file);
}

// ✅ Handle Remove Image Button
removeImageButton.addEventListener('click', function() {
    imagePreview.src = '';
    imageContainer.classList.add('hidden');
    dropArea.classList.remove('hidden');
    imageInput.value = '';
    clearImageError();
    isImageAdded = false;  // ✅ Reset image check
});

// ✅ Prevent Image from Opening in New Tab During Drag
imagePreview.addEventListener('dragstart', (event) => {
    event.preventDefault();
});

// ✅ Prevent Empty Circle Initially
window.addEventListener('load', function() {
    imageContainer.classList.add('hidden');
    dropArea.classList.remove('hidden');
});

// ✅ Handle Form Submission Without Error
const form = document.querySelector('form');
form.addEventListener('submit', function(event) {
    const allFieldsValid = form.checkValidity();

    // ✅ FIX: Check if image is added or not
    if (allFieldsValid && !isImageAdded) {
        showImageError();
        event.preventDefault();
    }
});

// ✅ Show Error Message If Image Is Missing
function showImageError() {
    if (!document.getElementById('image_error')) {
        const error = document.createElement('p');
        error.id = 'image_error';
        error.className = 'text-red-500 text-xs mt-1';
        error.textContent = '⚠️ Please upload a contact image.';
        dropArea.parentElement.appendChild(error);
    }
}

// ✅ Clear Error Message
function clearImageError() {
    const error = document.getElementById('image_error');
    if (error) {
        error.remove();
    }
}
