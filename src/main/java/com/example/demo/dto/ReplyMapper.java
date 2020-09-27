package com.example.demo.dto;

import com.example.demo.domain.Reply;

public class ReplyMapper {

  public static ReplyDto convertToReplyDto(Reply reply) {
    return reply == null ? null : ReplyDto.builder()
        .id(reply.getId())
        .questionId(reply.getQuestion() == null ? null : reply.getQuestion().getId())
        .author(reply.getAuthor())
        .message(reply.getMessage()).build();
  }
  
  public static ReplyDto convertToReplyDtoSkipQuestionId(Reply reply) {
    return reply == null ? null : ReplyDto.builder()
        .id(reply.getId())
        .author(reply.getAuthor())
        .message(reply.getMessage()).build();
  }
  
}
