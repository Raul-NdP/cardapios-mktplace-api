package br.com.senai.cardapiosmktplaceapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import br.com.senai.cardapiosmktplaceapi.entity.Categoria;
import br.com.senai.cardapiosmktplaceapi.entity.enums.Status;
import br.com.senai.cardapiosmktplaceapi.entity.enums.TipoCategoria;
import br.com.senai.cardapiosmktplaceapi.repository.CategoriasRepository;
import br.com.senai.cardapiosmktplaceapi.repository.RestaurantesRepository;
import br.com.senai.cardapiosmktplaceapi.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService{
	
	@Autowired
	private CategoriasRepository categoriasRepository;
	
	@Autowired
	private RestaurantesRepository restaurantesRepository;
	
	@Override
	public Categoria salvar(Categoria categoria) {
		
		Categoria outracategoria = categoriasRepository.buscarPor(categoria.getNome(), categoria.getTipo());
		
		if (outracategoria != null) {
			if (categoria.isPersistido()) {
				Preconditions.checkArgument(outracategoria.equals(categoria), 
						"O nome da categoria já está em uso");
			}
		}
		Categoria categoriaSalva = categoriasRepository.save(categoria);
		
		return categoriaSalva;
		
		
	}

	@Override
	public void atualizarStatusPor(Integer id, Status status) {
		
		Categoria categoriaEncontrada = categoriasRepository.buscarPor(id);
		
		Preconditions.checkNotNull(categoriaEncontrada, 
				"Não existe categoria vinculada ao id informado");
		Preconditions.checkArgument(categoriaEncontrada.getStatus() != status, 
				"O status já está salvo para a categoria");
		
		this.categoriasRepository.atualizarPor(id, status);
		
	}

	@Override
	public Page<Categoria> listarPor(String nome, Status status, 
			TipoCategoria tipo, Pageable paginacao) {
		
		return categoriasRepository.listarPor(nome + "%", status, tipo, paginacao);
		
	}

	@Override
	public Categoria buscarPor(Integer id) {
		
		Categoria categoriaEncontrada = categoriasRepository.buscarPor(id);
		Preconditions.checkNotNull(categoriaEncontrada, "Não existe categoria vinculada ao id informado");
		Preconditions.checkArgument(categoriaEncontrada.isAtiva(), 
				"A categoria está inativa");
		
		return categoriaEncontrada;
		
	}

	@Override
	public Categoria excluirPor(Integer id) {
		
		Categoria categoriaEncontrado = buscarPor(id);
		Long qtdeRestaurantesVinculados = restaurantesRepository.contarPor(id);
		
		Preconditions.checkArgument(qtdeRestaurantesVinculados == 0, 
				"Não é possível remover pois existem restaurantes vinculados");
		
		this.categoriasRepository.deleteById(categoriaEncontrado.getId());
		
		return categoriaEncontrado;
		
	}

}
