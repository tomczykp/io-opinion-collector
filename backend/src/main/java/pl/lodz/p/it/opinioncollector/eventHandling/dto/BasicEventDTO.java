package pl.lodz.p.it.opinioncollector.eventHandling.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.lodz.p.it.opinioncollector.eventHandling.events.Event;
import pl.lodz.p.it.opinioncollector.eventHandling.events.EventStatus;

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

    public BasicEventDTO(Event event) {
        this.eventID = event.getEventID();
        this.userName = event.getUser().getVisibleName();
        this.description = event.getDescription();
        this.status = event.getStatus();
    }
}
