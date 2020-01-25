package com.zreyes.unapec.springexcel.service;

import java.util.List;

import com.zreyes.unapec.springexcel.model.Producto;

public interface IProductoService {

	List<Producto> GetPaged(int pageSize, int page);

	Producto GetById(int id);

	Producto Add(Producto producto);

	Producto Update(Producto producto);

	Producto Delete(Producto producto);

}