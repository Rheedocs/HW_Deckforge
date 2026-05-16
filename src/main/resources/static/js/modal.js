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
    var adminHandlinger = document.getElementById("modalAdminHandlinger");
    if (adminHandlinger) {
        var harAdmin = !!img.dataset.editUrl;
        adminHandlinger.classList.toggle("modal-handlinger", !harAdmin);
        if (harAdmin) {
            document.getElementById("modalRediger").href = img.dataset.editUrl;
            document.getElementById("modalSlet").href = img.dataset.deleteUrl;
        }
    }
    document.getElementById("modalOverlay").classList.add("active");
}

function closeModal() {
    document.getElementById("modalOverlay").classList.remove("active");
}

function closeModalOnOverlay(event) {
    if (event.target === document.getElementById("modalOverlay")) closeModal();
}

document.addEventListener("keydown", function(e) {
    if (e.key !== "Escape") return;
    closeModal();
    if (typeof lukDeckKortModal === "function") lukDeckKortModal();
    if (typeof lukSamlingsKortModal === "function") lukSamlingsKortModal();
    if (typeof lukKortVælgerModal === "function") lukKortVælgerModal();
});

function filterKortVælger(visKunEgne) {
    document.querySelectorAll("#deckKortPicker .card-picker-item").forEach(function(item) {
        item.classList.toggle("filter-hidden", visKunEgne && item.dataset.owned !== "true");
    });
    document.getElementById("filterAlle").className = visKunEgne ? "btn btn-secondary" : "btn";
    document.getElementById("filterEgne").className = visKunEgne ? "btn" : "btn btn-secondary";
    visSide("deckKortPicker", "deckKortPaginering", 1);
}

function getSidestørrelse() {
    return window.innerWidth >= 768 ? 12 : 8;
}
var paginering = {};

function initPaginering(pickerId, controlId) {
    paginering[pickerId] = 1;
    visSide(pickerId, controlId, 1);
}

function visSide(pickerId, controlId, side) {
    var pageSize = getSidestørrelse();
    var items = document.querySelectorAll("#" + pickerId + " .card-picker-item:not(.filter-hidden)");
    var total = items.length;
    var sider = Math.max(1, Math.ceil(total / pageSize));
    if (side < 1) side = 1;
    if (side > sider) side = sider;
    paginering[pickerId] = side;

    // Skjul alle items med page-hidden, vis kun den aktuelle side
    document.querySelectorAll("#" + pickerId + " .card-picker-item").forEach(function(item) {
        item.classList.add("page-hidden");
    });
    var tæller = 0;
    for (var i = 0; i < items.length; i++) {
        if (tæller >= (side - 1) * pageSize && tæller < side * pageSize) {
            items[i].classList.remove("page-hidden");
        }
        tæller++;
    }

    var controls = document.getElementById(controlId);
    if (!controls) return;
    document.getElementById(controlId + "Forrige").disabled = side <= 1;
    document.getElementById(controlId + "Næste").disabled = side >= sider;
    document.getElementById(controlId + "Info").textContent = "Side " + side + " af " + sider + " (" + total + " kort)";
}

function skiftSide(btn, delta) {
    var controls = btn.closest(".paginering-controls");
    var pickerId = controls.dataset.picker;
    var controlId = controls.id;
    var nuværende = paginering[pickerId] || 1;
    visSide(pickerId, controlId, nuværende + delta);
}

function vælgKort(el) {
    if (window.innerWidth < 768) {
        åbnKortVælgerModal(el, "deck");
        return;
    }
    document.querySelectorAll("#deckKortPicker .card-picker-item.selected").forEach(function(item) {
        item.classList.remove("selected");
    });
    el.classList.add("selected");
    document.getElementById("selectedCardId").value = el.dataset.id;
    var label = document.getElementById("kortVælgerLabel");
    label.textContent = "Valgt: " + el.dataset.name;
    label.classList.add("valgt");
    // Sticky bar
    var img = el.querySelector("img");
    document.getElementById("deckPickerPreviewImg").src = img ? img.src : "";
    document.getElementById("deckPickerPreviewImg").alt = el.dataset.name || "";
    document.getElementById("deckPickerPreviewNavn").textContent = el.dataset.name || "";
    document.getElementById("deckPickerStickyBar").classList.add("actief");
    valtKortEjetAntal = parseInt(el.dataset.quantity) || 0;
    opdaterDeckAdvarsel(parseInt(document.getElementById("quantity").value) || 1);
}

