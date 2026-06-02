package com.sgs.sgs.controller;

import com.sgs.sgs.dto.SolicitacaoListagemDTO;
import com.sgs.sgs.dto.SolicitacaoRequestDTO;
import com.sgs.sgs.modal.EnumStatusSolicitacao;
import com.sgs.sgs.modal.HistoricoSolicitacao;
import com.sgs.sgs.modal.Solicitacao;
import com.sgs.sgs.repository.HistoricoSolicitacaoRepository;
import com.sgs.sgs.service.SolicitacaoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/solicitacoes")
@CrossOrigin(origins = "*")
public class SolicitacaoController {
    private final SolicitacaoService service;
    private final HistoricoSolicitacaoRepository historicoRepository;

    public SolicitacaoController(
            SolicitacaoService service,
            HistoricoSolicitacaoRepository historicoRepository) {
        this.service = service;
        this.historicoRepository = historicoRepository;
    }

    @PostMapping
    public ResponseEntity<Solicitacao> cadastrar(@RequestBody @Valid SolicitacaoRequestDTO dto) {
        Solicitacao salva = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoListagemDTO>> listar(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return ResponseEntity.ok(service.listar(status, categoriaId, dataInicio, dataFim));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solicitacao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Solicitacao> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        EnumStatusSolicitacao novoStatus = EnumStatusSolicitacao.valueOf(body.get("status"));
        return ResponseEntity.ok(service.atualizarStatus(id, novoStatus));
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<List<HistoricoSolicitacao>> buscarHistorico(@PathVariable Long id) {
        List<HistoricoSolicitacao> historico = historicoRepository
                .findBySolicitacaoIdOrderByDataAlteracaoAsc(id);
        return ResponseEntity.ok(historico);
    }
}