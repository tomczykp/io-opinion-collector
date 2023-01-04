package pl.lodz.p.it.opinioncollector.opinion;

import lombok.Builder;
import lombok.Data;
import pl.lodz.p.it.opinioncollector.opinion.model.Advantage;
import pl.lodz.p.it.opinioncollector.opinion.model.Disadvantage;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class OpinionDetailsDto {
    private UUID productId;
    private UUID opinionId;
    private LocalDateTime createdAt;
    private int rate;
    private String description;
    private int likesCounter;
    private String authorName;
    private boolean isLiked;
    private boolean isDisliked;
    private Set<Advantage> pros;
    private Set<Disadvantage> cons;
}
