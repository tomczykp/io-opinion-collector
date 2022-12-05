package pl.lodz.p.it.opinioncollector.eventHandling;

import pl.lodz.p.it.opinioncollector.eventHandling.events.AnswerReportEvent;
import pl.lodz.p.it.opinioncollector.eventHandling.events.QuestionNotifyEvent;
import pl.lodz.p.it.opinioncollector.eventHandling.events.QuestionReportEvent;

import java.util.UUID;

public interface IQuestionEventManager extends IEventManager {
    QuestionNotifyEvent createQuestionNotifyEvent(UUID userID, String description, UUID questionID);
    QuestionReportEvent createQuestionReportEvent(UUID userID, String description, UUID questionID);
    AnswerReportEvent createAnswerReportEvent(UUID userID, String description, UUID questionID);
}
