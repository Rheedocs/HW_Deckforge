package dk.zealand.hw_deckforge.domain;

import dk.zealand.hw_deckforge.domain.enums.EventStatus;
import dk.zealand.hw_deckforge.domain.enums.Format;

import java.time.LocalDate;

public class Event {
    private int id;
    private String name;
    private String location;
    private LocalDate date;
    private int maxPlayers;
    private EventStatus status;
    private Format format;

    public Event(int id, String name, String location, LocalDate date, int maxPlayers, EventStatus status, Format format) {
        validate(name, maxPlayers);
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.maxPlayers = maxPlayers;
        this.status = status;
        this.format = format;
    }

    // --- Getters ---

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public LocalDate getDate() { return date; }
    public int getMaxPlayers() { return maxPlayers; }
    public EventStatus getStatus() { return status; }
    public Format getFormat() { return format; }

    // --- Setters ---

    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
    public void setFormat(Format format) { this.format = format; }
    public void setStatus(EventStatus status) { this.status = status; }

    // --- Statustjek ---

    public boolean isUpcoming() { return this.status == EventStatus.UPCOMING; }
    public boolean isOngoing() { return this.status == EventStatus.ONGOING; }
    public boolean isCompleted() { return this.status == EventStatus.COMPLETED; }
    public boolean isCancelled() { return this.status == EventStatus.CANCELLED; }

    // --- Validering ---

    private void validate(String name, int maxPlayers) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Eventnavn må ikke være tomt");
        if (maxPlayers < 1) throw new IllegalArgumentException("Antal spillere skal være mindst 1");
    }
}