package pl.lodz.p.it.opinioncollector.opinion;

import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.opinioncollector.eventHandling.EventManager;
import pl.lodz.p.it.opinioncollector.opinion.exceptions.OpinionNotFoundException;
import pl.lodz.p.it.opinioncollector.opinion.exceptions.OpinionOperationAccessForbiddenException;

@RestController
@RequestMapping("/products/{productId}/opinions")
@RequiredArgsConstructor
public class OpinionController {

    private final OpinionManager opinionManager;
    private final EventManager eventManager;

    @GetMapping
    @ResponseBody
    public List<Opinion> getAll(@PathVariable UUID productId) {
        return opinionManager.getOpinions(productId);
    }

    @GetMapping("/{opinionId}")
    @ResponseBody
    public Opinion getById(@PathVariable UUID productId,
                           @PathVariable UUID opinionId)
        throws OpinionNotFoundException {
        return opinionManager.getOpinion(productId, opinionId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Opinion create(@PathVariable UUID productId,
                          @Valid @RequestBody CreateOpinionDto createOpinionDto) {
        return opinionManager.create(productId, createOpinionDto);
    }

    @DeleteMapping("/{opinionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletyById(@PathVariable UUID productId,
                           @PathVariable UUID opinionId)
        throws OpinionOperationAccessForbiddenException {
        opinionManager.deleteOpinion(productId, opinionId);
    }

    @PutMapping("/{opinionId}")
    @ResponseBody
    public Opinion update(@PathVariable UUID productId,
                          @PathVariable UUID opinionId,
                          @Valid @RequestBody CreateOpinionDto dto)
        throws OpinionNotFoundException {
        return opinionManager.update(productId, opinionId, dto);
    }

    @PostMapping("/{opinionId}/report")
    // @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void reportOpinion(@PathVariable UUID productId,
                              @PathVariable UUID opinionId,
                              @RequestBody String reason) {
        // FIXME userId should be of type UUID
        // User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // eventManager.createOpinionReportEvent(user.getId(), reason, opinionId);
    }
}
