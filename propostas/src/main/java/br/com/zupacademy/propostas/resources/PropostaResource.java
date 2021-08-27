package br.com.zupacademy.propostas.resources;

import br.com.zupacademy.propostas.customizations.binders.ValidarCPFOuCNPJ;
import br.com.zupacademy.propostas.repositories.PropostaRepository;
import br.com.zupacademy.propostas.resources.externals.SolicitacaoAnaliseExternalResource;
import br.com.zupacademy.propostas.models.PropostaModel;
import br.com.zupacademy.propostas.requests.SolicitacaoAnaliseRequest;
import br.com.zupacademy.propostas.requests.NovaPropostaRequest;
import br.com.zupacademy.propostas.response.ConsultaPropostaResponse;
import br.com.zupacademy.propostas.response.SolicitacaoAnaliseResponse;

import feign.FeignException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
    public ResponseEntity solicitarProposta(@RequestBody @Valid NovaPropostaRequest novaPropostaRequest,
                                            UriComponentsBuilder uriComponentsBuilder) {

        PropostaModel proposta = cadastrarProposta(novaPropostaRequest);
        solicitarAnalise(novaPropostaRequest, proposta);

        URI uri = uriComponentsBuilder.path("/api/v1/propostas/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    private PropostaModel cadastrarProposta(NovaPropostaRequest novaPropostaRequest) {
        PropostaModel proposta = novaPropostaRequest.toModel();
        propostaRepository.save(proposta);
        return proposta;
    }

    private void solicitarAnalise(NovaPropostaRequest novaPropostaRequest, PropostaModel proposta) {
        try {
            SolicitacaoAnaliseRequest propostaASerAnalisada = new SolicitacaoAnaliseRequest(proposta);

            SolicitacaoAnaliseResponse analiseDePropostaResponse =
                    solicitacaoAnaliseExternalResource.enviarParaAnalise(propostaASerAnalisada);

            proposta.alterarStatusDaAnaliseDeProposta(analiseDePropostaResponse);

            propostaRepository.save(proposta);

        } catch (FeignException exception) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Erro ao tentar comunicar com API externa");
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