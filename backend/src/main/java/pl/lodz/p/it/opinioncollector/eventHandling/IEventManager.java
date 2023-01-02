package pl.lodz.p.it.opinioncollector.eventHandling;

import pl.lodz.p.it.opinioncollector.eventHandling.events.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public interface IEventManager {
    Optional<Event> getEvent(UUID ID);

    Optional<Event> modifyEvent(Event event);

    List<Event> getEvents();

    List<Event> getEvents(Predicate<Event> Predicate);

    Optional<Event> answerEvent(UUID eventID);
}
