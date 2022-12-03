package pl.lodz.p.it.opinioncollector.events;

import java.util.UUID;

public interface IEventManager {
    Event getEvent(UUID ID);
    void modifyEvent(UUID ID, Event event);
}
