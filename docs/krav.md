# Krav – Andensemesterprøve 2026

Gruppe 8 – Hello World: Mattias E., Nicki og Goncalo  
Vejledere: Camilla Gejl Dilling-Hansen (IT&F), Mikkel Nørgaard Hansen (Systemudvikling/Teknologi), Mikkel Krøll Christensen (Programmering)

---

## Formål med dokumentet

Dette er gruppens interne krav- og kontekstfil.

Den bruges som huskenote i projektet, så vi kan holde styr på:

- krav fra eksamensmodulet
- krav udledt af interview med Holger
- senere afklaringer fra Holger
- hvad der er MVP og hvad der er nice-to-have

Hvis Holger senere har præciseret noget andet end det oprindelige materiale, følger vi den nyeste Holger-afklaring.

---

## Casebeskrivelse

En lokal kæde af spilbutikker ønsker en samlet digital platform til Magic: The Gathering-communityet.

I dag foregår administration via sociale medier, regneark, papirnoter og beskeder. Det giver manglende overblik, fejl i tilmeldinger, uklare bytteaftaler og besværlig opfølgning på resultater.

**Løsningen:** Responsiv webapplikation i Java Spring Boot kaldet Deckforge.

**Product owner:** Holger, franchisetager i Næstved.

Applikationen skal understøtte:

- Samling
- Decks
- Events
- Bytning
- Resultater

Løsningen skal fungere på mobil, tablet og computer og persistere data i en database.

---

## Eksamensperiode

- Projektperiode: 30. april – 28. maj 2026
- Mundtlige eksamener: 9.–12. juni 2026
- Der forventes fortsat kodning og forbedring mellem aflevering og eksamen

---

## Formalia

- Maks. 30 normalsider
- Sidenummerering og indholdsfortegnelse
- Nummererede bilag
- Link til åben GitHub repository
- Zip-fil af kildekode på WiseFlow
- Minimum 10 meningsfulde commits per studerende
- Minimum 7–8 sider i rapporten per studerende

---

## Fagkrav

### IT og Forretningsudvikling

Skal minimum inkludere:

- Mindst én BPMN over en proces hos slutbrugeren
- Wireframes eller mockup af løsningen
- Mindst to valgfrie elementer fra faget, fx BMC, SWOT, TOWS, GDPR, bæredygtighed eller IT Governance
- Tydelig rød tråd mellem IT&F og de andre fag
- Argumentation for valg af modeller

---

### Teknologi

Skal minimum inkludere:

- E/R diagram over databasen
- Dokumentation af omformning til tabeldesign
- Normalformer, 1NF–3NF
- Overvejelser om operativsystem og hardware
- Overvejelser om tråde i programmet, gerne med eksempel

---

### Systemudvikling

Skal minimum inkludere:

- Iterativ og agil projektplanlægning med SCRUM
- GitHub Scrumboard
- Domænemodel
- Use cases
- System sekvensdiagrammer, SSD
- Pakke- og klassediagram
- Sekvensdiagrammer
- Clean Architecture
- Design patterns
- Brugergrænsefladedesign testet med projektstiller
- Refleksionsafsnit om processen

---

### Programmering

Skal minimum inkludere:

- Konkret applikation baseret på casebeskrivelsen
- Lagdelt softwarearkitektur / Clean Architecture
- Datapersistens i database med begrundelse
- Testmetoder beskrevet og begrundet
- Unit tests, hvor alle gruppemedlemmer har kodet nogle
- Beskrivelse af datastrukturer med argumentation
- Kildekode på GitHub og som zip

---

## Teknisk stack

- Java 21
- Spring Boot
- Thymeleaf
- JdbcTemplate
- MySQL
- BCrypt
- JUnit 5
- Mockito
- H2 til test
- Clean Architecture

---

## Overordnede principper fra Holger

- Hellere afgrænset og gennemført end kæmpe og halvfærdig
- Samling, decks og events skal hænge logisk sammen
- Bytning og resultater må være enklere, men stadig brugbare
- Systemet skal skabe reel værdi: mere struktur, mindre manuel administration og stærkere community
- Datakvalitet er kritisk
- Ingen dubletter
- Ingen overbooking
- Ingen kort i to aktive bytter samtidig
- Kortdata på engelsk
- UI på dansk
- Mobile first
- Funktionalitet før fancy design
- Det skal være nemt og trygt at bytte og deltage i events

---

## MVP – Must have

### Bruger og profil

MVP skal understøtte:

- Brugeroprettelse
- Login
- Profil
- Roller: spiller og admin
- Samlingssynlighed
- Mulighed for soft delete/deaktivering

Samlingssynlighed:

