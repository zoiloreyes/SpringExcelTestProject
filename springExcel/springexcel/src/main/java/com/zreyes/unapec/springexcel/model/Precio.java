package com.zreyes.unapec.springexcel.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class Precio {
	private int id;
	private int productoID;
	private Date desdeFecha;
	private BigDecimal precioCompra;
	private BigDecimal precioVenta;
	
	public Precio() {
		this.setId(0);
		this.setProductoID(0);
		this.setDesdeFecha(Calendar.getInstance().getTime());
		this.setPrecioCompra(new BigDecimal(0));
		this.setPrecioVenta(new BigDecimal(0));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProductoID() {
		return productoID;
	}

	public void setProductoID(int productoID) {
		this.productoID = productoID;
	}

	public Date getDesdeFecha() {
		return desdeFecha;
	}

	public void setDesdeFecha(Date desdeFecha) {
		this.desdeFecha = desdeFecha;
	}

	public BigDecimal getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(BigDecimal precioCompra) {
		this.precioCompra = precioCompra;
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}
}
