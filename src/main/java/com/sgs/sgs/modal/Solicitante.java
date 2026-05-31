package com.sgs.sgs.modal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "solicitante")
public class Solicitante {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    @Column(name = "cpf_cnpj", unique = true)
    private String cpfCnpj;
}
