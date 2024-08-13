/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class DatosUsuario {

    ArrayList<Usuarios> ListaUsuarios;
    private ArrayList<Usuarios> ListaUsuariosEliminados;

    public String UsuarioLogeado;
    public String SegundoUsuario;

    String[] fechas;

    public DatosUsuario() {
        ListaUsuarios = new ArrayList<>();
        ListaUsuariosEliminados = new ArrayList<>();
        this.fechas = new String[0];
    }

    //Buscar Usuarios si existentes
    public Usuarios buscarUsuario(String username) {

        for (int i = 0; i < ListaUsuarios.size(); i++) {
            Usuarios usuario = ListaUsuarios.get(i);

            if (usuario != null) {
                if (usuario.getUsername().equals(username)) {
                    return usuario;
                }
            }
        }
        return null;
    }

    //Agregar Usuarios
    public boolean agregarUsuario(Usuarios usuario) {
        Usuarios aux = buscarUsuario(usuario.getUsername());

        if (aux == null) {
            ListaUsuarios.add(usuario);
            this.fechas = new String[0];
            return true;
        }

        return false;
    }

    public boolean agregarUsuarioEliminado(Usuarios usuario) {

        ListaUsuariosEliminados.add(usuario);
        return true;
    }

    public void elimarusuario(Usuarios usuario) {
        ListaUsuarios.remove(usuario);
    }

    public ArrayList<Usuarios> getListaUsuarios() {
        return ListaUsuarios;
    }

    public ArrayList<Usuarios> getListaUsuariosEliminado() {
        return ListaUsuariosEliminados;
    }

    public Date getfECHA() {
        Date fecha = new Date();
        return fecha;
    }

    public void agregarFecha(String fecha, String jugador1, String jugador2, String resultado) {
        String[] nuevoArreglo = new String[fechas.length + 1];
        System.arraycopy(fechas, 0, nuevoArreglo, 0, fechas.length);
        nuevoArreglo[fechas.length] = (fechas.length) + " " + fecha + "  Jugador:" + jugador1 + "  Jugador2:" + jugador2 + "  Resultado:" + resultado;
        fechas = nuevoArreglo;
    }

}
