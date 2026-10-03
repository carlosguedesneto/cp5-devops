package com.example.nexusverde.controller;

import com.example.nexusverde.entity.Profissao;
import com.example.nexusverde.service.ProfissaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/profissoes")
public class ProfissaoController {
    private final ProfissaoService service;

    public ProfissaoController(ProfissaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Profissao> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profissao> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Profissao criar(@RequestBody Profissao profissao) {
        return service.criar(profissao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profissao> atualizar(@PathVariable Long id, @RequestBody Profissao profissao) {
        return service.atualizar(id, profissao)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (service.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
