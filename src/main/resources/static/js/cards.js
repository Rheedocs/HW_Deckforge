document.getElementById("scryfallUrl").addEventListener("change", onScryfallLinkChanged);

function onScryfallLinkChanged() {
    const link = this.value.trim();
    const cardIdentifier = parseScryfallLink(link);
    if (cardIdentifier === null) return;
    fetchAndFillCard(cardIdentifier.set, cardIdentifier.number);
}

function parseScryfallLink(link) {
    if (!link.startsWith("https://scryfall.com/card/")) return null;
    const parts = link.split("/");
    if (parts.length < 6) return null;
    return { set: parts[4], number: parts[5] };
}

function fetchAndFillCard(set, number) {
    fetch("https://api.scryfall.com/cards/" + set + "/" + number)
        .then(response => response.json())
        .then(card => fillForm(card))
        .catch(() => null);
}

function fillForm(card) {
    document.getElementById("name").value = card.name;
    document.getElementById("setName").value = card.set_name;
    document.getElementById("ruleText").value = card.oracle_text ?? "";
    showCardPreview(card);
}

function showCardPreview(card) {
    const preview = document.getElementById("cardPreview");
    const imageUrl = getImageUrl(card);
    if (imageUrl === null) return;
    preview.src = imageUrl;
    preview.style.display = "block";
}

function getImageUrl(card) {
    if (card.image_uris) return card.image_uris.normal;
    if (card.card_faces) return card.card_faces[0].image_uris.normal;
    return null;
}