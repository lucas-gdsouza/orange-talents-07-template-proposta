package br.com.zupacademy.propostas.resources.externals;

import br.com.zupacademy.propostas.requests.both.AvisoViagemRequest;
import br.com.zupacademy.propostas.requests.externals.NovoCartaoExternalRequest;
import br.com.zupacademy.propostas.requests.externals.BloqueioCartaoExternalRequest;
import br.com.zupacademy.propostas.response.BloqueioCartaoResponse;
import br.com.zupacademy.propostas.response.NovoCartaoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "cartoes-resource", url = "${cartoes.resource}")
public interface CartoesExternalResource {

    @PostMapping(value = "/")
    NovoCartaoResponse realizarPedidoDeNovoCartao
            (@RequestBody @Valid NovoCartaoExternalRequest request);

    @PostMapping(value = "/{id}/bloqueios")
    BloqueioCartaoResponse solicitarBloqueioDeCartao
            (@PathVariable("id") String numeroCartao, @RequestBody BloqueioCartaoExternalRequest bloqueioCartaoExternalRequest);

    @PostMapping(value = "/{id}/avisos")
    void enviarAvisoDeViagem(@PathVariable("id") String numeroCartao,
                                       @RequestBody AvisoViagemRequest avisoViagemRequest);
}