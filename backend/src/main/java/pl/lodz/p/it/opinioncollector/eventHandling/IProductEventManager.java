package pl.lodz.p.it.opinioncollector.eventHandling;

import pl.lodz.p.it.opinioncollector.eventHandling.events.ProductReportEvent;
import pl.lodz.p.it.opinioncollector.eventHandling.events.ProductSuggestionEvent;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.UUID;

public interface IProductEventManager extends IEventManager {
    ProductReportEvent createProductReportEvent(User user, String description, UUID productID);
    ProductSuggestionEvent createProductSuggestionEvent(User user, String description, UUID productID);
}
