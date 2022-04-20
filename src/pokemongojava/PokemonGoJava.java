/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pokemongojava;

import dao.PokeBag;
import objects.Pokemon;
import utilities.ConsoleColors;
import utilities.ConsoleLoading;
import utilities.Menu;
import utilities.Option;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
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

        displayAnimation(logoAnimation);

        String user;
        user = userLogIn();

        displayAnimation(logInAnimation);

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
                    catchPokemon();
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
                    System.out.println("Opción no implementada \nRecibir Pokemon transferido");
                    break;
                case 5:
                    //Comprar Poké Balls
                    buyPokeballs();
                    break;
                case 6:
                    //Listar usuarios que han jugado una partida
                    displayUsersList();
                    break;
                case 7:
                    //Listar usuarios que han jugado una partida
                    displayPokeDex();
                    break;
                case 8:
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

    private void displayPokeDex() {
        ArrayList<Pokemon> pokeDex = pokeBag.getPokeDex();
        System.out.println("\nPokéDex\n");
        if (pokeDex.size() <= 0) {
            System.out.println("Todavía no tines ningún pokémon registrado.");
        } else {
            displayLoading();
            for (Pokemon pokemon : pokeDex) {
                System.out.println(pokemon.showData());
            }

            System.out.println("Total Pokémons registrados: " + pokeDex.size());
        }
    }

    private void transferPokemon() {
        Scanner sc = new Scanner(System.in);
        // Pedir datos
        System.out.print("➤ ¿Que pokemon deseas tranferir?: ");
        String pokemonToTransfer = sc.nextLine();
        // TODO: MOSTAR POKEMON A ELEGUIR SEGÚN CP
        System.out.print("Dime sus CombatPoints:");
        int CP = sc.nextInt();
        sc.nextLine();
        System.out.print("➤ ¿A que usuario lo quieres tranferir?: ");
        String userToTransfer = sc.nextLine();

        System.out.println("Transferiendo " + pokemonToTransfer + " a " + userToTransfer + "... ⏱");

        if (pokeBag.transferPokemon(new Pokemon(pokemonToTransfer, CP), userToTransfer)) {
            System.out.println(ConsoleColors.TEXT_BRIGHT_GREEN + "Se ha transferido correctamente. ✔" + ConsoleColors.TEXT_RESET);
        } else {
            System.out.println(ConsoleColors.TEXT_BRIGHT_RED + "Ha ocurrido algún fallo, el pokemon o usuario no existe. ❌" + ConsoleColors.TEXT_RESET);
        }
    }

    private void exitGame(String user) {
        saveItems(user);
        boolean exit = askExit("➤ ¿Quieres cambiar de usuario? s/n: ");
        if (!exit) {
            startGame();
        } else {
            saveItems(user);
            System.out.println("Saliendo... ⏱");
        }
    }

    private void buyPokeballs() {
        Scanner sc = new Scanner(System.in);
        File pokeBall = new File("src/data/assets/pokeball.pok");
        displayAnimation(pokeBall);
        int num;
        do {
            System.out.print("¿Cuantas Poké Balls quieres comprar? (5 Max.): ");
            num = sc.nextInt();

            if (num > 5) { // Máximo se le dará 5 Poké Balls
                num = 5;
            }

        } while (num < 0);

        try {
            File numPokeBalls = new File("src/data/assets/numPokeBalls.dat");
            String oldContent;
            oldContent = readFile(numPokeBalls).get(0); // Recoger el numero de Poké Balls que tenemos

            int numTotal = num;
            numTotal += Integer.parseInt(oldContent); // convertir en entero para sumarlo a las Poké Balls compradas

            if (createFile(numPokeBalls, String.valueOf(numTotal))) {
                System.out.println(ConsoleColors.TEXT_BRIGHT_YELLOW + "★★ ¡Has recibido " + num
                        + " Poké Balls! ★★" + ConsoleColors.TEXT_RESET);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void displayUsersList() {

        displayLoading();

        System.out.println("\nLista de usuarios: ");
        for (String usuario : pokeBag.getUsersWithBag()) {
            for (int i = 0; i < usuario.length(); i++) {
                if (usuario.charAt(i) == '_') { // Comprueba que hay una barra baja
                    // Devuelve el nombre del usuario sin la extensión
                    System.out.println("- " + usuario.substring(0, i));
                }
            }
        }

    }

    private void displayLoading() {
        ConsoleLoading consoleHelper = new ConsoleLoading();
        for (int i = 0; i <= 100; i = i + 6) {
            consoleHelper.animate(i + (ConsoleColors.TEXT_BRIGHT_GREEN + ""));
            //simulate a piece of task
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i == 96) { // Para que el contador al parar muestre 100%
                consoleHelper.animate((i += 4) + "");
                System.out.println(ConsoleColors.TEXT_RESET);
            }
        }
    }

    private boolean askExit(String question) {
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

    private void catchPokemon() {
        Random r = new Random();
        int randomPosition;
        int pokeBalls;
        boolean isExit = false;

        try {
            File namesFile = new File("src/data/assets/names.dat");
            File numPokeBalls = new File("src/data/assets/numPokeBalls.dat");
            pokeBalls = Integer.parseInt(readFile(numPokeBalls).get(0));
            while (pokeBalls > 0 && !isExit) {
                // Crear pokemon random
                randomPosition = r.nextInt(readFile(namesFile).size());
                String name = readFile(namesFile).get(randomPosition);
                Pokemon randomPokemon = new Pokemon(name);
                // LEER POKEMON ASCCI
                // Visualizar Pokemon
                System.out.println("¡Ha aparecido un " + name + " salvaje!\n");
                showPokemon(name); // Mostrar imagen
                System.out.println(randomPokemon.showData()); // Mostrar datos
                // Añadir Pokemon
                if (tryGetPokemon(randomPokemon)) {
                    pokeBag.catchPokemon(randomPokemon);
                    System.out.println(ConsoleColors.TEXT_BRIGHT_YELLOW + "★★ ¡Felicidades lo has capturado! ★★" + ConsoleColors.TEXT_RESET);
                    System.out.println("Se ha añadido " + name + " a tu mochila. ✔️");

                } else {
                    System.out.println("¡" + name + " se escapó!");
                }
                pokeBalls--;
                System.out.println("\nDisponible: ◓ " + pokeBalls + " Poké Ball(s)");

                if (pokeBalls > 0) {
                    isExit = askExit("➤ ¿Deseas seguir capturando Pokemons? s/n: ");
                }
            }

            if (pokeBalls <= 0) {
                System.out.println(ConsoleColors.TEXT_BRIGHT_RED
                        + "No te quedan Poké Balls ◓" + ConsoleColors.TEXT_RESET);
            }

            // Actualizar el numero de Poké Balls en el fichero
            createFile(numPokeBalls, String.valueOf(pokeBalls));

            System.out.println("Volviendo al menu principal... ⏱");

        } catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void showPokemon(String name) {
        File nameFile = new File("src/data/pokemons/" + name + ".pok");
        try {
            for (String line : readFile(nameFile)) {
                System.out.println(ConsoleColors.TEXT_BRIGHT_GREEN + line.replace("printf(\"", "").replace("\\n\");", "") + ConsoleColors.TEXT_RESET);
                Thread.sleep(80);
            }
            System.out.println();
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void displayAnimation(File file) {
        try {
            for (String linea : readFile(file)) {
                System.out.println(ConsoleColors.TEXT_BRIGHT_YELLOW + linea + ConsoleColors.TEXT_RESET);
                Thread.sleep(80);
            }
            System.out.println();
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private ArrayList<String> readFile(File file) throws FileNotFoundException {
        Scanner reader = new Scanner(file); // Apuntador al fichero de entrada
        ArrayList<String> data = new ArrayList<>();

        while (reader.hasNextLine()) {
            data.add(reader.nextLine()); // Leer fichero y añadirlo al array
        }
        reader.close(); // Cerrar apuntador
        return data;
    }

    private String userLogIn() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*******************      Log In      **********************");
        // Pedir datos
        System.out.print("➤ Usuario: ");
        String user = sc.nextLine();

        // Nombre de archivo a buscar
        String fileName = "user_" + user + ".dat";
        File userFile = new File("src/data/users/" + fileName);

        String password;
        do {
            System.out.print("➤ Contraseña: ");
            password = sc.nextLine();
            if (userFile.exists()) { // Comprobación si existe el usuario (fichero) // TODO None case sensitive
                if (passwordCheck(userFile, password)) { // Comprobación si la contraseña es correcta
                    System.out.println("Iniciando sesión... ⏱");
                    return user;
                } else {
                    System.out.println(ConsoleColors.TEXT_BRIGHT_RED + "Contraseña incorrecta. ❌" + ConsoleColors.TEXT_RESET);
                }
            } else {
                System.out.println("Usuario \"" + user + "\" no existe.");
                System.out.println("Creando nuevo usuario... ⏱");
                if (createFile(userFile, password)) { // Crear nuevo usuario (fichero)
                    System.out.println(ConsoleColors.TEXT_BRIGHT_GREEN + "Se creado el usuario correctamente. ✔️"
                            + ConsoleColors.TEXT_RESET);
                }
                return user;
            }
        } while (!passwordCheck(userFile, password) || createFile(userFile, password));
        // Mientras la contraseña sea incorrecta o se crea nuevo usuario
        return user;
    }

    private boolean createFile(File userFile, String data) {
        try {
            userFile.createNewFile(); // Crear fichero
            FileWriter writer = new FileWriter(userFile);

            writer.write(data); // Grabar
            writer.close(); // Cerrar apuntador
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean passwordCheck(File userFile, String password) {
        try {
            String data;
            data = readFile(userFile).get(0); // Leer fichero

            if (data.equals(password)) {
                return true;
            }

        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    private void addOptionsMenu() {
        mainMenu.add(new Option("Cazar Pokémons"));
        mainMenu.add(new Option("Mostrar Pokémons en mochila"));
        mainMenu.add(new Option("Transferir Pokémon"));
        mainMenu.add(new Option("Recibir Pokémon transferido"));
        mainMenu.add(new Option("Comprar Poké Balls"));
        mainMenu.add(new Option("Mostar usuarios que han jugado"));
        mainMenu.add(new Option("Abrir PokéDex"));
        mainMenu.add(new Option("Deshacerse de Pokemon"));
        mainMenu.add(new Option("Salir"));
    }

    private void displayPokeBag() {
        ArrayList<Pokemon> bag = pokeBag.getPokeBag();
        for (Pokemon pokemon : bag) {
            System.out.println(pokemon.showData());
        }
        System.out.println("TOTAL POKEMONS: " + bag.size());
    }

    private boolean tryGetPokemon(Pokemon randomPokemon) {
        Random r = new Random();
        Scanner sc = new Scanner(System.in);
        int number = (randomPokemon.getCP() / 10) + 1;
        //TODO BORRAR LOS NUM
        System.out.println("Number " + number);
        int numberRandom = r.nextInt(number) + 1;
        System.out.println("number random " + numberRandom);
        
        if (numberRandom != 1) {
            System.out.println("Adivina el numero del 1 al " + number + " para cazar a " + randomPokemon.getName());
            int number_user = sc.nextInt();
 
            if (number_user != numberRandom) { 
                return false;
            } 
        } 
        
        return true;
    }

    private void saveItems(String user) {
        int sizePokeBag;
        int sizePokeDex;

        try {
            sizePokeBag = pokeBag.savePokemonsIntoBag(user);
            sizePokeDex = pokeBag.savePokemonsIntoPokedex(user);
        } catch (FileNotFoundException ex) {
            System.out.println("Fichero no existente" + ex.getMessage());
            sizePokeBag = 0;
            sizePokeDex = 0;
        } catch (IOException ex) {
            System.out.println("Error de escritura" + ex.getMessage());
            sizePokeBag = 0;
            sizePokeDex = 0;
        }
        System.out.println("Numero de Pokémon en mochila: " + sizePokeBag);
        System.out.println("Numero de Pokémon registrados en PokéDex: " + sizePokeDex);
    }

    private void readItems(String user) {
        int num_items = 0;
        try {
            num_items = pokeBag.readPokeBag(user);
            System.out.println("Poke Bag: " + num_items);
            num_items = pokeBag.readPokeDex(user);
            System.out.println("Poke Dex:  " + num_items);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            System.out.println("Error lectura" + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PokemonGoJava.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void deletePokemon() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Dime el nombre del Pokemon a eliminar de la PokeBag:");
        String name = sc.nextLine();
        // TODO: Mostrar cual de todos
        System.out.println("Dime sus CombatPoints:");
        int CP = sc.nextInt();
        Pokemon delete = new Pokemon(name, CP);
        if (pokeBag.deletePokemon(delete)) {
            System.out.println("Pokemon eliminado con exito");
        } else {
            System.out.println("Pokemon no existe, no se ha eliminado");
        }
    }
}
