package br.com.projetojsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "pessoaBean")
@RequestScoped
public class PessoaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String sobrenome;
	private String nomeCompleto;

	private List<String> nomes = new ArrayList<String>();

	public String addNome() {
		nomes.add(nome);
		return "";
	}

	public List<String> getNomes() {
		return nomes;
	}

	public void setNomes(List<String> nomes) {
		this.nomes = nomes;
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

