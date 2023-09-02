package br.com.senai.cardapiosmktplaceapi.entity;

import java.math.BigDecimal;

import br.com.senai.cardapiosmktplaceapi.entity.composite.OpcaoCardapioId;
import br.com.senai.cardapiosmktplaceapi.entity.enums.Confirmacao;
import br.com.senai.cardapiosmktplaceapi.entity.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "opcoes_cardapios")
@Entity(name = "OpcaoCardapio")
public class OpcaoCardapio {
	
	@EqualsAndHashCode.Include
	@EmbeddedId
	@NotNull(message = "O id da opção do cardapio é obrigatório")
	private OpcaoCardapioId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idCardapio")
	@NotNull(message = "O cardapio é obrigatório")
	@JoinColumn(name = "id_cardapio")
	private Cardapio cardapio;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idOpcao")
	@NotNull(message = "A opção é obrigatória")
	@JoinColumn(name = "id_opcao")
	private Opcao opcao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull(message = "A seção é obrigatória")
	@JoinColumn(name = "id_secao")
	private Secao secao;
	
	@DecimalMin(value = "00.00", inclusive = false, 
			message = "O preço deve ser positivo")
	@Digits(integer = 9, fraction = 2, message = "O preço deve possuir o formato 'NNNNNNNNN.NN'")
	@Column(name = "preco")
	private BigDecimal preco;
	
	@Enumerated(value = EnumType.STRING)
	@NotNull(message = "O status é obrigatório")
	@Column(name = "status")
	private Status status;
	
	@Enumerated(value = EnumType.STRING)
	@NotNull(message = "A recomendação é obrigatória")
	@Column(name = "recomendado")
	private Confirmacao recomendado;
	
	public OpcaoCardapio() {
		this.status = Status.A;
	}
	
	public boolean isPersistido() {
		return getId() != null && getId().getIdCardapio() > 0 && getId().getIdOpcao() > 0;
	}
	
	public boolean isAtivo() {
		return getStatus() == Status.A;
	}
	
}
