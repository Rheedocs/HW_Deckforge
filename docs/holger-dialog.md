# Holger Dialog – Gruppe 8 Hello World

Product owner: Holger (GPT-instans, franchisetager i Næstved)  
Projekt: Deckforge – Magic: The Gathering community platform

---

## Sprint 1 – Analyse og IT&F

### Introduktion og fokusvalg

Holger anbefalede at vælge én stærk kerneproces frem for at sprede sig.
Gruppen valgte fokus på: **Samling → Deck → Event → Resultat** samt **Bytning**.

Wireframes laves i Figma, mobile first.

---

### Valg af use cases

Gruppen valgte:
1. Deckoprettelse
2. Eventtilmelding
3. Bytteforløb (skiftede fra "Slet Event" efter Holgers anbefaling)

Holger begrundelse: Bytning viser community-værdi, status-flow, reservation og historik, og er langt stærkere end slet-funktionalitet til eksamen.

---

### Bytteflow – afklaring

**Beslutning:** Spiller A foreslår bytte → kort reserveres → Spiller B accepterer/afviser inden 24 timer → spillere mødes fysisk → begge bekræfter i systemet → samlinger opdateres.

Holger understregede at accept alene ikke er nok. Byttet er fysisk og bekræftelsen sker i systemet efterfølgende.

**Reservation:** Kort låses i 24 timer. Udløber reservationen frigives kortene automatisk.

---

### Events – fuldt event

Første version: tydelig fejlbesked + disabled knap. Venteliste er nice-to-have til version 2.

---

### Kortdata og ansvar

Admin styrer kortdatabasen. Brugere må ikke frit oprette kort (risiko for dubletter og stavefejl).
Brugere kan foreslå kort, men admin godkender.

Vigtigste kortfelter: Navn, Type, Farve, Sæt, Sjældenhed, Billede (via Scryfall URL).
Kortdetaljer: billede + basisinfo i listen, "Åbn i Scryfall"-link. Ingen intern detaljevisning i MVP.
Kortdata holdes på **engelsk** (navn, type, farve osv.), UI på **dansk**.

---

### Registreringsflow og samlingssynlighed

Synlighed sættes ikke ved oprettelse, men efterfølgende i profil.
Default anbefalet: "Kun byttekort synlige".
Tre niveauer: Privat / Kun byttekort / Offentlig.

---

### Admin vs. spiller – hvad må hvad

**Admin:**
- Se, rediger og slet/deaktiver spillere
- Se samling, decks og byttehistorik (read-only)
- Må ikke foreslå bytter (admin er systemrolle, ikke spillerrolle)

**Spiller:**
- Kan se andre spilleres profiler og synlige samlinger
- Kan foreslå bytter
- Kan redigere og slette/deaktivere sin egen konto (soft delete anbefalet)

---

### Wireframe feedback (godkendt af Holger)

**Event flow:** Events liste → Event detalje → Vælg deck → Bekræftelse. "Registered" og "Full" badges. Fejlbesked og disabled knap ved fuldt event eller allerede tilmeldt.

**Bytteflow:** Collection → Mark for Trade → Opret bytte → Bekræftelse. Spiller B modtager notifikation. Accept/Decline med tydelig feedback. Udløbne forslag vises med advarsel og disabled knapper.

**Deck flow:** Deck liste → Deck detalje → Tilføj kort.

---

### Domænemodel og arkitektur (godkendt)

8 domænebegreber: Spiller, Kort, SpillerKort, Deck, Event, Eventtilmelding, Bytte, Resultat.
Clean Architecture med fire lag. Data persisteres i MySQL via repositories med JdbcTemplate.

---

### IT&F modeller (godkendt med feedback)

**BMC:** Value proposition skærpes til "Nem og tryg platform til at bytte kort og deltage i events". Revenue streams kan udvides med fee pr. event og premium features. Customer relationships skal inkludere in-app feedback og community engagement.

**SWOT:** Reservationsfunktionen som styrke er spot on. Brugeradoption er den største reelle risiko. AI i opportunities skal knyttes konkret til kortgenkendelse og prisestimering, ikke bare buzzwords.

**TOWS:** Stærkeste del. Fokus på trust via reservation og samarbejde med community er korrekt differentiering.

---

### Brugerrejser

