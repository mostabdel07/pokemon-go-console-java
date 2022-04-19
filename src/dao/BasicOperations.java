/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import objects.Pokemon;

/**
 *
 * @author alumne
 */
public interface BasicOperations {
    boolean catchPokemon(Pokemon pokemon);
    void showBag();
    boolean transferPokemon(Pokemon pokemonToTransfer, String userToTransfer);
    boolean recivePokemon(Pokemon pokemon);
}
