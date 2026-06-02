package com.sgs.sgs.service;

import com.sgs.sgs.dto.SolicitacaoListagemDTO;
import com.sgs.sgs.dto.SolicitacaoRequestDTO;
import com.sgs.sgs.exception.RegraNegocioException;
import com.sgs.sgs.modal.*;
import com.sgs.sgs.repository.CategoriaRepository;
import com.sgs.sgs.repository.HistoricoSolicitacaoRepository;
import com.sgs.sgs.repository.SolicitacaoRepository;
import com.sgs.sgs.repository.SolicitanteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitacaoService {
    private final SolicitacaoRepository solicitacaoRepository;
    private final SolicitanteRepository solicitanteRepository;
    private final CategoriaRepository categoriaRepository;
    private final HistoricoSolicitacaoRepository historicoRepository;

    public SolicitacaoService(SolicitacaoRepository solicitacaoRepository,
                              SolicitanteRepository solicitanteRepository,
                              CategoriaRepository categoriaRepository,
                              HistoricoSolicitacaoRepository historicoRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.solicitanteRepository = solicitanteRepository;
        this.categoriaRepository = categoriaRepository;
        this.historicoRepository = historicoRepository;
    }

    public Solicitacao cadastrar(SolicitacaoRequestDTO dto) {
        Solicitante solicitante = solicitanteRepository.findById(dto.getSolicitanteId())
                .orElseThrow(() -> new RegraNegocioException("Solicitante não encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RegraNegocioException("Categoria não encontrada"));

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setSolicitante(solicitante);
        solicitacao.setCategoria(categoria);
        solicitacao.setDescricao(dto.getDescricao());
        solicitacao.setValor(dto.getValor());
        solicitacao.setDataSolicitacao(LocalDate.now());
        solicitacao.setStatus(EnumStatusSolicitacao.SOLICITADO);
        Solicitacao salvar = solicitacaoRepository.save(solicitacao);

        gravarHistorico(salvar, null, EnumStatusSolicitacao.SOLICITADO);
        return salvar;
    }

    public List<SolicitacaoListagemDTO> listar(String status, Long categoriaId,
                                               LocalDate dataInicio, LocalDate dataFim) {
        return solicitacaoRepository
                .listaComFiltros(status, categoriaId, dataInicio, dataFim)
                .stream()
                .map(SolicitacaoListagemDTO::new)
                .collect(Collectors.toList());
    }

    public Solicitacao buscarPorId(Long id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Solicitação não encontrada"));
    }

    public Solicitacao atualizarStatus(Long id, EnumStatusSolicitacao novoStatus) {
        Solicitacao solicitacao = buscarPorId(id);
        EnumStatusSolicitacao statusAnterior = solicitacao.getStatus();
        validarTransicao(statusAnterior, novoStatus);
        solicitacao.setStatus(novoStatus);
        Solicitacao atualizacao = solicitacaoRepository.save(solicitacao);

        gravarHistorico(atualizacao, statusAnterior, novoStatus);
        return atualizacao;
    }

    private void validarTransicao(EnumStatusSolicitacao atual, EnumStatusSolicitacao novo) {
        boolean valido = switch (atual) {
            case SOLICITADO -> novo == EnumStatusSolicitacao.LIBERADO || novo == EnumStatusSolicitacao.REJEITADO;
            case LIBERADO -> novo == EnumStatusSolicitacao.APROVADO || novo == EnumStatusSolicitacao.REJEITADO;
            case APROVADO -> novo == EnumStatusSolicitacao.CANCELADO;
            case REJEITADO, CANCELADO -> false;
        };

        if (!valido)
            throw new RegraNegocioException("Transição inválida: " + atual + " -> " + novo);
    }

    private void gravarHistorico(
            Solicitacao solicitacao,
            EnumStatusSolicitacao anterior,
            EnumStatusSolicitacao novo) {
        HistoricoSolicitacao historico = new HistoricoSolicitacao();
        historico.setSolicitacao(solicitacao);
        historico.setStatusAnterior(anterior);
        historico.setStatusNovo(novo);
        historico.setDataAlteracao(LocalDateTime.now());
        historicoRepository.save(historico);
    }
}