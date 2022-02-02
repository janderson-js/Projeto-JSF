package br.com.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.jpautil.JPAUtil;

public class DAOGeneric<E> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public void salvar(E entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		entityManager.persist(entidade);
		
		transaction.commit();
		entityManager.close();
	}
	
	public E merge(E entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		E retorno = entityManager.merge(entidade);
		
		transaction.commit();
		entityManager.close();
		
		return retorno;
	}

}
