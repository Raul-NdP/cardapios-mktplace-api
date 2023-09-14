package br.com.senai.cardapiosmktplaceapi.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import br.com.senai.cardapiosmktplaceapi.dto.CardapioSalvo;
import br.com.senai.cardapiosmktplaceapi.dto.NovaOpcaoCardapio;
import br.com.senai.cardapiosmktplaceapi.dto.NovoCardapio;
import br.com.senai.cardapiosmktplaceapi.entity.Cardapio;
import br.com.senai.cardapiosmktplaceapi.entity.Opcao;
import br.com.senai.cardapiosmktplaceapi.entity.OpcaoCardapio;
import br.com.senai.cardapiosmktplaceapi.entity.Restaurante;
import br.com.senai.cardapiosmktplaceapi.entity.Secao;
import br.com.senai.cardapiosmktplaceapi.entity.composite.OpcaoCardapioId;
import br.com.senai.cardapiosmktplaceapi.entity.enums.Status;
import br.com.senai.cardapiosmktplaceapi.repository.CardapiosRepository;
import br.com.senai.cardapiosmktplaceapi.repository.OpcoesRepository;
import br.com.senai.cardapiosmktplaceapi.repository.RestaurantesRepository;
import br.com.senai.cardapiosmktplaceapi.repository.SecoesRepository;
import br.com.senai.cardapiosmktplaceapi.service.CardapioService;

@Service
public class CardapioServiceImpl implements CardapioService{
	
	@Autowired
	private CardapiosRepository cardapiosRepository;
	
	@Autowired
	private OpcoesRepository opcoesRepository;
	
	@Autowired
	private SecoesRepository secoesRepository;
	
	@Autowired
	private RestaurantesRepository restaurantesRepository;
	
	private Restaurante getRestaurantePor(NovoCardapio novoCardapio) {
		
		Preconditions.checkNotNull(novoCardapio.getRestaurante(), 
				"O restaurante é obrigatório");
		
		Restaurante restaurante = restaurantesRepository.buscarPor(
				novoCardapio.getRestaurante().getId());
		
		Preconditions.checkNotNull(restaurante, "O restaurante '" 
				+ novoCardapio.getRestaurante().getId() + "' não foi salvo");
		Preconditions.checkArgument(restaurante.isAtivo(), 
				"O restaurante está inativo");
		
		return restaurante;
	}
	
	private Secao getSecaoPor(NovaOpcaoCardapio novaOpcaoCardapio) {
		
		Preconditions.checkNotNull(novaOpcaoCardapio.getSecao(), 
				"A seção da opção é obrigatória");
		
		Secao secao = secoesRepository.findById(novaOpcaoCardapio.getSecao().getId()).get();
		
		Preconditions.checkNotNull(secao, "A seção '" 
				+ novaOpcaoCardapio.getSecao().getId() + "' não foi salva");
		Preconditions.checkArgument(secao.isAtiva(), 
				"A seção está inativa");
		
		return secao;
	}
	
	private Opcao getOpcaoPor(Integer idDaOpcao, Restaurante restaurante) {
		
		Opcao opcao = opcoesRepository.buscarPor(idDaOpcao);
		
		Preconditions.checkNotNull(opcao, "A opção '" 
				+ idDaOpcao + "' não foi salva");
		Preconditions.checkArgument(opcao.isAtiva(), 
				"A opção está inativa");
		
		Preconditions.checkArgument(opcao.getRestaurante().equals(restaurante), 
				"A opção '" + idDaOpcao + "' não pertence ao restaurante do cardápio");
		
		return opcao;
	}
	
	private void validarDuplicidadeEm(List<NovaOpcaoCardapio> opcoesCardapio) {
		
		for (NovaOpcaoCardapio novaOpcao : opcoesCardapio) {
			int qtdOcorrencias = 0;
			for (NovaOpcaoCardapio outraOpcao : opcoesCardapio) {
				if (novaOpcao.getIdOpcao().equals(outraOpcao.getIdOpcao())) {
					qtdOcorrencias++;
				}
			}
			Preconditions.checkArgument(qtdOcorrencias == 1, 
					"A opção '" + novaOpcao.getIdOpcao() + "' está duplicada no cardápio");
		}
		
	}
	
