package pl.lodz.p.it.opinioncollector.opinion;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.opinioncollector.opinion.exceptions.OpinionNotFoundException;
import pl.lodz.p.it.opinioncollector.opinion.exceptions.OpinionOperationAccessForbiddenException;
import pl.lodz.p.it.opinioncollector.productManagment.ProductRepository;
import pl.lodz.p.it.opinioncollector.productManagment.exceptions.ProductNotFoundException;
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

    public Opinion create(UUID productId, CreateOpinionDto createOpinionDto) {
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

    public void deleteOpinion(UUID productId, UUID opinionId) throws OpinionOperationAccessForbiddenException {
        Optional<Opinion> opinionOptional = opinionRepository.findOne(productId, opinionId);

        if (opinionOptional.isPresent()) {
            Opinion opinion = opinionOptional.get();
            Long userId = ((User) SecurityContextHolder.getContext()
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
        throws OpinionNotFoundException {
        return opinionRepository.findOne(productId, opinionId)
                                .map(opinion -> {
                                    opinion.setDescription(dto.getDescription());
                                    opinion.setRate(dto.getRate());
                                    return opinionRepository.save(opinion);
                                })
                                .orElseThrow(OpinionNotFoundException::new);
    }


    private Opinion mapDtoToEntity(CreateOpinionDto createOpinionDto) {
        return Opinion.builder()
                      .rate(createOpinionDto.getRate())
                      .description(createOpinionDto.getDescription())
                      .build();
    }
}
