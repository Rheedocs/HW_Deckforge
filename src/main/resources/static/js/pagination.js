// --- Deck-filter tilstand ---

let deckShowOwnOnly = false;

// --- Kortfiltrering ---

function filterCardPicker(showOwnOnly) {
    deckShowOwnOnly = showOwnOnly;
    document.getElementById("filterAlle").className = showOwnOnly ? "btn btn-secondary" : "btn";
    document.getElementById("filterOwn").className = showOwnOnly ? "btn" : "btn btn-secondary";
    applyDeckFilter();
}

function applyDeckFilter() {
    let searchInput = document.getElementById("deckCardSearch");
    let colorInput = document.getElementById("deckCardColor");
    let typeInput = document.getElementById("deckCardType");
    let searchText = searchInput ? searchInput.value.toLowerCase() : "";
    let selectedColor = colorInput ? colorInput.value : "";
    let selectedType = typeInput ? typeInput.value : "";

    document.querySelectorAll("#deckCardPicker .card-picker-item").forEach(function (item) {
        let matches = matchesFilter(item, searchText, selectedColor, selectedType) &&
            (!deckShowOwnOnly || item.dataset.owned === "true");
        item.classList.toggle("filter-hidden", !matches);
    });
    showPage("deckCardPicker", "deckCardPagination", 1);
}

function filterCollectionCards() {
    let searchInput = document.getElementById("collectionCardSearch");
    let colorInput = document.getElementById("collectionCardColor");
    let typeInput = document.getElementById("collectionCardType");
    let searchText = searchInput ? searchInput.value.toLowerCase() : "";
    let selectedColor = colorInput ? colorInput.value : "";
    let selectedType = typeInput ? typeInput.value : "";

    document.querySelectorAll("#collectionCardPicker .card-picker-item").forEach(function (item) {
        item.classList.toggle("filter-hidden", !matchesFilter(item, searchText, selectedColor, selectedType));
    });
    showPage("collectionCardPicker", "collectionCardPagination", 1);
}

function filterCardList() {
    let searchInput = document.getElementById("cardListSearch");
    let colorInput = document.getElementById("cardListColor");
    let typeInput = document.getElementById("cardListType");
    let searchText = searchInput ? searchInput.value.toLowerCase() : "";
    let selectedColor = colorInput ? colorInput.value : "";
    let selectedType = typeInput ? typeInput.value : "";

    document.querySelectorAll(".card-grid .card-thumb").forEach(function (el) {
        el.classList.toggle("filter-hidden", !matchesFilter(el, searchText, selectedColor, selectedType));
    });
    if (document.getElementById("cardListGrid")) showPage("cardListGrid", "cardListPagination", 1);

    document.querySelectorAll(".desktop-table tbody tr").forEach(function (tr) {
        let c = tr.cells;
        if (!c || c.length < 4) return;
        let name = c[1].textContent.toLowerCase();
        let cardType = c[2].textContent.trim();
        let cardColor = c[3].textContent.trim();
        let matches = (!searchText || name.includes(searchText)) &&
            (!selectedColor || cardColor === selectedColor) &&
            (!selectedType || cardType === selectedType);
        tr.classList.toggle("filter-hidden", !matches);
    });
}

function filterPlayerList() {
    let searchInput = document.getElementById("playerSearch");
    let searchText = searchInput ? searchInput.value.toLowerCase() : "";

    document.querySelectorAll(".list-grid .list-thumb").forEach(function (el) {
        let name = el.querySelector(".list-thumb-title") ? el.querySelector(".list-thumb-title")
            .textContent.toLowerCase() : "";
        el.classList.toggle("filter-hidden", searchText && !name.includes(searchText));
    });

    document.querySelectorAll(".desktop-table tbody tr").forEach(function (tr) {
        let c = tr.cells;
        if (!c || c.length < 2) return;
        let nameCell = tr.querySelector("td:nth-child(2)") || tr.querySelector("td");
        let name = nameCell ? nameCell.textContent.toLowerCase() : "";
        tr.classList.toggle("filter-hidden", searchText && !name.includes(searchText));
    });
}

function matchesFilter(el, search, color, type) {
    let name = (el.dataset.name || "").toLowerCase();
    let cardColor = el.dataset.color || "";
    let cardType = el.dataset.type || "";
    return (!search || name.includes(search)) &&
        (!color || cardColor === color) &&
        (!type || cardType === type);
}

