/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;
/**
 *
 * @author Administrator
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import proyecto.Juego;

public class TableroStratego extends JPanel{
    //Atributos
    Usuarios usuario;
    DatosUsuario datos;
    Login login;
    Menu menu;
    Juego juego;
    ArrayList<Personajes> ArregloHeroes = Personajes.getPeronajesHeroe();
    ArrayList<Personajes> ArregloVillanos = Personajes.getPersonajesVillanos();
    private ArrayList<Personajes> ArregloHeroesEliminados = new ArrayList<Personajes>();
    private ArrayList<Personajes> ArregloVillanosEliminados = new ArrayList<Personajes>(); 
    private JTextArea txtAreaEliminados;
    private JLabel Turnos;
    private boolean SeguirJugando = true;
    private boolean SeSeleccionoCasilla = false;
    private casillas casillaSeleccionada;
    private casillas[][] fichas;
    private boolean turnoHeroes = true;
    
    public static int contadorVictoriasHeroes = 0;
    public static int contadorVictoriasVillanos = 0;
    private int ConfirmarEsHeroe;
    private boolean HayGanador = false;
    public static boolean esHeroe;
    
    private Image tablero;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibujar la imagen de fondo
        g.drawImage(tablero, 0, 0, getWidth(), getHeight(), this);
    }
    
    public TableroStratego(DatosUsuario datos, Login login, JTextArea txtAreaEliminados, JLabel Turnos, Juego juego) {
        
        ImageIcon fondo = new ImageIcon("src/imagenes/tableroo.png");
        tablero = fondo.getImage();
        
        this.datos = datos;
        this.login = login;
        this.juego = juego;
        this.txtAreaEliminados = txtAreaEliminados;
        this.Turnos = Turnos;
        
        UIManager.put("OptionPane.yesButtonText", "Heroe");
        UIManager.put("OptionPane.noButtonText", "Villano");       
        ConfirmarEsHeroe = JOptionPane.showOptionDialog(null, datos.UsuarioLogeado + " elige tu bando", "Elegir Bando", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Heroe", "Villano"}, null);
        if (ConfirmarEsHeroe == JOptionPane.YES_OPTION) {
            Turnos.setText("Turno de: " + datos.UsuarioLogeado);
            esHeroe=true;
        }
        else if (ConfirmarEsHeroe == JOptionPane.NO_OPTION){
            Turnos.setText("Turno de: " + datos.SegundoUsuario);
            esHeroe=false;
            
        }
 
        UIManager.put("OptionPane.yesButtonText", "Yes");
        UIManager.put("OptionPane.noButtonText", "No");

        //Definir un grid de 10 x 10 para las fichas
        setLayout(new GridLayout(10, 10));

        //Creacion de Jlabels para las fichas en el Grid de 10 x 10, se definen los personajes con otro metodo
        fichas = new casillas[10][10];
        for (int filas = 0; filas < 10; filas++) {
            for (int columnas = 0; columnas < 10; columnas++) {
                casillas ficha = new casillas(filas, columnas, null);
                fichas[filas][columnas] = ficha;
                add(fichas[filas][columnas].label);  
            }
        }
        
        //Agregar evento para aceptar los clics del mouse en el tablero
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                int row = -1, column = -1;  //valores negativos en caso de que no se seleccione la casilla correcta

                //Si se selecciona una casilla con un personaje
                if (SeSeleccionoCasilla == false) { 
                    //Se obtienen las coordenadas de la casilla
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            // La casilla seleccionada es de la casilla actual
                            if (fichas[i][j].label == label) {                         
                                casillaSeleccionada = fichas[i][j];
                                ////Comprobar que la casilla contnga un personaje y sea el turno correcto
                                if (casillaSeleccionada.personajeActual != null && casillaSeleccionada.personajeActual.PersonajeHeroe == turnoHeroes) {
                                    SeSeleccionoCasilla = true;  
                                    if(login.esTutorial==true){
                                    ResaltarMovimientosValidos();
                                    }
                                    resaltarZonasProhibidas();
                                    break;
                                } else {
                                    casillaSeleccionada = null;
                                    SeSeleccionoCasilla = false;
                                    break;
                                }
                            }
                        }
                    }
                //si ya se selecciono una casilla y se quiere mover a otro puesto
                } else { 
                    for (int filas = 0; filas < 10; filas++)  {
                        for (int columnas = 0; columnas < 10; columnas++) {
                            if (fichas[filas][columnas].label == label) {
                                // Si se selecciona una  ficha del mismo bando se cambia a esa ficha 
                                if (fichas[filas][columnas].personajeActual != null) {
                                    if (fichas[filas][columnas].personajeActual.PersonajeHeroe == turnoHeroes) {
                                        // Actualizar casillas  
                                        borrarResaltadoMovimientos();
                                        casillaSeleccionada = fichas[filas][columnas];
                                        if(login.esTutorial==true){
                                        ResaltarMovimientosValidos();
                                        }
                                        resaltarZonasProhibidas();
                                        break;
                                    }
                                }
                                //si se comprobo que el movimiento es valido se mueve a esa casilla seleccionada
                                if (ComprobarMovimientoValido(filas, columnas) == true) {
                                    moverPersonaje(filas, columnas);
                                } else{
                                    JOptionPane.showMessageDialog(null, "Solo puedes moverte en donde las casillas estan verdes");
                                }
                            }
                            
                        }
                    } 
                }
            }
        };

        // Agregar el MouseListener a las casillas
        for (int filas = 0; filas < 10; filas++) {
            for (int columnas = 0; columnas < 10; columnas++) {
                fichas[filas][columnas].label.addMouseListener(mouseAdapter);
            }
        }

        //Posicionar las fichas, marcar las zonas prohibidas y esconder los personajes del bando contrario
        posicionarTodo();
        resaltarZonasProhibidas();
       // if(login.esTutorial==false){
        esconderPersonajesBandoOpuesto();
       // }
        setVisible(true);
    }
    
   
    
    private boolean ComprobarMovimientoValido(int fila, int columna) {
        int FilaActual = casillaSeleccionada.row;
        int ColumnaActual = casillaSeleccionada.column;

        //si se selecciona una bomba o la tierra no se pueden mover
        if (casillaSeleccionada.personajeActual.rangoPersonaje == 0) {
            return false;
        }
        //si se selecciona una ficha de rango 2 se podra mover mas de una casilla a la vez
        if (casillaSeleccionada.personajeActual.rangoPersonaje == 2) {
            //se verifica que el movimiento sea ortogonal
            boolean EsMovimientoOrtogonal = (fila == FilaActual && Math.abs(columna - ColumnaActual) > 0) ||
                    (columna == ColumnaActual && Math.abs(fila - FilaActual) > 0);
            // Verificar si la nueva posición está dentro de los espacios restringidos
            boolean EsZonaProhibida = (fila >= 4 && fila <= 5 && columna >= 2 && columna <= 3) ||
                    (fila >= 4 && fila <= 5 && columna >= 6 && columna <= 7);
            
            if (EsMovimientoOrtogonal == true && EsZonaProhibida == false) {
                //se revisa las casillas disponibles al lado izquierdo
                if (columna < ColumnaActual) { 
                    for (int i = ColumnaActual-1; i >= columna ;i--){
                        if (fichas[FilaActual][i].personajeActual != null) {
                            //revisar si la ficha de destino es del mismo bando o no
                            if (fichas[FilaActual][i].personajeActual.PersonajeHeroe != turnoHeroes) { 
                                //revisar si se puede atacar o se bloquea el camino
                                if (i == columna) {
                                    return true; 
                                }
                                else {
                                    return false; 
                                }
                            } 
                            
                            else {
                                return false;
                            }
                        } 
                        //verificar si es zona prohibida
                        else if ((FilaActual >= 4 && FilaActual <= 5) && ((i>=3 && i<=4) || (i>=6 && i<=7))) {
                            return false;
                        }
                    }
                    // Se hizo el recorrido sin encontrar ningun personaje en el camino
                    return true;
                } 
                //se revisa las casillas disponibles al lado izquierdo
                else if (columna > ColumnaActual) { 
                    for (int i = ColumnaActual+1; i <= columna;i++) {
                        if (fichas[FilaActual][i].personajeActual != null) {
                            if (fichas[FilaActual][i].personajeActual.PersonajeHeroe != turnoHeroes) {
                                if (i == columna) {
                                    return true;
                                }
                                else {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        } 
                        else if ((FilaActual >= 4 && FilaActual <= 5) && ((i>=3 && i<=4) || (i>=6 && i<=7))) {
                            return false;
                        }
                    }                    
                    return true;
                } 
                else if (fila < FilaActual) {
                    for (int i = FilaActual-1; i >= fila;i--){
                        if (fichas[i][ColumnaActual].personajeActual != null) {
                            if (fichas[i][ColumnaActual].personajeActual.PersonajeHeroe != turnoHeroes) {
                                if (i == fila) {
                                    return true;
                                }
                                else {
                                    return false;
                                }
                            } 
                            else {
                                return false;
                            }
                        } 
                        else if ((i >= 4 && i<=5) && ((ColumnaActual >= 2 && ColumnaActual <= 3) || (ColumnaActual >= 6 && ColumnaActual <= 7))) {
                            return false;
                        }
                    }
                    return true;
                } 
                else if (fila > FilaActual) {
                    for (int i = FilaActual+1; i <= fila;i++){
                        // Verificar si la nueva posición está dentro de los espacios restringidos
                        if (fichas[i][ColumnaActual].personajeActual != null) {
                            if (fichas[i][ColumnaActual].personajeActual.PersonajeHeroe != turnoHeroes) {
                                if (i == fila) {
                                    return true;
                                }
                                else {
                                    return false;
                                }
                            } 
                            else {
                                return false;
                            }
                            //Una casilla restringida bloquea el paso
                        }
                        else if ((i >= 4 && i<=5) && ((ColumnaActual >= 2 && ColumnaActual <= 3) || (ColumnaActual >= 6 && ColumnaActual <= 7))) {
                            return false;
                        }
                    }
                    return true;
                }
            } else return false;
        }
        
        if (casillaSeleccionada.personajeActual.rangoPersonaje != 2) {
            // Verificar si el movimiento es ortogonal
            boolean EsOrtogonal = (fila == FilaActual && Math.abs(columna - ColumnaActual) == 1) ||
                    (columna == ColumnaActual && Math.abs(fila - FilaActual) == 1);

            // Verificar si la nueva posición está dentro de los espacios restringidos
            boolean EsZonaProhibida = (fila >= 4 && fila <= 5 && columna >= 2 && columna <= 3) ||
                    (fila >= 4 && fila <= 5 && columna >= 6 && columna <= 7);

            //comprobar si un personaje esta en la casilla
            boolean FichaEstaOcupada = (fichas[fila][columna].personajeActual != null);

            //se verifica que sea del mismo bando el personaje
            if (FichaEstaOcupada == true) { 
                FichaEstaOcupada = (fichas[fila][columna].personajeActual.PersonajeHeroe == casillaSeleccionada.personajeActual.PersonajeHeroe);
            }
            
            // El movimiento es válido solo si es ortogonal y no está en un espacio restringido (zonas prohibidas) y no tiene otra ficha del mismo bando
            return (EsOrtogonal == true) && (EsZonaProhibida == false) && (FichaEstaOcupada == false);
        } 
        return false;
    }
    //Revisar todas las casilla y resaltar los movimientos validos
    private void ResaltarMovimientosValidos() {
        for (int filas = 0; filas < 10; filas++) {
            for (int columnas = 0; columnas <10 ; columnas++) {
                fichas[filas][columnas].highlightMove(ComprobarMovimientoValido(filas, columnas));
                fichas[filas][columnas].label.repaint();
            }
        }
        resaltarZonasProhibidas();
     }
    //se itera por todas las casillas y se revisa si es un personaje del bando contrario se esconde su informacion
    private void esconderPersonajesBandoOpuesto() {
        for (int filas = 0; filas < 10; filas++) {
            for (int columnas = 0; columnas < 10; columnas++) {
                if (fichas[filas][columnas].personajeActual != null) {
                    fichas[filas][columnas].esconderCasilla(turnoHeroes != fichas[filas][columnas].personajeActual.PersonajeHeroe);
                } 
            }
        }
    }
    //se resalta con un color diferente las zonas prohibidas
    public void resaltarZonasProhibidas() {
        for (int filas = 4; filas <= 5; filas++) {
            for (int columnas = 2; columnas <= 3; columnas++) {
                fichas[filas][columnas].label.setOpaque(false);
                fichas[filas][columnas].label.repaint();
                
            }
            for (int columnas = 6; columnas <= 7; columnas++) {
                fichas[filas][columnas].label.setOpaque(false);
                fichas[filas][columnas].label.repaint();
            }
        }
    }
    //se mueven los personajes
    private void moverPersonaje(int filanueva, int columananueva) {
        String TurnoDeUsuario = Turnos.getText();
    // Se verifica que haya un personaje para empezar el combate
    if (fichas[filanueva][columananueva].personajeActual != null) {
        Personajes ganador = EmpezarBatalla(casillaSeleccionada.personajeActual, fichas[filanueva][columananueva].personajeActual);
       String mensajeBatalla = "" +casillaSeleccionada.personajeActual.toString() + " vs " + fichas[filanueva][columananueva].personajeActual.toString() + "\nGanador: " + (ganador != null ? ganador.toString() : "Empate"+"    ");
        JOptionPane.showMessageDialog(null, mensajeBatalla);

        // Se eliminan las dos fichas si eran del mismo rango
        if (ganador == null) {
            casillaSeleccionada.setPersonaje(null);
            fichas[filanueva][columananueva].setPersonaje(null);
        } else if (casillaSeleccionada.personajeActual == ganador) {                
            casillaSeleccionada.setPersonaje(null);
            fichas[filanueva][columananueva].setPersonaje(ganador);                
        } else {
            casillaSeleccionada.setPersonaje(null);
        }
        CambiarTurno();
        
            
        if (HayGanador) {
            if (!turnoHeroes) {
                if (TurnoDeUsuario.substring(10).equals(datos.UsuarioLogeado)) {
                    JOptionPane.showMessageDialog(null, TurnoDeUsuario.substring(10) + 
                            " usando los heroes ha salvado la tierra! Venciendo a " + datos.SegundoUsuario +  
                            ". Presiona OK para volver al menú principal");
                    
                     String FECHA=String.valueOf(datos.getfECHA());
              String EstadoVictoria="Victoria";
              datos.agregarFecha(FECHA, datos.UsuarioLogeado, datos.SegundoUsuario, EstadoVictoria);
                }
                else {
                    JOptionPane.showMessageDialog(null, TurnoDeUsuario.substring(10) + 
                            " usando los heroes ha salvado la tierra! Venciendo a " + datos.UsuarioLogeado +  
                            ". Presiona OK para volver al menú principal");
                    String FECHA=String.valueOf(datos.getfECHA());
              String EstadoVictoria="Derrota";
              datos.agregarFecha(FECHA, datos.UsuarioLogeado, datos.SegundoUsuario, EstadoVictoria);
                    
                }                    
            }
            else {
                if (TurnoDeUsuario.substring(10).equals(datos.UsuarioLogeado)) {
                    JOptionPane.showMessageDialog(null, TurnoDeUsuario.substring(10) + 
                            " usando los villanos ha capturado la tierra! Venciendo a " + datos.SegundoUsuario +  
                            ". Presiona OK para volver al menú principal");
                     String FECHA=String.valueOf(datos.getfECHA());
              String EstadoVictoria="Victoria";
              datos.agregarFecha(FECHA, datos.UsuarioLogeado, datos.SegundoUsuario, EstadoVictoria);
                }
                else {
                    JOptionPane.showMessageDialog(null, TurnoDeUsuario.substring(10) + 
                            " usando los villanos ha capturado la tierra! Venciendo a " + datos.UsuarioLogeado +  
                            ". Presiona OK para volver al menú principal");
                    String FECHA=String.valueOf(datos.getfECHA());
              String EstadoVictoria="Derrota";
              datos.agregarFecha(FECHA, datos.UsuarioLogeado, datos.SegundoUsuario, EstadoVictoria);
                }
            }
            Usuarios usuarioGanador = datos.buscarUsuario(TurnoDeUsuario.substring(10));
            if (usuarioGanador != null) {
                usuarioGanador.IncrementarPuntos(3);
                 if (!turnoHeroes) {  
                contadorVictoriasHeroes++;
            } else {
               contadorVictoriasVillanos++;
            }
            }
            menu = new Menu(login, datos);
            menu.setVisible(true);
            juego.dispose();
        }
        
        return;
    }

    // No había un personaje en esa casilla, así que solo se actualiza la posición
    Personajes personaje = casillaSeleccionada.personajeActual;
    casillaSeleccionada.setPersonaje(null);
    borrarResaltadoMovimientos();

    // Mover la imagen a la nueva posición
    fichas[filanueva][columananueva].setPersonaje(personaje);        
    CambiarTurno();
}

// Se verifican los rangos de las fichas y gana quien tenga mayor rango (con excepciones)
public Personajes EmpezarBatalla(Personajes atacante, Personajes defensor) {
    // Solo los que tienen rango 3 pueden eliminar las bombas
    if (atacante.rangoPersonaje == 3 && defensor.rangoPersonaje == 0) {
        if (defensor.PersonajeHeroe) {
            ArregloHeroesEliminados.add(defensor);
        } else {
            ArregloVillanosEliminados.add(defensor);
        }
        return atacante;
    } else if (atacante.rangoPersonaje != 3 && defensor.rangoPersonaje == 0) {
        if (atacante.PersonajeHeroe) {
            ArregloHeroesEliminados.add(atacante);
        } else {
            ArregloVillanosEliminados.add(atacante);
        }
        return defensor;
    } else if (atacante.rangoPersonaje == 1 && (defensor.rangoPersonaje == 10 || defensor.rangoPersonaje == -1)) {
        if (defensor.PersonajeHeroe) {
            ArregloHeroesEliminados.add(defensor);
        } else {
            ArregloVillanosEliminados.add(defensor);
        }

        if (defensor.rangoPersonaje == -1) {
            SeguirJugando = false;
            HayGanador = true;
        }
        return atacante;
    } else if (atacante.rangoPersonaje > defensor.rangoPersonaje && defensor.rangoPersonaje != 0) {
        if (defensor.PersonajeHeroe) {
            ArregloHeroesEliminados.add(defensor);
            if (defensor.rangoPersonaje == -1) {
                SeguirJugando = false;
                HayGanador = true;
            }  
        } else {
            ArregloVillanosEliminados.add(defensor);
            if (defensor.rangoPersonaje == -1) {
                SeguirJugando = false;
                HayGanador = true;
            }
        }
        return atacante;
    } else if (atacante.rangoPersonaje < defensor.rangoPersonaje) {
        if (atacante.PersonajeHeroe) {
            ArregloHeroesEliminados.add(atacante);
        } else {
            ArregloVillanosEliminados.add(atacante);
        }
        return defensor;
    } else if (atacante.rangoPersonaje == defensor.rangoPersonaje) {
        if (atacante.PersonajeHeroe) {
            ArregloHeroesEliminados.add(atacante);
            ArregloVillanosEliminados.add(defensor);
        } else {
            ArregloVillanosEliminados.add(atacante);
            ArregloHeroesEliminados.add(defensor);
        }
        return null; // Retornar un objeto "empate" en lugar de null
    } else {
        return null;
    }
}
   
    public void CambiarTurno() {
        int confirmacion;      
        casillaSeleccionada = null;
        SeSeleccionoCasilla = false;
        turnoHeroes = !turnoHeroes;
        setVisible(false);
    //    if(login.esTutorial==false){
        esconderPersonajesBandoOpuesto();
      //  }
        txtAreaEliminados.setText("");
        
        if (SeguirJugando == true) {
            if (turnoHeroes == true){
                if (ConfirmarEsHeroe == JOptionPane.YES_OPTION) {
                    Turnos.setText("Turno de: " + datos.UsuarioLogeado);
                    JOptionPane.showMessageDialog(null, "Turno de: " + datos.UsuarioLogeado);
                }
                else {
                    Turnos.setText("Turno de: " + datos.SegundoUsuario);
                    JOptionPane.showMessageDialog(null, "Turno de: " + datos.SegundoUsuario);
                }
            }
            else {
                if (ConfirmarEsHeroe == JOptionPane.NO_OPTION) {
                    Turnos.setText("Turno de: " + datos.UsuarioLogeado);
                    JOptionPane.showMessageDialog(null, "Turno de: " + datos.UsuarioLogeado);
                }
                else {
                    Turnos.setText("Turno de: " + datos.SegundoUsuario);
                    JOptionPane.showMessageDialog(null, "Turno de: " + datos.SegundoUsuario);
                }
            }   
        }
        
        borrarResaltadoMovimientos();
        resaltarZonasProhibidas();
        setVisible(true);
        mostrarPersonajesEliminados();        
    }
    
    public void borrarResaltadoMovimientos() {
        for (int i = 0; i<10;i++) {
            for (int j = 0; j<10;j++){
                fichas[i][j].label.repaint();
            }
        }
        resaltarZonasProhibidas();
    }    
    
    public void mostrarPersonajesEliminados() {
        String mensaje = "";
        
        if (turnoHeroes) {
            for (int i = 0; i<ArregloHeroesEliminados.toArray().length;i++) {
                Personajes personaje = ArregloHeroesEliminados.get(i);
                mensaje += personaje.nombrePersonaje + 
                        " (Rango " + personaje.rangoPersonaje + ")\n";
            }
        } else {
            for (int i = 0; i<ArregloVillanosEliminados.toArray().length;i++) {
                Personajes personaje = ArregloVillanosEliminados.get(i);
                mensaje += personaje.nombrePersonaje + 
                        " (Rango " + personaje.rangoPersonaje + ")\n";
            }
        }
        txtAreaEliminados.setText(mensaje);
    }

    public void posicionarPersonajesRango2() {
        
        // Iterar por toda la lista de heroes disponisbles
        
        Random random = new Random();
        // PERSONAJES RANGO 2 HEROES
        for (int i =0;i<ArregloHeroes.toArray().length;i++) {
            Personajes personajeActual = ArregloHeroes.get(i);
            int columnaAleatoria;
            int filaAleatoria;
            
            if (personajeActual.rangoPersonaje == 2) {
                // Elegir entre fila 6 y 7
                
                int filas[] = new int[2];
                filas[0] = 6;
                filas[1] = 7;
                filaAleatoria = filas[random.nextInt(0,2)];

                // Elegir columna aleatoria hasta que este libre ese espacio
                do {
                    columnaAleatoria = random.nextInt(0, 10);
                } while(fichas[filaAleatoria][columnaAleatoria].personajeActual != null);
                
                fichas[filaAleatoria][columnaAleatoria].setPersonaje(personajeActual);
                personajeActual.posicionado = true;
            }
        }
        
        for (int i =0;i<ArregloVillanos.toArray().length;i++) {
            Personajes personajeActual = ArregloVillanos.get(i);
            int columnaAleatoria;
            int filaAleatoria;
            
            if (personajeActual.rangoPersonaje == 2) {
                // Elegir entre fila 6 y 7
                
                int filas[] = new int[2];
                filas[0] = 2;
                filas[1] = 3;
                filaAleatoria = filas[random.nextInt(0,2)];

                // Elegir columna aleatoria hasta que este libre ese espacio
                do {
                    columnaAleatoria = random.nextInt(0, 10);
                } while(fichas[filaAleatoria][columnaAleatoria].personajeActual != null);
                
                fichas[filaAleatoria][columnaAleatoria].setPersonaje(personajeActual);
                personajeActual.posicionado = true;
            }
        }
        
    }
    
    public void posicionarTierras() {
        Random random = new Random();
        
        // TIERRA HEROES
        for (int i =0;i<ArregloHeroes.toArray().length;i++){
            
            Personajes personajeActual= ArregloHeroes.get(i);
            if (personajeActual.posicionado) continue;
            
            int columnaAleatoria;
            
            // Posicionar en la ultima fila de manera aleatoria la ficha de tipo tierra.
            if (personajeActual.rangoPersonaje == -1) {
                // Generar una columna aleatoria hasta que no este en los bordes del tablero
                do {
                    columnaAleatoria = random.nextInt(0, 10);
                } while (columnaAleatoria == 0 || columnaAleatoria == 9);
                
                fichas[9][columnaAleatoria].setPersonaje(personajeActual);
                
                // Agregas las bombas alrededor de la tierra
                Personajes bomba1 = new Personajes("Nova Blast", 0, true,null);
                Personajes bomba2 = new Personajes("Nova Blast", 0, true,null);
                Personajes bomba3 = new Personajes("Nova Blast", 0, true,null);
                
                bomba1.posicionado = true;
                bomba2.posicionado = true;
                bomba3.posicionado = true;

                fichas[8][columnaAleatoria].setPersonaje(bomba1);
                fichas[9][columnaAleatoria-1].setPersonaje(bomba2);
                fichas[9][columnaAleatoria+1].setPersonaje(bomba3);
                
                personajeActual.posicionado = true;
            }
        }
        
        // TIERRA VILLANOS
        
        for (int i =0; i<ArregloVillanos.toArray().length;i++){
            Personajes personajeActual = ArregloVillanos.get(i);
            
            if (personajeActual.posicionado) continue;
            
            int columnaAleatoria;
            
            // Posicionar en la ultima fila de manera aleatoria la ficha de tipo tierra.
            if (personajeActual.rangoPersonaje == -1) {
                // Generar una columna aleatoria hasta que no este en los bordes del tablero
                do {
                    columnaAleatoria = random.nextInt(0, 10);
                } while (columnaAleatoria == 0 || columnaAleatoria == 9);
                
                fichas[0][columnaAleatoria].setPersonaje(personajeActual);
                
                // Agregas las bombas alrededor de la tierra
                Personajes bomba1 = new Personajes("Pumpkin Bomb", 0, false,null);
                Personajes bomba2 = new Personajes("Pumpkin Bomb", 0, false,null);
                Personajes bomba3 = new Personajes("Pumpkin Bomb", 0, false,null);
                
                bomba1.posicionado = true;
                bomba2.posicionado = true;
                bomba3.posicionado = true;

                fichas[1][columnaAleatoria].setPersonaje(bomba1);
                fichas[0][columnaAleatoria-1].setPersonaje(bomba2);
                fichas[0][columnaAleatoria+1].setPersonaje(bomba3);
                
                personajeActual.posicionado = true;
            }
        }
    }
    
    public void posicionarBombas() {
        
        // Iterar por toda la lista de heroes disponisbles
        
        // BOMBAS HEROES
        Random random = new Random();
        for (int i =0;i<ArregloHeroes.toArray().length;i++) {
            Personajes personajeActual = ArregloHeroes.get(i);
            
            if (personajeActual.posicionado) continue;
            
            int columnaAleatoria;
            int filaAleatoria;
            
            if (personajeActual.rangoPersonaje == 0) {
                
                // Elegir una fila entre la 8 y 9
                int filas[] = new int[2];
                filas[0] = 8;
                filas[1] = 9;
                filaAleatoria = filas[random.nextInt(0,2)];
                
                // Generar una columna nueva hasta que esa casilla no tenga un personaje adentro
                do {
                    columnaAleatoria = random.nextInt(0, 10);
                } while(fichas[filaAleatoria][columnaAleatoria].personajeActual != null);
                
                fichas[filaAleatoria][columnaAleatoria].setPersonaje(personajeActual);
                personajeActual.posicionado = true;
            }
        }
        
        // BOMBAS VILLANOS
        for (int i =0;i<ArregloVillanos.toArray().length;i++) {
            Personajes personajeActual = ArregloVillanos.get(i);
            
            if (personajeActual.posicionado) continue;
            int columnaAleatoria;
            int filaAleatoria;
            
            if (personajeActual.rangoPersonaje == 0) {
                
                // Elegir una fila entre la 8 y 9
                int filas[] = new int[2];
                filas[0] = 0;
                filas[1] = 1;
                filaAleatoria = filas[random.nextInt(0,2)];
                
                // Generar una columna nueva hasta que esa casilla no tenga un personaje adentro
                do {
                    columnaAleatoria = random.nextInt(0, 10);
                } while(fichas[filaAleatoria][columnaAleatoria].personajeActual != null);
                
                fichas[filaAleatoria][columnaAleatoria].setPersonaje(personajeActual);
                personajeActual.posicionado = true;
            }
        }
    }
    
    public void posicionarPersonajesRestantes() {
        // Generar una ficha aleatoria y una columna aleatoria
        repaint();
        boolean placed = false;
        
        // Restantes HEROES
        for (int i = 0; i<ArregloHeroes.toArray().length;i++) {
            
            Personajes personajeActual = ArregloHeroes.get(i);
        
            for (int fila = 6;fila<10;fila++){
                for (int columna = 0; columna < 10; columna++){
                    if (!personajeActual.posicionado) {
                        placed = false;
                        if (fichas[fila][columna].personajeActual == null) {
                            fichas[fila][columna].setPersonaje(personajeActual);
                            
                        
                            personajeActual.posicionado = true;
                            placed = true;
                            break;
                        }
                    }
                }
                
                if (placed) break;
            }
        } 
        
        // Restantes VILLANOS
        for (int i = 0; i<ArregloVillanos.toArray().length;i++) {
            
            Personajes personajeActual = ArregloVillanos.get(i);
        
            for (int fila = 0;fila<4;fila++){
                for (int columna = 0; columna < 10; columna++){
                    if (!personajeActual.posicionado) {
                        placed = false;
                        if (fichas[fila][columna].personajeActual == null) {
                           fichas[fila][columna].setPersonaje(personajeActual);
                        
                            personajeActual.posicionado = true;
                            placed = true;
                            break;
                        }
                    }
                }
                
                if (placed) break;
            }
        } 
    }

    public void posicionarTodo() {
        posicionarTierras();
        posicionarBombas();
        posicionarPersonajesRango2();
        posicionarPersonajesRestantes();       
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TableroStratego(null, null, null, null, null);
                
            }
        });
    }
    
}

