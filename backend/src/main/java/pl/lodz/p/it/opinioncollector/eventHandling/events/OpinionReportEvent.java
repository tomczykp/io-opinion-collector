package pl.lodz.p.it.opinioncollector.eventHandling.events;

import jakarta.persistence.*;
import lombok.*;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.UUID;

@Entity
@Getter
@ToString
public class OpinionReportEvent extends Event {
    private UUID opinionID;
    private UUID productID;

    public OpinionReportEvent(UUID eventID, User user, String description, UUID opinionID, UUID productID) {
        super(eventID, user, description);
        this.opinionID = opinionID;
        this.productID = productID;
    }

    public OpinionReportEvent() {
        super();
    }
}
