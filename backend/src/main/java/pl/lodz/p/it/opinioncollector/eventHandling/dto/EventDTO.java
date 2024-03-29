package pl.lodz.p.it.opinioncollector.eventHandling.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.lodz.p.it.opinioncollector.eventHandling.events.*;

import java.util.UUID;

@Data
@AllArgsConstructor
public class EventDTO {
    @NotNull
    UUID eventID;

    @NotNull
    UUID userID;

    @NotNull
    String description;

    @NotNull
    EventStatus status;

    UUID questionID;
    UUID answerID;
    UUID opinionID;
    UUID productID;

    @NotNull
    String type;

    public EventDTO(Event event) {
        this.eventID = event.getEventID();
        this.userID = event.getUserID();
        this.description = event.getDescription();
        this.status = event.getStatus();
        this.productID = event.getProductID();
        this.questionID = null;
        this.opinionID = null;


        if (event instanceof AnswerReportEvent castedEvent) {
            this.answerID = castedEvent.getAnswerID();
            this.questionID = castedEvent.getQuestionID();
            this.type = "answerReport";
        }
        else if (event instanceof QuestionNotifyEvent castedEvent) {
            this.questionID = castedEvent.getQuestionID();
            this.type = "questionNotify";
        }
        else if (event instanceof QuestionReportEvent castedEvent) {
            this.questionID = castedEvent.getQuestionID();
            this.type = "questionReport";
        }
        else if (event instanceof OpinionReportEvent castedEvent) {
            this.opinionID = castedEvent.getOpinionID();
            this.type = "opinionReport";
        }
        else if (event instanceof ProductReportEvent castedEvent) {
            this.type = "productReport";
        }
        else if (event instanceof AnswerNotifyEvent castedEvent) {
            this.answerID = castedEvent.getAnswerID();
            this.questionID = castedEvent.getQuestionID();
            this.type = "answerNotify";
        }
        else if (event instanceof ProductSuggestionEvent castedEvent) {
            this.type = "productSuggestion";
        }
    }

}