- Offentlig
- Kun byttekort
- Privat

---

### Privat profil

Endelig MVP-beslutning fra Holger:

- Privat spiller er skjult for andre spillere
- Privat spiller vises ikke i almindelige spilleres spillerliste
- Privat spiller kan ikke findes eller vælges som modtager af nye bytter
- Kun admins kan se private spillere

Private spillere kan stadig:

- Logge ind
- Se andre spillere
- Se offentlige profiler
- Se offentlige samlinger og decks
- Foreslå bytte
- Bruge systemet normalt

Andre spillere kan ikke se:

- Profilen
- Samlingen
- Decks
- Turneringer
- Resultater
- Kortantal
- Deckantal
- Byttefunktioner

Hvis en privat spiller sender et bytteforslag:

- Modtageren skal kunne se afsenderen
- Modtageren skal kunne se de relevante kort i byttet

Kort princip:

> Privat = usynlig for andre spillere, men stadig aktiv i systemet.

---

### Admin

Admin skal kunne:

- Se spillere
- Redigere spillere
- Deaktivere/slette spillere
- Se relevante data til administration

Admin-visning bør vise:

- ID
- Navn
- Email
- Rolle
- Samlingssynlighed
- Aktiv/deaktiveret status, hvis muligt

Der skal ikke være en stor dedikeret admin-fane i MVP, hvis det tager fokus fra spilleroplevelsen.

Admin må gerne også fungere som almindelig spiller:

- Admin kan have profil
- Admin kan have kort
- Admin kan have decks
- Admin kan deltage i bytter

Vigtigt:

- Admin-handlinger må kun ses af admins
- Almindelige spillere må aldrig kunne redigere eller slette admin
- Admin-rettigheder og spillerhandlinger skal være adskilt

---

### Kort og samling

MVP skal understøtte:

- Kortdatabase
- Spillerens samling
- Antal kort
- Markering af kort til bytte
- Kortdata på engelsk
- UI på dansk

Admin styrer kortdatabasen.

Brugere må ikke frit oprette kort, fordi det kan give dubletter og stavefejl.

Vigtige kortfelter:

- Navn
- Type
- Farve
- Sæt
- Sjældenhed
- Billede via Scryfall URL

Kortliste:

- Viser billede, navn, type, sjældenhed og sæt
- Har "Åbn i Scryfall"-link
- Behøver ikke intern detaljevisning i MVP

Demo-data:

- 8 kort er for lidt
- Der bør være ca. 20–30 kort til demo
- Der bør være variation i farve, type og rarity
- Der bør være basic lands
- Der bør være 1–2 legendary creatures

---

### Søgning, filter og sortering

Prioritet ifølge Holger:

1. Søgning
2. Simple filtre
3. Sortering

Kortliste:

- Fritekstsøgning på kortnavn er must-have
- Filter på farve og type er værdifuldt
- Sortering er lavere prioritet

Deck-vælger:

- Søgning er god idé
- Filter er nice-to-have

Kortsamling:

- Søgning er nok i MVP
- Filter kan vente

Standard sortering:

- Spillerliste alfabetisk på navn
- Kortliste alfabetisk på navn
- Kortsamling alfabetisk på kortnavn
- Deckliste alfabetisk på navn
- Events sorteres efter dato, kommende først

---

### Desktop og responsivt design

Løsningen laves til desktop, men med responsivitet som fokus.

Holger-afklaring:

- Mobil: grid-layout med kortbilleder
- Desktop: tabel-layout
- Ingen grid/tabel-toggle på desktop i MVP

Begrundelse:

- Desktop tabel giver bedst overblik
- Mobil grid giver bedst visuel genkendelse
- Toggle giver ekstra kompleksitet uden stor MVP-værdi

---

### Decks

MVP skal understøtte:

- Oprettelse af decks
- Decknavn
- Format
- Tilknyttede kort
- Deckliste
- Deckdetalje

Deck-vælger:

- Viser alle kort i systemet, ikke kun dem spilleren ejer
- Spillere bygger ofte decks før de ejer alle kort
- Der skal vises tydelig advarsel hvis et kort ikke er i spillerens samling

Nice-to-have:

- Filter "Vis kun mine kort"

---

### Events

MVP skal understøtte:

- Eventliste
- Eventdetalje
- Tilmelding
- Kapacitet
- Status

Eventstatus:

- UPCOMING
- ONGOING
- COMPLETED

Ved fuldt event:

- Tydelig fejlbesked
- Disabled knap

Spillere kan afmelde sig indtil eventet starter.

Ved afmelding frigives pladsen.

Events skal sorteres efter dato, kommende først.

