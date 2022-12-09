package pl.lodz.p.it.opinioncollector.eventHandling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.opinioncollector.eventHandling.events.Event;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

@RestController
@CrossOrigin
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
        UUID userUUID = UUID.fromString(userID);
        Predicate<Event> UserPredicate = event -> event.getUserID().equals(userUUID);

        List<Event> foundEvents = eventManager.getEvents(UserPredicate);

        if (foundEvents.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(foundEvents);
    }

    @PostMapping("/events/{eventID}/close")
    public ResponseEntity<Object> CloseEvent(@PathVariable("eventID") String eventID)
    {
        var response = eventManager.answerEvent(UUID.fromString(eventID));

        if (!response.isPresent())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().build();
    }


}
