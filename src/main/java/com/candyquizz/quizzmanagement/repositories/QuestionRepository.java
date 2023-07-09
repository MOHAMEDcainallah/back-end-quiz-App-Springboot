package com.candyquizz.quizzmanagement.repositories;

import com.candyquizz.quizzmanagement.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
