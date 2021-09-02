package br.com.zupacademy.propostas.resources;

import br.com.zupacademy.propostas.models.CarteiraDigitalModel;
import br.com.zupacademy.propostas.repositories.CartaoRepository;
import br.com.zupacademy.propostas.repositories.CarteiraDigitalRepository;
import br.com.zupacademy.propostas.requests.both.CarteiraDigitalRequest;
import br.com.zupacademy.propostas.resources.externals.CartoesExternalResource;
import br.com.zupacademy.propostas.response.CarteiraDigitalResponse;
import feign.FeignException;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/carteiras")
public class CarteiraDigitalResource {

    @Autowired
    private CarteiraDigitalRepository carteiraDigitalRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private CartoesExternalResource cartoesExternalResource;

    @PostMapping(value = "/{numeroCartao}/cartoes")
    public ResponseEntity cadastrarEmissor(@PathVariable String numeroCartao,
                                           @RequestBody @Valid CarteiraDigitalRequest carteiraDigitalRequest,
                                           UriComponentsBuilder uriComponentsBuilder) {

        CarteiraDigitalModel carteiraDigitalModel =
                carteiraDigitalRequest.toModel(cartaoRepository, carteiraDigitalRepository, numeroCartao);

        try {
            CarteiraDigitalResponse carteiraDigitalResponse =
                    cartoesExternalResource.associarCartaoAEmissor(numeroCartao, carteiraDigitalRequest);

            if (carteiraDigitalResponse.getResultado().equalsIgnoreCase("ASSOCIADA")) {
                carteiraDigitalRepository.save(carteiraDigitalModel);
            }
        } catch (FeignException exception) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Erro ao tentar comunicar com API externa");
        }

        URI uri = uriComponentsBuilder.path("/api/v1/propostas/{id}").
                buildAndExpand(carteiraDigitalModel.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }
}