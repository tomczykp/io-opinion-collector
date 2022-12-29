package pl.lodz.p.it.opinioncollector.category.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Valid
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryDTO {
    private String parentCategoryID;

    @NotEmpty
    @NotBlank
    private String name;
}
