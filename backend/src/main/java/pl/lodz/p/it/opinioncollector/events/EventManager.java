package pl.lodz.p.it.opinioncollector.events;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Component
public class EventManager implements IOpinionEventManager, IProductEventManager, IQuestionEventManager {
    @Override
    public Event getEvent(UUID ID) {
        //TODO create implementation
        return new QuestionNotifyEvent(ID, UUID.randomUUID(), "Test Question Notify Event", UUID.randomUUID());
    }

    @Override
    public Event modifyEvent(Event event) {
        //TODO create implementation
        return event;
    }

    @Override
    public OpinionReportEvent createOpinionReportEvent(UUID userID, String description, UUID opinionID) {
        //TODO create implementation
        OpinionReportEvent newEvent = new OpinionReportEvent(UUID.randomUUID(), userID, description, opinionID);
        return newEvent;
    }

    @Override
    public ProductReportEvent createProductReportEvent(UUID userID, String description, UUID productID) {
        //TODO: Create implementation
        ProductReportEvent newEvent = new ProductReportEvent(UUID.randomUUID(), userID, description, productID);
        return newEvent;
    }

    @Override
    public QuestionNotifyEvent createQuestionNotifyEvent(UUID userID, String description, UUID questionID) {
        //TODO: Create implementation
        QuestionNotifyEvent newEvent = new QuestionNotifyEvent(UUID.randomUUID(), userID, description, questionID);
        return newEvent;
    }

    @Override
    public QuestionReportEvent createQuestionReportEvent(UUID userID, String description, UUID questionID) {
        //TODO: Create implementation
        QuestionReportEvent newEvent = new QuestionReportEvent(UUID.randomUUID(), userID, description, questionID);
        return null;
    }

    @Override
    public AnswerReportEvent createAnswerReportEvent(UUID userID, String description, UUID questionID) {
        //TODO: Create implementation
        AnswerReportEvent newEvent = new AnswerReportEvent(UUID.randomUUID(), userID, description, questionID);
        return null;
    }

    @Override
    public List<Event> getEvents() {
        //TODO: Create implementation
        List<Event> Result = new ArrayList<Event>();
        Result.add(new OpinionReportEvent(UUID.randomUUID(), UUID.randomUUID(), "Test Opinion Report Event", UUID.randomUUID()));
        Result.add(new ProductReportEvent(UUID.randomUUID(), UUID.randomUUID(), "Test Product Report Event", UUID.randomUUID()));
        Result.add(new QuestionNotifyEvent(UUID.randomUUID(), UUID.randomUUID(), "Test Question Notify Event", UUID.randomUUID()));

        return Result;
    }

    @Override
    public void answerEvent(UUID event) {
        //TODO: Create implementation
        return;
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
