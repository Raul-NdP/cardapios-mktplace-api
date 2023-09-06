package br.com.senai.cardapiosmktplaceapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import br.com.senai.cardapiosmktplaceapi.entity.Categoria;
import br.com.senai.cardapiosmktplaceapi.entity.Restaurante;
import br.com.senai.cardapiosmktplaceapi.entity.enums.Status;
import br.com.senai.cardapiosmktplaceapi.repository.CardapiosRepository;
import br.com.senai.cardapiosmktplaceapi.repository.RestaurantesRepository;
import br.com.senai.cardapiosmktplaceapi.service.CategoriaService;
import br.com.senai.cardapiosmktplaceapi.service.RestauranteService;

@Service
public class RestauranteServiceImpl implements RestauranteService{
	
	@Autowired
	private RestaurantesRepository restaurantesRepository;
	
	@Autowired
	private CardapiosRepository cardapiosRepository;
	
	@Autowired
	@Qualifier("categoriaServiceImpl")
	private CategoriaService categoriaService;
	
	@Override
	public Restaurante salvar(Restaurante restaurante) {
		
		Restaurante outroRestaurante = restaurantesRepository.buscarPor(restaurante.getNome());
		
		if (outroRestaurante != null) {
			if (outroRestaurante.isPersistido()) {
				Preconditions.checkArgument(outroRestaurante.equals(restaurante), 
						"O nome do restaurante ja está em uso");
			}
		}
		
		categoriaService.buscarPor(restaurante.getCategoria().getId());
		
		Restaurante restauranteSalvo = restaurantesRepository.save(restaurante);
		
		return restauranteSalvo;
		
	}

	@Override
	public void atualizarStatusPor(Integer id, Status status) {
		
		Restaurante restauranteEncontrado = restaurantesRepository.buscarPor(id);
		
		Preconditions.checkNotNull(restauranteEncontrado, 
				"Não existe restaurante vinculado ao id informado");
		Preconditions.checkArgument(restauranteEncontrado.getStatus() != status, 
				"O status já está salvo para o restaurante");
		
		this.restaurantesRepository.atualizarPor(id, status);
		
	}

	@Override
	public Page<Restaurante> listarPor(String nome, Categoria categoria, Pageable paginacao) {
		
		return restaurantesRepository.listarPor("%" + nome + "%", categoria, paginacao);
	
	}

	@Override
	public Restaurante buscarPor(Integer id) {
		
		Restaurante restauranteEncontrado = restaurantesRepository.buscarPor(id);
		
		Preconditions.checkNotNull(restauranteEncontrado, 
				"Não existe restaurante vinculado ao id informado");
		Preconditions.checkArgument(restauranteEncontrado.isAtivo(), 
				"O restaurante está inativo");
		
		return restauranteEncontrado;
		
	}

	@Override
	public Restaurante excluirPor(Integer id) {
		
		Restaurante restauranteEncontrado = buscarPor(id);
		Long qtdeCardapiosVinculados = cardapiosRepository.contarPor(id);
		
		Preconditions.checkArgument(qtdeCardapiosVinculados == 0, 
				"Não é possível remover pois existem cardapios vinculados");
		
		this.restaurantesRepository.deleteById(restauranteEncontrado.getId());
		
		return restauranteEncontrado;
		
	}
	
	
	
}
