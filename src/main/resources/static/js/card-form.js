window.addEventListener("DOMContentLoaded", function() {
    const preview = document.getElementById("cardPreview");
    if (!preview) return;
    const imageUrl = preview.dataset.imageUrl;
    if (!imageUrl) return;
    preview.src = imageUrl;
    preview.style.display = "block";
});