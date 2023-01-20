package pl.lodz.p.it.opinioncollector.eventHandling;

import pl.lodz.p.it.opinioncollector.eventHandling.events.ProductReportEvent;
import pl.lodz.p.it.opinioncollector.eventHandling.events.ProductSuggestionEvent;
import pl.lodz.p.it.opinioncollector.productManagment.Product;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.UUID;

public interface IProductEventManager extends IEventManager {
    ProductReportEvent createProductReportEvent(User user, UUID productID, String description);
    ProductSuggestionEvent createProductSuggestionEvent(User user, UUID productID, String description);
}
