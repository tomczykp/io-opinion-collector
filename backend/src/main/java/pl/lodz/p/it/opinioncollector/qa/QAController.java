package pl.lodz.p.it.opinioncollector.qa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class QAController {

    private final QAManager qaManager;

    @Autowired
    public QAController(QAManager qaManager) {
        this.qaManager = qaManager;
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

}
