package com.zreyes.unapec.springexcel.model;

import java.math.BigDecimal;

public class Compra {
	private int id;
	private int suplidorID;
	private BigDecimal costoExtra;
	private BigDecimal descuentoOrden;
	private BigDecimal tazaInteres;
	private String comentario;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSuplidorID() {
		return suplidorID;
	}

	public void setSuplidorID(int suplidorID) {
		this.suplidorID = suplidorID;
	}

	public BigDecimal getCostoExtra() {
		return costoExtra;
	}

	public void setCostoExtra(BigDecimal costoExtra) {
		this.costoExtra = costoExtra;
	}

	public BigDecimal getDescuentoOrden() {
		return descuentoOrden;
	}

	public void setDescuentoOrden(BigDecimal descuentoOrden) {
		this.descuentoOrden = descuentoOrden;
	}

	public BigDecimal getTazaInteres() {
		return tazaInteres;
	}

	public void setTazaInteres(BigDecimal tazaInteres) {
		this.tazaInteres = tazaInteres;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

}
