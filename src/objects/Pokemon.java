/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objects;

import java.util.Objects;
import java.util.Random;
import utilities.ConsoleColors;

/**
 *
 * @author alumne
 */
public class Pokemon {

    private final String nombre;
    private int CP;

    public Pokemon(String nombre) {
        this.nombre = nombre;
        this.CP = this.setCP();
    }

    public String getNombre() {
        return nombre;
    }

    public int getCP() {
        return CP;
    }

    private int setCP() {
        Random r = new Random();
        this.CP = r.nextInt(100) + 1;
        return this.CP;
    }

    public String showData() {
        return "\n-------------------------------------------"
                + ConsoleColors.TEXT_BRIGHT_YELLOW + "\nPokédex " + ConsoleColors.TEXT_RESET
                + "\nNombre: " + nombre
                + "\nCP: " + CP + " ✦"
                + "\n-------------------------------------------\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pokemon other = (Pokemon) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

}
