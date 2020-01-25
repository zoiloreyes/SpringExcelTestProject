package com.zreyes.unapec.springexcel.service;

import java.util.List;

import com.zreyes.unapec.springexcel.model.Categoria;

public interface ICategoriaService {
	
	List<Categoria> GetPaged(int pageSize, int page);
	Categoria GetById(int id);
	Categoria Add(Categoria categoria);
	Categoria Update(Categoria categoria);
	Categoria Delete(Categoria categoria);
}
