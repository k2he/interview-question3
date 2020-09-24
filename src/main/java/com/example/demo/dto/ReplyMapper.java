package com.example.demo.dto;

import com.example.demo.domain.Reply;

public class ReplyMapper {

  public static ReplyDto convertToReplyDto(Reply reply) {
    return reply == null ? null : ReplyDto.builder()
        .id(reply.getId())
        .author(reply.getAuthor())
        .message(reply.getMessage())
        .questionId(reply.getQuestion().getId()).build();
  }
  
  public static Reply convertToReply(ReplyDto replyDto) {
    return replyDto == null ? null : Reply.builder()
        .id(replyDto.getId())
        .author(replyDto.getAuthor())
        .message(replyDto.getMessage()).build();
  }
}
