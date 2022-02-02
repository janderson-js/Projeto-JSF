package br.com.projetojsf;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.com.dao.DAOGeneric;
import br.com.entidades.Pessoa;

@ManagedBean(name = "pessoaBean")
@RequestScoped
//@ViewScoped
//@SessionScoped
//@ApplicationScoped
public class PessoaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pessoa pessoa = new Pessoa();
	
	private DAOGeneric<Pessoa> daoGeneric = new DAOGeneric<Pessoa>();
	
	public String salvar() {
		daoGeneric.salvar(pessoa);
		return "";
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
}

