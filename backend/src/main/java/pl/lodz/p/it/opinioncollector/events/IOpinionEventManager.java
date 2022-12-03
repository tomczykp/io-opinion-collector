package pl.lodz.p.it.opinioncollector.events;

import java.util.UUID;

public interface IOpinionEventManager extends IEventManager {
    public OpinionReportEvent createOpinionReportEvent(UUID userID, String description, UUID opinionID);
}
