package pl.lodz.p.it.opinioncollector.opinion;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.opinioncollector.exceptions.opinion.OpinionNotFoundException;
import pl.lodz.p.it.opinioncollector.exceptions.opinion.OpinionOperationAccessForbiddenException;
import pl.lodz.p.it.opinioncollector.exceptions.products.ProductNotFoundException;
import pl.lodz.p.it.opinioncollector.opinion.model.Advantage;
import pl.lodz.p.it.opinioncollector.opinion.model.Disadvantage;
import pl.lodz.p.it.opinioncollector.opinion.model.Opinion;
import pl.lodz.p.it.opinioncollector.opinion.model.OpinionId;
import pl.lodz.p.it.opinioncollector.opinion.model.Reaction;
import pl.lodz.p.it.opinioncollector.opinion.model.ReactionId;
import pl.lodz.p.it.opinioncollector.opinion.repositories.OpinionRepository;
import pl.lodz.p.it.opinioncollector.opinion.repositories.ReactionRepository;
import pl.lodz.p.it.opinioncollector.productManagment.ProductRepository;
import pl.lodz.p.it.opinioncollector.userModule.user.User;
import pl.lodz.p.it.opinioncollector.userModule.user.UserType;

@Service
@RequiredArgsConstructor
public class OpinionManager {

    private final OpinionRepository opinionRepository;
    private final ProductRepository productRepository;
    private final ReactionRepository reactionRepository;

    public List<OpinionDetailsDto> getOpinions(UUID productId) {
        return opinionRepository.findById_ProductId(productId)
                                .stream()
                                .map(this::mapOpinionToDto)
                                .toList();
    }

    public OpinionDetailsDto getOpinion(UUID productId, UUID opinionId)
        throws OpinionNotFoundException {
        return opinionRepository.findOne(productId, opinionId)
                                .map(this::mapOpinionToDto)
                                .orElseThrow(OpinionNotFoundException::new);
    }

    public OpinionDetailsDto create(UUID productId, CreateOpinionDto createOpinionDto) throws ProductNotFoundException {
        return productRepository.findById(productId)
                                .map(product -> {
                                    User user = (User) SecurityContextHolder.getContext()
                                                                            .getAuthentication()
                                                                            .getPrincipal();

                                    Opinion opinion = mapDtoToEntity(createOpinionDto);

                                    OpinionId opinionId = new OpinionId(productId, UUID.randomUUID());
                                    opinion.setProduct(product);
                                    opinion.setAuthor(user);
                                    opinion.setId(opinionId);
                                    return mapOpinionToDto(opinionRepository.save(opinion));
                                })
                                .orElseThrow(ProductNotFoundException::new);
    }

    @Transactional
    public void deleteOpinion(UUID productId, UUID opinionId)
        throws OpinionOperationAccessForbiddenException {
        Optional<Opinion> opinionOptional = opinionRepository.findOne(productId, opinionId);

        if (opinionOptional.isPresent()) {
            Opinion opinion = opinionOptional.get();
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            UUID userId = user.getId();
            if (Objects.equals(opinion.getAuthor().getId(), userId)) {
                opinionRepository.deleteById_ProductIdAndId_OpinionId(productId, opinionId);
            } else if (user.getRole() == UserType.ADMIN) {
                opinionRepository.deleteById_ProductIdAndId_OpinionId(productId, opinionId);
            } else {
                throw new OpinionOperationAccessForbiddenException();
            }
        }
    }

    @Transactional
    public OpinionDetailsDto update(UUID productId, UUID opinionId, CreateOpinionDto dto)
        throws OpinionNotFoundException, OpinionOperationAccessForbiddenException {

        Opinion opinion = opinionRepository.findOne(productId, opinionId)
                                           .orElseThrow(OpinionNotFoundException::new);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!Objects.equals(opinion.getAuthor().getId(), user.getId())) {
            throw new OpinionOperationAccessForbiddenException();
        }

