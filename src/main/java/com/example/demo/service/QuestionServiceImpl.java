/**
 * 
 */
package com.example.demo.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demo.domain.Question;
import com.example.demo.domain.Reply;
import com.example.demo.exception.RecordNotFoundException;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.ReplyRepository;
import com.example.demo.util.DemoConstants;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author kaihe
 *
 */

@Builder
@RequiredArgsConstructor // It's suggested to use constructor injection over field injection
@Service
public class QuestionServiceImpl implements QuestionService {

  @NonNull
  private QuestionRepository questionRepository;

  @NonNull
  private ReplyRepository replyRepository;

  @NonNull
  private MessageService messageService;

  @Override
  public Question addQuestion(Question question) {
    return questionRepository.save(question);
  }

  @Override
  public Reply addReplyToQuestion(Long questionId, Reply reply) throws RecordNotFoundException {
    Question question = getQuestionById(questionId);

    // if found, then set question to reply
    reply.setQuestion(question);
    return replyRepository.save(reply);
  }

  @Override
  public Question getQuestionById(Long questionId) throws RecordNotFoundException {
    return questionRepository.findById(questionId)
        .orElseThrow(() -> new RecordNotFoundException(messageService
            .getMessage(DemoConstants.QUESTION_NOT_FOUND_ERROR_CODE, questionId.toString()))); // load
                                                                                               // error
                                                                                               // message
                                                                                               // from
                                                                                               // messages.properties
                                                                                               // file
  }

  @Override
  public List<Question> getAllQuestions() {
    return questionRepository.findAll();
  }
}
