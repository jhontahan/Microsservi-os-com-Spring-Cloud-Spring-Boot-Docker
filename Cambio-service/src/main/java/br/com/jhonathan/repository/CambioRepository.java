package br.com.jhonathan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jhonathan.model.Cambio;

public interface CambioRepository extends JpaRepository<Cambio, Long>{
	
	Cambio findByFromAndTo(String from, String to);

}
