package pl.lodz.p.it.opinioncollector.eventHandling.events;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.ToString;
import pl.lodz.p.it.opinioncollector.productManagment.Product;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.UUID;

@Entity
@Getter
@ToString
public class AnswerReportEvent extends Event implements AdminEvent {
    private UUID answerID;
    private UUID questionID;

    public AnswerReportEvent(UUID eventID, User user, UUID product, String description, UUID answerID, UUID questionID) {
        super(eventID, user, product, description);
        this.answerID = answerID;
        this.questionID = questionID;
    }

    public AnswerReportEvent() {
        super();
    }

}