var addCardForm = document.getElementById("addCardForm");
if (addCardForm) {
    addCardForm.addEventListener("submit", function(e) {
        if (!document.getElementById("selectedCardId").value) {
            e.preventDefault();
            alert("Vælg et kort inden du tilføjer.");
        }
    });
}

function vælgSamlingsKort(el) {
    if (window.innerWidth < 768) {
        åbnKortVælgerModal(el, "samling");
        return;
    }
    document.querySelectorAll("#samlingsKortPicker .card-picker-item.selected").forEach(function(item) {
        item.classList.remove("selected");
    });
    el.classList.add("selected");
    document.getElementById("collectionCardId").value = el.dataset.id;
    var label = document.getElementById("collectionKortLabel");
    label.textContent = "Valgt: " + el.dataset.name;
    label.classList.add("valgt");
    // Sticky bar
    var img = el.querySelector("img");
    document.getElementById("samlingsPickerPreviewImg").src = img ? img.src : "";
    document.getElementById("samlingsPickerPreviewImg").alt = el.dataset.name || "";
    document.getElementById("samlingsPickerPreviewNavn").textContent = el.dataset.name || "";
    document.getElementById("samlingsPickerStickyBar").classList.add("actief");
}

function åbnSamlingsKortModal(el) {
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

    var harHandlinger = !!el.dataset.addUrl;
    document.getElementById("skModalHandlinger").classList.toggle("modal-handlinger", !harHandlinger);
    if (harHandlinger) {
        document.getElementById("skAntal").textContent = "1";
        document.getElementById("skTilføjQuantity").value = "1";
        document.getElementById("skFjernQuantity").value = "1";
        document.getElementById("skCardId").value = el.dataset.cardId || "";
        document.getElementById("skTilføjForm").action = el.dataset.addUrl;
        document.getElementById("skFjernForm").action = el.dataset.removeUrl;
        document.getElementById("skByteForm").action = el.dataset.tradeUrl;
        var forTrade = el.dataset.forTrade === "true";
        document.getElementById("skForTrade").value = String(!forTrade);
        var byteKnap = document.getElementById("skByteKnap");
        byteKnap.textContent = forTrade ? "Fjern fra bytte" : "Markér til bytte";
        byteKnap.className = "btn" + (forTrade ? " btn-secondary" : "");
    }
    document.getElementById("samlingsKortModalOverlay").classList.add("active");
}

function lukSamlingsKortModal(event) {
    if (!event || event.target === document.getElementById("samlingsKortModalOverlay")) {
        document.getElementById("samlingsKortModalOverlay").classList.remove("active");
    }
}

var kortVælgerKontekst = null;
var kortVælgerEl = null;
var valtKortEjetAntal = 0;

function opdaterDeckAdvarsel(antal) {
    var vis = valtKortEjetAntal < antal;
    var tekst = valtKortEjetAntal === 0
        ? "Du har ikke dette kort i din samling"
        : "Du har kun " + valtKortEjetAntal + " af dette kort i din samling";
    var desktop = document.getElementById("deckPickerAdvarsel");
    if (desktop) {
        desktop.textContent = vis ? tekst : "";
        desktop.style.display = vis ? "" : "none";
    }
    var mobil = document.getElementById("kvAdvarsel");
    if (mobil) {
        var visMobil = vis && kortVælgerKontekst === "deck";
        mobil.textContent = visMobil ? tekst : "";
        mobil.style.display = visMobil ? "" : "none";
    }
}

