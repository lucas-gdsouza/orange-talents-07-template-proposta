package br.com.zupacademy.propostas.controllers;

import br.com.zupacademy.propostas.binders.ValidarCPFOuCNPJ;
import br.com.zupacademy.propostas.controllers.externals.EndpointValidadorDePropostas;
import br.com.zupacademy.propostas.models.PropostaModel;
import br.com.zupacademy.propostas.requests.ConsultaDadosDaPropostaRequest;
import br.com.zupacademy.propostas.requests.PropostaRequest;
import br.com.zupacademy.propostas.response.ConsultaDadosDaPropostaResponse;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/propostas")
public class NovaPropostaController {

    private final Logger logger = LoggerFactory.getLogger(NovaPropostaController.class);

    @Autowired
    private EndpointValidadorDePropostas endpointValidadorDePropostas;

    @Autowired
    private ValidarCPFOuCNPJ validarCPFOuCNPJ;

    @PersistenceContext
    private EntityManager manager;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(validarCPFOuCNPJ);
    }

    @PostMapping
    @Transactional
    public ResponseEntity inserir(@RequestBody @Valid PropostaRequest novaPropostaRequest,
                                  UriComponentsBuilder uriComponentsBuilder) {

        PropostaModel proposta = novaPropostaRequest.toModel();
        manager.persist(proposta);

        logger.info("Proposta Persistida: " + proposta);

        try {
            ConsultaDadosDaPropostaRequest novaRequestDaProposta =
                    new ConsultaDadosDaPropostaRequest(proposta.getId(), novaPropostaRequest);

            ConsultaDadosDaPropostaResponse analiseDePropostaResponse =
                    endpointValidadorDePropostas.enviarParaAnalise(novaRequestDaProposta);

            logger.info("Resultado Obtido", analiseDePropostaResponse.toString());

        } catch (FeignException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        URI uri = uriComponentsBuilder.path("/api/v1/propostas/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}