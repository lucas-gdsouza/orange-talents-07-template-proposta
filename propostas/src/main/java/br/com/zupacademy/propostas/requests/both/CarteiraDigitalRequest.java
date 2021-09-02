package br.com.zupacademy.propostas.requests.both;

import br.com.zupacademy.propostas.models.CartaoModel;
import br.com.zupacademy.propostas.models.CarteiraDigitalModel;
import br.com.zupacademy.propostas.models.enums.Emissor;
import br.com.zupacademy.propostas.repositories.CartaoRepository;
import br.com.zupacademy.propostas.repositories.CarteiraDigitalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

public class CarteiraDigitalRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String emissor;

    public CarteiraDigitalRequest(@NotBlank String email, @NotBlank String emissor) {
        this.email = email;
        this.emissor = Emissor.valueOf(emissor).toString();
    }

    /**
     * Os getters são necessários para o funcionamento do FeignClient.
     */

    public String getEmail() {
        return email;
    }

    public String getEmissor() {
        return emissor;
    }

    public CarteiraDigitalModel toModel(CartaoRepository cartaoRepository,
                                        CarteiraDigitalRepository carteiraDigitalRepository, String numeroCartao) {

        Optional<CartaoModel> cartao = cartaoRepository.findByNumeroCartao(numeroCartao);

        if (cartao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado");
        }

        Optional<CarteiraDigitalModel> carteiraDigitalModel = carteiraDigitalRepository.findByCartao(cartao.get());

        if (carteiraDigitalModel.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "O cartão já está associado a uma carteira");
        }

        return new CarteiraDigitalModel(this.email, Emissor.valueOf(emissor), cartao.get());
    }
}