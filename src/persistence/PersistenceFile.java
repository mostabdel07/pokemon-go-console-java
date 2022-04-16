package persistence;

import objects.Pokemon;

import java.io.*;
import java.util.ArrayList;

public class PersistenceFile {
    private final static String pokedex = "pokedex.pok";

    public static boolean saveAllItems(ArrayList<Pokemon> pokedex) throws FileNotFoundException, IOException
    {
        FileOutputStream escritura = null;
        escritura = new FileOutputStream(PersistenceFile.pokedex);
        ObjectOutputStream StreamDatos = new ObjectOutputStream(escritura);

        StreamDatos.writeObject(pokedex);

        return true;
    }

    public static ArrayList<Pokemon> readAllItems(ArrayList<Pokemon> pokedex) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        FileInputStream fichero= new FileInputStream(PersistenceFile.pokedex);
        ObjectInputStream StreamDatos = new ObjectInputStream(fichero);

        ArrayList<Pokemon> read = (ArrayList < Pokemon >) StreamDatos.readObject();

        return read;
    }
}
