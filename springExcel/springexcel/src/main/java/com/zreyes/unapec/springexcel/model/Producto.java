package com.zreyes.unapec.springexcel.model;

public class Producto {
	private int id;
	private String nombre;
	private String descripcion;
	private int inicioInventario;
	private int puntoDePedido;
	private String unidadMedida;
	private int categoriaID;
	private String comentario;
	private int cantidadInventario;
	private Categoria categoria;
	
	public Producto() {
		this.id = 0;
		this.nombre = "";
		this.descripcion = "";
		this.inicioInventario = 0;
		this.puntoDePedido = 0;
		this.unidadMedida = "Pieza";
		this.categoriaID = 0;
		this.comentario = "";
		this.cantidadInventario = 0;
		this.setCategoria(null);
	}

	public int getCantidadInventario() {
		return cantidadInventario;
	}

	public void setCantidadInventario(int cantidadInventario) {
		this.cantidadInventario = cantidadInventario;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getInicioInventario() {
		return inicioInventario;
	}

	public void setInicioInventario(int inicioInventario) {
		this.inicioInventario = inicioInventario;
	}

	public int getPuntoDePedido() {
		return puntoDePedido;
	}

	public void setPuntoDePedido(int puntoDePedido) {
		this.puntoDePedido = puntoDePedido;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public int getCategoriaID() {
		return categoriaID;
	}

	public void setCategoriaID(int categoriaID) {
		this.categoriaID = categoriaID;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}
