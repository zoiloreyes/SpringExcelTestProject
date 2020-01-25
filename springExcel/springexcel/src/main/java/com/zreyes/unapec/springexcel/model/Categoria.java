package com.zreyes.unapec.springexcel.model;

public class Categoria {
	private int id;
	private String nombre;
	
	public Categoria() {
		this.setId(0);
		this.setNombre("");
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
