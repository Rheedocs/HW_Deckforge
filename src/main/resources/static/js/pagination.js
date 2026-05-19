// --- Deck-filter tilstand ---

var deckShowOwnOnly = false;

// --- Kortfiltrering ---

function filterCardPicker(showOwnOnly) {
    deckShowOwnOnly = showOwnOnly;
    document.getElementById("filterAlle").className = showOwnOnly ? "btn btn-secondary" : "btn";
    document.getElementById("filterOwn").className = showOwnOnly ? "btn" : "btn btn-secondary";
    applyDeckFilter();
}

function applyDeckFilter() {
    var searchInput = document.getElementById("deckCardSearch");
    var colorInput = document.getElementById("deckCardColor");
    var typeInput = document.getElementById("deckCardType");
    var searchText = searchInput ? searchInput.value.toLowerCase() : "";
    var selectedColor = colorInput ? colorInput.value : "";
    var selectedType = typeInput ? typeInput.value : "";

    document.querySelectorAll("#deckCardPicker .card-picker-item").forEach(function(item) {
        var matches = matchesFilter(item, searchText, selectedColor, selectedType) &&
            (!deckShowOwnOnly || item.dataset.owned === "true");
        item.classList.toggle("filter-hidden", !matches);
    });
    showPage("deckCardPicker", "deckCardPagination", 1);
}

function filterCollectionCards() {
    var searchInput = document.getElementById("collectionCardSearch");
    var colorInput = document.getElementById("collectionCardColor");
    var typeInput = document.getElementById("collectionCardType");
    var searchText = searchInput ? searchInput.value.toLowerCase() : "";
    var selectedColor = colorInput ? colorInput.value : "";
    var selectedType = typeInput ? typeInput.value : "";

    document.querySelectorAll("#collectionCardPicker .card-picker-item").forEach(function(item) {
        item.classList.toggle("filter-hidden", !matchesFilter(item, searchText, selectedColor, selectedType));
    });
    showPage("collectionCardPicker", "collectionCardPagination", 1);
}

function filterCardList() {
    var searchInput = document.getElementById("cardListSearch");
    var colorInput = document.getElementById("cardListColor");
    var typeInput = document.getElementById("cardListType");
    var searchText = searchInput ? searchInput.value.toLowerCase() : "";
    var selectedColor = colorInput ? colorInput.value : "";
    var selectedType = typeInput ? typeInput.value : "";

    // Mobil: filtrer .card-thumb
    document.querySelectorAll(".card-grid .card-thumb").forEach(function(el) {
        el.classList.toggle("filter-hidden", !matchesFilter(el, searchText, selectedColor, selectedType));
    });

    // Desktop: filtrer tabelrækker — name=1, type=2, color=3
    document.querySelectorAll(".desktop-table tbody tr").forEach(function(tr) {
        var c = tr.cells;
        if (!c || c.length < 4) return;
        var name = c[1].textContent.toLowerCase();
        var cardType = c[2].textContent.trim();
        var cardColor = c[3].textContent.trim();
        var matches = (!searchText || name.includes(searchText)) &&
            (!selectedColor || cardColor === selectedColor) &&
            (!selectedType || cardType === selectedType);
        tr.classList.toggle("filter-hidden", !matches);
    });
}

function filterPlayerList() {
    var searchInput = document.getElementById("playerSearch");
    var searchText = searchInput ? searchInput.value.toLowerCase() : "";

    // Mobil: filtrer .list-thumb elementer
    document.querySelectorAll(".list-grid .list-thumb").forEach(function(el) {
        var name = el.querySelector(".list-thumb-title") ? el.querySelector(".list-thumb-title")
            .textContent.toLowerCase() : "";
        el.classList.toggle("filter-hidden", searchText && !name.includes(searchText));
    });

    // Desktop: filtrer tabelrækker på brugernavn kolonnen
    document.querySelectorAll(".desktop-table tbody tr").forEach(function(tr) {
        var c = tr.cells;
        if (!c || c.length < 2) return;
        var nameCell = tr.querySelector("td:nth-child(2)") || tr.querySelector("td");
        var name = nameCell ? nameCell.textContent.toLowerCase() : "";
        tr.classList.toggle("filter-hidden", searchText && !name.includes(searchText));
    });
}