function buildFilterDropdowns(containerSelector, colorId, typeId) {
    let colors = new Set();
    let types = new Set();
    document.querySelectorAll(containerSelector).forEach(function (el) {
        if (el.dataset.color) colors.add(el.dataset.color);
        if (el.dataset.type) types.add(el.dataset.type);
    });
    let colorSelect = document.getElementById(colorId);
    let typeSelect = document.getElementById(typeId);
    if (colorSelect) {
        Array.from(colors).sort().forEach(function (value) {
            let option = document.createElement("option");
            option.value = value;
            option.textContent = value;
            colorSelect.appendChild(option);
        });
    }
    if (typeSelect) {
        Array.from(types).sort().forEach(function (value) {
            let option = document.createElement("option");
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

let pagination = {};

function initPagination(pickerId, controlId) {
    pagination[pickerId] = 1;
    showPage(pickerId, controlId, 1);
}

function getPaginationButtons(controlId) {
    let controls = document.getElementById(controlId);
    if (!controls) return { forrige: null, naeste: null, info: null };
    let buttons = controls.querySelectorAll("button");
    return {
        forrige: buttons[0] || null,
        naeste: buttons[1] || null,
        info: controls.querySelector(".pagination-info") || null
    };
}

function showPage(pickerId, controlId, side) {
    let pageSize = getPageSize();
    let selector = "#" + pickerId + " .card-picker-item:not(.filter-hidden), " +
        "#" + pickerId + " .card-thumb:not(.filter-hidden)";
    let items = document.querySelectorAll(selector);
    let total = items.length;
    let pages = Math.max(1, Math.ceil(total / pageSize));
    if (side < 1) side = 1;
    if (side > pages) side = pages;
    pagination[pickerId] = side;

    // Skjul alle mobil items
    let allSelector = "#" + pickerId + " .card-picker-item, " +
        "#" + pickerId + " .card-thumb";
    document.querySelectorAll(allSelector).forEach(function (item) {
        item.classList.add("page-hidden");
    });

    // Vis items for denne side
    for (let i = 0; i < items.length; i++) {
        if (i >= (side - 1) * pageSize && i < side * pageSize) items[i].classList.remove("page-hidden");
    }

    // Desktop tabel paginering
    let controls = document.getElementById(controlId);
    if (controls && controls.dataset.table) {
        let tableId = controls.dataset.table;
        let allRows = document.querySelectorAll("#" + tableId + " tbody tr");
        let visibleRows = document.querySelectorAll("#" + tableId + " tbody tr:not(.filter-hidden)");
        allRows.forEach(function (row) { row.classList.add("page-hidden"); });
        for (let i = 0; i < visibleRows.length; i++) {
            if (i >= (side - 1) * pageSize && i < side * pageSize) visibleRows[i].classList.remove("page-hidden");
        }
    }

    let btns = getPaginationButtons(controlId);
    if (btns.forrige) btns.forrige.disabled = side <= 1;
    if (btns.naeste) btns.naeste.disabled = side >= pages;
    if (btns.info) btns.info.textContent = "Side " + side + " af " + pages + " (" + total + " kort)";
}

function changePage(btn, delta) {
    let controls = btn.closest(".pagination-controls");
    let pickerId = controls.dataset.picker;
    let controlId = controls.id;
    let current = pagination[pickerId] || 1;
    showPage(pickerId, controlId, current + delta);
}

document.addEventListener("DOMContentLoaded", function () {
    if (document.getElementById("deckCardPicker")) {
        initPagination("deckCardPicker", "deckCardPagination");
        buildFilterDropdowns("#deckCardPicker .card-picker-item", "deckCardColor", "deckCardType");
    }
    if (document.getElementById("collectionCardPicker")) {
        initPagination("collectionCardPicker", "collectionCardPagination");
        buildFilterDropdowns("#collectionCardPicker .card-picker-item", "collectionCardColor",
            "collectionCardType");
    }
    if (document.getElementById("playerCollectionGrid")) {
        initPagination("playerCollectionGrid", "playerCollectionPagination");
    }
    if (document.getElementById("deckCardsGrid")) {
        initPagination("deckCardsGrid", "deckCardsPagination");
    }
    if (document.getElementById("cardListSearch")) {
        buildFilterDropdowns(".card-grid .card-thumb", "cardListColor", "cardListType");
    }
    if (document.getElementById("cardListGrid")) {
        initPagination("cardListGrid", "cardListPagination");
    }
    let quantityInput = document.getElementById("quantity");
    if (quantityInput) {
        quantityInput.addEventListener("input", function () {
            let cardId = document.getElementById("selectedCardId");
            if (cardId && cardId.value) {
                updateDeckWarning(parseInt(this.value) || 1);
            }
        });
    }
});