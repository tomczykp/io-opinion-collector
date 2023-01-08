package pl.lodz.p.it.opinioncollector.eventHandling.events;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.ToString;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.UUID;

@Entity
@Getter
@ToString
public class AnswerReportEvent extends Event implements AdminEvent {
    private UUID answerID;

    public AnswerReportEvent(UUID eventID, User user, String description, UUID answerID) {
        super(eventID, user, description);
        this.answerID = answerID;
    }

    public AnswerReportEvent() {
        super();
    }

}
