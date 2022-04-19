/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objects;

import java.io.Serializable;
import java.util.Random;
import utilities.ConsoleColors;

/**
 *
 * @author alumne
 */
public class Pokemon implements Serializable{

    private final String name;
    private int CP;

    public Pokemon(String nombre) {
        this.name = nombre;
        this.CP = this.setCP();
    }

    public String getName() {
        return name;
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
                + "\nNombre: " + name
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
        if (!(obj instanceof Pokemon))
        {
            return false;
        }
        final Pokemon other = (Pokemon) obj;
        return this.name.equalsIgnoreCase(other.getName());
    }

}
