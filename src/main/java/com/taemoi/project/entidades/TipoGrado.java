package com.taemoi.project.entidades;

public enum TipoGrado {
	BLANCO("Blanco"), BLANCO_AMARILLO("Blanco-Amarillo"), AMARILLO("Amarillo"), AMARILLO_NARANJA("Amarillo-Naranja"),
	NARANJA("Naranja"), NARANJA_VERDE("Naranja-Verde"), VERDE("Verde"), VERDE_AZUL("Verde-Azul"), AZUL("Azul"),
	AZUL_ROJO("Azul-Rojo"), ROJO("Rojo"), ROJO_NEGRO("Rojo-Negro"), NEGRO("Negro");

	private final String nombre;

	private TipoGrado(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public static TipoGrado fromNombre(String nombre) {
		for (TipoGrado tipo : TipoGrado.values()) {
			if (tipo.nombre.equalsIgnoreCase(nombre)) {
				return tipo;
			}
		}
		throw new IllegalArgumentException("Tipo de grado no válido: " + nombre);
	}
}
