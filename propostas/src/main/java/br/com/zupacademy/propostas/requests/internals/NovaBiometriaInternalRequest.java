package br.com.zupacademy.propostas.requests.internals;

import br.com.zupacademy.propostas.models.BiometriaModel;
import br.com.zupacademy.propostas.models.CartaoModel;
import com.fasterxml.jackson.annotation.JsonProperty;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class NovaBiometriaInternalRequest {

    @NotBlank
    private String biometria;

    /**
     * @param biometria - Necessário utilizar @JsonProperty quando há somente um parâmetro no construtor
     */
    public NovaBiometriaInternalRequest(@NotBlank @JsonProperty(value = "biometria") String biometria) {
        this.biometria = biometria;
    }

    public String getBiometria() {
        return biometria;
    }

    public @NotNull BiometriaModel toModel(CartaoModel cartao) {
        return new BiometriaModel(biometria, cartao);
    }
}