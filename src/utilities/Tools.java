package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Tools {

    /**
     * Crear o modifica ficheros con los datos y en la ruta que se le pasa por parámetros.
     * @param userFile
     * @param data
     * @return boleano true si se ha creado corrrectamente y false en caso negativo.
     */
    public static boolean createFile(File userFile, String data) {
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


    /**
     * Lee un fichero el cuál se entrega su ruta por parámetro.
     * @param file
     * @return ArrayList con los datos leídos del fichero.
     * @throws FileNotFoundException
     */
    public static ArrayList<String> readFile(File file) throws FileNotFoundException {
        Scanner reader = new Scanner(file); // Apuntador al fichero de entrada
        ArrayList<String> data = new ArrayList<>();

        while (reader.hasNextLine()) {
            data.add(reader.nextLine()); // Leer fichero y añadirlo al array
        }
        reader.close(); // Cerrar apuntador
        return data;
    }


    /**
     * Muestra los datos por pantalla de forma animada, leidos de un
     * fichero el cual se pasa por parámetro.
     * @param file
     */
    public static void displayAnimation(File file) {
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


    /**
     * Muestra una animación que simula una barra de carga.
     */
    public static void displayLoading() {
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


    /**
     * Hace una comprobación de la contraseña si coincide con los datos que
     * hay en el fichero usuario pasados como parámetros.
     * @param userFile
     * @param password
     * @return boleano true si se ha creado corrrectamente y false en caso negativo.
     */
    public static boolean passwordCheck(File userFile, String password) {
        try {
            String data;
            data = Tools.readFile(userFile).get(0); // Leer fichero

            if (data.equals(password)) {
                return true;
            }

        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }


    /**
     * Sistema de logeo el cuál pide los datos por pantalla y si no existe el usuario
     * lo crea y guarda los datos en un fichero.
     * @return nombre de usuario creado.
     */
    public static String userLogIn() {
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
                if (Tools.createFile(userFile, password)) { // Crear nuevo usuario (fichero)
                    System.out.println(ConsoleColors.TEXT_BRIGHT_GREEN + "Se creado el usuario correctamente. ✔️"
                            + ConsoleColors.TEXT_RESET);
                }
                return user;
            }
        } while (!passwordCheck(userFile, password) || Tools.createFile(userFile, password));
        // Mientras la contraseña sea incorrecta o se crea nuevo usuario
        return user;
    }


}
