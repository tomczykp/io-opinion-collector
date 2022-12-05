package pl.lodz.p.it.opinioncollector.eventHandling.events;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@ToString
public class OpinionReportEvent extends Event {
    private UUID opinionID;

    public OpinionReportEvent(UUID eventID, UUID userID, String description, UUID opinionID) {
        super(eventID, userID, description);
        this.opinionID = opinionID;
    }

    public OpinionReportEvent() {
        super();
    }
}
