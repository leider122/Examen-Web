/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import DAO.UsuarioDAO;
import DTO.Usuario;

/**
 *
 * @author USER
 */
public class AnalizadorLlamadas {
    public Usuario validarBase(int cedula){
        UsuarioDAO uDAO=new UsuarioDAO();
        Usuario u=uDAO.readUsuario(cedula);
        if(u!=null){
        return u;
        }
        return null;
    }

    public String getTablaUsuario(Usuario u) {
        String tabla="";
        return tabla;
    }

    public String getTablaLlamadas(Usuario u) {
        String tabla="";
        return tabla;
    }
}
