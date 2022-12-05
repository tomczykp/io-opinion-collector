package pl.lodz.p.it.opinioncollector.eventHandling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.opinioncollector.eventHandling.events.Event;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

@RestController
public class EventsController {
    EventManager eventManager;

    @Autowired
    public EventsController(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @GetMapping("/events")
    public List<Event> Events() {
        return eventManager.getEvents();
    }

    @GetMapping("/events/{eventID}")
    public ResponseEntity<Event> GetEvent(@PathVariable("eventID") String eventID) {
        var foundEvent = eventManager.getEvent(UUID.fromString(eventID));

        if (!foundEvent.isPresent())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(foundEvent.get());
    }

    // TODO: Rename according to users module
    @GetMapping("/users/{userID}/events/")
    public ResponseEntity<List<Event>> GetUserEvents(@PathVariable("userID") String userID) {
        Predicate<Event> UserPredicate = event -> event.getUserID().toString().equals(userID);
        List<Event> foundEvents = eventManager.getEvents(UserPredicate);

        if (foundEvents.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(foundEvents);
    }


}
