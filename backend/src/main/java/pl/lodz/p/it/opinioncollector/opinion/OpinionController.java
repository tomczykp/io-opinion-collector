package pl.lodz.p.it.opinioncollector.opinion;

import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import pl.lodz.p.it.opinioncollector.exceptions.opinion.OpinionNotFoundException;
import pl.lodz.p.it.opinioncollector.exceptions.opinion.OpinionOperationAccessForbiddenException;
import pl.lodz.p.it.opinioncollector.exceptions.products.ProductNotFoundException;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

@CrossOrigin
@RestController
@RequestMapping("/products/{productId}/opinions")
@RequiredArgsConstructor
public class OpinionController {

    private final OpinionManager opinionManager;
    private final EventManager eventManager;

    @GetMapping
    @ResponseBody
    public List<OpinionDetailsDto> getAll(@PathVariable UUID productId) {
        return opinionManager.getOpinions(productId);
    }

    @GetMapping("/{opinionId}")
    @ResponseBody
    public OpinionDetailsDto getById(@PathVariable UUID productId,
                                     @PathVariable UUID opinionId)
        throws OpinionNotFoundException {
        return opinionManager.getOpinion(productId, opinionId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public OpinionDetailsDto create(@PathVariable UUID productId,
                                    @Valid @RequestBody CreateOpinionDto createOpinionDto)
        throws ProductNotFoundException {
        return opinionManager.create(productId, createOpinionDto);
    }

    @DeleteMapping("/{opinionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID productId,
                           @PathVariable UUID opinionId)
        throws OpinionOperationAccessForbiddenException {
        opinionManager.deleteOpinion(productId, opinionId);
    }

    @PutMapping("/{opinionId}")
    @ResponseBody
    public OpinionDetailsDto update(@PathVariable UUID productId,
                                    @PathVariable UUID opinionId,
                                    @Valid @RequestBody CreateOpinionDto dto)
        throws OpinionNotFoundException, OpinionOperationAccessForbiddenException {
        return opinionManager.update(productId, opinionId, dto);
    }

    @PutMapping("/{opinionId}/like")
    public OpinionDetailsDto like(@PathVariable UUID productId,
                                  @PathVariable UUID opinionId)
        throws OpinionNotFoundException {
        return opinionManager.addReaction(productId, opinionId, true);
    }

    @PutMapping("/{opinionId}/dislike")
    public OpinionDetailsDto dislike(@PathVariable UUID productId,
                                     @PathVariable UUID opinionId)
        throws OpinionNotFoundException {
        return opinionManager.addReaction(productId, opinionId, false);
    }

    @DeleteMapping({ "/{opinionId}/like", "/{opinionId}/dislike" })
    public OpinionDetailsDto removeReaction(@PathVariable UUID productId,
                                            @PathVariable UUID opinionId) throws OpinionNotFoundException {
        return opinionManager.removeReaction(productId, opinionId);
    }

    @PostMapping(value = "/{opinionId}/report",
                 consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void reportOpinion(@PathVariable UUID productId,
                              @PathVariable UUID opinionId,
                              @RequestBody @NotBlank String reason)
        throws OpinionNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!opinionManager.existsById(productId, opinionId)) {
            throw new OpinionNotFoundException();
        }

        eventManager.createOpinionReportEvent(user, "Opinion reported with reason: " + reason, opinionId, productId);
    }
}
