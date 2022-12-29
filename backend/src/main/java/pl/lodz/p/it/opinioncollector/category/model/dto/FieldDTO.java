package pl.lodz.p.it.opinioncollector.category.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldDTO {

    @NotBlank
    String name;

    @NotBlank
    String type;

    public String getName () {
        return name;
    }

    public String getType () {
        return type;
    }

}