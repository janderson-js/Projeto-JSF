package br.com.projetojsf;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "pessoaBean")
public class PessoaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String sobrenome;
	private String nomeCompleto;

	public String mostrarNome() {
		nomeCompleto = nome + " " + sobrenome;
		return "";
	}

	public String getNome() {
		return nome;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

}

