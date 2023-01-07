package pl.lodz.p.it.opinioncollector.eventHandling.events;

import jakarta.persistence.*;
import lombok.*;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.UUID;

@Entity
@Getter
@ToString
public class ProductReportEvent extends Event implements AdminEvent {
    private UUID productID;

    public ProductReportEvent(UUID eventID, User user, String description, UUID productID) {
        super(eventID, user, description);
        this.productID = productID;
    }

    public ProductReportEvent() {
        super();
    }
}
