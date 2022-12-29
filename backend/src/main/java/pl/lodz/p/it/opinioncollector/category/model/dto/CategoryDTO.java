package pl.lodz.p.it.opinioncollector.category.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.UUID;

@Data
@Valid
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private UUID parentCategoryID;

    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    private ArrayList<@Valid FieldDTO> fields = new ArrayList<>();


}
