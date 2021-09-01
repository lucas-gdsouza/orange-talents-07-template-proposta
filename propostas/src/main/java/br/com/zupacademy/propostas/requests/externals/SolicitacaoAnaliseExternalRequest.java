package br.com.zupacademy.propostas.requests.externals;

import br.com.zupacademy.propostas.customizations.annotations.CPFOrCNPJ;
import br.com.zupacademy.propostas.models.PropostaModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SolicitacaoAnaliseExternalRequest {

    @NotNull
    @Positive
    private Long idProposta;

    @CPFOrCNPJ
    @NotBlank
    private String documento;

    @NotBlank
    private String nome;

    public SolicitacaoAnaliseExternalRequest(@NotNull PropostaModel propostaModel) {
        this.idProposta = propostaModel.getId();
        this.documento = propostaModel.getDocumento();
        this.nome = propostaModel.getNome();
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