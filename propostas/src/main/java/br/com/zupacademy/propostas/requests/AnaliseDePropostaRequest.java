package br.com.zupacademy.propostas.requests;

import br.com.zupacademy.propostas.annotations.CPFOrCNPJ;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class AnaliseDePropostaRequest {

    @NotNull
    @Positive
    private Long idProposta;

    @CPFOrCNPJ
    @NotBlank
    private String documento;

    @NotBlank
    private String nome;

    public AnaliseDePropostaRequest(Long idProposta, String documento, String nome) {
        this.idProposta = idProposta;
        this.documento = documento;
        this.nome = nome;
    }

    /**
     * Os getters são necessários para o funcionamento do FeignClient.
     */

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public Long getIdProposta() {
        return idProposta;
    }
}