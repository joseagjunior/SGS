package com.sgs.sgs.service;

import com.sgs.sgs.dto.SolicitacaoListagemDTO;
import com.sgs.sgs.dto.SolicitacaoRequestDTO;
import com.sgs.sgs.exception.RegraNegocioException;
import com.sgs.sgs.modal.Categoria;
import com.sgs.sgs.modal.EnumStatusSolicitacao;
import com.sgs.sgs.modal.Solicitacao;
import com.sgs.sgs.modal.Solicitante;
import com.sgs.sgs.repository.CategoriaRepository;
import com.sgs.sgs.repository.SolicitacaoRepository;
import com.sgs.sgs.repository.SolicitanteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitacaoService {
    private final SolicitacaoRepository solicitacaoRepository;
    private final SolicitanteRepository solicitanteRepository;
    private final CategoriaRepository categoriaRepository;

    public SolicitacaoService(SolicitacaoRepository solicitacaoRepository,
                              SolicitanteRepository solicitanteRepository,
                              CategoriaRepository categoriaRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.solicitanteRepository = solicitanteRepository;
        this.categoriaRepository = categoriaRepository;
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

        return solicitacaoRepository.save(solicitacao);
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
        validarTransicao(solicitacao.getStatus(), novoStatus);
        solicitacao.setStatus(novoStatus);
        return solicitacaoRepository.save(solicitacao);
    }

    private void validarTransicao(EnumStatusSolicitacao atual, EnumStatusSolicitacao novo) {
        boolean valido = switch (atual) {
            case SOLICITADO -> novo == EnumStatusSolicitacao.LIBERADO || novo == EnumStatusSolicitacao.REJEITADO;
            case LIBERADO -> novo == EnumStatusSolicitacao.APROVADO || novo == EnumStatusSolicitacao.REJEITADO;
            case APROVADO -> novo == EnumStatusSolicitacao.REJEITADO;
            case REJEITADO, CANCELADO -> false;
        };

        if (!valido)
            throw new RegraNegocioException("Transição inválida: " + atual + " -> " + novo);
    }
}
