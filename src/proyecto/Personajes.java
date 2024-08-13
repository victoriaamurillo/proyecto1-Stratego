/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
/**
 *
 * @author Administrator
 */
public class Personajes {    
    private int currentRow;
    private int currentColumn;
    String nombrePersonaje;
    int rangoPersonaje;
    boolean PersonajeHeroe;
    boolean posicionado = false;
    ImageIcon icono;
    ImageIcon iconoEscondido;
    
    //Contstructor
    public Personajes(String nombrePersonaje,int rangoPersonaje,boolean PersonajeHeroe, String path){
        this.nombrePersonaje=nombrePersonaje;
        this.rangoPersonaje=rangoPersonaje;
        this.PersonajeHeroe=PersonajeHeroe;
        
        try {
           Image resizedImg = resizeImage(ImageIO.read(new File("src/imagenes/oculto.png")), 85, 85);
           iconoEscondido = new ImageIcon(resizedImg);
        } catch (Exception e) {
            iconoEscondido = null;
        }
        
        
        try {
            Image newImg = resizeImage(ImageIO.read(new File(path)), 55, 55);
            icono = new ImageIcon(newImg);
        } catch (Exception e) {
            icono = null;
        }
       
        loadIcon();
    }
    
    private void loadIcon() {
        String filename;
        if (nombrePersonaje.equals("Planet Earth")) {
            if (PersonajeHeroe)
                filename = "src/imagenes/Heroes/Planet-Earth.PNG";
            else
                filename = "src/imagenes.Villanos/Planet-Earth.PNG";  
        }
        else if (!PersonajeHeroe){
            filename = "src/imagenes.Villanos/" + nombrePersonaje.replace(" ", "") + ".png";
        }
        else {
            filename = "src/imagenes/Heroes/" + nombrePersonaje.replace(" ", "") + ".png";
        }
            
        
        try {
            
            Image newImg = resizeImage(ImageIO.read(new File(filename)), 80, 72);
            icono = new ImageIcon(newImg);
        } catch (Exception e) {
            icono = null;
        }
    }
    
