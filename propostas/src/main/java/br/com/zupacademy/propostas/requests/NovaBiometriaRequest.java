package br.com.zupacademy.propostas.requests;

import br.com.zupacademy.propostas.models.BiometriaModel;
import br.com.zupacademy.propostas.models.CartaoModel;
import br.com.zupacademy.propostas.repositories.CartaoRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

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

    public @NotNull BiometriaModel toModel(CartaoRepository cartaoRepository, String numeroCartao) {
        Optional<CartaoModel> cartao = cartaoRepository.findByNumeroCartao(numeroCartao);

        if (cartao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado");
        }

        return new BiometriaModel(biometria, cartao.get());
    }
}