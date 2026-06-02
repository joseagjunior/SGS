package com.sgs.sgs.repository;

import com.sgs.sgs.modal.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    @Query(value = """
        SELECT S.id, SOL.nome AS solicitante_nome, SOL.cpf_cnpj AS cpf_cnpj,
            C.nome AS categoria_nome, S.status, S.valor, S.data_solicitacao
        FROM solicitacao AS S
        	INNER JOIN solicitante AS SOL ON SOL.id = S.solicitante_id
        	INNER JOIN categoria AS C ON C.id = S.categoria_id
        WHERE (:status IS NULL OR S.status = :status)
        	AND (:categoriaId IS NULL OR S.categoria_id = :categoriaId)
        	AND (CAST(:dataInicio AS DATE) IS NULL OR S.data_solicitacao >= CAST(:dataInicio AS DATE))
        	AND (CAST(:dataFim AS DATE) IS NULL OR S.data_solicitacao <= CAST(:dataFim AS DATE))
        ORDER BY S.data_solicitacao DESC
        """, nativeQuery = true)
    List<Object[]> listaComFiltros(
            @Param("status") String status,
            @Param("categoriaId") Long categoriaId,
            @Param("dataInicio")LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );
}