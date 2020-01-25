package com.zreyes.unapec.springexcel.service;

import java.util.List;

import com.zreyes.unapec.springexcel.model.VentaDetalle;

public interface IVentaDetalleService {

	List<VentaDetalle> GetPaged(int pageSize, int page);

	VentaDetalle GetById(int id);

	VentaDetalle Add(VentaDetalle ventaDetalle);

	VentaDetalle Update(VentaDetalle ventaDetalle);

	VentaDetalle Delete(VentaDetalle ventaDetalle);

}