function åbnDeckKortModal(el) {
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
    var harHandlinger = !!el.dataset.addUrl;
    document.getElementById("dkModalHandlinger").classList.toggle("modal-handlinger", !harHandlinger);
    if (harHandlinger) {
        document.getElementById("dkAntal").textContent = "1";
        document.getElementById("dkTilføjQuantity").value = "1";
        document.getElementById("dkFjernQuantity").value = "1";
        document.getElementById("dkCardId").value = el.dataset.cardId || "";
        document.getElementById("dkTilføjForm").action = el.dataset.addUrl;
        document.getElementById("dkFjernForm").action = el.dataset.removeUrl;
    }
    document.getElementById("deckKortModalOverlay").classList.add("active");
}

function lukDeckKortModal(event) {
    if (!event || event.target === document.getElementById("deckKortModalOverlay")) {
        document.getElementById("deckKortModalOverlay").classList.remove("active");
    }
}

function justerDkAntal(delta) {
    var el = document.getElementById("dkAntal");
    var val = parseInt(el.textContent) + delta;
    if (val < 1) val = 1;
    el.textContent = val;
    document.getElementById("dkTilføjQuantity").value = val;
    document.getElementById("dkFjernQuantity").value = val;
}

function åbnKortVælgerModal(el, kontekst) {
    kortVælgerKontekst = kontekst;
    kortVælgerEl = el;
    var img = el.querySelector("img");
    document.getElementById("kvModalBillede").src = img ? img.src : "";
    document.getElementById("kvModalBillede").alt = el.dataset.name || "";
    document.getElementById("kvModalNavn").textContent = el.dataset.name || "";
    document.getElementById("kvAntal").textContent = "1";
    valtKortEjetAntal = parseInt(el.dataset.quantity) || 0;
    opdaterDeckAdvarsel(1);
    document.getElementById("kortVælgerModalOverlay").classList.add("active");
}

function lukKortVælgerModal(event) {
    if (!event || event.target === document.getElementById("kortVælgerModalOverlay")) {
        document.getElementById("kortVælgerModalOverlay").classList.remove("active");
    }
}

function justerSkAntal(delta) {
    var el = document.getElementById("skAntal");
    var val = parseInt(el.textContent) + delta;
    if (val < 1) val = 1;
    el.textContent = val;
    document.getElementById("skTilføjQuantity").value = val;
    document.getElementById("skFjernQuantity").value = val;
}

function justerKvAntal(delta) {
    var el = document.getElementById("kvAntal");
    var val = parseInt(el.textContent) + delta;
    if (val < 1) val = 1;
    el.textContent = val;
    opdaterDeckAdvarsel(val);
}

function tilføjFraPicker() {
    var antal = parseInt(document.getElementById("kvAntal").textContent);
    if (kortVælgerKontekst === "deck") {
        document.getElementById("selectedCardId").value = kortVælgerEl.dataset.id;
        document.getElementById("quantity").value = antal;
        document.getElementById("addCardForm").submit();
    } else if (kortVælgerKontekst === "samling") {
        document.getElementById("collectionCardId").value = kortVælgerEl.dataset.id;
        document.getElementById("collectionQuantity").value = antal;
        document.getElementById("addCollectionCardForm").submit();
    }
    document.getElementById("kortVælgerModalOverlay").classList.remove("active");
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

document.addEventListener("DOMContentLoaded", function() {
    if (document.getElementById("deckKortPicker")) {
        initPaginering("deckKortPicker", "deckKortPaginering");
    }
    if (document.getElementById("samlingsKortPicker")) {
        initPaginering("samlingsKortPicker", "samlingsKortPaginering");
    }
    var quantityInput = document.getElementById("quantity");
    if (quantityInput) {
        quantityInput.addEventListener("input", function() {
            if (document.getElementById("selectedCardId").value) {
                opdaterDeckAdvarsel(parseInt(this.value) || 1);
            }
        });
    }
});