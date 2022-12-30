package pl.lodz.p.it.opinioncollector.eventHandling.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.lodz.p.it.opinioncollector.eventHandling.events.EventStatus;

@Data
@AllArgsConstructor
public class EventDTO {
    @NotNull
    String eventID;

    @NotNull
    String userName;

    @NotNull
    String description;

    @NotNull
    EventStatus status;
}
