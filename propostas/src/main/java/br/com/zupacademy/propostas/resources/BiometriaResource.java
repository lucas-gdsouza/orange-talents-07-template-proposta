package br.com.zupacademy.propostas.resources;

import br.com.zupacademy.propostas.customizations.binders.ValidarBiometria;
import br.com.zupacademy.propostas.models.BiometriaModel;
import br.com.zupacademy.propostas.models.CartaoModel;
import br.com.zupacademy.propostas.repositories.CartaoRepository;
import br.com.zupacademy.propostas.requests.internals.NovaBiometriaInternalRequest;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/biometrias")
public class BiometriaResource {

    @Autowired
    private CartaoRepository cartaoRepository;

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private ValidarBiometria validarBiometria;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(validarBiometria);
    }

    @PostMapping(value = "/{numeroCartao}")
    @Transactional
    public ResponseEntity cadastrarBiometria(@PathVariable("numeroCartao") String numeroCartao,
                                             @RequestBody @Valid NovaBiometriaInternalRequest request,
                                             UriComponentsBuilder uriComponentsBuilder) {

        Optional<CartaoModel> cartao = cartaoRepository.findByNumeroCartao(numeroCartao);

        if (cartao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        BiometriaModel biometriaModel = request.toModel(cartao.get());
        manager.persist(biometriaModel);

        URI uri = uriComponentsBuilder.path("/api/v1/biometrias/{id}").buildAndExpand(biometriaModel.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }
}