function openCollectionCardModal(el) {
    document.getElementById("skModalBillede").src = el.dataset.image || "";
    document.getElementById("skModalBillede").alt = el.dataset.name || "";
    document.getElementById("skModalNavn").textContent = el.dataset.name || "";
    document.getElementById("skModalType").textContent = el.dataset.type || "";
    document.getElementById("skModalColor").textContent = el.dataset.color || "";
    document.getElementById("skModalRarity").textContent = el.dataset.rarity || "";
    document.getElementById("skModalSet").textContent = el.dataset.set || "";
    document.getElementById("skModalRules").textContent = el.dataset.rules || "";
    document.getElementById("skModalRulesItem").style.display = el.dataset.rules ? "list-item" : "none";
    document.getElementById("skModalAntal").textContent = "I samling: " + (el.dataset.quantity || "?");
    document.getElementById("skModalScryfall").href = el.dataset.url || "";

    var hasActions = !!el.dataset.addUrl;
    document.getElementById("skModalHandlinger").classList.toggle("modal-actions", !hasActions);
    if (hasActions) {
        document.getElementById("skAntal").textContent = "1";
        document.getElementById("skTilføjQuantity").value = "1";
        document.getElementById("skFjernQuantity").value = "1";
        document.getElementById("skCardId").value = el.dataset.cardId || "";
        document.getElementById("skTilføjForm").action = el.dataset.addUrl;
        document.getElementById("skFjernForm").action = el.dataset.removeUrl;
        document.getElementById("skByteForm").action = el.dataset.tradeUrl;
        var forTrade = el.dataset.forTrade === "true";
        document.getElementById("skForTrade").value = String(!forTrade);
        var tradeButton = document.getElementById("skByteKnap");
        tradeButton.textContent = forTrade ? "Fjern fra bytte" : "Markér til bytte";
        tradeButton.className = "btn" + (forTrade ? " btn-secondary" : "");
    }
    document.getElementById("collectionCardModalOverlay").classList.add("active");
}

function closeCollectionCardModal(event) {
    if (!event || event.target === document.getElementById("collectionCardModalOverlay")) {
        document.getElementById("collectionCardModalOverlay").classList.remove("active");
    }
}

function adjustCollectionQuantity(delta) {
    var el = document.getElementById("skAntal");
    var val = parseInt(el.textContent) + delta;
    if (val < 1) val = 1;
    el.textContent = val;
    document.getElementById("skTilføjQuantity").value = val;
    document.getElementById("skFjernQuantity").value = val;
}