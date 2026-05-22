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
3. Bytteforløb

Bytteforløb blev valgt i stedet for "Slet Event" efter Holgers anbefaling.

Holgers begrundelse: Bytning viser community-værdi, status-flow, reservation og historik, og er stærkere end slet-funktionalitet til eksamen.

---

### Bytteflow – første afklaring

**Beslutning:** Spiller A foreslår bytte → kort reserveres → Spiller B accepterer/afviser inden 24 timer → spillere mødes fysisk → begge bekræfter i systemet → samlinger opdateres.

Holger understregede at accept alene ikke er nok. Byttet sker fysisk, og bekræftelsen sker i systemet efterfølgende.

**Reservation:** Kort låses i 24 timer. Udløber reservationen, frigives kortene automatisk.

---

### Events – fuldt event

Første version:

- Tydelig fejlbesked
- Disabled knap
- Venteliste er nice-to-have til version 2

---

### Kortdata og ansvar

Admin styrer kortdatabasen. Brugere må ikke frit oprette kort, fordi det kan give dubletter og stavefejl.

Brugere kan foreslå kort, men admin godkender.

Vigtigste kortfelter:

- Navn
- Type
- Farve
- Sæt
- Sjældenhed
- Billede via Scryfall URL

Kortdetaljer:

- Billede + basisinfo i listen
- "Åbn i Scryfall"-link
- Ingen intern detaljevisning i MVP

Kortdata holdes på **engelsk**. UI holdes på **dansk**.

---

### Registreringsflow og samlingssynlighed

Synlighed sættes ikke ved oprettelse, men efterfølgende i profil.

Default anbefalet: **Kun byttekort synlige**

Tre niveauer:

- Privat
- Kun byttekort
- Offentlig

---

### Admin vs. spiller – første afklaring

**Admin:**

- Se, rediger og slet/deaktiver spillere
- Se samling, decks og byttehistorik
- Admin blev først tænkt som systemrolle, ikke spillerrolle

**Spiller:**

- Kan se andre spilleres profiler og synlige samlinger
- Kan foreslå bytter
- Kan redigere og slette/deaktivere sin egen konto

**Opdateret senere:** Admin må gerne fungere som almindelig spiller, hvis admin også har profil, kort og decks. Admin-rettigheder og spillerhandlinger skal dog holdes adskilt.

---

### Wireframe feedback

**Event flow:** Events liste → Event detalje → Vælg deck → Bekræftelse.

Holger godkendte:

- "Registered" og "Full" badges
- Fejlbesked og disabled knap ved fuldt event eller allerede tilmeldt

**Bytteflow:** Collection → Mark for Trade → Opret bytte → Bekræftelse.

Holger godkendte:

- Spiller B modtager synlig besked/indikator
- Accept/Decline med tydelig feedback
- Udløbne forslag vises med advarsel og disabled knapper

**Deck flow:** Deck liste → Deck detalje → Tilføj kort.

---

### Domænemodel og arkitektur

8 domænebegreber:

- Spiller
- Kort
- SpillerKort
- Deck
- Event
- Eventtilmelding
- Bytte
- Resultat

Clean Architecture med fire lag.

Data persisteres i MySQL via repositories med JdbcTemplate.

---

### IT&F modeller

**BMC:** Value proposition skærpes til "Nem og tryg platform til at bytte kort og deltage i events".

Revenue streams kan udvides med fee pr. event og premium features.

Customer relationships skal inkludere in-app feedback og community engagement.

**SWOT:** Reservationsfunktionen som styrke er god. Brugeradoption er den største reelle risiko.

AI i opportunities skal knyttes konkret til kortgenkendelse og prisestimering, ikke bare nævnes som buzzword.

**TOWS:** Fokus på trust via reservation og samarbejde med community er korrekt differentiering.

---

### Brugerrejser

**Spiller (kerneflow):** Opdagelse → Opret konto → Tilføj kort → Opret deck → Tilmeld event → Resultat

**Bytterejse:** Find byttekort → Mark for Trade → Send forslag → Accept → Mødes fysisk → Bekræft → Samling opdateret

---

## Sprint 2 – Implementering

### Kortvisning

- Kortliste viser billede, navn, type, sjældenhed og sæt
- Ingen intern detaljevisning i MVP
- Knap: "Åbn i Scryfall"
- Kortdata på engelsk, UI på dansk

---

### Kortliste på mobil

- Grid-layout med kortbilleder som thumbnails på mobil
- Desktop beholder tabel-layout
- Ved klik: vis navn, basisinfo og link til Scryfall
- Grid er bedst på mobil, fordi spillere genkender kort visuelt hurtigere

---

### Profilbillede

Nice-to-have.

Hvis implementeret:

- URL til eksternt billede
- Ikke filupload

---

### Eventtilmelding og resultater

Første afklaring:

- Spillere kan afmelde sig indtil eventet starter
- Pladsen frigives ved afmelding
- Resultater registreres kun når event = COMPLETED
- Resultater vises på både event-siden og spillerens profil
- Tydelig eventstatus: UPCOMING → ONGOING → COMPLETED

