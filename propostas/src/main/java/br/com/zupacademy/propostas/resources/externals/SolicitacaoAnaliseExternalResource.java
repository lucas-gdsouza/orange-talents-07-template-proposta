package br.com.zupacademy.propostas.resources.externals;

import br.com.zupacademy.propostas.requests.SolicitacaoAnaliseRequest;
import br.com.zupacademy.propostas.response.SolicitacaoAnaliseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "solicitacao-analise-resource", url = "${analises.resource}")
public interface SolicitacaoAnaliseExternalResource {

    @PostMapping(value = "/")
    SolicitacaoAnaliseResponse enviarParaAnalise(@RequestBody @Valid SolicitacaoAnaliseRequest request);
}