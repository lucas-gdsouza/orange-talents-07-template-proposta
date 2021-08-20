package br.com.zupacademy.propostas.requests;

import br.com.zupacademy.propostas.annotations.CPFOrCNPJ;
import br.com.zupacademy.propostas.annotations.UniqueElement;
import br.com.zupacademy.propostas.models.PropostaModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class PropostaRequest {

    @CPFOrCNPJ
    @NotBlank
    private String documento;

    @NotBlank
    @UniqueElement(domainClass = PropostaModel.class, fieldName = "email", message = "O e-mail está em uso.")
    private String email;

    @NotBlank
    private String nome;

    @NotBlank
    private String endereco;

    @NotNull
    @PositiveOrZero
    private BigDecimal salarioBruto;

    public PropostaRequest(String documento, String email, String nome, String endereco, BigDecimal salarioBruto) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salarioBruto = salarioBruto;
    }

    public String getDocumento() {
        return documento;
    }

    public PropostaModel toModel() {
        return new PropostaModel(this.documento, this.email, this.nome, this.endereco, this.salarioBruto);
    }
}