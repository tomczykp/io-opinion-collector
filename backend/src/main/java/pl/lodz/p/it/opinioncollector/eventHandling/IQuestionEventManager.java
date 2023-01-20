package pl.lodz.p.it.opinioncollector.eventHandling;

import pl.lodz.p.it.opinioncollector.eventHandling.events.AnswerReportEvent;
import pl.lodz.p.it.opinioncollector.eventHandling.events.AnswerNotifyEvent;
import pl.lodz.p.it.opinioncollector.eventHandling.events.QuestionNotifyEvent;
import pl.lodz.p.it.opinioncollector.eventHandling.events.QuestionReportEvent;
import pl.lodz.p.it.opinioncollector.productManagment.Product;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.UUID;

public interface IQuestionEventManager extends IEventManager {
    QuestionNotifyEvent createQuestionNotifyEvent(User user, UUID productID, String description, UUID questionID);
    QuestionReportEvent createQuestionReportEvent(User user, UUID productID, String description, UUID questionID);
    AnswerReportEvent createAnswerReportEvent(User user, UUID productID, String description, UUID answerID, UUID questionID);
    AnswerNotifyEvent createAnswerNotifyEvent(User user, UUID productID, String description, UUID answerID, UUID questionID);
}
