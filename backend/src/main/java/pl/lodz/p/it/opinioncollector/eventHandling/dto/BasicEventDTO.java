package pl.lodz.p.it.opinioncollector.eventHandling.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.lodz.p.it.opinioncollector.eventHandling.events.*;

import java.util.UUID;

@Data
@AllArgsConstructor
public class BasicEventDTO {
    @NotNull
    UUID eventID;

    @NotNull
    String userName;

    @NotNull
    String description;

    @NotNull
    EventStatus status;

    @NotNull
    String type;

    public BasicEventDTO(Event event) {
        this.eventID = event.getEventID();
        this.userName = event.getUser().getVisibleName();
        this.description = event.getDescription();
        this.status = event.getStatus();


        if (event instanceof AnswerReportEvent castedEvent) {
            this.type = "answerReport";
        }
        else if (event instanceof QuestionNotifyEvent castedEvent) {
            this.type = "questionNotify";
        }
        else if (event instanceof QuestionReportEvent castedEvent) {
            this.type = "questionReport";
        }
        else if (event instanceof OpinionReportEvent castedEvent) {
            this.type = "opinionReport";
        }
        else if (event instanceof ProductReportEvent castedEvent) {
            this.type = "productReport";
        }
        else if (event instanceof AnswerNotifyEvent castedEvent) {
            this.type = "answerNotify";
        }
        else if (event instanceof ProductSuggestionEvent castedEvent) {
            this.type = "productSuggestion";
        }
    }
}
