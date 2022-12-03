package pl.lodz.p.it.opinioncollector.events;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventManager implements IOpinionEventManager, IProductEventManager, IQuestionEventManager  {
    @Override
    public Event getEvent(UUID ID) {
        return null;
    }

    @Override
    public void modifyEvent(UUID ID, Event event) {

    }

    @Override
    public OpinionReportEvent createOpinionReportEvent(UUID userID, String description, UUID opinionID) {
        return null;
    }

    @Override
    public ProductReportEvent createProductReportEvent(UUID userID, String description, UUID productID) {
        return null;
    }

    @Override
    public QuestionNotifyEvent createQuestionNotifyEvent(UUID userID, String description, UUID questionID) {
        return null;
    }

    @Override
    public QuestionReportEvent createQuestionReportEvent(UUID userID, String description, UUID questionID) {
        return null;
    }

    @Override
    public AnswerReportEvent createAnswerReportEvent(UUID userID, String description, UUID questionID) {
        return null;
    }
}
