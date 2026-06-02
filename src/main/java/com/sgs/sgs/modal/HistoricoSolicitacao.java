package com.sgs.sgs.modal;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_solicitacao")
public class HistoricoSolicitacao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "solicitacao_id", nullable = false)
    private Solicitacao solicitacao;

    @Column(name = "status_anterior")
    @Enumerated(EnumType.STRING)
    private EnumStatusSolicitacao statusAnterior;

    @Column(name = "status_novo")
    @Enumerated(EnumType.STRING)
    private EnumStatusSolicitacao statusNovo;

    @Column(name = "data_alteracao")
    private LocalDateTime dataAlteracao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Solicitacao getSolicitacao() { return solicitacao; }
    public void setSolicitacao(Solicitacao solicitacao) { this.solicitacao = solicitacao; }

    public EnumStatusSolicitacao getStatusAnterior() { return statusAnterior; }
    public void setStatusAnterior(EnumStatusSolicitacao statusAnterior) { this.statusAnterior = statusAnterior; }

    public EnumStatusSolicitacao getStatusNovo() { return statusNovo; }
    public void setStatusNovo(EnumStatusSolicitacao statusNovo) { this.statusNovo = statusNovo; }

    public LocalDateTime getDataAlteracao() { return dataAlteracao; }
    public void setDataAlteracao(LocalDateTime dataAlteracao) { this.dataAlteracao = dataAlteracao; }

}