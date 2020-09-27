/**
 * 
 */
package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kaihe
 *
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBL_REPLIES")
public class Reply {

  @Id
  @GeneratedValue
  private Long id;
  
  private String author;
  
  private String message;
  
  @ManyToOne
  @JoinColumn(name = "questionId")
  private Question question;
  /*
   * In real world, we may need add updatedTime, createTime using
   * @EnableJpaAuditing with @CreatedDate and @CreatedBy
   */
}
