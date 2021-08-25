package br.com.zupacademy.propostas.response;

import br.com.zupacademy.propostas.models.CartaoModel;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.StringJoiner;

public class NovoCartaoResponse {
    public String id;
    public LocalDateTime emitidoEm;
    public String titular;

    public NovoCartaoResponse(String id, LocalDateTime emitidoEm, String titular) {
        this.id = id;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
    }

    public @NotNull CartaoModel toModel() {
        return new CartaoModel(this.id, this.emitidoEm, this.titular);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NovoCartaoResponse.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("emitidoEm=" + emitidoEm)
                .add("titular='" + titular + "'")
                .toString();
    }
}