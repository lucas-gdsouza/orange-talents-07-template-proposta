package br.com.zupacademy.propostas.requests;

import br.com.zupacademy.propostas.annotations.CPFOrCNPJ;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ConsultaDadosDaPropostaRequest {

    @NotNull
    @Positive
    private Long idProposta;

    @CPFOrCNPJ
    @NotBlank
    private String documento;

    @NotBlank
    private String nome;

    public ConsultaDadosDaPropostaRequest(Long idProposta, PropostaRequest novaPropostaRequest) {
        this.idProposta = idProposta;
        this.documento = novaPropostaRequest.getDocumento();
        this.nome = novaPropostaRequest.getNome();
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