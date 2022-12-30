package pl.lodz.p.it.opinioncollector.eventHandling;

import pl.lodz.p.it.opinioncollector.eventHandling.events.AnswerReportEvent;
import pl.lodz.p.it.opinioncollector.eventHandling.events.QuestionNotifyEvent;
import pl.lodz.p.it.opinioncollector.eventHandling.events.QuestionReportEvent;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.UUID;

public interface IQuestionEventManager extends IEventManager {
    QuestionNotifyEvent createQuestionNotifyEvent(User user, String description, UUID questionID);
    QuestionReportEvent createQuestionReportEvent(User user, String description, UUID questionID);
    AnswerReportEvent createAnswerReportEvent(User user, String description, UUID questionID);
}