Nice-to-have:

- Venteliste

---

### Resultater

MVP skal understøtte:

- Resultater på events
- Resultater registreres kun når event = COMPLETED
- Resultater vises på event-siden
- Resultater kan vises på spillerens profil, hvis profilen ikke er privat

Ved privat profil vises turneringer og resultater ikke for andre spillere.

---

### Bytning

MVP skal understøtte:

- Markering af kort til bytte
- Forslag af bytte
- Reservation af kort
- Accept
- Afvisning
- Annullering
- Udløb
- Double confirmation
- Historik

Holgers kerneprincip:

> Bytte skal føles trygt og kontrolleret – ikke som alle kan bytte alt.

---

### Bytteflow

Flow:

1. Spiller A foreslår bytte
2. Kort reserveres i 24 timer
3. Status = PENDING
4. Spiller B accepterer eller afviser
5. Ved accept sættes status = ACCEPTED
6. Begge spillere mødes fysisk
7. Begge spillere bekræfter gennemførsel i systemet
8. Først når begge har bekræftet, sættes status = COMPLETED
9. Kortene fjernes fra byttelisten

Hvis kun én spiller bekræfter:

- Byttet forbliver i accepteret/afventende tilstand
- Det er ikke completed endnu

Trade statuser:

- PENDING
- ACCEPTED
- COMPLETED
- DECLINED
- CANCELLED
- EXPIRED

---

### Byttebegrænsninger

MVP-beslutninger:

- Kun 1:1 bytter
- Multi-kort bytter er ikke MVP
- Man kan kun foreslå bytte på kort markeret til bytte
- Kort i aktive bytter må ikke kunne bruges i andre aktive bytter
- Kort reserveres i 24 timer
- Udløbne reservationer frigives automatisk

Efter completed trade:

- Kort fjernes automatisk fra byttelisten
- Ny ejer skal selv vælge om kortet igen skal være til bytte

---

### Start af bytte

Holger foretrækker at bytte kan startes fra:

- Spillerprofil
- Spillerens samling/kort

Begge giver mening:

- "Jeg vil bytte med den person"
- "Jeg vil have det kort"

Hvis kun ét flow vælges, prioriteres start fra samling højest.

Bytteoversigten bør ikke have en stor "Foreslå bytte"-knap, medmindre den bare leder til spillerlisten.

---

### Trade-overblik

Holger anbefaler én samlet trade-side frem for tre separate sider.

Trade-overview bør have sektioner:

- Dine forslag
- Modtagne forslag
- Historik

Modtagne forslag skal fremhæves tydeligt, fordi de kræver handling.

---

### Forsideindikator for bytteanmodninger

MVP skal have en simpel indikator på forsiden/dashboardet, hvis brugeren har indgående bytteanmodninger.

Eksempler:

- "Du har 1 indgående bytteanmodning"
- Badge på trade-knappen

Ikke MVP:

- Push-notifikationer
- Email-notifikationer
- Realtime/WebSocket

Begrundelse:

- Indgående bytter kræver handling
- Trade-overview alene er for skjult
- Simpel indikator giver stor UX-værdi

---

## Nice-to-have / version 2

Kan beskrives som udvidelser:

- Venteliste til events
- Profilbillede via URL
- Ønskelister
- Chat
- Push-notifikationer
- Email-notifikationer
- Fuld turneringsmotor
- Betalingsløsning
- Integration til eksterne kortdatabaser
- Multi-kort bytter
- Grid/tabel-toggle på desktop
- Avancerede filtre
- Filter "Vis kun mine kort" i deckbuilder
- Rigtigt logo
- Premium features
- AI-kortgenkendelse
- Prisestimering

---

## UI og design

Holger har godkendt:

- Mørk header
- Lys baggrund
- Orange/rust accent
- Orange til handlinger
- Neutral hvid/grå til indhold
- Mørk navigation
- Standard system-fonte
- "Deckforge" som tekst-logo i MVP

Undgå:

- For mange farver
- For mørkt overall design
- For flashy gaming UI

Prioritet:

1. Funktionalitet
2. Klart UI
3. Konsistent design

---

## Vigtige ting at huske ved implementering

- Tjek at private spillere skjules for almindelige spillere
- Admin skal stadig kunne se private spillere
- Private spillere kan stadig selv bruge systemet
- Bytter kræver double confirmation
- Forsiden skal vise indikator for indgående bytter
- Trade-overview skal fremhæve modtagne forslag
- Kort må ikke være i to aktive bytter samtidig
- Kort fjernes fra byttelisten efter completed trade
- Desktop tabel, mobil grid
- Søgning på kortliste er vigtigere end avanceret filter
