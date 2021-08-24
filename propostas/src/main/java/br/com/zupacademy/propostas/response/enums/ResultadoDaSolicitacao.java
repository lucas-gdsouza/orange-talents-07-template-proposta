package br.com.zupacademy.propostas.response.enums;

import br.com.zupacademy.propostas.models.enums.EstadoDaProposta;

public enum ResultadoDaSolicitacao {

    COM_RESTRICAO {
        @Override
        public EstadoDaProposta resultadoFinal() {
            return EstadoDaProposta.NAO_ELEGIVEL;
        }
    },
    SEM_RESTRICAO {
        @Override
        public EstadoDaProposta resultadoFinal() {
            return EstadoDaProposta.ELEGIVEL;
        }
    };

    public abstract EstadoDaProposta resultadoFinal();
}
