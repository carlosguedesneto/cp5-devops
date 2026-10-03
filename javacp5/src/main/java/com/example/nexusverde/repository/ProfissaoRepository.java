package com.example.nexusverde.repository;

import com.example.nexusverde.entity.Profissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfissaoRepository extends JpaRepository<Profissao, Long> {}
