package pl.lodz.p.it.opinioncollector.eventHandling.events;

import jakarta.persistence.*;
import lombok.*;
import pl.lodz.p.it.opinioncollector.productManagment.Product;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.util.UUID;

@Entity
@Getter
@ToString
public class QuestionNotifyEvent extends Event implements AdminEvent {
    private UUID questionID;

    public QuestionNotifyEvent(UUID eventID, User user, UUID productID, String description, UUID questionID) {
        super(eventID, user, productID, description);
        this.questionID = questionID;
    }

    public QuestionNotifyEvent() {
        super();
    }
}
