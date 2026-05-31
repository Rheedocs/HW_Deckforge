// --- Indhold ---

function setDeckModalContent(el) {
    document.getElementById("dkModalBillede").src = el.dataset.image || "";
    document.getElementById("dkModalBillede").alt = el.dataset.name || "";
    document.getElementById("dkModalNavn").textContent = el.dataset.name || "";
    document.getElementById("dkModalType").textContent = el.dataset.type || "";
    document.getElementById("dkModalColor").textContent = el.dataset.color || "";
    document.getElementById("dkModalRarity").textContent = el.dataset.rarity || "";
    document.getElementById("dkModalSet").textContent = el.dataset.set || "";
    document.getElementById("dkModalRules").textContent = el.dataset.rules || "";
    document.getElementById("dkModalRulesItem").style.display = el.dataset.rules ? "list-item" : "none";
    document.getElementById("dkModalAntal").textContent = "I decket: " + (el.dataset.quantity || "?");
    document.getElementById("dkModalScryfall").href = el.dataset.url || "";
}

// --- Handlinger ---

function setDeckModalActions(el) {
    let hasActions = !!el.dataset.addUrl;
    document.getElementById("dkModalHandlinger").classList.toggle("modal-actions", !hasActions);
    if (!hasActions) return;
    document.getElementById("dkAntal").textContent = "1";
    document.getElementById("dkTilføjQuantity").value = "1";
    document.getElementById("dkFjernQuantity").value = "1";
    document.getElementById("dkCardId").value = el.dataset.cardId || "";
    document.getElementById("dkTilføjForm").action = el.dataset.addUrl;
    document.getElementById("dkFjernForm").action = el.dataset.removeUrl;
}

// --- Modal-styring ---

function openDeckCardModal(el) {
    setDeckModalContent(el);
    setDeckModalActions(el);
    document.getElementById("deckCardModalOverlay").classList.add("active");
}

function closeDeckCardModal(event) {
    if (!event || event.target === document.getElementById("deckCardModalOverlay")) {
        document.getElementById("deckCardModalOverlay").classList.remove("active");
    }
}

function adjustDeckQuantity(delta) {
    let el = document.getElementById("dkAntal");
    let val = parseInt(el.textContent) + delta;
    if (val < 1) val = 1;
    el.textContent = val;
    document.getElementById("dkTilføjQuantity").value = val;
    document.getElementById("dkFjernQuantity").value = val;
}