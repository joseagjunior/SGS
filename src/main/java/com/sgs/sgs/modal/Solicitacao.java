package com.sgs.sgs.modal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "solicitacao")
public class Solicitacao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "solicitante_id", nullable = false)
    private Solicitante solicitante;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    private String descricao;

    @NotNull
    private BigDecimal valor;

    @Column(name = "data_solicitacao")
    private LocalDate dataSolicitacao;

    @Enumerated(EnumType.STRING)
    private EnumStatusSolicitacao status;
}