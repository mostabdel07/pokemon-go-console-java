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
 *
 * @author alumne
 */
public class PokeBag implements BasicOperations {

    private ArrayList<Pokemon> pokeDex;
    private ArrayList<Pokemon> pokeBag;

    public PokeBag() {
        this.pokeDex = new ArrayList<>();
        this.pokeBag = new ArrayList<>();
    }

    /**
     * Comprueba si el pokemon existe en la mochila y lo añade.
     *
     * @param pokemon
     * @return integer 0 añadido, -1 no añadido y 1 añadido pero repetido
     */
    @Override
    public int catchPokemon(Pokemon pokemon) {
        int added = 0;
//        if(pokeBag.size() > 10)
        // TODO: Maximo 10 pokemons en mochila, explicar
        for (int i = 0; i < pokeBag.size() - 1; i++) {
            if (pokemon.equals(pokeBag.get(i))) {
                added = 1;
            }
        }

        if (!pokeBag.add(pokemon)) {
            added = -1;
        }

        return added;
    }

    /**
     * Comprueba si el pokemon existe en la pokedex y lo añade.
     *
     * @param pokemon
     * @return boleano
     */
    public boolean registerPokemon(Pokemon pokemon) {
        // ERROR. (NO)Comprueba que no este repetido
        for (int i = 0; i < pokeDex.size()-1; i++) {
            if(pokemon.equals(pokeDex.get(i))) {
                return false;
            }
        }
        return pokeDex.add(pokemon);
    }

    @Override
    public boolean transferPokemon(Pokemon pokemonToTransfer, String userToTransfer) {

        try {
            FileOutputStream transferFile = new FileOutputStream("src/data/assets/transfer_" + userToTransfer + ".dat");

            // Buscar Pokemon por indice
            int posPokemon = pokeBag.indexOf(pokemonToTransfer);
            if (posPokemon > -1) {
                PersistenceFile.saveOneItem(pokeBag.get(posPokemon), transferFile);

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
    public boolean recivePokemon(Pokemon pokemonTransfer) {
        return true;
    }

    public String[] getUsersWithBag() {
        File users = new File("src/data/bags");
        return users.list();
    }

    public ArrayList<Pokemon> getPokeBag() {
        Collections.sort(pokeBag);
        return this.pokeBag;
    }

    public ArrayList<Pokemon> getPokeDex() {
        Collections.sort(pokeDex);
        return this.pokeDex;
    }

    public boolean checkBag(Pokemon randomPokemon) {
        return pokeBag.contains(randomPokemon);
    }

    // Save into binary files
    public int savePokemonsIntoBag(String user) throws FileNotFoundException, IOException {
        FileOutputStream bagFile = new FileOutputStream("src/data/bags/" + user + "_bag.dat");
        if (PersistenceFile.saveAllItems(pokeBag, bagFile)) {
            return pokeBag.size();
        } else {
            return 0;
        }
    }

    public int savePokemonsIntoPokedex(String user) throws FileNotFoundException, IOException {
        FileOutputStream pokedexFile = new FileOutputStream("src/data/pokedex/" + user + "_pokedex.dat");
        if (PersistenceFile.saveAllItems(pokeDex, pokedexFile)) {
            return pokeDex.size();
        } else {
            return 0;
        }
    }

    // Read binary files
    public int readPokeBag(String user) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream bagFile = new FileInputStream("src/data/bags/" + user + "_bag.dat");
        pokeBag = PersistenceFile.readAllItems(pokeBag, bagFile);
        return pokeBag.size();
    }

    public int readPokeDex(String user) throws IOException, ClassNotFoundException {
        FileInputStream pokedexFile = new FileInputStream("src/data/pokedex/" + user + "_pokedex.dat");
        pokeDex = PersistenceFile.readAllItems(pokeDex, pokedexFile);
        return pokeDex.size();
    }
    
    public boolean deletePokemon(Pokemon delete) {
        return pokeBag.remove(delete);
    }

}
