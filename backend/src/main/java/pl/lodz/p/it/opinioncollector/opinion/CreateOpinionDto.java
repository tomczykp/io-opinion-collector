package pl.lodz.p.it.opinioncollector.opinion;

import java.io.Serializable;
import lombok.Data;

@Data
public class CreateOpinionDto implements Serializable {
    private int rate;
    private String description;
    // TODO add pros and cons
}
