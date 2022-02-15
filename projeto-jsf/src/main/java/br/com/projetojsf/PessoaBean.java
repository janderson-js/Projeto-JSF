package br.com.projetojsf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import br.com.dao.DAOGeneric;
import br.com.entidades.Pessoa;
import br.com.repository.IDAOPessoa;
import br.com.repository.IDAOPessoaImpl;

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
	
	private IDAOPessoa iDaoPessoa = new  IDAOPessoaImpl();
	
	public String salvar() {
		pessoa =  daoGeneric.merge(pessoa);
		carregarPessoas();
		motrarMsg("Cadastrado com sucesso!!");
		return "";
	}
	
	private void motrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage("labelMSG", message);
		
		
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
		motrarMsg("Cadastrado com sucesso!!");
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
	
	public String logar() {
		
		Pessoa pessoaUser = iDaoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());
		
		if(pessoa != null) {
			
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			
			HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
			HttpSession session = req.getSession();
			
			session.setAttribute("usuarioLogado", pessoaUser);
			
			return "paginajsf.jsf";
		}
		return "index.jsf";
	}
	
	public Boolean permintirAcesso(String acesso) {
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		
		return pessoaUser.getPerfilUser().equalsIgnoreCase(acesso);
	}
	
	public void pesquisaCEP(AjaxBehaviorEvent event) {
		try {
			
			URL url = new URL("https://viacep.com.br/ws/"+ pessoa.getCep() +"/json/");
			
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			
			String cep = "";
			StringBuilder jsonCEP = new StringBuilder();
			
			while((cep = br.readLine()) != null) {
				jsonCEP.append(cep);
			}
			
			Pessoa gsonAux = new Gson().fromJson(jsonCEP.toString(), Pessoa.class);
			
			pessoa.setCep(gsonAux.getCep());
			pessoa.setLogradouro(gsonAux.getLogradouro());
			pessoa.setComplemento(gsonAux.getComplemento());
			pessoa.setBairro(gsonAux.getBairro());
			pessoa.setLocalidade(gsonAux.getLocalidade());
			pessoa.setUf(gsonAux.getUf());
			
		} catch (Exception e) {
			e.printStackTrace();
			motrarMsg("Erro ao consultar o cep!");
		}
	}
	
	public String deslogar() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getSessionMap().remove("usuarioLogado");
		
		HttpServletRequest req = (HttpServletRequest) context.getCurrentInstance().getExternalContext().getRequest();
		
		req.getSession().invalidate();
		
		return "index.jsf";
	}
	
}

