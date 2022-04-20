package persistence;

import objects.Pokemon;

import java.io.*;
import java.util.ArrayList;


public class PersistenceFile {

    public static boolean saveAllItems(ArrayList<Pokemon> arrayList, FileOutputStream file) throws FileNotFoundException, IOException {
        ObjectOutputStream save = new ObjectOutputStream(file);
        save.writeObject(arrayList);
        save.close();

        return true;
    }

    public static ArrayList<Pokemon> readAllItems(ArrayList<Pokemon> pokeBag, FileInputStream file) throws FileNotFoundException, IOException, ClassNotFoundException {

        ObjectInputStream read = new ObjectInputStream(file);
        ArrayList<Pokemon> bag = (ArrayList< Pokemon>) read.readObject();
        read.close();
        
        return bag;

    }

    public static boolean saveOneItem(Pokemon pokemon, FileOutputStream file) throws FileNotFoundException, IOException {
        ObjectOutputStream save = new ObjectOutputStream(file);
        save.writeObject(pokemon);
        save.close();
        return true;
    }

}
