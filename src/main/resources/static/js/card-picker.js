var cardPickerContext = null;
var cardPickerElement = null;
var selectedCardOwnedQuantity = 0;

function updateDeckWarning(quantity) {
    var show = selectedCardOwnedQuantity < quantity;
    var message = selectedCardOwnedQuantity === 0
        ? "Du har ikke dette kort i din samling"
        : "Du har kun " + selectedCardOwnedQuantity + " af dette kort i din samling";
    var desktop = document.getElementById("deckPickerAdvarsel");
    if (desktop) {
        desktop.textContent = show ? message : "";
        desktop.style.display = show ? "" : "none";
    }
    var mobil = document.getElementById("kvAdvarsel");
    if (mobil) {
        var showMobile = show && cardPickerContext === "deck";
        mobil.textContent = showMobile ? message : "";
        mobil.style.display = showMobile ? "" : "none";
    }
}

function selectCard(el) {
    if (window.innerWidth < 768) {
        openCardPickerModal(el, "deck");
        return;
    }
    var isSelected = el.classList.contains("selected");
    document.querySelectorAll("#deckCardPicker .card-picker-item.selected").forEach(function(item) {
        item.classList.remove("selected");
    });
    if (isSelected) {
        document.getElementById("selectedCardId").value = "";
        var label = document.getElementById("cardPickerLabel");
        label.textContent = "Intet kort valgt — klik et kort herunder";
        label.classList.remove("selected");
        document.getElementById("deckPickerStickyBar").classList.remove("active");
        selectedCardOwnedQuantity = 0;
        updateDeckWarning(0);
        return;
    }
    el.classList.add("selected");
    document.getElementById("selectedCardId").value = el.dataset.id;
    label = document.getElementById("cardPickerLabel");
    label.textContent = "Valgt: " + el.dataset.name;
    label.classList.add("selected");
    var img = el.querySelector("img");
    document.getElementById("deckPickerPreviewImg").src = img ? img.src : "";
    document.getElementById("deckPickerPreviewImg").alt = el.dataset.name || "";
    document.getElementById("deckPickerPreviewNavn").textContent = el.dataset.name || "";
    document.getElementById("deckPickerStickyBar").classList.add("active");
    selectedCardOwnedQuantity = parseInt(el.dataset.quantity) || 0;
    updateDeckWarning(parseInt(document.getElementById("quantity").value) || 1);
}

function selectCollectionCard(el) {
    if (window.innerWidth < 768) {
        openCardPickerModal(el, "samling");
        return;
    }
    var isSelected = el.classList.contains("selected");
    document.querySelectorAll("#collectionCardPicker .card-picker-item.selected").forEach(function(item) {
        item.classList.remove("selected");
    });
    if (isSelected) {
        document.getElementById("collectionCardId").value = "";
        var label = document.getElementById("collectionCardLabel");
        label.textContent = "Intet kort valgt — klik et kort herunder";
        label.classList.remove("selected");
        document.getElementById("collectionPickerStickyBar").classList.remove("active");
        return;
    }
    el.classList.add("selected");
    document.getElementById("collectionCardId").value = el.dataset.id;
    label = document.getElementById("collectionCardLabel");
    label.textContent = "Valgt: " + el.dataset.name;
    label.classList.add("selected");
    var img = el.querySelector("img");
    document.getElementById("collectionPickerPreviewImg").src = img ? img.src : "";
    document.getElementById("collectionPickerPreviewImg").alt = el.dataset.name || "";
    document.getElementById("collectionPickerPreviewName").textContent = el.dataset.name || "";
    document.getElementById("collectionPickerStickyBar").classList.add("active");
}

function openCardPickerModal(el, context) {
    cardPickerContext = context;
    cardPickerElement = el;
    var img = el.querySelector("img");
    document.getElementById("kvModalBillede").src = img ? img.src : "";
    document.getElementById("kvModalBillede").alt = el.dataset.name || "";
    document.getElementById("kvModalNavn").textContent = el.dataset.name || "";
    document.getElementById("kvAntal").textContent = "1";
    selectedCardOwnedQuantity = parseInt(el.dataset.quantity) || 0;
    updateDeckWarning(1);
    document.getElementById("cardPickerModalOverlay").classList.add("active");
}

function closeCardPickerModal(event) {
    if (!event || event.target === document.getElementById("cardPickerModalOverlay")) {
        document.getElementById("cardPickerModalOverlay").classList.remove("active");
    }
}

function adjustPickerQuantity(delta) {
    var el = document.getElementById("kvAntal");
    var val = parseInt(el.textContent) + delta;
    if (val < 1) val = 1;
    el.textContent = val;
    updateDeckWarning(val);
}

function addFromPicker() {
    var quantity = parseInt(document.getElementById("kvAntal").textContent);
    if (cardPickerContext === "deck") {
        document.getElementById("selectedCardId").value = cardPickerElement.dataset.id;
        document.getElementById("quantity").value = quantity;
        document.getElementById("addCardForm").submit();
    } else if (cardPickerContext === "samling") {
        document.getElementById("collectionCardId").value = cardPickerElement.dataset.id;
        document.getElementById("collectionQuantity").value = quantity;
        document.getElementById("addCollectionCardForm").submit();
    }
    document.getElementById("cardPickerModalOverlay").classList.remove("active");
}

var addCollectionCardForm = document.getElementById("addCollectionCardForm");
if (addCollectionCardForm) {
    addCollectionCardForm.addEventListener("submit", function(e) {
        if (!document.getElementById("collectionCardId").value) {
            e.preventDefault();
            alert("Vælg et kort inden du tilføjer.");
        }
    });
}