package br.com.zupacademy.propostas.requests;

import br.com.zupacademy.propostas.models.BiometriaModel;
import br.com.zupacademy.propostas.models.CartaoModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class NovaBiometriaRequest {

    @NotBlank
    private String biometria;

    /**
     * @param biometria - Necessário utilizar @JsonProperty quando há somente um parâmetro no construtor
     */
    public NovaBiometriaRequest(@NotBlank @JsonProperty(value = "biometria") String biometria) {
        this.biometria = biometria;
    }

    public String getBiometria() {
        return biometria;
    }

    public @NotNull BiometriaModel toModel(CartaoModel cartaoModel) {
        return new BiometriaModel(biometria, cartaoModel);
    }
}