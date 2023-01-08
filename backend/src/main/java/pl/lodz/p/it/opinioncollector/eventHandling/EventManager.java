package pl.lodz.p.it.opinioncollector.eventHandling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.opinioncollector.eventHandling.events.*;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

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
        return eventsRepository.findById(ID);
    }

    @Override
    public Optional<Event> modifyEvent(Event event) {
        var foundEvent = eventsRepository.findById(event.getEventID());
        if (foundEvent.isPresent()) {
            eventsRepository.save(event);
        }
        return foundEvent;
    }

    @Override
    public OpinionReportEvent createOpinionReportEvent(User user, String description, UUID opinionID, UUID productID) {
        OpinionReportEvent newEvent = new OpinionReportEvent(UUID.randomUUID(), user, description, opinionID, productID);
        eventsRepository.save(newEvent);
        return newEvent;
    }

    @Override
    public ProductReportEvent createProductReportEvent(User user, String description, UUID productID) {
        ProductReportEvent newEvent = new ProductReportEvent(UUID.randomUUID(), user, description, productID);
        eventsRepository.save(newEvent);
        return newEvent;
    }

    @Override
    public ProductSuggestionEvent createProductSuggestionEvent(User user, String description, UUID productID) {
        var newEvent = new ProductSuggestionEvent(UUID.randomUUID(), user, description, productID);
        eventsRepository.save(newEvent);
        return newEvent;
    }

    @Override
    public QuestionNotifyEvent createQuestionNotifyEvent(User user, String description, UUID questionID) {
        QuestionNotifyEvent newEvent = new QuestionNotifyEvent(UUID.randomUUID(), user, description, questionID);
        eventsRepository.save(newEvent);
        return newEvent;
    }

    @Override
    public QuestionReportEvent createQuestionReportEvent(User user, String description, UUID questionID) {
        QuestionReportEvent newEvent = new QuestionReportEvent(UUID.randomUUID(), user, description, questionID);
        eventsRepository.save(newEvent);
        return null;
    }

    @Override
    public AnswerReportEvent createAnswerReportEvent(User user, String description, UUID questionID) {
        AnswerReportEvent newEvent = new AnswerReportEvent(UUID.randomUUID(), user, description, questionID);
        eventsRepository.save(newEvent);
        return newEvent;
    }

    public AnswerNotifyEvent createAnswerNotifyEvent(User user, String description, UUID questionID) {
        var newEvent = new AnswerNotifyEvent(UUID.randomUUID(), user, description, questionID);
        eventsRepository.save(newEvent);
        return newEvent;
    }

    @Override
    public List<Event> getEvents() {
        return eventsRepository.findAll();
    }

    @Override
    public Optional<Event> answerEvent(UUID eventID) {
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

    public List<Event> getUserEvents(User user) {
        return eventsRepository.findByUser(user);
    }

    public int getUserEventsCount(User user) {
        return eventsRepository.countByUserAndStatus(user, EventStatus.Open);
    }
}
