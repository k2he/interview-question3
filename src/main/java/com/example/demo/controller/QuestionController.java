package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.domain.Question;
import com.example.demo.domain.Reply;
import com.example.demo.dto.GetQuestionResponseDto;
import com.example.demo.dto.QuestionDto;
import com.example.demo.dto.ReplyDto;
import com.example.demo.dto.ReplyMapper;
import com.example.demo.exception.RecordNotFoundException;
import com.example.demo.persistence.EmployeeEntity;
import com.example.demo.service.QuestionService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kaihe
 *
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

  @NonNull
  private QuestionService questionService;

  // Create a Question
  @PostMapping
  public ResponseEntity<QuestionDto> createQuestion(@Valid @RequestBody QuestionDto questionDto)
      throws RecordNotFoundException {
    log.debug("Creating a new question." + questionDto.toString());
    Question question = Question.builder().author(questionDto.getAuthor())
        .message(questionDto.getMessage()).build();

    Question addedQuestion = questionService.addQuestion(question);
    questionDto.setId(addedQuestion.getId());

    log.debug("Qeustion has been added." + addedQuestion.toString());
    return new ResponseEntity<QuestionDto>(questionDto, new HttpHeaders(), HttpStatus.CREATED);
  }

  @PostMapping("/{questionId}/reply")
  public ResponseEntity<ReplyDto> addReply(@RequestBody ReplyDto replyDto,
      @PathVariable Long questionId) throws RecordNotFoundException {
    log.debug("Add a new reply." + replyDto.toString());

    Reply reply =
        Reply.builder().author(replyDto.getAuthor()).message(replyDto.getMessage()).build();
    reply = questionService.addReply(questionId, reply);

    replyDto.setQuestionId(questionId);
    replyDto.setId(reply.getId());
    log.debug("Reply has been added." + replyDto.toString());
    return new ResponseEntity<ReplyDto>(replyDto, new HttpHeaders(), HttpStatus.CREATED);
  }

  @GetMapping("/{questionId}")
  public ResponseEntity<GetQuestionResponseDto> getQuestionById(@PathVariable("questionId") Long questionId)
      throws RecordNotFoundException {
    Question question = questionService.getQuestionById(questionId);

    // Convert result to response dto type
    List<ReplyDto> replies = question.getReplies().stream()
    .map(ReplyMapper::convertToReplyDto).collect(Collectors.toList());
    
    GetQuestionResponseDto responseDto = GetQuestionResponseDto.builder()
        .id(question.getId())
        .author(question.getAuthor())
        .message(question.getMessage())
        .replies(replies).build();
    
    return new ResponseEntity<GetQuestionResponseDto>(responseDto, new HttpHeaders(), HttpStatus.OK);
  }
}
