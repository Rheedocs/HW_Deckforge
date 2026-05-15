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

**Admin (fra spillerliste):**
- Se, rediger og slet/deaktiver spillere
- Se samling, decks og byttehistorik (read-only)
- Må ikke foreslå bytter (admin er systemrolle, ikke spillerrolle)

**Spiller:**
- Kan se andre spilleres profiler og synlige samlinger
- Kan foreslå bytter
- Kan redigere og slette/deaktivere sin egen konto (soft delete anbefalet)

---

### Wireframe feedback (godkendt af Holger)

Holger godkendte wireframes med følgende elementer:

**Event flow:**
- Events liste → Event detalje → Vælg deck → Bekræftelse
- "Registered" og "Full" badges på listen
- Fejlbesked og disabled knap ved fuldt event eller allerede tilmeldt

**Bytteflow:**
- Collection → Mark for Trade → Opret bytte → Bekræftelse
- Spiller B modtager notifikation på dashboard
- Accept viser "Trade accepted", Decline viser "Trade declined"
- Udløbne forslag vises med advarsel og disabled knapper

**Deck flow:**
- Deck liste → Deck detalje → Tilføj kort

---

### Domænemodel og arkitektur (godkendt)

**8 domænebegreber:** Spiller, Kort, SpillerKort, Deck, Event, Eventtilmelding, Bytte, Resultat

**Arkitektur:** Clean Architecture med fire lag: domain, application, infrastructure, presentation.  
Data persisteres i MySQL via repositories med JdbcTemplate.

---

### IT&F modeller (godkendt med feedback)

**BMC:**
- Kunder: MTG-spillere og kortbyttere
- Partnere: Wizards of the Coast, lokale butikker
- Indtægt: Event tilmeldinger, køb under events
- Feedback: Value proposition skal skærpes til "Nem og tryg platform til bytning og events"

**SWOT:**
- Styrker: Reservation feature, rollebaseret adgang, struktureret eventstyring
- Svagheder: Teknisk vedligehold, brugeradoption, afhængighed af aktive brugere
- Muligheder: Voksende interesse for digitale TCG-platforme, AI til kortgenkendelse
- Trusler: AI-konkurrenter, skepsis mod nye platforme, GDPR-krav

**TOWS:**
- SO: Samarbejd med MTG-streamere ved launch
- ST: Brug reservationsfunktionen aktivt i markedsføring
- WO: Tutorials og community Q&A for at øge adoption
- WT: Simpel onboarding, automatiser kortopdateringer via API

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

### Profilbillede

Nice-to-have. Hvis implementeret: URL til eksternt billede (ikke filupload).

---

### Event tilmelding og resultater

- Spillere kan afmelde sig indtil eventet starter → pladsen frigives
- Resultater registreres kun når event = COMPLETED
- Resultater vises på både event-siden og spillerens profil

---

### Trade flow detaljer

- Spiller A fortryder → kortene frigives med det samme, status = "Cancelled"
- Accept er ikke nok til at markere byttet som gennemført
- Begge spillere bekræfter fysisk gennemførelse i systemet
- Byttehistorik viser: status, dato (oprettet + gennemført), hvilke kort, hvem man byttede med

**Trade statuser:** Pending / Accepted / Completed / Cancelled / Expired

---

### Kortliste – detaljegrad (afklaret i Sprint 2)

Holger bekræftede: alt vigtigt vises i listen, ingen separat kortdetalje-side, kun "Åbn i Scryfall"-knap.

---

## Generelle principper fra Holger

- Hellere en afgrænset og gennemført løsning end en kæmpe halvfærdig platform
- Samling, decks og events skal hænge logisk sammen
- Bytning og resultater må være lidt enklere, men stadig brugbare
- Systemet skal skabe reel værdi: mere struktur, mindre manuel administration, stærkere community
- Datakvalitet er kritisk: ingen dubletter, ingen overbookinger, ingen kort i to bytter samtidig
- Bytning understøtter bæredygtighed via cirkulation af eksisterende kort