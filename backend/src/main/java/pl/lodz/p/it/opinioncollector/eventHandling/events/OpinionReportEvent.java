package pl.lodz.p.it.opinioncollector.eventHandling.events;

import jakarta.persistence.*;
import lombok.*;
import pl.lodz.p.it.opinioncollector.productManagment.Product;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.UUID;

@Entity
@Getter
@ToString
public class OpinionReportEvent extends Event implements AdminEvent {
    private UUID opinionID;

    public OpinionReportEvent(UUID eventID, User user, UUID productID, String description, UUID opinionID) {
        super(eventID, user, productID, description);
        this.opinionID = opinionID;
    }

    public OpinionReportEvent() {
        super();
    }
}
