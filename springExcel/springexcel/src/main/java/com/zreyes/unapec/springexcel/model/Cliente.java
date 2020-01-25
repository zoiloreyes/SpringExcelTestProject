package com.zreyes.unapec.springexcel.model;

public class Cliente {
	
	private int id;
	private String nombre;
	private String apellido;
	private String comentario;
	private String correo;
	private String telefono;
	
	public Cliente() {
		this.id = 0;
		this.nombre = "";
		this.apellido = "";
		this.comentario = "";
		this.correo = "";
		this.telefono = "";
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
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
}
