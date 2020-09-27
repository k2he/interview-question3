/**
 * 
 */
package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.demo.domain.Question;
import com.example.demo.domain.Reply;
import com.example.demo.exception.RecordNotFoundException;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.ReplyRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author kaihe
 *
 */

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {

  @NonNull
  private QuestionRepository questionRepository;

  @NonNull
  private ReplyRepository replyRepository;

  @Override
  public Question addQuestion(Question question) {
    return questionRepository.save(question);
  }

  @Override
  public Reply addReplyToQuestion(Long questionId, Reply reply) throws RecordNotFoundException {
    Question question = questionRepository.findById(questionId)
        .orElseThrow(() -> new RecordNotFoundException("No Question found with given id: " + questionId));
    reply.setQuestion(question);
    return replyRepository.save(reply);
  }

  @Override
  public Question getQuestionById(Long questionId) {
    Optional<Question> result = questionRepository.findById(questionId);
    return result.isPresent() ? result.get() : null;
  }

  @Override
  public List<Question> getAllQuestions() {
    return questionRepository.findAll();
  }

}
