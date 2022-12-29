package pl.lodz.p.it.opinioncollector.productManagment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Data
@Valid
@NoArgsConstructor
public class ProductDeleteForm {

        @NotEmpty()
        private String description;

    public ProductDeleteForm(String description) {
        this.description = description;
    }
}
