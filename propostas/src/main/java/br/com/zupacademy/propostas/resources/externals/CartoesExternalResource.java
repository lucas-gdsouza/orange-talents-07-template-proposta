package br.com.zupacademy.propostas.resources.externals;

import br.com.zupacademy.propostas.requests.NovoCartaoRequest;
import br.com.zupacademy.propostas.requests.BloqueioCartaoExternalRequest;
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
            (@RequestBody @Valid NovoCartaoRequest request);

    /**
     *
     * @param id é o Número do Cartão
     * @return
     */
    @PostMapping(value = "/{id}/bloqueios")
    BloqueioCartaoResponse solicitarBloqueioDeCartao
            (@PathVariable String id, @RequestBody BloqueioCartaoExternalRequest bloqueioCartaoExternalRequest);
}