        opinion.setDescription(dto.getDescription());
        opinion.setRate(dto.getRate());

        opinion.getPros().clear();
        opinion.getPros().addAll(dto.getPros().stream()
                                    .map(value -> new Advantage(value, opinion))
                                    .collect(Collectors.toSet()));

        opinion.getCons().clear();
        opinion.getCons().addAll(dto.getCons().stream()
                                    .map(value -> new Disadvantage(value, opinion))
                                    .collect(Collectors.toSet()));
        return mapOpinionToDto(opinionRepository.save(opinion));
    }

    public boolean existsById(UUID productId, UUID opinionId) {
        return opinionRepository.existsById(new OpinionId(productId, opinionId));
    }

    @Transactional
    public OpinionDetailsDto addReaction(UUID productId, UUID opinionId, boolean positive)
        throws OpinionNotFoundException {
        return opinionRepository.findOne(productId, opinionId)
                                .map(opinion -> {
                                    User author = (User) SecurityContextHolder.getContext()
                                                                              .getAuthentication()
                                                                              .getPrincipal();

                                    Reaction r = new Reaction(author, opinion, positive);
                                    Reaction saved = reactionRepository.save(r);

                                    if (!opinion.getReactions().add(saved)) {
                                        opinion.getReactions().remove(saved);
                                        opinion.getReactions().add(saved);
                                    }
                                    opinion.setLikesCounter(reactionRepository.calculateLikesCounter(opinion.getId()));
                                    return mapOpinionToDto(opinion);
                                })
                                .orElseThrow(OpinionNotFoundException::new);
    }

    @Transactional
    public OpinionDetailsDto removeReaction(UUID productId, UUID opinionId) throws OpinionNotFoundException {
        return opinionRepository.findOne(productId, opinionId)
                                .map(o -> {
                                    UUID userId = getCurrentUserId();

                                    ReactionId reactionId = new ReactionId(o.getId(), userId);

                                    reactionRepository.deleteById(reactionId);
                                    o.setLikesCounter(reactionRepository.calculateLikesCounter(o.getId()));

                                    return mapOpinionToDto(o);
                                })
                                .orElseThrow(OpinionNotFoundException::new);
    }

    private Opinion mapDtoToEntity(CreateOpinionDto createOpinionDto) {
        Opinion built = Opinion.builder()
                               .rate(createOpinionDto.getRate())
                               .description(createOpinionDto.getDescription())
                               .build();

        built.setPros(createOpinionDto.getPros()
                                      .stream()
                                      .map(p -> new Advantage(p, built))
                                      .collect(Collectors.toSet()));

        built.setCons(createOpinionDto.getCons()
                                      .stream()
                                      .map(c -> new Disadvantage(c, built))
                                      .collect(Collectors.toSet()));

        return built;
    }

    private OpinionDetailsDto mapOpinionToDto(Opinion opinion) {
        Boolean liked = reactionRepository.findReaction(opinion.getId().getProductId(),
                                                        opinion.getId().getOpinionId(),
                                                        getCurrentUserId());
        return OpinionDetailsDto.builder()
                                .productId(opinion.getId().getProductId())
                                .opinionId(opinion.getId().getOpinionId())
                                .createdAt(opinion.getCreatedAt())
                                .rate(opinion.getRate())
                                .description(opinion.getDescription())
                                .likesCounter(opinion.getLikesCounter())
                                .authorName(opinion.getAuthor().getVisibleName())
                                .pros(opinion.getPros())
                                .cons(opinion.getCons())
                                .isLiked(liked != null && liked)
                                .isDisliked(liked != null && !liked)
                                .build();
    }

    /**
     * Helper method to get current user's id from SecurityContextHolder.
     *
     * @return ID of the current user or null.
     */
    private UUID getCurrentUserId() {
        var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if (user != null) {
            return user.getId();
        }

        return null;
    }
}
