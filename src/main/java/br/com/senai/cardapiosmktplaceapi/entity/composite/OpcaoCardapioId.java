package br.com.senai.cardapiosmktplaceapi.entity.composite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OpcaoCardapioId {
	
	@Column(name = "id_cardapio")
	private Integer idCardapio;
	
	@Column(name = "id_opcao")
	private Integer idOpcao;
	
}
