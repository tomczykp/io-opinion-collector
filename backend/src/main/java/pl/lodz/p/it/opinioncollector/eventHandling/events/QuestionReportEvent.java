package pl.lodz.p.it.opinioncollector.eventHandling.events;

import jakarta.persistence.*;
import lombok.*;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.UUID;

@Entity
@Getter
@ToString
public class QuestionReportEvent extends Event {
    private UUID questionID;

    public QuestionReportEvent(UUID eventID, User user, String description, UUID questionID) {
        super(eventID, user, description);
        this.questionID = questionID;
    }

    public QuestionReportEvent() {
        super();
    }
}
