package br.edu.toledoprudente.pojo;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pessoas")
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoas extends AbstractEntity<Integer> {

	@NotBlank(message = "Nome é obrigatório")
	@Column(length = 150, nullable = false)
	private String nome;

	@NotBlank(message = "CPF é obrigatório")
	@Column(length = 11)
	private String cpf;

	@NotNull(message = "Data de nascimento é obrigatório")
	@Column(nullable = false, columnDefinition = "DATE")
	private LocalDate data_nascimento;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getData_nascimento() {
		return data_nascimento;
	}

	public void setData_nascimento(LocalDate data_nascimento) {
		this.data_nascimento = data_nascimento;
	}

}
