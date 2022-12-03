package pl.lodz.p.it.opinioncollector.eventHandling.events;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.ToString;
import pl.lodz.p.it.opinioncollector.eventHandling.events.Event;

import java.util.UUID;

@Entity
@Getter
@ToString
public class AnswerReportEvent extends Event {
    private UUID questionID;

    public AnswerReportEvent(UUID eventID, UUID userID, String description, UUID questionID) {
        super(eventID, userID, description);
        this.questionID = questionID;
    }

    public AnswerReportEvent() {
        super();
    }

}
