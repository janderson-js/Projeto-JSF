package br.com.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Cidades;
import br.com.jpautil.JPAUtil;

@FacesConverter(forClass = Cidades.class)
public class CidadesConverter implements Converter,Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,String idCidade) {
		
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		Cidades cidades = (Cidades) entityManager.find(Cidades.class, Long.parseLong(idCidade));
		
		return cidades;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,Object cidade) {
		return ((Cidades) cidade).getId().toString();
	}
	
	

}
