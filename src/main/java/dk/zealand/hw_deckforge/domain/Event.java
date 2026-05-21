package dk.zealand.hw_deckforge.domain;

import dk.zealand.hw_deckforge.domain.enums.EventStatus;
import dk.zealand.hw_deckforge.domain.enums.Format;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private int id;
    private String name;
    private String location;
    private LocalDate date;
    private int maxPlayers;
    private EventStatus status;
    private Format format;

    public Event() {}

    public Event(int id, String name, String location, LocalDate date, int maxPlayers, EventStatus status, Format format) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.maxPlayers = maxPlayers;
        this.status = status;
        this.format = format;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public LocalDate getDate() { return date; }
    public int getMaxPlayers() { return maxPlayers; }
    public EventStatus getStatus() { return status; }
    public Format getFormat() { return format; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
    public void setFormat(Format format) { this.format = format; }
    public void setStatus(EventStatus status) { this.status = status; }
    public void setEventId(int eventId){this.id = eventId; }

    public boolean isUpcoming() { return this.status == EventStatus.UPCOMING; }
    public boolean isOngoing() { return this.status == EventStatus.ONGOING; }
    public boolean isCompleted() { return this.status == EventStatus.COMPLETED; }
    public boolean isCancelled() { return this.status == EventStatus.CANCELLED; }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();

        if (name == null || name.isBlank())
            errors.add("Navn må ikke være tomt");

        if (location == null || location.isBlank())
            errors.add("Lokation må ikke være tom");

        if (date == null)
            errors.add("Dato må ikke være tom");
        else if (date.isBefore(LocalDate.now()))
            errors.add("Dato må ikke være i fortiden");

        if (maxPlayers <= 1)
            errors.add("Maks spillere skal være mindst 2");

        if (status == null)
            errors.add("Status skal angives");

        if (format == null)
            errors.add("Format skal angives");

        return errors;
    }
}