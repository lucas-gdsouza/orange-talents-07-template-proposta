package br.com.zupacademy.propostas.controllers;

import br.com.zupacademy.propostas.binders.ValidarCPFOuCNPJ;
import br.com.zupacademy.propostas.models.PropostaModel;
import br.com.zupacademy.propostas.requests.PropostaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/propostas")
public class PropostaController {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private ValidarCPFOuCNPJ validarCPFOuCNPJ;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(validarCPFOuCNPJ);
    }

    @PostMapping
    @Transactional
    public ResponseEntity inserir(@RequestBody @Valid PropostaRequest request,
                                  UriComponentsBuilder uriComponentsBuilder) {

        PropostaModel proposta = request.toModel();
        manager.persist(proposta);

        URI uri = uriComponentsBuilder.path("/api/v1/propostas/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}