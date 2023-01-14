package pl.lodz.p.it.opinioncollector.eventHandling.events;

import jakarta.persistence.*;
import lombok.*;
import pl.lodz.p.it.opinioncollector.productManagment.Product;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.UUID;

@Entity
@Getter
@ToString
public class ProductReportEvent extends Event implements AdminEvent {
    public ProductReportEvent(UUID eventID, User user, UUID productID, String description) {
        super(eventID, user, productID, description);
    }

    public ProductReportEvent() {
        super();
    }
}
