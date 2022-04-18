/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import objects.Pokemon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import persistence.PersistenceFile;

/**
 *
 * @author alumne
 */
public class PokeBag implements BasicOperations {

    private final ArrayList<Pokemon> pokeDex;
    private ArrayList<Pokemon> pokeBag;

    public PokeBag() {
        this.pokeDex = new ArrayList<>();
        this.pokeBag = new ArrayList<>();
    }

    @Override
    public boolean catchPokemon(Pokemon pokemon) {
        return pokeBag.add(pokemon) && pokeDex.add(pokemon);
    }

    @Override
    public void showBag() {
    }

    @Override
    public boolean transferPokemon(Pokemon pokemonTransfer) {
        return true;
    }

    @Override
    public boolean recivePokemon(Pokemon pokemonTransfer) {
        return true;
    }

    public String[] getUsersWithBag() {
        File users = new File("src/data/users"); // TODO: Cambiar por la carpeta bags
        return users.list();
    }

    public ArrayList<Pokemon> getPokeBag() {
        return this.pokeBag;

    }

    public boolean checkBag(Pokemon randomPokemon) {
        return pokeBag.contains(randomPokemon);
    }

    public int saveItemsToFile(String user) throws FileNotFoundException, IOException {
        if (PersistenceFile.saveAllItems(pokeBag, user)) {
            return pokeBag.size();
        } else {
            return 0;
        }

    }
    public int readItems(String user) throws FileNotFoundException, IOException, ClassNotFoundException {
        pokeBag=PersistenceFile.readAllItems(pokeBag, user);
        return pokeBag.size();
    }

}
