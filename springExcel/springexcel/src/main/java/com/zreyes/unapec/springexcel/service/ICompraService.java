package com.zreyes.unapec.springexcel.service;

import java.util.List;

import com.zreyes.unapec.springexcel.model.Compra;

public interface ICompraService {

	List<Compra> GetPaged(int pageSize, int page);

	Compra GetById(int id);

	Compra Add(Compra compra);

	Compra Update(Compra compra);

	Compra Delete(Compra compra);

}