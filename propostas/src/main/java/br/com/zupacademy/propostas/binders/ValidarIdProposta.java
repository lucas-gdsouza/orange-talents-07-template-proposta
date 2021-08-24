package br.com.zupacademy.propostas.binders;

import br.com.zupacademy.propostas.models.PropostaModel;
import br.com.zupacademy.propostas.repositories.PropostaRepository;
import br.com.zupacademy.propostas.requests.ConsultaDadosDaPropostaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class ValidarIdProposta implements Validator {

    @Autowired
    private PropostaRepository propostaRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return ConsultaDadosDaPropostaRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        ConsultaDadosDaPropostaRequest request = (ConsultaDadosDaPropostaRequest) o;
        Optional<PropostaModel> possivelDocumento = propostaRepository.findById(request.getIdProposta());

        if (possivelDocumento.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A proposta não existe.");
        }
    }
}