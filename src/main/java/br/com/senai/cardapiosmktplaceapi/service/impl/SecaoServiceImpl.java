package br.com.senai.cardapiosmktplaceapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import br.com.senai.cardapiosmktplaceapi.entity.Secao;
import br.com.senai.cardapiosmktplaceapi.entity.enums.Status;
import br.com.senai.cardapiosmktplaceapi.repository.SecoesRepository;
import br.com.senai.cardapiosmktplaceapi.service.SecaoService;

@Service
public class SecaoServiceImpl implements SecaoService {
	
	@Autowired
	private SecoesRepository repository;
	
	@Override
	public Secao salvar(Secao secao) {
		
		Secao outraSecao = repository.buscarPor(secao.getNome());
		
		if (outraSecao != null) {
			if (secao.isPersistido()) {
				Preconditions.checkArgument(outraSecao.equals(secao), 
						"A nome da seção ja está em uso");
			}
		}
		Secao secaoSalva = repository.save(secao);
		
		return secaoSalva;
	}

	@Override
	public void atualizarStatusPor(Integer id, Status status) {
		
		Secao secaoEncontrada = repository.buscarPor(id);
		
		Preconditions.checkNotNull(secaoEncontrada, 
				"Não existe seção vinculada ao id informado");
		Preconditions.checkArgument(secaoEncontrada.getStatus() != status, 
				"O status já está salvo para a seção");
		
		this.repository.atualizarPor(id, status);
		
	}

	@Override
	public Page<Secao> listarPor(String nome, Pageable paginacao) {
		return repository.listarPor(nome + "%", paginacao);
	}

	@Override
	public Secao buscarPor(Integer id) {
		
		Secao secaoEncontrada = repository.buscarPor(id);
		Preconditions.checkNotNull(secaoEncontrada, "Não existe seção vinculada ao id informado");
		Preconditions.checkArgument(secaoEncontrada.isAtiva(), 
				"A seção está inativa");
		
		return secaoEncontrada;
		
	}

	@Override
	public Secao excluirPor(Integer id) {
		
		Secao secaoEncontrado = buscarPor(id);
		
		this.repository.deleteById(secaoEncontrado.getId());
		
		return secaoEncontrado;
		
	}

}
