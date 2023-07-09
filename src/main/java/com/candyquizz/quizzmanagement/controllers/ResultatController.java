package com.candyquizz.quizzmanagement.controllers;


import com.candyquizz.quizzmanagement.models.Resultat;
import com.candyquizz.quizzmanagement.repositories.ResultatRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/resultats")
public class ResultatController {
    @Autowired
    private ResultatRepository resultatRepository;

    @GetMapping
    public List<Resultat> getAllResultats() {
        return resultatRepository.findAll();
    }

    @PostMapping
    public Resultat createResultat(@RequestBody Resultat resultat) {
        return resultatRepository.save(resultat);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resultat> getResultatById(@PathVariable Long id) {
        Optional<Resultat> optionalResultat = resultatRepository.findById(id);
        if (optionalResultat.isPresent()) {
            return ResponseEntity.ok(optionalResultat.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}