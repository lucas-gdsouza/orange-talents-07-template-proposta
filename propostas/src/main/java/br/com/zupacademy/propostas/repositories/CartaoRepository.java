package br.com.zupacademy.propostas.repositories;

import br.com.zupacademy.propostas.models.CartaoModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaoRepository extends CrudRepository<CartaoModel, Long> {
    Optional<CartaoModel> findByNumeroCartao(String numeroCartao);
}