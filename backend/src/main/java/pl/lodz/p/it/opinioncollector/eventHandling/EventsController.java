package pl.lodz.p.it.opinioncollector.eventHandling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.opinioncollector.eventHandling.dto.EventDTO;
import pl.lodz.p.it.opinioncollector.eventHandling.events.Event;
import pl.lodz.p.it.opinioncollector.eventHandling.events.EventStatus;

import java.util.ArrayList;
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
    public List<EventDTO> Events() {
        List<EventDTO> result = new ArrayList<>();

        for(var event : eventManager.getEvents())
        {
            result.add(new EventDTO(event.getEventID().toString(), event.getUser().getVisibleName(), event.getDescription(), event.getStatus()));
        }

        return result;
    }

    @GetMapping("/events/{eventID}")
    public ResponseEntity<EventDTO> GetEvent(@PathVariable("eventID") String eventID) {
        var foundEvent = eventManager.getEvent(UUID.fromString(eventID));

        if (!foundEvent.isPresent())
            return ResponseEntity.notFound().build();

        Event result = foundEvent.get();
        EventDTO resultDTO = new EventDTO(result.getEventID().toString(), result.getUser().getVisibleName(), result.getDescription(), result.getStatus());

        return ResponseEntity.ok(resultDTO);
    }

    @GetMapping("/users/{userID}/events")
    public ResponseEntity<List<Event>> GetUserEvents(@PathVariable("userID") String userID) {
        UUID userUUID = UUID.fromString(userID);
        Predicate<Event> UserPredicate = event -> event.getUserID().equals(userUUID);

        List<Event> foundEvents = eventManager.getEvents(UserPredicate);

        if (foundEvents.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(foundEvents);
    }

    @GetMapping("/users/{userID}/eventsCount")
    public ResponseEntity<Integer> GetUserEventsCount(@PathVariable("userID") String userID) {
        UUID userUUID = UUID.fromString(userID);
        Predicate<Event> UserPredicate = event -> event.getUserID().equals(userUUID);

        List<Event> foundEvents = eventManager.getEvents(UserPredicate);
        int activeEvents = 0;

        for (var event : foundEvents) {
            if (event.getStatus() == EventStatus.Open)
                activeEvents++;
        }

        return ResponseEntity.ok(activeEvents);
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
