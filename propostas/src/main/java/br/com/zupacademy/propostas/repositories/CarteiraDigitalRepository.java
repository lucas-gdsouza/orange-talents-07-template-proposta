package br.com.zupacademy.propostas.repositories;

import br.com.zupacademy.propostas.models.CartaoModel;
import br.com.zupacademy.propostas.models.CarteiraDigitalModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarteiraDigitalRepository extends CrudRepository<CarteiraDigitalModel, Long> {
    Optional<CarteiraDigitalModel> findByCartao(CartaoModel Cartao);
}