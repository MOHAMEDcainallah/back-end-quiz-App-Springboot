package com.candyquizz.quizzmanagement.controllers;

import com.candyquizz.quizzmanagement.models.Reponse;
import com.candyquizz.quizzmanagement.repositories.ReponseRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reponses")
public class ReponseController {
    @Autowired
    private ReponseRepository reponseRepository;

    @GetMapping
    public List<Reponse> getAllReponses() {
        return reponseRepository.findAll();
    }

    @PostMapping
    public Reponse createReponse(@RequestBody Reponse reponse) {
        return reponseRepository.save(reponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reponse> getReponseById(@PathVariable Long id) {
        Optional<Reponse> optionalReponse = reponseRepository.findById(id);
        if (optionalReponse.isPresent()) {
            return ResponseEntity.ok(optionalReponse.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}