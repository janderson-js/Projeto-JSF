package br.com.repository;

import br.com.entidades.Pessoa;

public interface IDAOPessoa {
	
	Pessoa consultarUsuario(String login, String senha);
	
}