Senere MVP-implementering prioriterede:

- Tilmelding til events med deck
- Tjek af fuldt event
- Tjek af allerede tilmeldt spiller
- Tjek af at deck tilhører spilleren
- Tjek af at deck-format matcher event-format
- Tjek af at decket opfylder formatets regler
- Resultater samlet på event-siden

Afmelding og venteliste kan behandles som polish/version 2, hvis der ikke nås mere.

---

### Trade flow detaljer

- Spiller A kan fortryde
- Ved fortrydelse frigives kortene med det samme
- Status = CANCELLED
- Accept er ikke nok til at markere byttet som gennemført
- Begge spillere skal bekræfte fysisk gennemførsel
- Byttehistorik viser status, dato, kort og hvem man byttede med

**Trade statuser:**

- PENDING
- ACCEPTED
- COMPLETED
- DECLINED
- CANCELLED
- EXPIRED

---

### Trade – 15. maj 2026

**Efter gennemført bytte:**

Kortet fjernes automatisk fra byttelisten (`forTrade = false`).

Begrundelse: Ejeren har ændret sig, og den nye ejer har ikke nødvendigvis tænkt kortet som byttekort.

**Hvilke kort kan foreslås i bytte:**

Spiller A kan kun vælge kort markeret "til bytte" fra Spiller B's samling.

Holgers pointe:

> Bytte skal føles trygt og kontrolleret – ikke som alle kan bytte alt.

**Multi-kort bytter:**

Nej i MVP. Hold det 1:1.

Multi-kort bytter øger kompleksiteten i UI, logik og reservation.

**Vis antal til bytte:**

Ja.

Vis fx:

- X ejet
- Y til bytte

Det giver overblik og undgår frustration.

---

### Deck – kortbillede-vælger

- Viser alle kort i systemet, ikke kun dem spilleren ejer
- Spillere bygger ofte decks før de ejer alle kort
- Advarsel vises tydeligt hvis et kort ikke er i spillerens samling
- Filter "Vis kun mine kort" er nice-to-have

---

### Design og farver

- Mørk header
- Lys baggrund
- Orange/rust accent
- Orange bruges til handlinger som tilmeld, accepter bytte og bekræft
- Neutral hvid/grå til indhold
- Mørk farve til navigation
- Ikke for mange farver
- Ikke for mørkt overall
- Ikke for "flashy gaming UI"
- Funktionalitet før fancy

**Typografi og logo:**

- Standard system-fonte er fint i MVP
- "Deckforge" som tekst-logo er nok for nu
- Rigtigt logo kan komme senere

Prioritering:

1. Funktionalitet
2. Klart UI
3. Konsistent design

---

## Sprint 3 – UI, sikkerhed og afklaringer

### Demo-kortdatabase

Holger vurderede at 8 kort er for lidt.

MVP/demo bør have ca. **20–30 kort**.

Der bør være:

- Flere basic lands: Plains, Island, Swamp, Mountain, Forest
- Flere creatures
- Instants/sorceries
- Artifacts
- Enchantments
- 1–2 legendary creatures
- Variation i rarity fra common til mythic

Formål: Systemet skal føles som et lille realistisk card pool, ikke bare teknisk demo.

---

### Søgning, filtrering og sortering

**Kortliste:**

Prioritet:

1. Fritekstsøgning på kortnavn
2. Filter på farve og type
3. Sortering

Hvis der kun nås én ting, er søgning vigtigst.

**Deck-vælger:**

- Søgning er god idé
- Filter er nice-to-have

**Kortsamling:**

- Søgning er nok i MVP
- Filter kan vente

---

### Desktop og mobilvisning

- Mobil = grid-layout
- Desktop = tabel-layout
- Ingen toggle mellem grid og tabel i MVP

Begrundelse: Desktop tabel giver bedst overblik, mobil grid giver bedst visuel genkendelse.

---

### Admin-fane

Holger forventer ikke en dedikeret admin-fane i MVP.

Admin-funktioner kan eksistere, men bør være skjulte/simple og ikke tage fokus fra spilleroplevelsen.

---

### Spillerliste for admin

Admin-visning bør vise:

- ID
- Navn
- Email
- Rolle
- Samlingssynlighed
- Status aktiv/deaktiveret, hvis muligt

---

### Profil og samlingssynlighed

**Offentlig:**

- Andre kan se samling
- Andre kan se decks
- Andre kan se byttekort

**Kun byttekort:**

- Andre kan kun se kort markeret til bytte
- Spilleren styrer stadig selv hvilke kort der er til bytte
- Profilniveauet markerer ikke automatisk alle kort som byttekort

**Privat – tidligere afklaring:**

Tidligere var planen, at privat spiller stadig blev vist i spillerlisten med "Privat profil"-badge.

**Endelig MVP-beslutning:**

Privat spiller er helt skjult for andre spillere og kun synlig for admins.

Det betyder:

- Private spillere vises ikke i almindelige spilleres spillerliste
- Private spillere kan ikke findes af andre spillere
- Andre spillere kan ikke starte nye bytter med private spillere
- Admins kan stadig se private spillere
- Private spillere kan stadig selv bruge systemet normalt

