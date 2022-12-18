package pl.lodz.p.it.opinioncollector.opinion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CreateOpinionDto implements Serializable {
    private int rate;
    private String description;
    private List<String> pros = new ArrayList<>();
    private List<String> cons = new ArrayList<>();
}
