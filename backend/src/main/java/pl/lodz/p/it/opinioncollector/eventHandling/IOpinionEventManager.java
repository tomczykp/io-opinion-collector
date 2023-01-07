package pl.lodz.p.it.opinioncollector.eventHandling;

import pl.lodz.p.it.opinioncollector.eventHandling.events.OpinionReportEvent;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.UUID;

public interface IOpinionEventManager extends IEventManager {
    public OpinionReportEvent createOpinionReportEvent(User user, String description, UUID opinionID);
}
