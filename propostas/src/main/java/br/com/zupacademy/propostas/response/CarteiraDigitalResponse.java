package br.com.zupacademy.propostas.response;

public class CarteiraDigitalResponse {

    private String resultado;
    private String id;

    public CarteiraDigitalResponse(String resultado, String id) {
        this.resultado = resultado;
        this.id = id;
    }

    public String getResultado() {
        return resultado;
    }

    public String getId() {
        return id;
    }
}