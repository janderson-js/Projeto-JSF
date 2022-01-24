package br.com.projetojsf;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

import lombok.Data;

@ManagedBean(name = "pessoaBean")
@Data
public class PessoaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String sobrenome;


}

