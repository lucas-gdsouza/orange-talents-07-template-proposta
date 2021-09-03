package br.com.zupacademy.propostas.resources;

import br.com.zupacademy.propostas.models.AvisoViagemModel;
import br.com.zupacademy.propostas.models.CartaoModel;
import br.com.zupacademy.propostas.repositories.CartaoRepository;
import br.com.zupacademy.propostas.requests.both.AvisoViagemRequest;
import br.com.zupacademy.propostas.resources.externals.CartoesExternalResource;
import br.com.zupacademy.propostas.response.AvisoViagemResponse;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/avisos")
public class AvisoViagemResource {

    @Autowired
    private CartoesExternalResource apiCartoes;

    @Autowired
    private CartaoRepository cartaoRepository;

    @PersistenceContext
    private EntityManager manager;

    @PostMapping(value = "/{numeroCartao}/cartoes")
    @Transactional
    public ResponseEntity emitirAvisoViagem(@PathVariable String numeroCartao,
                                            @RequestHeader(value = "ip") String ip,
                                            @RequestHeader(value = "User-Agent") String userAgent,
                                            @RequestBody @Valid AvisoViagemRequest request) {

        Optional<CartaoModel> cartao = cartaoRepository.findByNumeroCartao(numeroCartao);

        if (cartao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AvisoViagemModel avisoDeViagemModel = request.toModel(cartao.get(), numeroCartao, ip, userAgent);

        try {
            AvisoViagemResponse response = apiCartoes.enviarAvisoDeViagem(numeroCartao, request);

            if (response.getResultado().equalsIgnoreCase("CRIADO")) {
                manager.persist(avisoDeViagemModel);
            }

            return ResponseEntity.ok(response.getResultado());

        } catch (FeignException exception) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Erro em API Cartões");
        }
    }
}