	@Override
	public Cardapio inserir(NovoCardapio novoCardapio) {
		
		Restaurante restaurante = getRestaurantePor(novoCardapio);
		
		Cardapio cardapio = new Cardapio();
		cardapio.setNome(novoCardapio.getNome());
		cardapio.setDescricao(novoCardapio.getDescricao());
		cardapio.setRestaurante(restaurante);
		
		Cardapio cardapioSalvo = cardapiosRepository.save(cardapio);
		this.validarDuplicidadeEm(novoCardapio.getOpcoes());
		
		for (NovaOpcaoCardapio novaOpcao : novoCardapio.getOpcoes()) {
			Opcao opcao = getOpcaoPor(novaOpcao.getIdOpcao(), restaurante);
			Secao secao = getSecaoPor(novaOpcao);
			OpcaoCardapioId id = new OpcaoCardapioId(cardapioSalvo.getId(), opcao.getId());
			OpcaoCardapio opcaoCardapio = new OpcaoCardapio();
			opcaoCardapio.setId(id);
			opcaoCardapio.setCardapio(cardapioSalvo);
			opcaoCardapio.setOpcao(opcao);
			opcaoCardapio.setSecao(secao);
			opcaoCardapio.setPreco(novaOpcao.getPreco());
			opcaoCardapio.setRecomendado(novaOpcao.getRecomendado());
			cardapioSalvo.getOpcoes().add(opcaoCardapio);
			this.cardapiosRepository.saveAndFlush(cardapioSalvo);
		}
		
		return cardapiosRepository.buscarPor(cardapioSalvo.getId());
	}

	@Override
	public Cardapio alterar(CardapioSalvo cardapioSalvo) {
		
		Restaurante restaurante = restaurantesRepository.buscarPor(
				cardapioSalvo.getRestaurante().getId());
		
		Cardapio cardapio = cardapiosRepository.buscarPor(cardapioSalvo.getId());
		cardapio.setNome(cardapioSalvo.getNome());
		cardapio.setDescricao(cardapioSalvo.getDescricao());
		cardapio.setRestaurante(restaurante);
		cardapio.setStatus(cardapioSalvo.getStatus());
		
		Cardapio cardapioAtualizado = cardapiosRepository.saveAndFlush(cardapio);
		
		return buscarPor(cardapioAtualizado.getId());
	}
	
	private void atualizarPrecosDas(List<OpcaoCardapio> opcoesCardapio) {
		
		for (OpcaoCardapio opcaoCardapio : opcoesCardapio) {
			if (opcaoCardapio.getOpcao().isEmPromocao()) {
				BigDecimal divisor = new BigDecimal(100);
				BigDecimal percentualDesconto = opcaoCardapio.getOpcao()
						.getPercentualDesconto();
				BigDecimal valorDescontado = opcaoCardapio.getPreco()
						.multiply(percentualDesconto).divide(divisor);
				BigDecimal preco = opcaoCardapio.getPreco().subtract(valorDescontado).setScale(2, RoundingMode.CEILING);
				
				opcaoCardapio.setPreco(preco);
			}
		}
		
	}
	
	@Override
	public Page<Cardapio> listarPor(Restaurante restaurante, Pageable paginacao) {
		
		Page<Cardapio> cardapios = cardapiosRepository.listarPor(restaurante, paginacao);
		
		for (Cardapio cardapio : cardapios.getContent()) {
			this.atualizarPrecosDas(cardapio.getOpcoes());
		}
		
		return cardapios;
	}

	@Override
	public Cardapio buscarPor(Integer id) {
		
		Cardapio cardapioEncontrado = cardapiosRepository.buscarPor(id);
		
		Preconditions.checkNotNull(cardapioEncontrado, 
				"Não foi encontrado cardápio para o id informado");
		Preconditions.checkArgument(cardapioEncontrado.isAtiva(), 
				"O cardápio está inativo");
		
		this.atualizarPrecosDas(cardapioEncontrado.getOpcoes());
		
		return cardapioEncontrado;
	}

	@Override
	public void atualizarStatusPor(Integer id, Status status) {
		
		Cardapio cardapio = buscarPor(id);
		
		Preconditions.checkArgument(cardapio.getStatus() == status, 
				"O status informado já foi salvo anteriormente");
		
		this.cardapiosRepository.atualizarPor(id, status);
		
	}
	
}
