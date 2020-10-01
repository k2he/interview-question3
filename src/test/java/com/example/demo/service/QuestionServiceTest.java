/**
 * 
 */
package com.example.demo.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.example.demo.domain.Question;
import com.example.demo.domain.Reply;
import com.example.demo.exception.RecordNotFoundException;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.ReplyRepository;

/**
 * @author kaihe
 *
 */
@SpringBootTest
public class QuestionServiceTest {

  @MockBean
  private QuestionRepository questionRepository;

  @MockBean
  private ReplyRepository replyRepository;

  @Autowired
  private QuestionService questionService;

  @Test
  @DisplayName("Test addQuestion() should return the question.")
  public void addQuestion_ShouldReturnQuestion() throws Exception {
    String author = "Author1";
    String message = "message1";
    Long id = 1L;

    Question question = Question.builder().id(id).author(author).message(message).build();

    Question insertedQuestion = Question.builder().id(id).author(author).message(message).build();

    // given
    Mockito.when(questionRepository.save(question)).thenReturn(insertedQuestion);

    // when
    Question result = questionService.addQuestion(question);

    // then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(id, result.getId());
    verify(questionRepository, times(1)).save(question);
  }

  @Test
  @DisplayName("Test addReplyToQuestion() with Question found, should return the reply.")
  public void addReply_ShouldReturnReply() throws Exception {
    Long questionId = 1L;

    Question question =
        Question.builder().id(questionId).author("Author1").message("message1").build();

    Reply reply = Reply.builder().author("author2").message("reply message1").build();

    // given
    Mockito.when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
    Mockito.when(replyRepository.save(reply)).thenReturn(reply);

    // when
    Reply result = questionService.addReplyToQuestion(questionId, reply);

    // then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getQuestion(), question);
    verify(questionRepository, times(1)).findById(questionId);
    verify(replyRepository, times(1)).save(reply);
  }

  @Test
  @DisplayName("Test addReplyToQuestion() with questionId doesn't exist, should throw RecordNotFoundException.")
  public void addReplyNotFoundQuestion_ShouldThrowExcepton() {
    Long questionId = 1L;

    Reply reply = Reply.builder().author("author2").message("reply message1").build();

    // given
    Mockito.when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

    // when
    Throwable exception = Assertions.assertThrows(RecordNotFoundException.class, () -> {
      questionService.addReplyToQuestion(questionId, reply);
    });

    // then
    Assertions.assertNotNull(exception);
    verify(questionRepository, times(1)).findById(questionId);
    verify(replyRepository, times(0)).save(reply);
  }

  @Test
  @DisplayName("Test getQuestionById() with questionId doesn't exist, should return null.")
  public void getQuestionByIdNotFound_ShouldThrowExcepton() {
    Long questionId = 1L;

    // given
    Mockito.when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

    // when
    Throwable exception = Assertions.assertThrows(RecordNotFoundException.class, () -> {
      questionService.getQuestionById(questionId);
    });
    
    // then
    Assertions.assertNotNull(exception);
    verify(questionRepository, times(1)).findById(questionId);
  }

  @Test
  @DisplayName("Test getQuestionById() with questionId found, should return Question.")
  public void getQuestionById_ShouldReturnResult() throws RecordNotFoundException {
    Long questionId = 1L;

    Question question = Question.builder()
        .id(questionId)
        .author("Author1")
        .message("message1").build();


    // given
    Mockito.when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

    // when
    Question result = questionService.getQuestionById(questionId);

    // then
    Assertions.assertNotNull(result);
    verify(questionRepository, times(1)).findById(questionId);
  }
  
  @Test
  @DisplayName("Test getAllQuestions() should return all Questions.")
  public void getAllQuestion_ShouldReturnList() {
    List<Question> questionList = new ArrayList<Question>();
    questionList.add(Question.builder()
        .id(1L)
        .author("Author1")
        .message("message1").build());
    questionList.add(Question.builder()
        .id(1L)
        .author("Author2")
        .message("message2").build());

    // given
    Mockito.when(questionRepository.findAll()).thenReturn(questionList);

    // when
    List<Question> result = questionService.getAllQuestions();

    // then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(2, result.size());
    verify(questionRepository, times(1)).findAll();
  }
  
  @Test
  @DisplayName("Test getAllQuestions() not record, should return empty list.")
  public void getAllQuestionNotFound_ShouldReturnEmptyList() {
    List<Question> questionList = new ArrayList<Question>();

    // given
    Mockito.when(questionRepository.findAll()).thenReturn(questionList);

    // when
    List<Question> result = questionService.getAllQuestions();

    // then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.size());
    verify(questionRepository, times(1)).findAll();
  }
}