**Spiller (kerneflow):** Opdagelse → Opret konto → Tilføj kort → Opret deck → Tilmeld event → Resultat

**Bytterejse:** Find byttekort → Mark for Trade → Send forslag → Accept → Mødes fysisk → Bekræft → Samling opdateret

---

## Sprint 2 – Implementering

### Kortvisning

- Kortliste viser: billede, navn, type, sjældenhed, sæt
- Ingen intern detaljevisning
- Knap: "Åbn i Scryfall" → eksternt link
- Kortdata på engelsk, UI på dansk

---

### Kortliste på mobil (afklaret Sprint 2)

- Grid-layout med kortbilleder som thumbnails på mobil
- Desktop beholder tabel-layoutet
- Ved klik: vis navn + basisinfo + link til Scryfall
- Grid er bedst fordi spillere genkender kort visuelt hurtigere, særligt i butik og til events

---

### Profilbillede

Nice-to-have. Hvis implementeret: URL til eksternt billede, ikke filupload.

---

### Event tilmelding og resultater

- Spillere kan afmelde sig indtil eventet starter, pladsen frigives
- Resultater registreres kun når event = COMPLETED
- Resultater vises på både event-siden og spillerens profil
- Tydelig status: UPCOMING → ONGOING → COMPLETED

---

### Trade flow detaljer

- Spiller A fortryder → kortene frigives med det samme, status = Cancelled
- Accept er ikke nok til at markere byttet som gennemført
- Begge spillere bekræfter fysisk gennemførelse i systemet
- Byttehistorik viser: status, dato, hvilke kort, hvem man byttede med

**Trade statuser:** Pending / Accepted / Completed / Cancelled / Expired

---

### Trade – nye afklaringer (15. maj 2026)

**Efter gennemført bytte:**
Kortet fjernes automatisk fra byttelisten (forTrade sættes til false). Ejeren har ændret sig, den nye ejer har ikke nødvendigvis tænkt det som byttekort.

**Hvilke kort kan foreslås i bytte:**
Spiller A kan kun vælge kort markeret "til bytte" fra Spiller B's samling. Respekterer spillerens intention, undgår spam og gør systemet mere troværdigt.

Holgers vigtigste pointe: "Bytte skal føles trygt og kontrolleret – ikke som alle kan bytte alt."

**Multi-kort bytter:**
Nej i første version. Hold det 1:1. Multi-kort bytter øger kompleksiteten markant i UI, logik og reservation. Kan udvides senere.

**Vis antal til bytte:**
Ja, vigtigt. Vis "X ejet / Y til bytte" på hvert kort i trade-siden og kortbillede-vælgeren. Giver overblik og undgår frustration.

---

### Deck – kortbillede-vælger (afklaret Sprint 2)

- Viser alle kort i systemet, ikke kun dem spilleren ejer
- Spillere bygger ofte decks før de har alle kortene, det er en del af hobbyen
- Advarsel vises tydeligt hvis et kort ikke er i spillerens samling
- Filter "Vis kun mine kort" godkendt som nice-to-have

---

### Design og farver (afklaret Sprint 2)

- Mørk header + lys baggrund + orange/rust accent er godkendt
- Orange = handling (tilmeld, accepter bytte, bekræft)
- Neutral hvid/grå til indhold, mørk til navigation
- Ingen for mange farver, ikke for mørkt overall, ikke for "flashy gaming UI"
- Funktionalitet før fancy

**Typografi og logo:**
- Standard system-fonte er fint i MVP
- "Deckforge" som tekst-logo er nok for nu
- På sigt ønskes et rigtigt logo der giver identitet
- Prioritering: 1) Funktionalitet, 2) Klart UI, 3) Konsistent design

---

## Generelle principper fra Holger

- Hellere en afgrænset og gennemført løsning end en kæmpe halvfærdig platform
- Samling, decks og events skal hænge logisk sammen
- Bytning og resultater må være lidt enklere, men stadig brugbare
- Systemet skal skabe reel værdi: mere struktur, mindre manuel administration, stærkere community
- Datakvalitet er kritisk: ingen dubletter, ingen overbookinger, ingen kort i to bytter samtidig
- Bytning understøtter bæredygtighed via cirkulation af eksisterende kort
- Alt skal pege tilbage på: "Det skal være nemt og trygt at bytte og deltage i events"