package pl.lodz.p.it.opinioncollector.eventHandling;

import pl.lodz.p.it.opinioncollector.eventHandling.events.ProductReportEvent;

import java.util.UUID;

public interface IProductEventManager extends IEventManager {
    ProductReportEvent createProductReportEvent(UUID userID, String description, UUID productID);
}
