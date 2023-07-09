package com.candyquizz.quizzmanagement.repositories;

import com.candyquizz.quizzmanagement.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
