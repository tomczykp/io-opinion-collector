package pl.lodz.p.it.opinioncollector.events;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface IEventManager {
    Event getEvent(UUID ID);

    Event modifyEvent(Event event);

    List<Event> getEvents();

    List<Event> getEvents(Predicate<Event> Predicate);

    void answerEvent(UUID event);
}
