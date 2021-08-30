package br.com.zupacademy.propostas.response.enums;

import br.com.zupacademy.propostas.models.enums.EstadoProposta;

public enum ResultadoAnalise {

    COM_RESTRICAO {
        @Override
        public EstadoProposta retornarEstado() {
            return EstadoProposta.NAO_ELEGIVEL;
        }
    },
    SEM_RESTRICAO {
        @Override
        public EstadoProposta retornarEstado() {
            return EstadoProposta.ELEGIVEL;
        }
    };

    public abstract EstadoProposta retornarEstado();
}