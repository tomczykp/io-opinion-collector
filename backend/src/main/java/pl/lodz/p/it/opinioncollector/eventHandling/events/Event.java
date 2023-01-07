package pl.lodz.p.it.opinioncollector.eventHandling.events;

import jakarta.persistence.*;
import lombok.*;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.UUID;

@Entity
@Getter
@ToString
public abstract class Event {
    @Id
    @Column(name = "eventID")
    private UUID eventID;
    @ManyToOne()
    @JoinColumn(
            name = "userid",
            referencedColumnName = "id"
    )
    private User user;
    private String description;

    private EventStatus status;

    public Event(UUID eventID, User user, String description) {
        this.eventID = eventID;
        this.user = user;
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

    public UUID getUserID()
    {
        return user.getId();
    }
}
