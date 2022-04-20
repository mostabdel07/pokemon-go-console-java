/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import objects.Pokemon;

import java.io.*;
import java.util.ArrayList;
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

    @Override
    public boolean catchPokemon(Pokemon pokemon) {
        return pokeBag.add(pokemon) && pokeDex.add(pokemon);
    }

    @Override
    public void showBag() {
    }

    @Override
    public boolean transferPokemon(Pokemon pokemonToTransfer, String userToTransfer) {

        try {
            FileOutputStream transferFile = new FileOutputStream("src/data/assets/transfer_" + userToTransfer + ".dat");

            // Buscar Pokemon por indice
            int posPokemon = pokeBag.indexOf(pokemonToTransfer);
            if(posPokemon > -1){
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
        return this.pokeBag;
    }

    public ArrayList<Pokemon> getPokeDex() {
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
        pokeBag=PersistenceFile.readAllItems(pokeBag, bagFile);
        return pokeBag.size();
    }

    public int readPokeDex(String user) throws IOException, ClassNotFoundException {
        FileInputStream pokedexFile = new FileInputStream("src/data/pokedex/" + user + "_pokedex.dat");
        pokeDex=PersistenceFile.readAllItems(pokeDex, pokedexFile);
        return pokeDex.size();
    }

}
