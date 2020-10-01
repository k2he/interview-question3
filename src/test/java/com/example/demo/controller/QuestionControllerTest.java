/**
 * 
 */
package com.example.demo.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.demo.domain.Question;
import com.example.demo.domain.Reply;
import com.example.demo.dto.QuestionDto;
import com.example.demo.dto.QuestionMapper;
import com.example.demo.dto.ReplyDto;
import com.example.demo.dto.ReplyMapper;
import com.example.demo.exception.RecordNotFoundException;
import com.example.demo.service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author kaihe
 *
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = QuestionController.class) // This will not start up entire application
public class QuestionControllerTest {
  
  @MockBean
  private QuestionService questionService;

  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @BeforeEach
  void setUp() {
    //TODO: add initial value setup if needed
  }

  @Test
  @DisplayName("POST /questions should return question with generated id.")
  public void postQuestion_ShouldReturnQuestion() throws Exception {
    QuestionDto questionRequest = QuestionDto.builder()
        .author("author1")
        .message("message1").build();
    
    Question questionInserted = Question.builder()
        .id(new Long(1))
        .author(questionRequest.getAuthor())
        .message(questionRequest.getMessage()).build();
    
    // given
    String expected = objectMapper.writeValueAsString(QuestionMapper.convertToQuestionDto(questionInserted));
    Mockito.when(questionService.addQuestion(any(Question.class))).thenReturn(questionInserted);
    
    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/questions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(questionRequest))
            .accept(MediaType.APPLICATION_JSON);

    // when 
    MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();
    
    // then
    Assertions.assertEquals(expected, mvcResult.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("POST /questions/1/reply should return reply with generated id and questionId = 1.")
  public void postReply_ShouldReturnReply() throws Exception {
    ReplyDto replyRequest = ReplyDto.builder()
        .author("author1")
        .message("reply message").build();
    
    Long questionId = new Long(1);
    Question question = Question.builder()
        .id(questionId)
        .author("author2")
        .message("question message").build();
    Reply replyInserted = Reply.builder()
        .id(new Long(2))
        .author(replyRequest.getAuthor())
        .message(replyRequest.getMessage())
        .question(question).build();
        
    // given
    String expected = objectMapper.writeValueAsString(ReplyMapper.convertToReplyDto(replyInserted));
    Mockito.when(questionService.addReplyToQuestion(eq(questionId), any(Reply.class))).thenReturn(replyInserted);
    
    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/questions/{questionId}/reply", questionId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(replyRequest))
            .accept(MediaType.APPLICATION_JSON);

    // when
    MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();
    
    // then
    Assertions.assertEquals(expected, mvcResult.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("GET /questions/1 should return the question with Id = 1.")
  public void getQuestion_ShouldReturnQuestion() throws Exception {
    Long questionId = new Long(1);
    
    Reply reply = Reply.builder()
        .id(new Long(2))
        .author("reply author1")
        .message("reply message").build();
    Question question = Question.builder()
        .id(questionId)
        .author("author2")
        .message("question message")
        .replies(Stream.of(reply).collect(Collectors.toList())).build();
        
    // given
    Mockito.when(questionService.getQuestionById(questionId)).thenReturn(question);
    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/questions/{questionId}", questionId)
            .accept(MediaType.APPLICATION_JSON);
    
    // when, then
    mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.author", is("author2")))
            .andExpect(jsonPath("$.message", is("question message")))
            .andExpect(jsonPath("$.replies", hasSize(1)))
            .andExpect(jsonPath("$.replies[0].author", is(reply.getAuthor())))
            .andReturn();
  }
  
  @Test
  @DisplayName("GET /questions/10 - not found. ")
  public void getQuestion_ShouldReturnNotFound() throws Exception {
    Long requestId = new Long(10);
    
    // given
    Mockito.when(questionService.getQuestionById(requestId)).thenThrow(new RecordNotFoundException(""));
    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/questions/{questionId}", requestId)
            .accept(MediaType.APPLICATION_JSON);
    
    // when, then
    mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
  
  @Test
  @DisplayName("GET /questions should return list of questions")
  public void getAllQuestion_ShouldListofQuestions() throws Exception {
    Reply reply = Reply.builder()
        .id(3L)
        .author("reply author1")
        .message("reply message").build();
    // Build a question with 1 reply
    Question question1 = Question.builder()
        .id(1L)
        .author("author1")
        .message("question message")
        .replies(Stream.of(reply).collect(Collectors.toList())).build();
    
    // Build a question with 0 reply
    Question question2 = Question.builder()
        .id(2L)
        .author("author2")
        .message("question message").build();
    
    List<Question> questionList = Arrays.asList(question1, question2);

    // given
    when(questionService.getAllQuestions()).thenReturn(questionList);
    RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
            "/questions").accept(MediaType.APPLICATION_JSON);
    
    // when, then
    mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].author", is("author1")))
            .andExpect(jsonPath("$[0].replies", is(1)))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].author", is("author2")))
            .andExpect(jsonPath("$[1].replies", is(0)));
  }
}
