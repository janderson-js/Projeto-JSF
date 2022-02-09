package br.com.projetojsf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.com.dao.DAOGeneric;
import br.com.entidades.Lancamento;
import br.com.entidades.Pessoa;

@ViewScoped
@ManagedBean(name = "lancamento")
public class LancamentoBean {
	private Lancamento lancamento = new Lancamento();
	private DAOGeneric<Lancamento> daoGeneric = new DAOGeneric<Lancamento>();
	private List<Lancamento> lacamentos = new ArrayList<Lancamento>();
	
	
	public String salvar() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		
		lancamento.setUsuario(pessoaUser);
		daoGeneric.salvar(lancamento);
		
		return"";
	}
	
	public String novo(){
		lancamento = new Lancamento();
		return "";
	}
	
	public String deletar() {
		daoGeneric.delete(lancamento);
		carregarLancamentos();
		return "";
	}
	
	public String deletarPorId() {
		daoGeneric.deletarPorId(lancamento);
		lancamento = new Lancamento();
		carregarLancamentos();
		return "";
	}
	
	@PostConstruct
	public void carregarLancamentos() {
		lacamentos = daoGeneric.getListEntity(lancamento);
	}
	
	public Lancamento getLacamento() {
		return lancamento;
	}
	public void setLacamento(Lancamento lacamento) {
		this.lancamento = lacamento;
	}
	public DAOGeneric<Lancamento> getDaoGeneric() {
		return daoGeneric;
	}
	public void setDaoGeneric(DAOGeneric<Lancamento> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}
	public List<Lancamento> getLacamentos() {
		return lacamentos;
	}
	public void setLacamentos(List<Lancamento> lacamentos) {
		this.lacamentos = lacamentos;
	}
	
	
}
