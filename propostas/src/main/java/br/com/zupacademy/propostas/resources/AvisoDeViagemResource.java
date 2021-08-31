package br.com.zupacademy.propostas.resources;

import br.com.zupacademy.propostas.models.AvisoDeViagemModel;
import br.com.zupacademy.propostas.models.CartaoModel;
import br.com.zupacademy.propostas.repositories.AvisoDeViagemRepository;
import br.com.zupacademy.propostas.repositories.CartaoRepository;
import br.com.zupacademy.propostas.requests.AvisoDeViagemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/avisos")
public class AvisoDeViagemResource {

    @Autowired
    private AvisoDeViagemRepository avisoDeViagemRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @PostMapping(value = "/{numeroCartao}/cartoes")
    public ResponseEntity emitirAvisoViagem(@PathVariable String numeroCartao,
                                            @RequestHeader(value = "ip") String ip,
                                            @RequestHeader(value = "User-Agent") String userAgent,
                                            @RequestBody @Valid AvisoDeViagemRequest avisoDeViagemRequest) {

        AvisoDeViagemModel avisoDeViagemModel =
                avisoDeViagemRequest.toModel(cartaoRepository, numeroCartao, ip, userAgent);

        avisoDeViagemRepository.save(avisoDeViagemModel);

        return ResponseEntity.ok().build();
    }
}
