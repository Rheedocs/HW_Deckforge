# Deckforge
---
## Beskrivelse
Deckforge er et webbaseret community-system til Magic: The Gathering spillere. Spillere kan administrere deres kortsamling og decks, tilmelde sig events og bytte kort med andre spillere på en sikker og struktureret måde.
(Zealand, 2. semester, DAT-2025)
---
## Teknologier
- **Java 21** er det primære programmeringssprog og grundlaget for hele programmet.
- **Spring Boot** håndterer controllers og starter en indbygget Tomcat-server, så hjemmesiden er tilgængelig når programmet kører.
- **MySQL** bruges til at gemme og hente data via en relationel database.
- **JdbcTemplate** bruges til at kommunikere med databasen fra Java-koden.
- **Thymeleaf** bruges til at indsætte data i HTML-siderne og returnere dem til brugeren.
- **BCrypt** bruges til sikker hashing af adgangskoder, så brugerdata beskyttes.
- **CSS** bruges til styling af hjemmesiden med responsivt layout til både mobil og desktop.
- **JavaScript** bruges til pagination, filtrering og bekræftelsesdialoger på kritiske handlinger.
---
## Arkitektur
Projektet følger Clean Architecture med en lagdelt struktur:
- **Presentation** modtager HTTP-requests via controllers og returnerer Thymeleaf-templates
- **Application** indeholder services med forretningslogik og repository-interfaces
- **Domain** indeholder entiteter, enums, exceptions og validators
- **Infrastructure** implementerer repositories med JdbcTemplate og MySQL samt ScryfallService til kortbilleder
---
## Funktionalitet
- **Kortsamling** håndterer tilføjelse, fjernelse og markering af kort til bytte
- **Decks** håndterer oprettelse og redigering af decks med Magic-formatvalidering
- **Events** håndterer oprettelse, tilmelding og resultater med automatisk statusopdatering
- **Bytning** håndterer bytteforslag med reservation, double confirmation og automatisk udløb efter 24 timer
- **Spillerprofiler** med synlighedsstyring (PUBLIC, TRADE_ONLY, PRIVATE)
- **Login og logout** med email og adgangskode samt rollebaseret adgangskontrol (PLAYER og ADMIN)
---
## Opsætning
- Åbn projektet i IntelliJ
- Kopier `application.properties.example` til `application.properties` og udfyld dit MySQL username og password
- Kør schema.sql og data.sql i din MySQL-database
- Kør `HwDeckforgeApplication.java`
- Åbn din browser og gå til `localhost:8080`
- Log ind med `goncalo@deckforge.dk` og password `password123` for demo
---
## Test
Unit tests køres med `.\mvnw test` eller grøn play-knap i IntelliJ.
Tests dækker alle centrale services med Mockito, domæneklasser og repositories med H2 in-memory database. Projektet har 214 grønne tests.
---
## Gruppe
Hello World:
Nicki, Goncalo, Mattias
