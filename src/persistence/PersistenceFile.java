package persistence;

import objects.Pokemon;

import java.io.*;
import java.util.ArrayList;


public class PersistenceFile {

    public static boolean saveAllItems(ArrayList<Pokemon> pokeBag, String user) throws FileNotFoundException, IOException {
        ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream("src/data/bags/" + user + "_mochila.dat"));
        save.writeObject(pokeBag);
        save.close();

        return true;
    }
    

    public static ArrayList<Pokemon> readAllItems(ArrayList<Pokemon> pokeBag, String user) throws FileNotFoundException, IOException, ClassNotFoundException {

        ObjectInputStream read = new ObjectInputStream(new FileInputStream("src/data/bags/" + user + "_mochila.dat"));
        ArrayList<Pokemon> bag = (ArrayList< Pokemon>) read.readObject();
        read.close();
        
        return bag;

    }
}
