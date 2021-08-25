package br.com.zupacademy.propostas.resources;

import br.com.zupacademy.propostas.customizations.binders.ValidarCPFOuCNPJ;
import br.com.zupacademy.propostas.resources.externals.SolicitacaoAnaliseExternalResource;
import br.com.zupacademy.propostas.models.PropostaModel;
import br.com.zupacademy.propostas.requests.SolicitacaoAnaliseRequest;
import br.com.zupacademy.propostas.requests.NovaPropostaRequest;
import br.com.zupacademy.propostas.response.ConsultaPropostaResponse;
import br.com.zupacademy.propostas.response.SolicitacaoAnaliseResponse;
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
public class PropostaResource {

    private final Logger logger = LoggerFactory.getLogger(PropostaResource.class);

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private SolicitacaoAnaliseExternalResource solicitacaoAnaliseExternalResource;

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
            SolicitacaoAnaliseResponse analiseDePropostaResponse =
                    getAnaliseDePropostaResponse(novaPropostaRequest, proposta.getId());

            proposta.alterarStatusDaAnaliseDeProposta(analiseDePropostaResponse);

            manager.merge(proposta);
            logger.info(proposta.toString());

        } catch (FeignException exception) {
            manager.getTransaction().rollback();
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Erro ao tentar comunicar com API externa");
        }

        URI uri = uriComponentsBuilder.path("/api/v1/propostas/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    private SolicitacaoAnaliseResponse getAnaliseDePropostaResponse(NovaPropostaRequest novaPropostaRequest,
                                                                    Long idProposta) {
        SolicitacaoAnaliseRequest propostaASerAnalisada =
                new SolicitacaoAnaliseRequest(idProposta,
                        novaPropostaRequest.getDocumento(), novaPropostaRequest.getNome());

        SolicitacaoAnaliseResponse analiseDePropostaResponse =
                solicitacaoAnaliseExternalResource.enviarParaAnalise(propostaASerAnalisada);

        return analiseDePropostaResponse;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity consultarProposta(@PathVariable Long id) {

        PropostaModel propostaModel = manager.find(PropostaModel.class, id);

        if (propostaModel == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A proposta não existe");
        }

        ConsultaPropostaResponse response = new ConsultaPropostaResponse(propostaModel);

        return ResponseEntity.ok(response);
    }
}