package pl.lodz.p.it.opinioncollector.eventHandling.events;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@ToString
public abstract class Event {
    @Id
    @Column(name = "eventID")
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

    public Event() {
        this.eventID = UUID.randomUUID();
        this.status = EventStatus.Open;
    }

    public void changeStatus(EventStatus status) {
        this.status = status;
    }
}
