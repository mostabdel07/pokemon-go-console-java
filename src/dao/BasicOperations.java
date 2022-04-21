/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import objects.Pokemon;

import java.io.IOException;

/**
 *
 * @author alumne
 */
public interface BasicOperations {
    boolean catchPokemon(Pokemon pokemon);
    boolean transferPokemon(Pokemon pokemonToTransfer, String userToTransfer);
    boolean recivePokemon(String user) throws IOException, ClassNotFoundException;
}
