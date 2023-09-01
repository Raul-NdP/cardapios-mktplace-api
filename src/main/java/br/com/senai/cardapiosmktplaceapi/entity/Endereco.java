package br.com.senai.cardapiosmktplaceapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Embeddable
public class Endereco {
	
	@Size(max = 80, message = "O nome da cidade não deve conter mais de 80 caracteres")
	@NotBlank(message = "A cidade é obrigatória")
	@Column(name = "cidade")
	private String cidade;
	
	@Size(max = 50, message = "O bairro da cidade não deve conter mais de 50 caracteres")
	@NotBlank(message = "O bairro é obrigatório")
	@Column(name = "bairro")
	private String bairro;
	
	@Size(max = 200, message = "O logradouro da cidade não deve conter mais de 200 caracteres")
	@NotBlank(message = "O logradouro é obrigatório")
	@Column(name = "logradouro")
	private String logradouro;
	
	@Column(name = "complemento")
	private String complemento;
	
}
