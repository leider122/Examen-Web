/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import DAO.LlamadaDAO;
import DAO.UsuarioDAO;
import DTO.Llamada;
import DTO.Usuario;
import java.util.ArrayList;
import java.util.List;

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
        String tabla="<table>"+
                "<tr><th>Nombre</th>"+
                "<th>Email</th>"+
                "<th>Foto</th><tr>"+
                "<tr><td>"+u.getNombre()+"</td>"+
                "<td>"+u.getEmail()+"</td>"+
                "<td><img src='"+u.getFoto()+"'></td></tr>"+
                "";
        return tabla+"</tabla>";
    }

    public String getTablaLlamadas(Usuario u) {
        String llamadas=getTLlama(u);
        String tabla="<table>"+
                "<tr><th>Pais</th>"+
                "<th>Min inicial</th>"+
                "<th>Min finth>"+
                "<th>Valor minuto</th>"+
                "<th>Valor Llamada</th><tr>";
        
        return tabla+llamadas+"</table>";
    }

    /*
    CODIGO REDUNDANTE , SI YA TENÍA READ(CEDULA)
    */
    public List<Llamada> llamadasPersona(Usuario u){
        List<Llamada> l=new ArrayList();
        
        LlamadaDAO ll=new LlamadaDAO();
        List<Llamada> l2=ll.read();
        for(Llamada lm:l2){
            if(lm.getCedula().equals(u)){
               l.add(lm);
            }
        }
        return l;
    }
    public String getTLlama(Usuario u){
        List<Llamada> l=llamadasPersona(u);
        String msg="";
        
        for(Llamada ll:l){
            msg+="<tr><td>"+ll.getIdPais().getNombre()+"</td>"+
                    "<td>"+ll.getMinInicio()+"</td>"+
                    "<td>"+ll.getMinFin()+"</td>"+
                    "<td>"+ll.getIdPais().getCostoMInuto()+"</td>"+
                    "<td>"+getCostoLlamada(ll.getMinInicio(),ll.getMinFin(),ll.getIdPais().getCostoMInuto())+"</td></tr>";
        }
        
        return msg;
    }
    public long getCostoLlamada(long minIni,long minFin,long costomin){
        long cantidadmin=minFin-minIni;
        if(cantidadmin<=0){
        return 0;
        }
        return (cantidadmin)*costomin;
    }
}