    private Image resizeImage(Image img, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        
        
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
    
    public static ArrayList<Personajes> getPeronajesHeroe(){
        ArrayList<Personajes> personajes=new ArrayList<Personajes>();
        personajes.add(new Personajes("Mr. Fantastic", 10, true, null));
        personajes.add(new Personajes("Captain America", 9, true, null));
        personajes.add(new Personajes("Professor X", 8, true, null));

        personajes.add(new Personajes("Nick Fury", 8, true, null));
        personajes.add(new Personajes("Spider-Man", 7, true, null));
        personajes.add(new Personajes("Wolverine", 7, true, null));
        personajes.add(new Personajes("Namor", 7, true, null));
        personajes.add(new Personajes("Daredevil", 6, true, null));
        personajes.add(new Personajes("Silver Surfer", 6, true, null));
        personajes.add(new Personajes("Hulk", 6, true, null));
        personajes.add(new Personajes("Iron Man", 6, true, null));
        personajes.add(new Personajes("Thor", 5, true, null));
        personajes.add(new Personajes("Human Torch", 5, true, null));
        personajes.add(new Personajes("Cyclops", 5, true, null));
        personajes.add(new Personajes("Invisible Woman", 5, true, null));
        personajes.add(new Personajes("Ghost Rider", 4, true, null));
        personajes.add(new Personajes("Punisher", 4, true, null));
        personajes.add(new Personajes("Blade", 4, true, null));
        personajes.add(new Personajes("Thing", 4, true, null));
        personajes.add(new Personajes("Emma Frost", 3, true, null));
        personajes.add(new Personajes("She-Hulk", 3, true, null));
        personajes.add(new Personajes("Giant Man", 3, true, null));
        personajes.add(new Personajes("Beast", 3, true, null));
        personajes.add(new Personajes("Colossus", 3, true, null));
        personajes.add(new Personajes("Gambit", 2, true, null));
        personajes.add(new Personajes("Spider-Girl", 2, true, null));
        personajes.add(new Personajes("Ice Man", 2, true, null));
        personajes.add(new Personajes("Storm", 2, true, null));
        personajes.add(new Personajes("Phoenix", 2, true, null));
        personajes.add(new Personajes("Dr. Strange", 2, true, null));
        personajes.add(new Personajes("Elektra", 2, true, null));
        personajes.add(new Personajes("Nightcrawler", 2, true, null));
        personajes.add(new Personajes("Black Widow", 1, true, null));
        
        personajes.add(new Personajes("Nova Blast", 0, true,null));
        personajes.add(new Personajes("Nova Blast", 0, true,null));
        personajes.add(new Personajes("Nova Blast", 0, true,null));
        
        personajes.add(new Personajes("Planet Earth", -1, true,null));
        
        return personajes;
    }
    
    public static ArrayList<Personajes>getPersonajesVillanos(){
        ArrayList<Personajes> personajes=new ArrayList<Personajes>();
        
        //Rango 10
        personajes.add(new Personajes("Dr. Doom", 10, false,null));
        //Rango 9
        personajes.add(new Personajes("Galactus", 9, false,null));
        //Rango 8 
        personajes.add(new Personajes("Kingpin", 8, false,null));
        personajes.add(new Personajes("Magneto", 8, false,null));
        //Rango 7
        personajes.add(new Personajes("Apocalypse", 7, false,null));
        personajes.add(new Personajes("Green Goblin", 7, false,null));
        personajes.add(new Personajes("Venom", 7, false,null));
        //Rango 6
        personajes.add(new Personajes("Bullseye", 6, false,null));
        personajes.add(new Personajes("Omega Red", 6, false,null));
        personajes.add(new Personajes("Onslaught", 6, false,null));
        personajes.add(new Personajes("Red Skull", 6, false,null));
       //Rango 5
        personajes.add(new Personajes("Mystique", 5, false,null));
        personajes.add(new Personajes("Mysterio", 5, false,null));
        personajes.add(new Personajes("Dr. Octopus", 5, false,null));
        personajes.add(new Personajes("Deadpool", 5, false,null));
        //Rango 4
        personajes.add(new Personajes("Abomination", 4, false,null));
        personajes.add(new Personajes("Thanos", 4, false,null));
        personajes.add(new Personajes("Black Cat", 4, false,null));
        personajes.add(new Personajes("Sabretooth", 4, false,null));
        //Rango 3
        personajes.add(new Personajes("Juggernaut", 3, false,null));
        personajes.add(new Personajes("Rhino", 3, false,null));
        personajes.add(new Personajes("Carnage", 3, false,null));
        personajes.add(new Personajes("Mole Man", 3, false,null));
        personajes.add(new Personajes("Lizard", 3, false,null));
        //Rango 2
        personajes.add(new Personajes("Mr. Sinister", 2, false,null));
        personajes.add(new Personajes("Sentinel 1", 2, false,null));
        personajes.add(new Personajes("Ultron", 2, false,null));
        personajes.add(new Personajes("Sandman", 2, false,null));
        personajes.add(new Personajes("Leader", 2, false,null));
        personajes.add(new Personajes("Viper", 2, false,null));
        personajes.add(new Personajes("Sentinel 2", 2, false,null));
        personajes.add(new Personajes("Electro", 2, false,null));
        //Rango 1
        personajes.add(new Personajes("Black Widow", 1, false,null));
        
        personajes.add(new Personajes("Pumpkin Bomb", 0, false,null));
        personajes.add(new Personajes("Pumpkin Bomb", 0, false,null));
        personajes.add(new Personajes("Pumpkin Bomb", 0, false,null));
        
        personajes.add(new Personajes("Planet Earth", -1, false,null));
        
        return personajes;
    }
    
    public String toString() {
    String nombreSinEtiquetas = nombrePersonaje.replaceAll("\\<.*?\\>", "");
    return nombreSinEtiquetas ;
}
    
   
    
}
