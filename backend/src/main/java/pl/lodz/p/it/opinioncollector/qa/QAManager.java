package pl.lodz.p.it.opinioncollector.qa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.opinioncollector.eventHandling.EventManager;
import pl.lodz.p.it.opinioncollector.userModule.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Service
public class QAManager {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final EventManager eventManager;

    @Autowired
    public QAManager(QuestionRepository questionRepository, AnswerRepository answerRepository, EventManager eventManager) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.eventManager = eventManager;
    }

    public Question createQuestion(Question question) {
        question.setDate(LocalDateTime.now());
        User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        question.setAuthor(user);
        eventManager.createQuestionNotifyEvent(user, question.getContent(), question.getQuestionId());
        return questionRepository.save(question);
    }

    public List<Question> getQuestions(Predicate<Question> questionPredicate) {
        List<Question> allQuestions = questionRepository.findAll();
        List<Question> result = new ArrayList<>();
        allQuestions.forEach(q -> {if (questionPredicate.test(q)) result.add(q);});

        return result;
    }

    public Optional<Question> getQuestion(UUID questionId) {
        return questionRepository.findById(questionId);
    }

    public boolean deleteQuestion(UUID questionId) {
        try {
            questionRepository.deleteById(questionId);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    public Answer createAnswer(Answer answer) {
        answer.setDate(LocalDateTime.now());
        User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        answer.setAuthor(user);

        Question question = getQuestion(answer.getQuestionId()).get();
        String description = String.format("New answer to your question: %s", answer.getContent());
        eventManager.createAnswerNotifyEvent(question.getAuthor(), description, answer.getQuestionId());

        return answerRepository.save(answer);
    }

    public List<Answer> getAnswers(Predicate<Answer> answerPredicate) {
        List<Answer> allAnswers = answerRepository.findAll();
        List<Answer> result = new ArrayList<>();
        allAnswers.forEach(a -> {if (answerPredicate.test(a)) result.add(a);});
        return result;
    }

    public Optional<Answer> getAnswer(UUID answerId) {
        return answerRepository.findById(answerId);
    }

    public boolean deleteAnswer(UUID answerId) {
        try {
            answerRepository.deleteById(answerId);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;

    }



}
