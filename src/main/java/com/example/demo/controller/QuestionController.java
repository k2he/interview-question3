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
import com.example.demo.dto.QuestionDto;
import com.example.demo.dto.QuestionMapper;
import com.example.demo.dto.QuestionWithReplyDetailDto;
import com.example.demo.dto.ReplyDto;
import com.example.demo.dto.ReplyMapper;
import com.example.demo.exception.RecordNotFoundException;
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

  @PostMapping
  public ResponseEntity<QuestionDto> createQuestion(@Valid @RequestBody QuestionDto questionDto)
      throws RecordNotFoundException {
    log.debug("Creating a new question. {}", questionDto.toString());
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
    log.debug("Add a new reply: {}", replyDto.toString());

    Reply reply =
        Reply.builder().author(replyDto.getAuthor()).message(replyDto.getMessage()).build();
    reply = questionService.addReplyToQuestion(questionId, reply);

    ReplyDto responseDto = ReplyMapper.convertToReplyDto(reply);
    log.debug("Reply has been added." + responseDto.toString());
    return new ResponseEntity<ReplyDto>(responseDto, new HttpHeaders(), HttpStatus.CREATED);
  }

  @GetMapping("/{questionId}")
  public ResponseEntity<QuestionWithReplyDetailDto> getQuestionById(
      @PathVariable("questionId") Long questionId) throws RecordNotFoundException {
    log.debug("Get question by id {}", questionId);
    Question question = questionService.getQuestionById(questionId);

    // Convert result to response dto type
    List<ReplyDto> replies = question.getReplies().stream()
        .map(ReplyMapper::convertToReplyDtoSkipQuestionId).collect(Collectors.toList());

    QuestionWithReplyDetailDto responseDto =
        QuestionWithReplyDetailDto.builder()
          .id(question.getId())
          .author(question.getAuthor())
          .message(question.getMessage())
          .replies(replies).build();

    return new ResponseEntity<QuestionWithReplyDetailDto>(responseDto, new HttpHeaders(),
        HttpStatus.OK);
  }

  @GetMapping()
  public ResponseEntity<List<QuestionDto>> getAllQuestions() {
    log.debug("Get all questions.");
    List<Question> questionList = questionService.getAllQuestions();

    List<QuestionDto> questionDtoList = questionList.stream()
        .map(QuestionMapper::convertToQuestionDto).collect(Collectors.toList());

    return new ResponseEntity<List<QuestionDto>>(questionDtoList, new HttpHeaders(), HttpStatus.OK);
  }
}
