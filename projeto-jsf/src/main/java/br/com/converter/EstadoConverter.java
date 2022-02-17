package br.com.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Estados;
import br.com.jpautil.JPAUtil;

@FacesConverter(forClass = Estados.class)
public class EstadoConverter implements Converter,Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,String idEstado) {
		
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		Estados estados = (Estados) entityManager.find(Estados.class, Long.parseLong(idEstado));
		
		return estados;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,Object estado) {
		return ((Estados) estado).getId().toString();
	}
	
	

}
