package br.com.zupacademy.propostas.controllers;

import br.com.zupacademy.propostas.customizations.binders.ValidarCPFOuCNPJ;
import br.com.zupacademy.propostas.controllers.externals.EndpointValidadorDePropostas;
import br.com.zupacademy.propostas.models.PropostaModel;
import br.com.zupacademy.propostas.requests.AnaliseDePropostaRequest;
import br.com.zupacademy.propostas.requests.NovaPropostaRequest;
import br.com.zupacademy.propostas.response.AnaliseDePropostaResponse;
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

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private EndpointValidadorDePropostas endpointValidadorDePropostas;

    @Autowired
    private ValidarCPFOuCNPJ validarCPFOuCNPJ;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(validarCPFOuCNPJ);
    }

    @PostMapping
    @Transactional
    public ResponseEntity solicitarProposta(@RequestBody @Valid NovaPropostaRequest novaPropostaRequest,
                                  UriComponentsBuilder uriComponentsBuilder) {

        PropostaModel proposta = novaPropostaRequest.toModel();
        manager.persist(proposta);

        try {
            AnaliseDePropostaResponse analiseDePropostaResponse =
                    getAnaliseDePropostaResponse(novaPropostaRequest, proposta.getId());

            proposta.alterarStatusDaAnaliseDeProposta(analiseDePropostaResponse);

            manager.merge(proposta);
            logger.info(proposta.toString());

        } catch (FeignException exception) {
            manager.getTransaction().rollback();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao tentar comunicar com API externa");
        }

        URI uri = uriComponentsBuilder.path("/api/v1/propostas/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    private AnaliseDePropostaResponse getAnaliseDePropostaResponse(NovaPropostaRequest novaPropostaRequest,
                                                                   Long idProposta) {
        AnaliseDePropostaRequest requestDePropostaSemStatus =
                new AnaliseDePropostaRequest(idProposta,
                        novaPropostaRequest.getDocumento(), novaPropostaRequest.getNome());

        AnaliseDePropostaResponse analiseDePropostaResponse =
                endpointValidadorDePropostas.enviarParaAnalise(requestDePropostaSemStatus);

        return analiseDePropostaResponse;
    }
}