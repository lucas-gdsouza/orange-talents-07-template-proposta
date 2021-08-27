package br.com.zupacademy.propostas.customizations.binders;

import br.com.zupacademy.propostas.requests.NovaBiometriaRequest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ValidarBiometria implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return NovaBiometriaRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        NovaBiometriaRequest request = (NovaBiometriaRequest) object;
        String biometriaRecebida = request.getBiometria();

        if (!Base64.isBase64(biometriaRecebida)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Biometria Inválida");
        }
    }
}