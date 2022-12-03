package pl.lodz.p.it.opinioncollector.events;

import java.util.UUID;

public interface IProductEventManager extends IEventManager {
    ProductReportEvent createProductReportEvent(UUID userID, String description, UUID productID);
}
