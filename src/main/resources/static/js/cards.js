// Lytter efter ændringer i Scryfall URL-feltet og starter auto-udfyldning
document.getElementById("scryfallUrl").addEventListener("change", onScryfallLinkChanged);

// Henter URL, parser sæt og kortnummer og sender videre til fetchAndFillCard
function onScryfallLinkChanged() {
    const link = this.value.trim();
    const cardIdentifier = parseScryfallLink(link);
    if (cardIdentifier === null) return;
    fetchAndFillCard(cardIdentifier.set, cardIdentifier.number);
}

// Validerer URL og returnerer { set, number } eller null hvis ugyldig
// Eksempel: https://scryfall.com/card/fdn/123 → { set: "fdn", number: "123" }
function parseScryfallLink(link) {
    if (!link.startsWith("https://scryfall.com/card/")) return null;
    const parts = link.split("/");
    if (parts.length < 6) return null;
    return { set: parts[4], number: parts[5] };
}

// Kalder Scryfall API og udfylder formularen med kortdata
// .catch ignorerer fejl stille hvis Scryfall er nede
function fetchAndFillCard(set, number) {
    fetch("https://api.scryfall.com/cards/" + set + "/" + number)
        .then(response => response.json())
        .then(card => fillForm(card))
        .catch(() => null);
}

// Udfylder formularfelter med navn, sæt og regeltekst fra Scryfall
function fillForm(card) {
    document.getElementById("name").value = card.name;
    document.getElementById("setName").value = card.set_name;
    document.getElementById("ruleText").value = card.oracle_text ?? "";
    showCardPreview(card);
}

// Viser kortbillede i formularen
function showCardPreview(card) {
    const preview = document.getElementById("cardPreview");
    const imageUrl = getImageUrl(card);
    if (imageUrl === null) return;
    preview.src = imageUrl;
    preview.style.display = "block";
}

// Normale kort: image_uris.normal — Dobbeltsidede kort: card_faces[0].image_uris.normal
function getImageUrl(card) {
    if (card.image_uris) return card.image_uris.normal;
    if (card.card_faces) return card.card_faces[0].image_uris.normal;
    return null;
}