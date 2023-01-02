package pl.lodz.p.it.opinioncollector.eventHandling.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.lodz.p.it.opinioncollector.eventHandling.events.EventStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
public class EventDTO {
    @NotNull
    UUID eventID;

    @NotNull
    String userName;

    @NotNull
    String description;

    @NotNull
    EventStatus status;
}
