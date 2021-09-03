package br.com.zupacademy.propostas.customizations.binders;

import br.com.zupacademy.propostas.models.PropostaModel;
import br.com.zupacademy.propostas.repositories.PropostaRepository;
import br.com.zupacademy.propostas.requests.internals.SolicitacaoAnaliseInternalRequest;

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
        return SolicitacaoAnaliseInternalRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

       SolicitacaoAnaliseInternalRequest request = (SolicitacaoAnaliseInternalRequest) object;
       Optional<PropostaModel> possivelDocumento = propostaRepository.findByDocumentoUnico(request.getDocumento());

       if (possivelDocumento.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "O documento já está cadastrado.");
       }
    }
}