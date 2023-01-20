package pl.lodz.p.it.opinioncollector.qa;

import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.opinioncollector.eventHandling.EventManager;
import pl.lodz.p.it.opinioncollector.userModule.user.User;
import pl.lodz.p.it.opinioncollector.userModule.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
public class QAController {

    private final QAManager qaManager;
    private final EventManager eventManager;

    @Autowired
    public QAController(QAManager qaManager, EventManager eventManager) {
        this.qaManager = qaManager;
        this.eventManager = eventManager;
    }

    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return qaManager.getQuestions(x -> true);
    }

    @GetMapping("/questions/{questionId}")
    public ResponseEntity<Question> getQuestion(
            @PathVariable("questionId") String questionId) {
        Optional<Question> found =  qaManager.getQuestion(UUID.fromString(questionId));
        return found.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/products/{productId}/questions")
    public List<Question> getQuestionsByProductId(@PathVariable("productId") String productId) {
        return qaManager.getQuestions(q -> q.getProductId().equals(UUID.fromString(productId)));
    }

    @GetMapping("/questions/{questionId}/answers")
    public List<Answer> getAnswersByQuestionId(@PathVariable("questionId") String questionId) {
        return qaManager.getAnswers(a -> a.getQuestionId().equals(UUID.fromString(questionId)));
    }

    @PostMapping("/questions")
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        return ResponseEntity.ok(qaManager.createQuestion(question));
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable("questionId") String questionId) {
        boolean result = qaManager.deleteQuestion(UUID.fromString(questionId));
        if (result)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.notFound().build();

    }

    @GetMapping("/answers")
    public List<Answer> getAllAnswers() {
        return qaManager.getAnswers(x -> true);
    }

    @GetMapping("/answers/{answerId}")
    public ResponseEntity<Answer> getAnswer(
            @PathVariable("answerId") String answerId) {
        Optional<Answer> found =  qaManager.getAnswer(UUID.fromString(answerId));
        return found.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping("/answers")
    public ResponseEntity<Answer> createAnswer(@RequestBody Answer answer) {
        return ResponseEntity.ok(qaManager.createAnswer(answer));
    }

    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<Void> deleteAnswer(
            @PathVariable("answerId") String answerId) {
        boolean result = qaManager.deleteAnswer(UUID.fromString(answerId));
        if (result)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/questions/{questionId}/report",
            consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity reportQuestion(@PathVariable UUID questionId, @RequestBody @NotBlank String reason) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var question = qaManager.getQuestion(questionId);

        if (question.isEmpty())
            return ResponseEntity.notFound().build();

        eventManager.createQuestionReportEvent(user, question.get().getProductId(), "Question reported with reason: " + reason, questionId);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/answers/{answerId}/report",
            consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity reportAnswer(@PathVariable UUID answerId, @RequestBody @NotBlank String reason) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Answer> answer = qaManager.getAnswer(answerId);

        if (answer.isEmpty())
            return ResponseEntity.notFound().build();

        UUID productID = qaManager.getQuestion(answer.get().getQuestionId()).get().getProductId();

        eventManager.createAnswerReportEvent(user, productID,"Answer reported with reason: " + reason, answer.get().getAnswerId() , answer.get().getQuestionId());
        return ResponseEntity.ok().build();
    }

}