Privat spiller kan:

- Se andre spillere
- Se offentlige profiler
- Se samlinger og decks, hvis de ikke er private
- Foreslå bytte

Hvis en privat spiller sender et bytteforslag:

- Modtageren skal kunne se hvem afsenderen er
- Modtageren skal kunne se de relevante kort i byttet

Princip:

> Privat = usynlig for andre spillere, men stadig aktiv i systemet.

---

### Standard sortering

Holger anbefalede:

- Spillerliste: alfabetisk på navn
- Kortliste: alfabetisk på navn
- Kortsamling: alfabetisk på kortnavn
- Deckliste: alfabetisk på navn
- Events: efter dato, næste først

Konsistens er vigtigere end fancy sortering.

---

### Start af bytte

Holger anbefalede:

- Fra spillerprofil: "Foreslå bytte"
- Fra spillerens samling/kort: "Foreslå bytte"

Begge flows giver mening:

- Nogle tænker "jeg vil bytte med den person"
- Andre tænker "jeg vil have det kort"

Hvis kun ét flow vælges, prioriteres start fra samling højest.

---

### Trade-overblik

Holger foretrækker én samlet side frem for tre separate sider.

Én side med sektioner:

- Dine forslag
- Modtagne forslag
- Historik

Vigtigst:

- Modtagne forslag skal fremhæves tydeligt som "kræver handling"

---

### Double confirmation

Holger bekræftede at begge spillere skal bekræfte gennemførsel, også i MVP.

Flow:

1. B accepterer
2. Status = ACCEPTED
3. Begge spillere får "Bekræft gennemført"
4. Først når begge har bekræftet, sættes status = COMPLETED

Hvis kun én bekræfter, skal byttet stadig afvente den anden bekræftelse.

Begrundelse: Byttet sker fysisk, og én bekræftelse er for svag i forhold til tillid.

---

### Admin som spiller

Admins må gerne vises på spillerlisten, men skal markeres tydeligt som Admin.

Admin må gerne:

- Have profil
- Have kort
- Have decks
- Bytte som almindelig spiller

Men:

- Admin-handlinger må kun ses af admins
- Almindelige spillere må aldrig kunne redigere/slette admin
- Rettigheder og spillerhandlinger skal holdes adskilt

---

### Forsideindikator for bytteanmodninger

Holger ønsker en simpel indikator på forsiden i MVP.

Eksempler:

- "Du har 1 indgående bytteanmodning"
- Badge på trade-knappen

Det skal ikke være:

- Push-notifikationer
- Email
- Realtime/WebSocket

Begrundelse:

- Indgående bytter kræver handling
- Trade-overview alene er for skjult
- Lille indikator giver stor værdi

---

## Generelle principper fra Holger

- Hellere en afgrænset og gennemført løsning end en kæmpe halvfærdig platform
- Samling, decks og events skal hænge logisk sammen
- Bytning og resultater må være enklere, men stadig brugbare
- Systemet skal skabe reel værdi: mere struktur, mindre manuel administration, stærkere community
- Datakvalitet er kritisk: ingen dubletter, ingen overbooking, ingen kort i to bytter samtidig
- Bytning understøtter bæredygtighed via cirkulation af eksisterende kort
- Alt skal pege tilbage på: "Det skal være nemt og trygt at bytte og deltage i events"

---

## Sprint 4 – Samlet MVP-status og Holgers vurdering

Efter første samlede udkast blev Holger præsenteret for systemets aktuelle funktioner: login/roller, kort, samlinger, decks, deck-regler, events, tilmelding, resultater, trades, visibility, mobilvisning og teststatus.

Holgers vurdering:

> Det her er stærkt. Det er en fuld MVP – og mere til.

Holger fremhævede, at systemet nu har:

- Core flows: deck, event og trade
- Forretningslogik: regler og validation
- UX-overvejelser: mobil, visibility og tydeligere flows
- Stabilitet: 211 grønne tests

Holgers vigtigste konklusion:

> Jeg kan forstå, bruge og stole på systemet.

Efter denne vurdering er fokus ændret fra nye features til polish:

- Små UX-forbedringer
- Tydelig feedback ved fejl, status og success
- Smooth demo-flow
- Rapport og dokumentation

Holgers konklusion var, at han ville godkende systemet som MVP uden problemer.

## Visuel stil – endelig MVP-retning

Holger blev præsenteret for den nuværende visuelle stil: varme grå/hvide toner, mørk header/navigation, orange/rød-orange 
accentfarve til handlinger, tydelige bokse/cards og et roligt butik/community-udtryk fremfor flashy gaming UI.

Holger godkendte retningen og vurderede, at designet føles roligt, troværdigt og praktisk til butik/events.

Vigtige designprincipper:
- Orange bruges konsekvent til handlinger som tilmeld, accepter bytte og andre vigtige knapper.
- Kontrast og læsbarhed skal prioriteres, især på mobil.
- Cards/bokse skal have nok spacing og må ikke føles for tætte.

Holgers konklusion var, at stilen er spot on, og at der ikke er behov for store visuelle ændringer.