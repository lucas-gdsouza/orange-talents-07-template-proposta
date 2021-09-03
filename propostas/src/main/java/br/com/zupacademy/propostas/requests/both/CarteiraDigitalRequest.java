package br.com.zupacademy.propostas.requests.both;

import br.com.zupacademy.propostas.models.CartaoModel;
import br.com.zupacademy.propostas.models.CarteiraDigitalModel;
import br.com.zupacademy.propostas.models.enums.Emissor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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

    public CarteiraDigitalModel toModel(CartaoModel cartao) {
        return new CarteiraDigitalModel(this.email, Emissor.valueOf(emissor), cartao);
    }
}