package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.example.demo.domain.Question;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class QuestionRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private QuestionRepository questionRepository;

  // In real world project, we don't need below code, but we can perform similar test for your
  // customized code in repository.
  @Test
  @DisplayName("Test Insert 2 questions and findAll() should return 2 questions.")
  public void findAll_ShouldReturnList() {
    List<Question> questions = new ArrayList<Question>();
    questions.add(Question.builder().author("User1").message("TestMessage1").build());
    questions.add(Question.builder().author("User2").message("TestMessage2").build());
    
    // given
    for (Question question : questions) {
      entityManager.persist(question);
    }
    entityManager.flush();

    // when
    List<Question> result = questionRepository.findAll();

    // then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(2, result.size());
  }
}
