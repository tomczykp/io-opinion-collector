package pl.lodz.p.it.opinioncollector.opinion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOpinionDto implements Serializable {

    @Min(0)
    @Max(10)
    private int rate;

    @NotBlank
    private String description;

    @NotNull
    private List<String> pros = new ArrayList<>();

    @NotNull
    private List<String> cons = new ArrayList<>();
}
