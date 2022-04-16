/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilities;


import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author alumne
 */
public class Menu {
    private String Title;
    private ArrayList<Option> options;

    public Menu(String Title) {
        this.Title = Title;
        options = new ArrayList<Option>(); //para inicializar arrayList
    }

    //añadir opcion menú
    public void add(Option nueva)
    {
        //aqui podria poner validaciones antes de añadirla
        options.add(nueva);
    }
    
    //removes
    public boolean removes(Option opcion)
    {
        return options.remove(opcion);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("------" + this.getTitle() + "-------");
        sb.append("\n");
            for (Option option : options) {
                sb.append(option.toString());
            }
            
           // Iterator it = options.iterator();
            
        return sb.toString();
        }
        
        
    
    //showMenu()
    public void showMenu()
    {
        //Iterator it = options.iterator();
        System.out.println("\n----" + this.Title + "-----");
        for (int i = 0; i < options.size(); i++) {
            if(i == options.size()-1) { // Hace que la ultima opción sea (0) salir
                System.out.format("%s ◓ %s \n",0,options.get(i).toString());
            }else { // Hace que las opciones comiencen desde 1
                System.out.format("%d ◓ %s \n",i+1,options.get(i).toString());
            }
            //format muy similar al printf
            //System.out.println(options.get(i).toString());
        }
        System.out.println();
    }
    
    
    public int choose() throws InputMismatchException
    {
        System.out.print("➤ Selecciona una opción: ");
        Scanner sc = new Scanner(System.in);
        int opt;
        //do{
            opt = sc.nextInt();
        //}while (opt<0 || opt > options.size());
        System.out.println();
        return opt;
    }
    
    
    
    public Option removes(int index)
    {
        return options.remove(index);
    }
    
    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }
    
    
    
}
