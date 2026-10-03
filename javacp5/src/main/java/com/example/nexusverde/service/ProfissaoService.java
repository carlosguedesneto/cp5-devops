package com.example.nexusverde.service;

import com.example.nexusverde.entity.Profissao;
import com.example.nexusverde.repository.ProfissaoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProfissaoService {
    private final ProfissaoRepository repository;

    public ProfissaoService(ProfissaoRepository repository) {
        this.repository = repository;
    }

    public List<Profissao> listarTodos() {
        return repository.findAll();
    }

    public Optional<Profissao> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Profissao criar(Profissao profissao) {
        return repository.save(profissao);
    }

    public Optional<Profissao> atualizar(Long id, Profissao dados) {
        return repository.findById(id).map(profissao -> {
            profissao.setNome(dados.getNome());
            profissao.setDescricao(dados.getDescricao());
            return repository.save(profissao);
        });
    }

    public boolean deletar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
