package com.taemoi.project.entidades;

public enum TipoGrado {
	BLANCO("Blanco"),
	BLANCO_AMARILLO("Blanco-Amarillo"),
	AMARILLO("Amarillo"),
	AMARILLO_NARANJA("Amarillo-Naranja"),
	NARANJA("Naranja"),
	NARANJA_VERDE("Naranja-Verde"),
	VERDE("Verde"),
	VERDE_AZUL("Verde-Azul"),
	AZUL("Azul"),
	AZUL_ROJO("Azul-Rojo"),
	ROJO("Rojo"),
	ROJO_NEGRO("Rojo-Negro"),
	NEGRO("Negro");
	
	private final String nombre;
	
	private TipoGrado(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
}
