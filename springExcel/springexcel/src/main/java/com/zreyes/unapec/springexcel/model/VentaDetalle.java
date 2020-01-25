package com.zreyes.unapec.springexcel.model;

import java.math.BigDecimal;

public class VentaDetalle {
	private int id;
	private int ventaID;
	private int cantidad;
	private BigDecimal descuentoXUnidad;
	private String comentario;
	private BigDecimal precioUnidad;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getVentaID() {
		return ventaID;
	}
	public void setVentaID(int ventaID) {
		this.ventaID = ventaID;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public BigDecimal getDescuentoXUnidad() {
		return descuentoXUnidad;
	}
	public void setDescuentoXUnidad(BigDecimal descuentoXUnidad) {
		this.descuentoXUnidad = descuentoXUnidad;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public BigDecimal getPrecioUnidad() {
		return precioUnidad;
	}
	public void setPrecioUnidad(BigDecimal precioUnidad) {
		this.precioUnidad = precioUnidad;
	}
}
