package pl.lodz.p.it.opinioncollector.eventHandling.events;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.ToString;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.UUID;

@Entity
@Getter
@ToString
public class ProductSuggestionEvent extends Event implements AdminEvent {
    private UUID productID;

    public ProductSuggestionEvent(UUID eventID, User user, String description, UUID productID) {
        super(eventID, user, description);
        this.productID = productID;
    }

    public ProductSuggestionEvent() {
        super();
    }
}
