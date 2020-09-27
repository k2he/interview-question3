package com.example.demo.dto;

import com.example.demo.domain.Question;

public class QuestionMapper {

  public static QuestionDto convertToQuestionDto(Question question) {
    return question == null ? null : QuestionDto.builder()
        .id(question.getId())
        .author(question.getAuthor())
        .message(question.getMessage())
        .replies(question.getReplies() != null ? question.getReplies().size() : 0)
        .build();
  }
}
