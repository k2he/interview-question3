package com.example.demo.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kaihe
 *
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class QuestionDto {
  
  private Long id;
  
  @NotBlank(message = "{author.notBlank}")
  private String author;
  
  @NotBlank(message = "{message.notBlank}")
  private String message;
  
  private int replies;
  
}
