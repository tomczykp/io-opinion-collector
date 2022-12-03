package pl.lodz.p.it.opinioncollector.events;

import java.util.UUID;

public class OpinionReportEvent extends Event {
    private UUID opinionID;

    public OpinionReportEvent(UUID eventID, UUID userID, String description, UUID opinionID) {
        super(eventID, userID, description);
        this.opinionID = opinionID;
    }

    public UUID getOpinionID() {
        return opinionID;
    }
}
