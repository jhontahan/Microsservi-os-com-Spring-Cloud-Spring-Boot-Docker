package br.com.jhonathan.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jhonathan.model.Book;
import br.com.jhonathan.proxy.CambioProxy;
import br.com.jhonathan.repository.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Book endpoint")
@RestController
@RequestMapping("book-service")
public class BookController {
	
	@Autowired
	private Environment environment;

	@Autowired
	private BookRepository repository;
	
	@Autowired
	private CambioProxy cambioProxy;
	
	/**@GetMapping("/{id}/{currency}")
	public ResponseEntity<?> findBook(@PathVariable("id") Long id,
						 @PathVariable("currency") String currency) {
		
		Optional<Book> book = repository.findById(id);
		
		if (!book.isPresent()) {
//			throw new RuntimeException("Book not found.");
			return ResponseEntity.badRequest().body("Book not found.");
		}
		
		Book livro = book.get();
		
		HashMap<String, String> params = new HashMap<>();
		
		params.put("amount", livro.getPrice().toString());
		params.put("from", "USD");
		params.put("to", currency);
		
		var response = new RestTemplate().getForEntity("http://localhost:8000/cambio-service/{amount}/{from}/{to}", 
										Cambio.class, 
										params);
		var cambio = response.getBody();
		
		var port = environment.getProperty("local.server.port");
		
		livro.setEnvironment(port);
		livro.setCurrency(currency);
		
		livro.setPrice(cambio.getConvertedValue());
		
		return ResponseEntity.ok(livro);
	}**/
	
	@Operation(summary = "Find a specific book by your ID")
	@GetMapping("/{id}/{currency}")
	public ResponseEntity<?> findBook(@PathVariable("id") Long id,
						 @PathVariable("currency") String currency) {
		
		Optional<Book> book = repository.findById(id);
		
		if (!book.isPresent()) {
//			throw new RuntimeException("Book not found.");
			return ResponseEntity.badRequest().body("Book not found.");
		}
		
		Book livro = book.get();
		
		
		var cambio = cambioProxy.getCambio(livro.getPrice(), "USD", currency);
		
		var port = environment.getProperty("local.server.port");
		
		livro.setEnvironment("Booke port: " + port + " Cambio port: " + cambio.getEnvironment());
		livro.setCurrency(currency);
		
		livro.setPrice(cambio.getConvertedValue());
		
		return ResponseEntity.ok(livro);
	}
	

}
