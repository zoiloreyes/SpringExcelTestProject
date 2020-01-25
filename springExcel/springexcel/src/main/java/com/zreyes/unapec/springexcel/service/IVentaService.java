package com.zreyes.unapec.springexcel.service;

import java.util.List;

import com.zreyes.unapec.springexcel.model.Venta;

public interface IVentaService {

	List<Venta> GetPaged(int pageSize, int page);

	Venta GetById(int id);

	Venta Add(Venta venta);

	Venta Update(Venta venta);

	Venta Delete(Venta venta);

}