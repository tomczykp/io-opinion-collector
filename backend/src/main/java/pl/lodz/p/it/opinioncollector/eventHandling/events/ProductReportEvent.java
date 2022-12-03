package pl.lodz.p.it.opinioncollector.eventHandling.events;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@ToString
public class ProductReportEvent extends Event {
    private UUID productID;

    public ProductReportEvent(UUID eventID, UUID userID, String description, UUID productID) {
        super(eventID, userID, description);
        this.productID = productID;
    }

    public ProductReportEvent() {
        super();
    }
}
