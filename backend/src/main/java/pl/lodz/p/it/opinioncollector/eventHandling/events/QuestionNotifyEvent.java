package pl.lodz.p.it.opinioncollector.eventHandling.events;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@ToString
public class QuestionNotifyEvent extends Event {
    private UUID questionID;

    public QuestionNotifyEvent(UUID eventID, UUID userID, String description, UUID questionID) {
        super(eventID, userID, description);
        this.questionID = questionID;
    }

    public QuestionNotifyEvent() {
        super();
    }
}
