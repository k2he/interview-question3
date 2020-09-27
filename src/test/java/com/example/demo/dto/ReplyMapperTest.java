/**
 * 
 */
package com.example.demo.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.domain.Question;
import com.example.demo.domain.Reply;

/**
 * @author kaihe
 *
 */
@ExtendWith(MockitoExtension.class)
public class ReplyMapperTest {

  @Test
  @DisplayName("Test convertToReplyDto() with null input should null.")
  public void convertToReplyDtoNullInput_ShouldNull() {
    // when
    ReplyDto result = ReplyMapper.convertToReplyDto(null);

    // then
    Assertions.assertNull(result);
  }
  
  @Test
  @DisplayName("Test convertToReplyDto() with input should ReplyDto.")
  public void convertToReplyDto_returnReplyDto() {
    Question question = Question.builder()
        .id(1L)
        .author("User1")
        .message("TestMessage1")
        .build();
    
    Reply reply = Reply.builder()
        .id(2L)
        .author("User1")
        .message("Reply Message1")
        .question(question)
        .build();
    
    // when
    ReplyDto result = ReplyMapper.convertToReplyDto(reply);

    // then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(reply.getId(), result.getId());
    Assertions.assertEquals(question.getId(), result.getQuestionId());
    Assertions.assertEquals(reply.getAuthor(), result.getAuthor());
    Assertions.assertEquals(reply.getMessage(), result.getMessage());
  }
  
  @Test
  @DisplayName("Test convertToReplyDto() with input but no Question should ReplyDto without questionId.")
  public void convertToReplyDtoNoQuestion_returnReplyDto() {
    Reply reply = Reply.builder()
        .id(1L)
        .author("User1")
        .message("Reply Message1")
        .build();
    
    // when
    ReplyDto result = ReplyMapper.convertToReplyDto(reply);

    // then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(reply.getId(), result.getId());
    Assertions.assertEquals(reply.getAuthor(), result.getAuthor());
    Assertions.assertEquals(reply.getMessage(), result.getMessage());
    Assertions.assertNull(result.getQuestionId());
  }
  
  @Test
  @DisplayName("Test convertToReplyDto() with null input should null.")
  public void convertToReplyDtoSkipQuestionIdNullInput_ShouldNull() {
    // when
    ReplyDto result = ReplyMapper.convertToReplyDtoSkipQuestionId(null);

    // then
    Assertions.assertNull(result);
  }
  
  @Test
  @DisplayName("Test convertToReplyDtoSkipQuestionId() with input should ReplyDto without QuestionId set.")
  public void convertToReplyDtoSkipQuestionId_returnReplyDto() {
    Question question = Question.builder()
        .id(1L)
        .author("User1")
        .message("TestMessage1")
        .build();
    
    Reply reply = Reply.builder()
        .id(2L)
        .question(question)
        .author("User1")
        .message("Reply Message1")
        .build();
    
    // when
    ReplyDto result = ReplyMapper.convertToReplyDtoSkipQuestionId(reply);

    // then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(reply.getId(), result.getId());
    Assertions.assertEquals(reply.getAuthor(), result.getAuthor());
    Assertions.assertEquals(reply.getMessage(), result.getMessage());
    Assertions.assertNull(result.getQuestionId());
  }
}
