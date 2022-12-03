package pl.lodz.p.it.opinioncollector.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventsController {
    EventManager eventManager;

    @Autowired
    public  EventsController(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @GetMapping("/events")
    public List<Event> Events()
    {
        return eventManager.GetEvents();
    }
}