function matchesFilter(el, search, color, type) {
    var name = (el.dataset.name || "").toLowerCase();
    var cardColor = el.dataset.color || "";
    var cardType = el.dataset.type || "";
    return (!search || name.includes(search)) &&
        (!color || cardColor === color) &&
        (!type || cardType === type);
}

function buildFilterDropdowns(containerSelector, colorId, typeId) {
    var colors = new Set();
    var types = new Set();
    document.querySelectorAll(containerSelector).forEach(function(el) {
        if (el.dataset.color) colors.add(el.dataset.color);
        if (el.dataset.type) types.add(el.dataset.type);
    });
    var colorSelect = document.getElementById(colorId);
    var typeSelect = document.getElementById(typeId);
    if (colorSelect) {
        Array.from(colors).sort().forEach(function(value) {
            var option = document.createElement("option");
            option.value = value;
            option.textContent = value;
            colorSelect.appendChild(option);
        });
    }
    if (typeSelect) {
        Array.from(types).sort().forEach(function(value) {
            var option = document.createElement("option");
            option.value = value;
            option.textContent = value;
            typeSelect.appendChild(option);
        });
    }
}

// --- Paginering ---

function getPageSize() {
    return window.innerWidth >= 768 ? 12 : 8;
}

var pagination = {};

function initPagination(pickerId, controlId) {
    pagination[pickerId] = 1;
    showPage(pickerId, controlId, 1);
}

function showPage(pickerId, controlId, side) {
    var pageSize = getPageSize();
    var items = document.querySelectorAll("#" + pickerId + " .card-picker-item:not(.filter-hidden)");
    var total = items.length;
    var pages = Math.max(1, Math.ceil(total / pageSize));
    if (side < 1) side = 1;
    if (side > pages) side = pages;
    pagination[pickerId] = side;

    document.querySelectorAll("#" + pickerId + " .card-picker-item").forEach(function(item) {
        item.classList.add("page-hidden");
    });
    var counter = 0;
    for (var i = 0; i < items.length; i++) {
        if (counter >= (side - 1) * pageSize && counter < side * pageSize) {
            items[i].classList.remove("page-hidden");
        }
        counter++;
    }

    var controls = document.getElementById(controlId);
    if (!controls) return;
    document.getElementById(controlId + "Forrige").disabled = side <= 1;
    document.getElementById(controlId + "Næste").disabled = side >= pages;
    document.getElementById(controlId + "Info").textContent = "Side " + side + " af " + pages + " (" + total + " kort)";
}

function changePage(btn, delta) {
    var controls = btn.closest(".pagination-controls");
    var pickerId = controls.dataset.picker;
    var controlId = controls.id;
    var current = pagination[pickerId] || 1;
    showPage(pickerId, controlId, current + delta);
}

document.addEventListener("DOMContentLoaded", function() {
    if (document.getElementById("deckCardPicker")) {
        initPagination("deckCardPicker", "deckCardPagination");
        buildFilterDropdowns("#deckCardPicker .card-picker-item", "deckCardColor", "deckCardType");
    }
    if (document.getElementById("collectionCardPicker")) {
        initPagination("collectionCardPicker", "collectionCardPagination");
        buildFilterDropdowns("#collectionCardPicker .card-picker-item", "collectionCardColor", "collectionCardType");
    }
    if (document.getElementById("cardListSearch")) {
        buildFilterDropdowns(".card-grid .card-thumb", "cardListColor", "cardListType");
    }
    var quantityInput = document.getElementById("quantity");
    if (quantityInput) {
        quantityInput.addEventListener("input", function() {
            if (document.getElementById("selectedCardId").value) {
                updateDeckWarning(parseInt(this.value) || 1);
            }
        });
    }
});