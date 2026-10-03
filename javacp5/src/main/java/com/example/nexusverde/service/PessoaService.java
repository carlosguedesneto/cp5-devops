package com.example.nexusverde.service;

import com.example.nexusverde.entity.Pessoa;
import com.example.nexusverde.repository.PessoaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {
    private final PessoaRepository repository;

    public PessoaService(PessoaRepository repository) {
        this.repository = repository;
    }

    public List<Pessoa> listarTodos() {
        return repository.findAll();
    }

    public Optional<Pessoa> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Pessoa criar(Pessoa pessoa) {
        return repository.save(pessoa);
    }

    public Optional<Pessoa> atualizar(Long id, Pessoa dados) {
        return repository.findById(id).map(pessoa -> {
            pessoa.setNome(dados.getNome());
            pessoa.setIdade(dados.getIdade());
            pessoa.setProfissao(dados.getProfissao());
            return repository.save(pessoa);
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
