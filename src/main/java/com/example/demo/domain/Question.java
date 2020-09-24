/**
 * 
 */
package com.example.demo.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "TBL_QUESTIONS")
public class Question {

  @Id
  @GeneratedValue
  private Long id;

  private String author;

  private String message;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", cascade=CascadeType.ALL)
  private List<Reply> replies;

  /*
   * In real world, we may need add updatedTime, createTime using
   * @EnableJpaAuditing with @CreatedDate
   */
  
  
}
