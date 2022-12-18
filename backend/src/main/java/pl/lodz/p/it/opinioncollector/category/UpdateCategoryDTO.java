package pl.lodz.p.it.opinioncollector.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Valid
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryDTO {

    private UUID parentCategoryID;
    @NotEmpty
    private String name;
}
