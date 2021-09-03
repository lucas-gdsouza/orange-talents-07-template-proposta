package br.com.zupacademy.propostas.resources;

import br.com.zupacademy.propostas.customizations.binders.ValidarCPFOuCNPJ;
import br.com.zupacademy.propostas.repositories.PropostaRepository;
import br.com.zupacademy.propostas.requests.externals.SolicitacaoAnaliseExternalRequest;
import br.com.zupacademy.propostas.resources.externals.SolicitacaoAnaliseExternalResource;
import br.com.zupacademy.propostas.models.PropostaModel;
import br.com.zupacademy.propostas.requests.internals.SolicitacaoAnaliseInternalRequest;
import br.com.zupacademy.propostas.response.ConsultaPropostaResponse;
import br.com.zupacademy.propostas.response.SolicitacaoAnaliseResponse;

import feign.FeignException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/propostas")
public class PropostaResource {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private SolicitacaoAnaliseExternalResource solicitacaoAnaliseExternalResource;

    @Autowired
    private ValidarCPFOuCNPJ validarCPFOuCNPJ;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(validarCPFOuCNPJ);
    }

    @PostMapping
    public ResponseEntity solicitarProposta(@RequestBody @Valid SolicitacaoAnaliseInternalRequest solicitacaoAnaliseInternalRequest,
                                            UriComponentsBuilder uriComponentsBuilder) {

        PropostaModel proposta = solicitacaoAnaliseInternalRequest.toModel();
        propostaRepository.save(proposta);

        try {
            SolicitacaoAnaliseExternalRequest propostaASerAnalisada = new SolicitacaoAnaliseExternalRequest(proposta);

            SolicitacaoAnaliseResponse analiseDePropostaResponse =
                    solicitacaoAnaliseExternalResource.enviarParaAnalise(propostaASerAnalisada);

            proposta.alterarStatusDaAnaliseDeProposta(analiseDePropostaResponse);

            propostaRepository.save(proposta);

            URI uri = uriComponentsBuilder.path("/api/v1/propostas/{id}").buildAndExpand(proposta.getId()).toUri();
            return ResponseEntity.created(uri).build();

        } catch (FeignException exception) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Erro em API Propostas");
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity consultarProposta(@PathVariable("id") Long id) {

        Optional<PropostaModel> propostaModel = propostaRepository.findById(id);

        if (propostaModel.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ConsultaPropostaResponse response = new ConsultaPropostaResponse(propostaModel.get());

        return ResponseEntity.ok(response);
    }
}