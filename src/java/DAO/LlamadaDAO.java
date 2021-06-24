/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.Llamada;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.LlamadaJpaController;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author USER
 */
public class LlamadaDAO {
    LlamadaJpaController l=new LlamadaJpaController();
    public void create(Llamada llam){
        try {
            l.create(llam);
        } catch (Exception ex) {
            Logger.getLogger(LlamadaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Llamada> read(){
    return l.findLlamadaEntities();
    }
    
    public Llamada readLlamada(int cod){
        return l.findLlamada(cod);
    }
    
    public void update(Llamada llam){
        try {
            l.edit(llam);
        } catch (Exception ex) {
            Logger.getLogger(LlamadaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    public void delete(int cod){
        try {
            l.destroy(cod);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(LlamadaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
