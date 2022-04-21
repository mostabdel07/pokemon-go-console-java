package persistence;

import objects.Pokemon;

import java.io.*;
import java.util.ArrayList;


public class PersistenceFile {

    public static boolean saveAllItems(ArrayList<Pokemon> arrayList, FileOutputStream file) throws IOException {
        ObjectOutputStream save = new ObjectOutputStream(file);
        save.writeObject(arrayList);
        save.close();

        return true;
    }

    public static ArrayList<Pokemon> readAllItems(FileInputStream file) throws IOException, ClassNotFoundException {

        ObjectInputStream read = new ObjectInputStream(file);
        ArrayList<Pokemon> bag = (ArrayList< Pokemon>) read.readObject();
        read.close();
        
        return bag;

    }

    public static boolean saveOneItem(Pokemon pokemon, FileOutputStream file) throws IOException {
        ObjectOutputStream save = new ObjectOutputStream(file);
        save.writeObject(pokemon);
        save.close();
        return true;
    }
    
    public static Pokemon readOneItem(String user) throws IOException, ClassNotFoundException{
        File fichero= new File("src/data/assets/transfer_" + user+ ".dat");
        if(fichero.exists()){
        ObjectInputStream read= new ObjectInputStream(new FileInputStream(fichero));
        Pokemon readPokemon= (Pokemon)read.readObject();
        fichero.delete();
        return readPokemon;
        }else{
            return null;
        }
        
        
        
    }
    
}
