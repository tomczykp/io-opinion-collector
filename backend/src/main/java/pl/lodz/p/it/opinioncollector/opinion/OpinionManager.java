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
import pl.lodz.p.it.opinioncollector.productManagment.ProductRepository;
import pl.lodz.p.it.opinioncollector.exceptions.products.ProductNotFoundException;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

@Service
@RequiredArgsConstructor
public class OpinionManager {

    private final OpinionRepository opinionRepository;
    private final ProductRepository productRepository;

    public List<Opinion> getOpinions(UUID productId) {
        return opinionRepository.findById_ProductId(productId);
    }

    public Opinion getOpinion(UUID productId, UUID opinionId)
        throws OpinionNotFoundException {
        return opinionRepository.findOne(productId, opinionId)
                                .orElseThrow(OpinionNotFoundException::new);
    }

    public Opinion create(UUID productId, CreateOpinionDto createOpinionDto) throws ProductNotFoundException {
        return productRepository.findById(productId)
                                .map(product -> {
                                    User user = (User) SecurityContextHolder.getContext()
                                                                            .getAuthentication()
                                                                            .getPrincipal();
                                    Opinion opinion = mapDtoToEntity(createOpinionDto);
                                    opinion.setProduct(product);
                                    opinion.setAuthor(user);
                                    return opinionRepository.save(opinion);
                                })
                                .orElseThrow(ProductNotFoundException::new);
    }

    public void deleteOpinion(UUID productId, UUID opinionId)
        throws OpinionOperationAccessForbiddenException {
        Optional<Opinion> opinionOptional = opinionRepository.findOne(productId, opinionId);

        if (opinionOptional.isPresent()) {
            Opinion opinion = opinionOptional.get();
            UUID userId = ((User) SecurityContextHolder.getContext()
                                                       .getAuthentication()
                                                       .getPrincipal()).getId();
            if (Objects.equals(opinion.getAuthor().getId(), userId)) {
                opinionRepository.deleteById_ProductIdAndId_OpinionId(productId, opinionId);
            } else {
                throw new OpinionOperationAccessForbiddenException();
            }
        }
    }

    @Transactional
    public Opinion update(UUID productId, UUID opinionId, CreateOpinionDto dto)
        throws OpinionNotFoundException, OpinionOperationAccessForbiddenException {

        Optional<Opinion> opinionOptional = opinionRepository.findOne(productId, opinionId);

        if (opinionOptional.isPresent()) {
            Opinion opinion = opinionOptional.get();

            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (!Objects.equals(opinion.getAuthor().getId(), user.getId())) {
                throw new OpinionOperationAccessForbiddenException();
            }

            opinion.setDescription(dto.getDescription());
            opinion.setRate(dto.getRate());
            return opinionRepository.save(opinion);
        } else {
            throw new OpinionNotFoundException();
        }
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
}
