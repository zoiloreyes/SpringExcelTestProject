package com.zreyes.unapec.springexcel.service;

import java.util.List;

import com.zreyes.unapec.springexcel.model.Precio;

public interface IPrecioService {

	List<Precio> GetPaged(int pageSize, int page);

	Precio GetById(int id);

	Precio Add(Precio precio);

	Precio Update(Precio precio);

	Precio Delete(Precio precio);

}