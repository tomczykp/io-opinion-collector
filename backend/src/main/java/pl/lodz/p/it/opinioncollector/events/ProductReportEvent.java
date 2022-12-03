package pl.lodz.p.it.opinioncollector.events;

import java.util.UUID;

public class ProductReportEvent extends Event {
    private UUID productID;

    public ProductReportEvent(UUID eventID, UUID userID, String description, UUID productID) {
        super(eventID, userID, description);
        this.productID = productID;
    }

    public UUID getProductID() {
        return productID;
    }
}
