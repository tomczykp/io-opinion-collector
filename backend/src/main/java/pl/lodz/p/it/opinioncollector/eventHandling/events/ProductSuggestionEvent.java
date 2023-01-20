package pl.lodz.p.it.opinioncollector.eventHandling.events;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.ToString;
import pl.lodz.p.it.opinioncollector.productManagment.Product;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.UUID;

@Entity
@Getter
@ToString
public class ProductSuggestionEvent extends Event implements AdminEvent {
    public ProductSuggestionEvent(UUID eventID, User user, UUID productID, String description) {
        super(eventID, user, productID, description);
    }

    public ProductSuggestionEvent() {
        super();
    }
}
