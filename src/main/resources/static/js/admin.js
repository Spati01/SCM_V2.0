document.addEventListener("DOMContentLoaded", function () {
    // ✅ Select Elements
    const imageInput = document.getElementById('image_file_input');
    const imagePreview = document.getElementById('upload_image_preview');
    const imageContainer = document.getElementById('image_container');
    const removeImageButton = document.getElementById('remove_image_btn');
    const dropArea = document.getElementById('drop_area');
    const uploadText = document.getElementById('upload_text'); // Placeholder Text

    // ✅ Handle File Input Change (Show Image Preview)
    imageInput.addEventListener('change', function (event) {
        const file = event.target.files[0];
        if (file) {
            readFile(file); // Show image preview
        }
    });

    // ✅ Handle Drag & Drop Image Upload
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
            const dataTransfer = new DataTransfer();
            dataTransfer.items.add(file);
            imageInput.files = dataTransfer.files;
            readFile(file); // Show image preview
        }
    });

    // ✅ Read & Display Image Preview
    function readFile(file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            imagePreview.src = e.target.result;
            imageContainer.classList.remove('hidden');
            dropArea.classList.add('hidden');
            uploadText.classList.add('hidden'); // Hide placeholder text
        };
        reader.readAsDataURL(file);
    }

    // ✅ Handle Remove Image
    removeImageButton.addEventListener('click', function (event) {
        event.preventDefault();
        imagePreview.src = '';
        imageContainer.classList.add('hidden');
        dropArea.classList.remove('hidden');
        uploadText.classList.remove('hidden'); // Show placeholder text again
        imageInput.value = ''; // Clear File Input
    });

    // ✅ Show Existing Image or Placeholder on Load
    if (imagePreview.src && imagePreview.src.trim() !== '' && imagePreview.src !== window.location.href) {
        imageContainer.classList.remove('hidden');
        dropArea.classList.add('hidden');
        uploadText.classList.add('hidden');
    } else {
        imageContainer.classList.add('hidden');
        dropArea.classList.remove('hidden');
        uploadText.classList.remove('hidden'); // Ensure placeholder is visible
    }
});







