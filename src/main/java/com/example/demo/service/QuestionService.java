/**
 * 
 */
package com.example.demo.service;

import java.util.List;
import com.example.demo.domain.Question;
import com.example.demo.domain.Reply;
import com.example.demo.exception.RecordNotFoundException;

/**
 * @author kaihe
 *
 */
public interface QuestionService {

  Question addQuestion(Question question);

  Reply addReplyToQuestion(Long questionId, Reply reply) throws RecordNotFoundException;
  
  Question getQuestionById(Long questionId);
  
  List<Question> getAllQuestions();
}
