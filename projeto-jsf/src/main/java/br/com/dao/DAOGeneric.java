package br.com.dao;

import java.io.Serializable;
import java.util.List;

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
	
	public void delete(E entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		entityManager.remove(entidade);
		
		transaction.commit();
		entityManager.close();
	}
	
	public void deletarPorId(E entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		Object id = JPAUtil.getPrimaryKey(entidade);
		entityManager.createQuery("delete from "+ entidade.getClass().getCanonicalName() + " where id = "+ id).executeUpdate();
		
		transaction.commit();
		entityManager.close();
	}
	
	public List<E> getListEntity(E entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		List<E> retorno = entityManager.createQuery("from "+ entidade.getClass().getCanonicalName()+ " order by id").getResultList() ;
		
		transaction.commit();
		entityManager.close();
		
		return retorno;
	}
	
	public E consultar(Class<E> entidade, String idUser) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		E objeto = (E) entityManager.find(entidade, Long.parseLong(idUser));
		
		transaction.commit();
		
		return objeto;
	}

}
