/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import objects.Pokemon;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import persistence.PersistenceFile;

/**
 * @author alumne
 */
public class PokeBag implements BasicOperations {

    private ArrayList<Pokemon> pokeBag;

    public PokeBag() {
        this.pokeBag = new ArrayList<>();
    }


    @Override
    public boolean catchPokemon(Pokemon pokemon) {
        return pokeBag.add(pokemon);
    }

    @Override
    public boolean transferPokemon(Pokemon pokemonToTransfer, String userToTransfer) {

        try {
            FileOutputStream transferFile = new FileOutputStream("src/data/assets/transfer_" + userToTransfer + ".dat");

            // Buscar Pokemon por indice
            int posPokemon = pokeBag.indexOf(pokemonToTransfer);
            if (posPokemon > -1) {
                if(!PersistenceFile.saveOneItem(pokeBag.get(posPokemon), transferFile)){
                    return false;
                }

                // Borrar Pokemon de la mochila
                pokeBag.remove(posPokemon);

                // Crear o remplazar fichero mochila
                savePokemonsIntoBag(userToTransfer);
            } else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean recivePokemon(String userToRecive) throws IOException, ClassNotFoundException {
        Pokemon pokemonTransfer = PersistenceFile.readOneItem(userToRecive);
        if (pokemonTransfer != null) {
            catchPokemon(pokemonTransfer);
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPokemonExists(Pokemon pokemon) {
        return pokeBag.contains(pokemon);
    }

    public boolean deletePokemon(Pokemon delete) {
        return pokeBag.remove(delete);
    }


    // Getters
    public String[] getUsersWithBag() throws NullPointerException {
        File users = new File("src/data/bags");
        return users.list();
    }

    public ArrayList<Pokemon> getPokeBag() {
        Collections.sort(pokeBag);
        return this.pokeBag;
    }


    // Save into binary files
    public int savePokemonsIntoBag(String user) throws IOException {
        FileOutputStream bagFile = new FileOutputStream("src/data/bags/" + user + "_bag.dat");
        if (PersistenceFile.saveAllItems(pokeBag, bagFile)) {
            return pokeBag.size();
        } else {
            return 0;
        }
    }


    // Read binary files
    public int readPokeBag(String user) throws ClassNotFoundException, IOException {
        FileInputStream bagFile = new FileInputStream("src/data/bags/" + user + "_bag.dat");
        pokeBag = PersistenceFile.readAllItems(bagFile);
        return pokeBag.size();
    }

}
