package br.com.senai.cardapiosmktplaceapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import br.com.senai.cardapiosmktplaceapi.entity.Categoria;
import br.com.senai.cardapiosmktplaceapi.repository.CategoriasRepository;

@SpringBootApplication
public class InitApp {
	
	@Autowired
	private CategoriasRepository repository;
	
	public static void main(String[] args) {
		SpringApplication.run(InitApp.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			
			/*Categoria novaCategoria = new Categoria();
			novaCategoria.setNome("Sushizeria");
			novaCategoria.setTipo(TipoCategoria.RESTAURANTE);
			this.repository.save(novaCategoria);*/
			
			Categoria categoria = repository.buscarPor(35);
			
			System.out.println(categoria.getNome());
			categoria.setNome("Novo nome da categoria");
			this.repository.save(categoria);
			System.out.println("Zé Fini");
			
		};
	}

}
