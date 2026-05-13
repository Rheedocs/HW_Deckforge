function openModal(img) {
    document.getElementById("modalImage").src = img.dataset.image;
    document.getElementById("modalImage").alt = img.dataset.name;
    document.getElementById("modalName").textContent = img.dataset.name;
    document.getElementById("modalType").textContent = img.dataset.type;
    document.getElementById("modalColor").textContent = img.dataset.color;
    document.getElementById("modalRarity").textContent = img.dataset.rarity;
    document.getElementById("modalSet").textContent = img.dataset.set;
    document.getElementById("modalRules").textContent = img.dataset.rules;
    document.getElementById("modalRulesItem").style.display = img.dataset.rules ? "list-item" : "none";
    document.getElementById("modalScryfall").href = img.dataset.url;
    document.getElementById("modalOverlay").classList.add("active");
}

function closeModal() {
    document.getElementById("modalOverlay").classList.remove("active");
}

function closeModalOnOverlay(event) {
    if (event.target === document.getElementById("modalOverlay")) closeModal();
}

document.addEventListener("keydown", function(e) {
    if (e.key === "Escape") closeModal();
});