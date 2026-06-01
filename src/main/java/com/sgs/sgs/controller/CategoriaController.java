package com.sgs.sgs.controller;

import com.sgs.sgs.modal.Categoria;
import com.sgs.sgs.repository.CategoriaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {
    private final CategoriaRepository repository;

    public CategoriaController(CategoriaRepository repository) { this.repository = repository; }

    @GetMapping
    public ResponseEntity<List<Categoria>> listar() { return ResponseEntity.ok(repository.findAll()); }
}