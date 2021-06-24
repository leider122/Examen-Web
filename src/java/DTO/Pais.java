/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "Pais")
@NamedQueries({
    @NamedQuery(name = "Pais.findAll", query = "SELECT p FROM Pais p"),
    @NamedQuery(name = "Pais.findByIdPais", query = "SELECT p FROM Pais p WHERE p.idPais = :idPais"),
    @NamedQuery(name = "Pais.findByNombre", query = "SELECT p FROM Pais p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Pais.findByCostoMInuto", query = "SELECT p FROM Pais p WHERE p.costoMInuto = :costoMInuto"),
    @NamedQuery(name = "Pais.findByCodigoTelefonico", query = "SELECT p FROM Pais p WHERE p.codigoTelefonico = :codigoTelefonico")})
public class Pais implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_pais")
    private Integer idPais;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "costoMInuto")
    private int costoMInuto;
    @Basic(optional = false)
    @Column(name = "codigo_telefonico")
    private int codigoTelefonico;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPais")
    private Collection<Llamada> llamadaCollection;

    public Pais() {
    }

    public Pais(Integer idPais) {
        this.idPais = idPais;
    }

    public Pais(Integer idPais, String nombre, int costoMInuto, int codigoTelefonico) {
        this.idPais = idPais;
        this.nombre = nombre;
        this.costoMInuto = costoMInuto;
        this.codigoTelefonico = codigoTelefonico;
    }

    public Integer getIdPais() {
        return idPais;
    }

    public void setIdPais(Integer idPais) {
        this.idPais = idPais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCostoMInuto() {
        return costoMInuto;
    }

    public void setCostoMInuto(int costoMInuto) {
        this.costoMInuto = costoMInuto;
    }

    public int getCodigoTelefonico() {
        return codigoTelefonico;
    }

    public void setCodigoTelefonico(int codigoTelefonico) {
        this.codigoTelefonico = codigoTelefonico;
    }

    public Collection<Llamada> getLlamadaCollection() {
        return llamadaCollection;
    }

    public void setLlamadaCollection(Collection<Llamada> llamadaCollection) {
        this.llamadaCollection = llamadaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPais != null ? idPais.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pais)) {
            return false;
        }
        Pais other = (Pais) object;
        if ((this.idPais == null && other.idPais != null) || (this.idPais != null && !this.idPais.equals(other.idPais))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Pais[ idPais=" + idPais + " ]";
    }
    
}
