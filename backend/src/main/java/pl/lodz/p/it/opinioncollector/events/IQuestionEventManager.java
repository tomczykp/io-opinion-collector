package pl.lodz.p.it.opinioncollector.events;

import java.util.UUID;

public interface IQuestionEventManager extends IEventManager {
    QuestionNotifyEvent createQuestionNotifyEvent(UUID userID, String description, UUID questionID);
    QuestionReportEvent createQuestionReportEvent(UUID userID, String description, UUID questionID);
    AnswerReportEvent createAnswerReportEvent(UUID userID, String description, UUID questionID);
}
