package br.com.bean;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import br.com.dao.DAOGeneric;
import br.com.entidades.Cidades;
import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;
import br.com.repository.IDAOPessoa;
import br.com.repository.IDAOPessoaImpl;

@ManagedBean(name = "pessoaBean")
// @RequestScoped
@ViewScoped
// @SessionScoped
// @ApplicationScoped
public class PessoaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pessoa pessoa = new Pessoa();

	private List<Pessoa> pessoas = new ArrayList<Pessoa>();

	private DAOGeneric<Pessoa> daoGeneric = new DAOGeneric<Pessoa>();

	private IDAOPessoa iDaoPessoa = new IDAOPessoaImpl();

	private List<SelectItem> estados;

	private List<SelectItem> cidades;
	
	private Part arquivoFoto;
	
	//Metodos

	public Part getArquivoFoto() {
		return arquivoFoto;
	}

	public void setArquivoFoto(Part arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}

	public List<SelectItem> getCidades() {
		return cidades;
	}

	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}

	public String salvar()throws IOException{
		//Inicio Processar imagem!!
			byte[] imagemByte = getByte(arquivoFoto.getInputStream());
			pessoa.setFotoIconBase64Original(imagemByte);
			// transforma em bufferimage
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemByte));
			// Pega o tipo da Imagem
			int type = bufferedImage.getType() == 0? bufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
			
			int largura = 200;
			int altura = 200;
			// Cria a miniatura
			BufferedImage resizedImage = new BufferedImage(altura, altura, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(resizedImage, 0, 0, largura, altura, null);
			g.dispose();
			// Escrever a imagem em um tamnho menor
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String extensao = arquivoFoto.getContentType().split("\\/")[1];
			ImageIO.write(resizedImage, extensao, baos);
			
			String miniImagem = "data:"+arquivoFoto.getContentType()+";base64" + 
			DatatypeConverter.printBase64Binary(baos.toByteArray());
		//Fim Processar imagem!!
		pessoa.setFotoIconBase64(miniImagem);
		pessoa.setExtensao(extensao);
		pessoa = daoGeneric.merge(pessoa);
		carregarPessoas();
		editar();
		motrarMsg("Cadastrado com sucesso!!");
		return "";
	}
	
	public void editar() {
		if(pessoa.getCidades() != null) {
			Estados estado = pessoa.getCidades().getEstados();
			pessoa.setEstado(estado);
			
			List<Cidades> cidades = JPAUtil.getEntityManager()
					.createQuery("from Cidades where estados_id=" + estado.getId())
					.getResultList();

			List<SelectItem> selectItemsCidades = new ArrayList<SelectItem>();

			for (Cidades cidade : cidades) {
				selectItemsCidades
						.add(new SelectItem(cidade, cidade.getNome()));
			}

			setCidades(selectItemsCidades);
		}
	}

	public void motrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage("labelMSG", message);

	}

	public String novo() {
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

	public List<SelectItem> getEstados() {
		estados = iDaoPessoa.lsitaEstados();
		return estados;
	}

	public void setEstados(List<SelectItem> estados) {
		this.estados = estados;
	}

	public String logar() {
		
		Pessoa pessoaUser = new Pessoa();
		
		if(iDaoPessoa.consultarUsuario(pessoa.getLogin(),pessoa.getSenha()) != null) {
			 pessoaUser = iDaoPessoa.consultarUsuario(pessoa.getLogin(),pessoa.getSenha());
		}

		if (pessoa != null) {

			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();

			HttpServletRequest req = (HttpServletRequest) externalContext
					.getRequest();
			HttpSession session = req.getSession();

			session.setAttribute("usuarioLogado", pessoaUser);

			return "paginajsf.jsf";
		}
		return "index.jsf";
	}

	public Boolean permintirAcesso(String acesso) {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();

		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap()
				.get("usuarioLogado");

		return pessoaUser.getPerfilUser().equalsIgnoreCase(acesso);
	}

	public void pesquisaCEP(AjaxBehaviorEvent event) {
		try {

			URL url = new URL(
					"https://viacep.com.br/ws/" + pessoa.getCep() + "/json/");

			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();

			BufferedReader br = new BufferedReader(
					new InputStreamReader(is, "UTF-8"));

			String cep = "";
			StringBuilder jsonCEP = new StringBuilder();

			while ((cep = br.readLine()) != null) {
				jsonCEP.append(cep);
			}

			Pessoa gsonAux = new Gson().fromJson(jsonCEP.toString(),
					Pessoa.class);

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

		HttpServletRequest req = (HttpServletRequest) context
				.getCurrentInstance().getExternalContext().getRequest();

		req.getSession().invalidate();

		return "index.jsf";
	}

	public void carregaCidades(AjaxBehaviorEvent event) {

		Estados estado = (Estados) ((HtmlSelectOneMenu) event.getSource())
				.getValue();

		if (estado != null) {
			pessoa.setEstado(estado);

			List<Cidades> cidades = JPAUtil.getEntityManager()
					.createQuery("from Cidades where estados_id=" + estado.getId())
					.getResultList();

			List<SelectItem> selectItemsCidades = new ArrayList<SelectItem>();

			for (Cidades cidade : cidades) {
				selectItemsCidades
						.add(new SelectItem(cidade, cidade.getNome()));
			}

			setCidades(selectItemsCidades);
		}

	}
	
	private byte[] getByte(InputStream is) throws IOException {
		
		int len;
		int size = 1024;
		byte[] buf = null;
		
		if(is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		}else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			
			while((len = is.read(buf,0,size)) != -1) {
				bos.write(buf,0,len);
			}
			
			buf = bos.toByteArray();
		}
		
		return buf;
	}
 }
