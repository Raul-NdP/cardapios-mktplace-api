package br.com.senai.cardapiosmktplaceapi.entity;

import br.com.senai.cardapiosmktplaceapi.entity.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "cardapios")
@Entity(name = "Cardapio")
public class Cardapio {
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Size(max = 100, message = "O nome não deve conter mais de 100 caracteres")
	@NotBlank(message = "O nome é obrigatório")
	@Column(name = "nome")
	private String nome;
	
	@NotBlank(message = "A descriçãp é obrigatória")
	@Column(name = "descricao")
	private String descricao;
	
	@Enumerated(value = EnumType.STRING)
	@NotNull(message = "O status é obrigatório")
	@Column(name = "status")
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull(message = "O restaurante é obrigatório")
	@JoinColumn(name = "id_restaurante")
	private Restaurante restaurante;
	
	public Cardapio() {
		this.status = Status.A;
	}
	
	@Transient
	public boolean isPersistido() {
		return getId() != null && getId() > 0;
	}
	
	@Transient
	public boolean isAtiva() {
		return getStatus() == Status.A;
	}
	
}
