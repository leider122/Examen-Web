/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.Pais;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.PaisJpaController;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author USER
 */
public class PaisDAO {
    PaisJpaController pa=new PaisJpaController();
    public void create(Pais d){
        try {
            pa.create(d);
        } catch (Exception ex) {
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Pais> read(){
    return pa.findPaisEntities();
    }
    
    public Pais readPais(int cod){
        return pa.findPais(cod);
    }
    
    public void update(Pais dpar){
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
