package com.sgs.sgs.controller;

import com.sgs.sgs.modal.Solicitante;
import com.sgs.sgs.repository.SolicitanteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitantes")
@CrossOrigin(origins = "*")
public class SolicitanteController {
    private final SolicitanteRepository repository;

    public SolicitanteController(SolicitanteRepository repository) { this.repository = repository; }

    @GetMapping
    public ResponseEntity<List<Solicitante>> listar() { return ResponseEntity.ok(repository.findAll()); }
}