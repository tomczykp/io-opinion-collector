package pl.lodz.p.it.opinioncollector.eventHandling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.opinioncollector.eventHandling.events.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Service
public class EventManager implements IOpinionEventManager, IProductEventManager, IQuestionEventManager {
    private final EventsRepository eventsRepository;

    @Autowired
    public EventManager(EventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    @Override
    public Optional<Event> getEvent(UUID ID) {
        var foundEvent = eventsRepository.findById(ID);
        return foundEvent;
    }

    @Override
    public Optional<Event> modifyEvent(Event event) {
        var foundEvent = eventsRepository.findById(event.getEventID());
        if (foundEvent.isPresent())
        {
            eventsRepository.save(event);
        }
        return foundEvent;
    }

    @Override
    public OpinionReportEvent createOpinionReportEvent(UUID userID, String description, UUID opinionID) {
        OpinionReportEvent newEvent = new OpinionReportEvent(UUID.randomUUID(), userID, description, opinionID);
        eventsRepository.save(newEvent);
        return newEvent;
    }

    @Override
    public ProductReportEvent createProductReportEvent(UUID userID, String description, UUID productID) {
        ProductReportEvent newEvent = new ProductReportEvent(UUID.randomUUID(), userID, description, productID);
        eventsRepository.save(newEvent);
        return newEvent;
    }

    @Override
    public QuestionNotifyEvent createQuestionNotifyEvent(UUID userID, String description, UUID questionID) {
        QuestionNotifyEvent newEvent = new QuestionNotifyEvent(UUID.randomUUID(), userID, description, questionID);
        eventsRepository.save(newEvent);
        return newEvent;
    }

    @Override
    public QuestionReportEvent createQuestionReportEvent(UUID userID, String description, UUID questionID) {
        QuestionReportEvent newEvent = new QuestionReportEvent(UUID.randomUUID(), userID, description, questionID);
        eventsRepository.save(newEvent);
        return null;
    }

    @Override
    public AnswerReportEvent createAnswerReportEvent(UUID userID, String description, UUID questionID) {
        AnswerReportEvent newEvent = new AnswerReportEvent(UUID.randomUUID(), userID, description, questionID);
        eventsRepository.save(newEvent);
        return newEvent;
    }

    @Override
    public List<Event> getEvents() {
        return eventsRepository.findAll();
    }

    @Override
    public Optional<Event> answerEvent( UUID eventID) {
        var foundEvent = eventsRepository.findById(eventID);

        if (!foundEvent.isPresent()) {
            return foundEvent;
        }

        foundEvent.get().changeStatus(EventStatus.Closed);
        eventsRepository.save(foundEvent.get());
        return foundEvent;
    }

    @Override
    public List<Event> getEvents(Predicate<Event> Predicate) {
        List<Event> eventList = this.getEvents();
        List<Event> result = new ArrayList<Event>();

        for (Event event : eventList) {
            if (Predicate.test(event)) {
                result.add(event);
            }
        }

        return result;
    }
}
