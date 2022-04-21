/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pokemongojava;

import dao.PokeBag;
import objects.Pokemon;
import utilities.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author alumne
 */
public class PokemonGoJava {

    private Menu mainMenu;
    private PokeBag pokeBag;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PokemonGoJava app = new PokemonGoJava();
        app.startGame();
    }

    private void startGame() {
        pokeBag = new PokeBag();
        mainMenu = new Menu("Pokemon GO Menu");

        File logoAnimation = new File("src/data/assets/logo.pok");
        File logInAnimation = new File("src/data/assets/logIn.pok");

        Tools.displayAnimation(logoAnimation);

        String user;
        user = Tools.userLogIn();

        Tools.displayAnimation(logInAnimation);

        readItems(user); // Comprobar mochila y pokédex

        addOptionsMenu();

        int option = -1; // Se le asigna un valor no válido por si entra en la excepción
        do {
            mainMenu.showMenu();
            try {
                option = mainMenu.choose(); // Se le asigna el valor elegido
            } catch (InputMismatchException e) {
                System.out.println("No se admiten carácteres.");
            }
            switch (option) {
                case 1:
                    //Cazar Pokemons
                    catchPokemon(user);
                    break;
                case 2:
                    //Listar Mochila Pokemon Cazado
                    displayPokeBag();
                    break;
                case 3:
                    //Transferir Pokemon
                    transferPokemon();
                    break;
                case 4:
                    //Recibir Pokemon transferido
                    getPokemonTransferred(user);
                    break;
                case 5:
                    //Comprar Poké Balls
                    buyPokeballs(user);
                    break;
                case 6:
                    //Listar usuarios que han jugado una partida
                    displayUsersList();
                    break;
                case 7:
                    //Deshacerse de Pokemon de la mochila
                    deletePokemon();
                    break;
                case 0:
                    exitGame(user);
                    break;
                default:
                    System.out.println(ConsoleColors.TEXT_BRIGHT_RED + "Opción no válida. ❌" + ConsoleColors.TEXT_RESET);
                    break;
            }
        } while (option != 0);

    }

    private void transferPokemon() {
        displayPokeBag();
        Scanner sc = new Scanner(System.in);
        // Pedir datos
        System.out.print("➤ ¿Que pokemon deseas tranferir?: ");
        String pokemonToTransfer = sc.nextLine();
        sc.nextLine();
        System.out.print("➤ ¿A que usuario lo quieres tranferir?: ");
        String userToTransfer = sc.nextLine();

        System.out.println("Transferiendo " + pokemonToTransfer + " a " + userToTransfer + "... ⏱");

        if (pokeBag.transferPokemon(new Pokemon(pokemonToTransfer), userToTransfer)) {
            System.out.println(ConsoleColors.TEXT_BRIGHT_GREEN + "Se ha transferido correctamente. ✔" + ConsoleColors.TEXT_RESET);
        } else {
            System.out.println(ConsoleColors.TEXT_BRIGHT_RED + "Ha ocurrido algún fallo, el pokemon o usuario no existe. ❌" + ConsoleColors.TEXT_RESET);
        }
    }

    private void buyPokeballs(String user) {
        Scanner sc = new Scanner(System.in);
        File pokeBall = new File("src/data/assets/pokeball.pok");
        File numPokeBalls = new File("src/data/pokeballs/" + user + "_pokeballs.dat");
        String oldContent;
        int numTotal = 0;
        int num;

        Tools.displayAnimation(pokeBall);

        do {
            System.out.print("¿Cuantas Poké Balls quieres comprar? (5 Max.): ");
            num = sc.nextInt();

            if (num > 5) { // Máximo se le dará 5 Poké Balls
                num = 5;
            }

        } while (num < 0);

        try {

            if (numPokeBalls.exists()) {
                oldContent = Tools.readFile(numPokeBalls).get(0); // Recoger el numero de Poké Balls que tenemos
                numTotal += Integer.parseInt(oldContent); // convertir en entero para sumarlo a las Poké Balls compradas
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        numTotal += num;

        if (Tools.createFile(numPokeBalls, String.valueOf(numTotal))) {
            System.out.println(ConsoleColors.TEXT_BRIGHT_YELLOW + "★★ ¡Has recibido " + num
                    + " Poké Balls! ★★" + ConsoleColors.TEXT_RESET);
        }

    }

    private void displayUsersList() {

        Tools.displayLoading();

        System.out.println("\nLista de usuarios: ");
        try {
            for (String user : pokeBag.getUsersWithBag()) {
                for (int i = 0; i < user.length(); i++) {
                    if (user.charAt(i) == '_') { // Comprueba que hay una barra baja
                        // Devuelve el nombre del usuario sin la extensión
                        System.out.println("- " + user.substring(0, i));
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("No hay usuarios que han jugado una partida y que tengan mochila");
        }

    }

    private boolean askQuestion(String question) {
        Scanner sc = new Scanner(System.in);
        char option;
        boolean isExit = false;

        do {
            System.out.print("\n" + question);
            option = sc.nextLine().charAt(0);

            if (!(option == 's' || option == 'S' || option == 'n' || option == 'N')) {
                System.out.println(ConsoleColors.TEXT_BRIGHT_RED
                        + "Opción incorrecta. ❌" + ConsoleColors.TEXT_RESET);
            } else {
                if (option == 'n' || option == 'N') {
                    isExit = true;
                }
            }
        } while (!(option == 's' || option == 'S' || option == 'n' || option == 'N'));

        return isExit;
    }

    private void exitGame(String user) {
        saveItems(user);
        boolean exit = askQuestion("➤ ¿Quieres cambiar de usuario? s/n: ");
        if (!exit) {
            startGame();
        } else {
            System.out.println("Saliendo... ⏱");
        }
    }

    private void catchPokemon(String user) {
        Random r = new Random();
        int randomPosition;
        int pokeBalls;
        boolean isExit = false;

        try {
            File namesFile = new File("src/data/assets/names.dat");
            File numPokeBalls = new File("src/data/pokeballs/" + user + "_pokeballs.dat");
            pokeBalls = Integer.parseInt(Tools.readFile(numPokeBalls).get(0));
            while (pokeBalls > 0 && !isExit) {
                // Crear pokemon random
                randomPosition = r.nextInt(Tools.readFile(namesFile).size());
                String name = Tools.readFile(namesFile).get(randomPosition);
                Pokemon randomPokemon = new Pokemon(name);
                // LEER POKEMON ASCCI
                // Visualizar Pokemon
                System.out.println("¡Ha aparecido un " + name + " salvaje!\n");
                showPokemon(name); // Mostrar imagen
                System.out.println(randomPokemon.showData()); // Mostrar datos

                if (checkPokemonExists(randomPokemon)) { // Comprueba si ya esta en la mochila
                    System.out.println("Este Pokémon ya lo tienes capturado.");
                    isExit = askQuestion("➤ ¿Deseas capturarlo de todas formas? s/n: ");

                    if (!isExit) {
                        if (tryGetPokemon(randomPokemon)) {

                            if (pokeBag.catchPokemon(randomPokemon)) {
                                System.out.println(ConsoleColors.TEXT_BRIGHT_YELLOW + "★★ ¡Felicidades lo has capturado! ★★" + ConsoleColors.TEXT_RESET);
                                System.out.println("Se ha añadido " + name + " a tu mochila. ✔️");

                            } else {
                                System.out.println("No se ha podido añadir.");
                            }

                        } else {
                            System.out.println("¡" + name + " se escapó!");
                        }
                        pokeBalls--;
                    }

                } else {
                    if (tryGetPokemon(randomPokemon)) {

                        if (pokeBag.catchPokemon(randomPokemon)) {
                            System.out.println(ConsoleColors.TEXT_BRIGHT_YELLOW + "★★ ¡Felicidades lo has capturado! ★★" + ConsoleColors.TEXT_RESET);
                            System.out.println("Se ha añadido " + name + " a tu mochila. ✔️");

                        } else {
                            System.out.println("No se ha podido añadir.");
                        }

                    } else {
                        System.out.println("¡" + name + " se escapó!");
                    }
                    pokeBalls--;
                }

                System.out.println("\nDisponible: ◓ " + pokeBalls + " Poké Ball(s)");

                if (pokeBalls > 0) {
                    isExit = askQuestion("➤ ¿Deseas seguir capturando Pokemons? s/n: ");
                }
            }

            if (pokeBalls <= 0) {
                System.out.println(ConsoleColors.TEXT_BRIGHT_RED
                        + "No te quedan Poké Balls ◓" + ConsoleColors.TEXT_RESET);
            }

            // Actualizar el numero de Poké Balls en el fichero
            Tools.createFile(numPokeBalls, String.valueOf(pokeBalls));

            System.out.println("Volviendo al menu principal... ⏱");

        } catch (FileNotFoundException | NullPointerException e) {
            System.out.println("No tienes PokéBalls ves a la tienda a comprar.");
        }
    }

    private boolean checkPokemonExists(Pokemon randomPokemon) {
        return pokeBag.checkPokemonExists(randomPokemon);
    }

    private void showPokemon(String name) {
        File nameFile = new File("src/data/pokemons/" + name + ".pok");
        try {
            for (String line : Tools.readFile(nameFile)) {
                System.out.println(ConsoleColors.TEXT_BRIGHT_GREEN + line.replace("printf(\"", "").replace("\\n\");", "") + ConsoleColors.TEXT_RESET);
                Thread.sleep(80);
            }
            System.out.println();
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addOptionsMenu() {
        mainMenu.add(new Option("Cazar Pokémons"));
        mainMenu.add(new Option("Mostrar Pokémons en mochila"));
        mainMenu.add(new Option("Transferir Pokémon"));
        mainMenu.add(new Option("Recibir Pokémon transferido"));
        mainMenu.add(new Option("Comprar Poké Balls"));
        mainMenu.add(new Option("Mostar usuarios que han jugado"));
        mainMenu.add(new Option("Deshacerse de Pokemon"));
        mainMenu.add(new Option("Salir"));
    }

    private void displayPokeBag() {
        ArrayList<Pokemon> bag = pokeBag.getPokeBag();
        System.out.println("\nPokéBag\n");
        if (bag.size() <= 0) {
            System.out.println("Todavía no has capturado ningún pokémon.");
        } else {
            for (Pokemon pokemon : bag) {
                System.out.println(pokemon.showData());
            }
            System.out.println("Cantidad Pokémons: " + bag.size());
        }
    }

    private boolean tryGetPokemon(Pokemon randomPokemon) {
        Random r = new Random();
        Scanner sc = new Scanner(System.in);

        if (randomPokemon.getCP() >= 10) {
            int number = (randomPokemon.getCP() / 10) + 1;
            int numberRandom = r.nextInt(number) + 1;
            System.out.println("('NUMERO')" + numberRandom);

            System.out.println("Adivina el numero del 1 al " + number + " para cazar a " + randomPokemon.getName());

            try {
                int number_user = sc.nextInt();
                if (number_user != numberRandom) {
                    return false;
                }
            } catch (InputMismatchException e) {
                System.out.println("No se admiten carácteres. ❌");
                return false;
            }

        } else { // si CP es menor a 9 se caza directamente sin adivinar
            return true;
        }
        return true;
    }

    private void saveItems(String user) {
        int sizePokeBag;

        try {
            sizePokeBag = pokeBag.savePokemonsIntoBag(user);
        } catch (FileNotFoundException ex) {
            System.out.println("Fichero no existente" + ex.getMessage());
            sizePokeBag = 0;
        } catch (IOException ex) {
            System.out.println("Error de escritura" + ex.getMessage());
            sizePokeBag = 0;
        }
        System.out.println("Numero de Pokémon en mochila: " + sizePokeBag);
    }

    private void readItems(String user) {
        int numItems;
        try {
            System.out.println("Cargando datos... ⏱");
            System.out.println("\n********************************************");
            System.out.println("Usuario: " + user);
            numItems = pokeBag.readPokeBag(user);
            System.out.println("PokéBag: " + numItems + " ◓ capturados.");
            System.out.println("********************************************");
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PokemonGoJava.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void deletePokemon() {
        displayPokeBag();
        Scanner sc = new Scanner(System.in);
        System.out.println("Dime el nombre del Pokemon a eliminar de la PokeBag:");
        String name = sc.nextLine();
            Pokemon delete = new Pokemon(name);
            if (pokeBag.deletePokemon(delete)) {
                System.out.println("Pokemon eliminado con exito");
            } else {
                System.out.println("Pokemon no existe, no se ha eliminado");
            }

    }

    private void getPokemonTransferred(String user) {
        try {
            if (pokeBag.recivePokemon(user)) {
                System.out.println(ConsoleColors.TEXT_BRIGHT_GREEN + "Se ha recivido correctamente. ✔" + ConsoleColors.TEXT_RESET);
            } else {
                System.out.println(ConsoleColors.TEXT_BRIGHT_RED + "No tienes Pokemons por recibir. ❌" + ConsoleColors.TEXT_RESET);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
