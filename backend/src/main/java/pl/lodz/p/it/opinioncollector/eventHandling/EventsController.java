package pl.lodz.p.it.opinioncollector.eventHandling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.opinioncollector.eventHandling.dto.BasicEventDTO;
import pl.lodz.p.it.opinioncollector.eventHandling.dto.EventDTO;
import pl.lodz.p.it.opinioncollector.eventHandling.events.*;
import pl.lodz.p.it.opinioncollector.userModule.user.User;
import pl.lodz.p.it.opinioncollector.userModule.user.UserManager;
import pl.lodz.p.it.opinioncollector.userModule.user.UserType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
public class EventsController {
    EventManager eventManager;
    UserManager userManager;

    @Autowired
    public EventsController(EventManager eventManager, UserManager userManager) {
        this.eventManager = eventManager;
        this.userManager = userManager;
    }

    @GetMapping("/events")
    public List<BasicEventDTO> Events() {
        List<BasicEventDTO> result = new ArrayList<>();

        for (var event : eventManager.getEvents()) {
            result.add(new BasicEventDTO(event));
        }

        return result;
    }

    @GetMapping("/events/{eventID}")
    public ResponseEntity<EventDTO> GetEvent(@PathVariable("eventID") UUID eventID) {
        Optional<Event> foundEvent = eventManager.getEvent(eventID);

        if (foundEvent.isEmpty())
            return ResponseEntity.notFound().build();

        Event result = foundEvent.get();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRole() == UserType.USER && !user.getId().equals(result.getUser().getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(new EventDTO(foundEvent.get()));
    }

    @GetMapping("/user/events")
    public ResponseEntity<List<BasicEventDTO>> GetUserEvents() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Event> foundEvents = eventManager.getUserEvents(user);

        List<BasicEventDTO> eventDTOS = new ArrayList<>();
        for (Event foundEvent : foundEvents) {
            if (foundEvent instanceof AdminEvent)
                continue;

            eventDTOS.add(new BasicEventDTO(foundEvent));
        }

        return ResponseEntity.ok(eventDTOS);
    }

    @GetMapping("/user/eventsCount")
    public ResponseEntity<Integer> GetUserEventsCount() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Event> foundEvents = eventManager.getUserEvents(user);

        int eventsCount = 0;
        for (Event foundEvent : foundEvents) {
            if (foundEvent instanceof AdminEvent)
                continue;

            if (foundEvent.getStatus() == EventStatus.Closed)
                continue;

            eventsCount++;
        }

        return ResponseEntity.ok(eventsCount);
    }

    @PostMapping("/events/{eventID}/close")
    public ResponseEntity<Object> CloseEvent(@PathVariable("eventID") UUID eventID) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var event = eventManager.getEvent(eventID);

        if (event.isEmpty())
            return ResponseEntity.notFound().build();

        if (!user.getId().equals(event.get().getUser().getId()) && user.getRole() == UserType.USER)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        var response = eventManager.answerEvent(eventID);
        if (response.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().build();
    }

}
