package pl.lodz.p.it.opinioncollector.eventHandling;

import pl.lodz.p.it.opinioncollector.eventHandling.events.OpinionReportEvent;

import java.util.UUID;

public interface IOpinionEventManager extends IEventManager {
    public OpinionReportEvent createOpinionReportEvent(UUID userID, String description, UUID opinionID);
}
