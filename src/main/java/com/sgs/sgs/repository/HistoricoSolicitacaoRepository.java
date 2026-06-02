package com.sgs.sgs.repository;

import com.sgs.sgs.modal.HistoricoSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoSolicitacaoRepository extends JpaRepository<HistoricoSolicitacao, Long> {
    List<HistoricoSolicitacao> findBySolicitacaoIdOrderByDataAlteracaoAsc(Long solicitacaoId);
}