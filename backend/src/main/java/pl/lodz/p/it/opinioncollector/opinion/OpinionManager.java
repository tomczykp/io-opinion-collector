package pl.lodz.p.it.opinioncollector.opinion;

import java.util.List;
import java.util.UUID;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.it.opinioncollector.productManagment.ProductRepository;

@Service
@RequiredArgsConstructor
public class OpinionManager {

    private final OpinionRepository opinionRepository;
    private final ProductRepository productRepository;

    public List<Opinion> getOpinions(UUID productId) {
        return opinionRepository.findById_ProductId(productId);
    }

    public Opinion getOpinion(UUID productId, UUID opinionId) {
        // TODO throw custom exception
        return opinionRepository.findOne(productId, opinionId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Opinion create(UUID productId, CreateOpinionDto createOpinionDto) {
        return productRepository.findById(productId)
                                .map(product -> {
                                    Opinion opinion = mapDtoToEntity(createOpinionDto);
                                    opinion.setProduct(product);
                                    return opinionRepository.save(opinion);
                                })
                                .orElse(null);
        // TODO throw
    }

    public void deleteOpinion(UUID productId, UUID opinionId) {
        // TODO check if current user is the author
        opinionRepository.deleteById_ProductIdAndId_OpinionId(productId, opinionId);
    }

    @Transactional
    public Opinion update(UUID productId, UUID opinionId, CreateOpinionDto dto) {
        return opinionRepository.findOne(productId, opinionId)
                                .map(opinion -> {
                                    opinion.setDescription(dto.getDescription());
                                    opinion.setRate(dto.getRate());
                                    return opinionRepository.save(opinion);
                                })
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // TODO replace with custom exception
    }


    private Opinion mapDtoToEntity(CreateOpinionDto createOpinionDto) {
        return Opinion.builder()
                      .rate(createOpinionDto.getRate())
                      .description(createOpinionDto.getDescription())
                      .build();
    }
}
