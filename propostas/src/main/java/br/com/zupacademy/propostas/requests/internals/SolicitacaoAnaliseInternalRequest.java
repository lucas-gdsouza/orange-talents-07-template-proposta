package br.com.zupacademy.propostas.requests.internals;

import br.com.zupacademy.propostas.customizations.annotations.CPFOrCNPJ;
import br.com.zupacademy.propostas.customizations.annotations.UniqueElement;
import br.com.zupacademy.propostas.models.PropostaModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class SolicitacaoAnaliseInternalRequest {

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

    public SolicitacaoAnaliseInternalRequest(@NotBlank String documento, @NotBlank String email,
                                             @NotBlank String nome, @NotBlank String endereco,
                                             @NotNull @PositiveOrZero BigDecimal salarioBruto) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salarioBruto = salarioBruto;
    }

    public String getDocumento() {
        return documento;
    }

    public @NotNull PropostaModel toModel() {
        return new PropostaModel(this.documento, this.email, this.nome, this.endereco, this.salarioBruto);
    }
}