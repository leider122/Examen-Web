/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.Usuario;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.UsuarioJpaController;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author USER
 */
public class UsuarioDAO {
    UsuarioJpaController pa=new UsuarioJpaController();
    public void create(Usuario d){
        try {
            pa.create(d);
        } catch (Exception ex) {
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Usuario> read(){
    return pa.findUsuarioEntities();
    }
    
    public Usuario readUsuario(int cod){
        return pa.findUsuario(cod);
    }
    
    public void update(Usuario dpar){
        try {
            pa.edit(dpar);
        } catch (Exception ex) {
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    public void delete(int cod) throws IllegalOrphanException{
        try {
            pa.destroy(cod);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
