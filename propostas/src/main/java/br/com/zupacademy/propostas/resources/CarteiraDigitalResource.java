package br.com.zupacademy.propostas.resources;

import br.com.zupacademy.propostas.models.CartaoModel;
import br.com.zupacademy.propostas.models.CarteiraDigitalModel;
import br.com.zupacademy.propostas.repositories.CartaoRepository;
import br.com.zupacademy.propostas.requests.both.CarteiraDigitalRequest;
import br.com.zupacademy.propostas.resources.externals.CartoesExternalResource;
import br.com.zupacademy.propostas.response.CarteiraDigitalResponse;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/carteiras")
public class CarteiraDigitalResource {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private CartoesExternalResource apiCartoes;

    @PersistenceContext
    private EntityManager manager;

    @PostMapping(value = "/{numeroCartao}/cartoes")
    @Transactional
    public ResponseEntity cadastrarEmissor(@PathVariable String numeroCartao,
                                           @RequestBody @Valid CarteiraDigitalRequest carteiraDigitalRequest,
                                           UriComponentsBuilder uriComponentsBuilder) {

        Optional<CartaoModel> cartao = cartaoRepository.findByNumeroCartao(numeroCartao);

        if (cartao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (cartao.get().getCarteiras().size() > 0) {
            return ResponseEntity.unprocessableEntity().body("O cartão já tem uma carteira associada.");
        }

        CarteiraDigitalModel carteiraDigitalModel = carteiraDigitalRequest.toModel(cartao.get());

        try {
            CarteiraDigitalResponse carteiraDigitalResponse =
                    apiCartoes.associarCartaoAEmissor(numeroCartao, carteiraDigitalRequest);

            if (carteiraDigitalResponse.getResultado().equalsIgnoreCase("ASSOCIADA")) {
                manager.persist(carteiraDigitalModel);
            }

            URI uri = uriComponentsBuilder.path("/api/v1/carteiras/{id}").
                    buildAndExpand(carteiraDigitalModel.getId()).toUri();

            return ResponseEntity.created(uri).build();

        } catch (FeignException exception) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Erro em API Cartões");
        }
    }
}