package com.sgs.sgs.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

public class SolicitacaoListagemDTO {
    private Long id;
    private String solicitanteNome;
    private String cpfCnpj;
    private String categoriaNome;
    private String status;
    private BigDecimal valor;
    private LocalDate dataSolicitacao;

    public SolicitacaoListagemDTO(Object[] row) {
        this.id = ((Number) row[0]).longValue();
        this.solicitanteNome = (String) row[1];
        this.cpfCnpj = (String) row[2];
        this.categoriaNome = (String) row[3];
        this.status = (String) row[4];
        this.valor = (BigDecimal) row[5];
        this.dataSolicitacao = row[6] != null ? ((Date) row[6]).toLocalDate() : null;
    }

    public Long getId() { return id; }
    public String getSolicitanteNome() { return solicitanteNome; }
    public String getCpfCnpj() { return cpfCnpj; }
    public String getCategoriaNome() { return categoriaNome; }
    public String getStatus() { return status; }
    public BigDecimal getValor() { return valor; }
    public LocalDate getDataSolicitacao() { return dataSolicitacao; }
}