package com.example.demo.dto;

import java.util.List;
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
public class GetQuestionResponseDto {
  
  private Long id;
  
  private Long questionId; 
  
//  @NotEmpty
  private String author;
  
//  @NotEmpty
  private String message;
  
  private List<ReplyDto> replies;
  
}
