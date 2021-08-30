package br.com.zupacademy.propostas.resources;

import br.com.zupacademy.propostas.models.BloqueioModel;
import br.com.zupacademy.propostas.repositories.BloqueioRepository;
import br.com.zupacademy.propostas.repositories.CartaoRepository;
import br.com.zupacademy.propostas.requests.BloqueioCartaoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bloqueio-cartao")
public class BloqueioResource {

    @Autowired
    private BloqueioRepository bloqueioRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @PostMapping(value = "/{id}")
    public ResponseEntity solicitarBloqueioDeCartaoPorId(@PathVariable Long id,
                                                         @RequestHeader(value = "ip") String ip,
                                                         @RequestHeader(value = "User-Agent") String userAgent) {

        BloqueioCartaoRequest request = new BloqueioCartaoRequest(id, ip, userAgent);
        BloqueioModel bloqueioModel = request.toModel(bloqueioRepository, cartaoRepository);
        bloqueioRepository.save(bloqueioModel);

        return ResponseEntity.ok(bloqueioModel.toString());
    }
}