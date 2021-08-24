package br.com.zupacademy.propostas.binders;

import br.com.zupacademy.propostas.models.PropostaModel;
import br.com.zupacademy.propostas.repositories.PropostaRepository;
import br.com.zupacademy.propostas.requests.NovaPropostaRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class ValidarCPFOuCNPJ implements Validator {

    @Autowired
    private PropostaRepository propostaRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return NovaPropostaRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        NovaPropostaRequest request = (NovaPropostaRequest) o;
        Optional<PropostaModel> possivelDocumento = propostaRepository.findByDocumento(request.getDocumento());

        if (possivelDocumento.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "O documento já está cadastrado.");
        }
    }
}