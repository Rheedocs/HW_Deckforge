# Krav – Andensemesterprøve 2026

Gruppe 8 – Hello World: Mattias E., Nicki og Goncalo  
Vejledere: Camilla Gejl Dilling-Hansen (IT&F), Mikkel Nørgaard Hansen (Systemudvikling/Teknologi), Mikkel Krøll Christensen (Programmering)

---

## Casebeskrivelse

En lokal kæde af spilbutikker ønsker en samlet digital platform til Magic: The Gathering-communityet.

I dag foregår administration via sociale medier, regneark, papirnoter og beskeder. Det giver manglende overblik, fejl i tilmeldinger, uklare bytteaftaler og besværlig opfølgning på resultater.

**Løsningen:** Responsiv webapplikation i Java Spring Boot kaldet Deckforge.

**Product owner:** Holger, franchisetager i Næstved.

**Applikationen skal understøtte:**
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

### IT og Forretningsudvikling (Camilla)

Skal minimum inkludere:
- Mindst én BPMN over en proces hos slutbrugeren
- Wireframes eller mockup af løsningen
- Mindst to valgfrie elementer fra faget (fx BMC, SWOT, TOWS, GDPR, bæredygtighed, IT Governance)
- Tydelig rød tråd mellem IT&F og de andre fag
- Argumentation for valg af modeller

### Teknologi (Mikkel N.)

- E/R diagram over databasen
- Dokumentation af omformning til tabeldesign
- Normalformer (1NF–3NF)
- Overvejelser om operativsystem og hardware
- Overvejelser om tråde i programmet, gerne med eksempel

### Systemudvikling (Mikkel N.)

- Iterativ og agil projektplanlægning med SCRUM (GitHub Scrumboard)
- Domænemodel, use cases og system sekvens diagrammer (SSD)
- Pakke- og klassediagram
- Sekvensdiagrammer
- Clean Architecture
- Design patterns (tydeligt hvilke der bruges)
- Brugergrænsefladedesign testet med projektstiller
- Refleksionsafsnit om processen

### Programmering (Mikkel K.)

- Konkret applikation baseret på casebeskrivelsen
- Lagdelt softwarearkitektur (Clean Architecture)
- Datapersistens i database med begrundelse
- Testmetoder beskrevet og begrundet
- Unit tests – alle gruppemedlemmer skal kode nogle
- Beskrivelse af datastrukturer med argumentation
- Kildekode på GitHub og som zip

---

## Casens kernefunktionalitet (udledt af interview med Holger)

**Must have:**
- Brugeroprettelse, login og profil
- Samling med kort, antal og bytestatus
- Decks med format og tilknyttede kort
- Events med tilmelding, kapacitet og status
- Bytteflow med reservation og bekræftelse
- Resultater på events

**Nice to have (kan beskrives som udvidelse):**
- Venteliste til events
- Profilbillede (URL)
- Ønskelister
- Chat
- Push-notifikationer
- Fuld turneringsmotor
- Betalingsløsning
- Integration til eksterne kortdatabaser

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
- H2 (test)
- Clean Architecture

---

## Vigtige principper fra Holger

- Hellere afgrænset og gennemført end kæmpe og halvfærdig
- Samling, decks og events skal hænge logisk sammen
- Datakvalitet er kritisk: ingen dubletter, ingen overbooking, ingen kort i to bytter samtidig
- Kortdata på engelsk, UI på dansk
- Mobile first