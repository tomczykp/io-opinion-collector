package pl.lodz.p.it.opinioncollector.events;

import java.util.UUID;

public class Event {
    private UUID eventID;
    private UUID userID;
    private String description;

    private EventStatus status;

    public Event(UUID eventID, UUID userID, String description) {
        this.eventID = eventID;
        this.userID = userID;
        this.description = description;
        this.status = EventStatus.Open;
    }

    public void changeStatus(EventStatus status) {
        this.status = status;
    }

    public EventStatus getStatus() {
        return status;
    }

    public UUID getEventID() {
        return eventID;
    }

    public UUID getUserID() {
        return userID;
    }

    public String getDescription() {
        return description;
    }

}
