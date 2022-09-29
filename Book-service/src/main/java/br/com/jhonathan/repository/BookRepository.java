package br.com.jhonathan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jhonathan.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

}
