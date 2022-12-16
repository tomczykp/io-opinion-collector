package pl.lodz.p.it.opinioncollector.category;

import jakarta.persistence.ElementCollection;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Valid
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private UUID parentCategoryID;
    @NotEmpty
    private String name;

    private ArrayList<FieldDTO> fields = new ArrayList<>();


}
