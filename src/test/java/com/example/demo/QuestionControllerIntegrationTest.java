/**
 * 
 */
package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.demo.dto.QuestionDto;
import com.example.demo.dto.QuestionWithReplyDetailDto;
import com.example.demo.dto.ReplyDto;

/**
 * @author kaihe
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class QuestionControllerIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  private QuestionDto questionRequest;

  private ReplyDto replyRequest;

  private String getRootUrl() {
    return "http://localhost:" + port;
  }

  @BeforeEach
  void setUp() {
    questionRequest = QuestionDto.builder().author("author1").message("message1").build();
    replyRequest = ReplyDto.builder().author("author1").message("reply message").build();

  }

  @Test
  @DisplayName("Integration Test: POST /questions should return question with generated id.")
  public void postQuestion_ShouldReturnQuestion() throws Exception {
    // execute
    ResponseEntity<QuestionDto> responseEntity =
        restTemplate.postForEntity(getRootUrl() + "/questions", questionRequest, QuestionDto.class);

    // collect Response
    int status = responseEntity.getStatusCodeValue();
    QuestionDto resultQuestion = responseEntity.getBody();

    // verify
    Assertions.assertNotNull(responseEntity.getBody());
    Assertions.assertEquals(HttpStatus.CREATED.value(), status);
    Assertions.assertNotNull(resultQuestion);
    Assertions.assertNotNull(resultQuestion.getId().longValue());
  }

  @Test
  @DisplayName("Integration Test: POST /questions/{questionId}/reply should return reply with generated id and questionId = 1.")
  public void postReply_ShouldReturnReply() throws Exception {
    // prepare
    ResponseEntity<QuestionDto> questionEntity =
        restTemplate.postForEntity(getRootUrl() + "/questions", questionRequest, QuestionDto.class);
    QuestionDto questionDto = questionEntity.getBody();
    Long questionId = questionDto.getId();

    // execute
    ResponseEntity<ReplyDto> responseEntity = restTemplate.postForEntity(
        getRootUrl() + "/questions/" + questionId + "/reply", replyRequest, ReplyDto.class);

    // collect Response
    int status = responseEntity.getStatusCodeValue();
    ReplyDto result = responseEntity.getBody();

    // verify
    Assertions.assertNotNull(responseEntity.getBody());
    Assertions.assertEquals(HttpStatus.CREATED.value(), status);
    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getId().longValue());
    Assertions.assertEquals(questionId, result.getQuestionId());
  }

  @Test
  @DisplayName("GET /questions/{questionId} should return the question with matching Id.")
  public void getQuestion_ShouldReturnQuestion() throws Exception {
    // prepare
    ResponseEntity<QuestionDto> questionEntity =
        restTemplate.postForEntity(getRootUrl() + "/questions", questionRequest, QuestionDto.class);
    QuestionDto questionDto = questionEntity.getBody();
    Long questionId = questionDto.getId();
    restTemplate.postForEntity(getRootUrl() + "/questions/" + questionId + "/reply", replyRequest,
        ReplyDto.class);

    // execute
    QuestionWithReplyDetailDto result = restTemplate.getForObject(
        getRootUrl() + "/questions/" + questionId, QuestionWithReplyDetailDto.class);

    // verify
    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getReplies());
    Assertions.assertEquals(1, result.getReplies().size());
  }
  
  @Test
  @DisplayName("Integration Test: GET /questions should return list of questions")
  public void getAllQuestion_ShouldListofQuestions() throws Exception {
    // prepare
    ResponseEntity<QuestionDto> questionEntity =
        restTemplate.postForEntity(getRootUrl() + "/questions", questionRequest, QuestionDto.class);
    QuestionDto questionDto = questionEntity.getBody();
    Long questionId = questionDto.getId();
    restTemplate.postForEntity(getRootUrl() + "/questions/" + questionId + "/reply", replyRequest,
        ReplyDto.class);

    // execute
    ResponseEntity<QuestionDto[]> responseEntity = restTemplate.getForEntity(getRootUrl() + "/questions", QuestionDto[].class);
    
    // collect response
    int status = responseEntity.getStatusCodeValue();
    QuestionDto[] questionsArray = responseEntity.getBody();

    // verify
    Assertions.assertEquals(HttpStatus.OK.value(), status);
    Assertions.assertNotNull(questionsArray, "Question not found");
    Assertions.assertTrue(questionsArray.length > 0, "Incorrect Question List");
  }
}
