package com.candyquizz.quizzmanagement.repositories;

import com.candyquizz.quizzmanagement.models.Resultat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultatRepository extends JpaRepository<Resultat, Long> {
}
