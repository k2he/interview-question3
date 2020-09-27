/**
 * 
 */
package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;
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
public class QuestionMapperTest {

  @Test
  @DisplayName("Test convertToQuestionDto() with null input should null.")
  public void convertToQuestionDtoNullInput_ShouldNull() {
    // when
    QuestionDto result = QuestionMapper.convertToQuestionDto(null);

    // then
    Assertions.assertNull(result);
  }
  
  @Test
  @DisplayName("Test convertToQuestionDto() with input should QuestionDto.")
  public void convertToQuestionDto_returnQuestionDto() {
    List<Reply> replies = new ArrayList<Reply>();
    replies.add(Reply.builder().message("reply1").build());
    replies.add(Reply.builder().message("reply2").build());
    
    Question question = Question.builder()
        .author("User1")
        .message("TestMessage1")
        .replies(replies).build();
    
    // when
    QuestionDto result = QuestionMapper.convertToQuestionDto(question);

    // then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(question.getAuthor(), result.getAuthor());
    Assertions.assertEquals(question.getMessage(), result.getMessage());
    Assertions.assertEquals(2, result.getReplies());
  }
}
