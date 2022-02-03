package br.com.projetojsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.dao.DAOGeneric;
import br.com.entidades.Pessoa;

@ManagedBean(name = "pessoaBean")
//@RequestScoped
@ViewScoped
//@SessionScoped
//@ApplicationScoped
public class PessoaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pessoa pessoa = new Pessoa();
	
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	
	private DAOGeneric<Pessoa> daoGeneric = new DAOGeneric<Pessoa>();
	
	public String salvar() {
		pessoa =  daoGeneric.merge(pessoa);
		carregarPessoas();
		return "";
	}
	
	public String novo(){
		pessoa = new Pessoa();
		return "";
	}
	
	public String deletar() {
		daoGeneric.delete(pessoa);
		carregarPessoas();
		return "";
	}
	
	public String deletarPorId() {
		daoGeneric.deletarPorId(pessoa);
		pessoa = new Pessoa();
		carregarPessoas();
		return "";
	}
	@PostConstruct
	public void carregarPessoas() {
		pessoas = daoGeneric.getListEntity(pessoa);
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	public List<Pessoa> getPessoas() {
		return pessoas;
	}
}

