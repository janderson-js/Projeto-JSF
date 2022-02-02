package br.com.jpautil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
	
	private static EntityManagerFactory factory;
	
	static {
		if(factory == null){
			factory = Persistence.createEntityManagerFactory("projeto-jsf");
		}
	}
	
	public static EntityManager getEntityManager() {
		return factory.createEntityManager();
	}
	
	public static Object getPrimaryKey(Object entidade) {
		
		return factory.getPersistenceUnitUtil().getIdentifier(entidade);
	}
	
}
