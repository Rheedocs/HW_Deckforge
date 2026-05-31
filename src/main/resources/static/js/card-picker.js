// --- Tilstand ---

let cardPickerContext = null;
let cardPickerElement = null;
let selectedCardOwnedQuantity = 0;

// --- Advarsel ---

function updateDeckWarning(quantity) {
    let show = selectedCardOwnedQuantity < quantity;
    let message = selectedCardOwnedQuantity === 0
        ? "Du har ikke dette kort i din samling"
        : "Du har kun " + selectedCardOwnedQuantity + " af dette kort i din samling";
    let desktop = document.getElementById("deckPickerAdvarsel");
    if (desktop) {
        desktop.textContent = show ? message : "";
        desktop.style.display = show ? "" : "none";
    }
    let mobil = document.getElementById("kvAdvarsel");
    if (mobil) {
        let showMobile = show && cardPickerContext === "deck";
        mobil.textContent = showMobile ? message : "";
        mobil.style.display = showMobile ? "" : "none";
    }
}

// --- Deck-kort valg ---

function selectCard(el) {
    if (window.innerWidth < 768) { openCardPickerModal(el, "deck"); return; }
    document.querySelectorAll("#deckCardPicker .card-picker-item.selected").forEach(function(item) {
        item.classList.remove("selected");
    });
    if (el.classList.contains("selected")) { clearDeckSelection(); return; }
    applyDeckSelection(el);
}

function clearDeckSelection() {
    document.getElementById("selectedCardId").value = "";
    let label = document.getElementById("cardPickerLabel");
    label.textContent = "Intet kort valgt — klik et kort herunder";
    label.classList.remove("selected");
    document.getElementById("deckPickerStickyBar").classList.remove("active");
    selectedCardOwnedQuantity = 0;
    updateDeckWarning(0);
}

function applyDeckSelection(el) {
    el.classList.add("selected");
    document.getElementById("selectedCardId").value = el.dataset.id;
    let label = document.getElementById("cardPickerLabel");
    label.textContent = "Valgt: " + el.dataset.name;
    label.classList.add("selected");
    let img = el.querySelector("img");
    document.getElementById("deckPickerPreviewImg").src = img ? img.src : "";
    document.getElementById("deckPickerPreviewImg").alt = el.dataset.name || "";
    document.getElementById("deckPickerPreviewNavn").textContent = el.dataset.name || "";
    document.getElementById("deckPickerStickyBar").classList.add("active");
    selectedCardOwnedQuantity = parseInt(el.dataset.quantity) || 0;
    updateDeckWarning(parseInt(document.getElementById("quantity").value) || 1);
}

// --- Samlings-kort valg ---

function selectCollectionCard(el) {
    if (window.innerWidth < 768) { openCardPickerModal(el, "samling"); return; }
    document.querySelectorAll("#collectionCardPicker .card-picker-item.selected").forEach(function(item) {
        item.classList.remove("selected");
    });
    if (el.classList.contains("selected")) { clearCollectionSelection(); return; }
    applyCollectionSelection(el);
}

function clearCollectionSelection() {
    document.getElementById("collectionCardId").value = "";
    let label = document.getElementById("collectionCardLabel");
    label.textContent = "Intet kort valgt — klik et kort herunder";
    label.classList.remove("selected");
    document.getElementById("collectionPickerStickyBar").classList.remove("active");
}

function applyCollectionSelection(el) {
    el.classList.add("selected");
    document.getElementById("collectionCardId").value = el.dataset.id;
    let label = document.getElementById("collectionCardLabel");
    label.textContent = "Valgt: " + el.dataset.name;
    label.classList.add("selected");
    let img = el.querySelector("img");
    document.getElementById("collectionPickerPreviewImg").src = img ? img.src : "";
    document.getElementById("collectionPickerPreviewImg").alt = el.dataset.name || "";
    document.getElementById("collectionPickerPreviewName").textContent = el.dataset.name || "";
    document.getElementById("collectionPickerStickyBar").classList.add("active");
}

// --- Modal ---

function openCardPickerModal(el, context) {
    cardPickerContext = context;
    cardPickerElement = el;
    let img = el.querySelector("img");
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
    let el = document.getElementById("kvAntal");
    let val = parseInt(el.textContent) + delta;
    if (val < 1) val = 1;
    el.textContent = val;
    updateDeckWarning(val);
}

function addFromPicker() {
    let quantity = parseInt(document.getElementById("kvAntal").textContent);
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

// --- Formular-validering ---

let addCollectionCardForm = document.getElementById("addCollectionCardForm");
if (addCollectionCardForm) {
    addCollectionCardForm.addEventListener("submit", function(e) {
        if (!document.getElementById("collectionCardId").value) {
            e.preventDefault();
            alert("Vælg et kort inden du tilføjer.");
        }
    });
}