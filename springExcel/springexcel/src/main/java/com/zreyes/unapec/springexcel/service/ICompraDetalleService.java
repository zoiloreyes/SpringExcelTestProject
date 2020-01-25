package com.zreyes.unapec.springexcel.service;

import java.util.List;

import com.zreyes.unapec.springexcel.model.CompraDetalle;

public interface ICompraDetalleService {

	List<CompraDetalle> GetPaged(int pageSize, int page);

	CompraDetalle GetById(int id);

	CompraDetalle Add(CompraDetalle compraDetalle);

	CompraDetalle Update(CompraDetalle compraDetalle);

	CompraDetalle Delete(CompraDetalle compraDetalle);

}