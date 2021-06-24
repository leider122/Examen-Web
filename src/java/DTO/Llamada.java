/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "Llamada")
@NamedQueries({
    @NamedQuery(name = "Llamada.findAll", query = "SELECT l FROM Llamada l"),
    @NamedQuery(name = "Llamada.findById", query = "SELECT l FROM Llamada l WHERE l.id = :id"),
    @NamedQuery(name = "Llamada.findByMinInicio", query = "SELECT l FROM Llamada l WHERE l.minInicio = :minInicio"),
    @NamedQuery(name = "Llamada.findByMinFin", query = "SELECT l FROM Llamada l WHERE l.minFin = :minFin")})
public class Llamada implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "min_inicio")
    private long minInicio;
    @Basic(optional = false)
    @Column(name = "min_fin")
    private long minFin;
    @JoinColumn(name = "cedula", referencedColumnName = "cedula")
    @ManyToOne(optional = false)
    private Usuario cedula;
    @JoinColumn(name = "id_pais", referencedColumnName = "id_pais")
    @ManyToOne(optional = false)
    private Pais idPais;

    public Llamada() {
    }

    public Llamada(Integer id) {
        this.id = id;
    }

    public Llamada(Integer id, long minInicio, long minFin) {
        this.id = id;
        this.minInicio = minInicio;
        this.minFin = minFin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getMinInicio() {
        return minInicio;
    }

    public void setMinInicio(long minInicio) {
        this.minInicio = minInicio;
    }

    public long getMinFin() {
        return minFin;
    }

    public void setMinFin(long minFin) {
        this.minFin = minFin;
    }

    public Usuario getCedula() {
        return cedula;
    }

    public void setCedula(Usuario cedula) {
        this.cedula = cedula;
    }

    public Pais getIdPais() {
        return idPais;
    }

    public void setIdPais(Pais idPais) {
        this.idPais = idPais;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Llamada)) {
            return false;
        }
        Llamada other = (Llamada) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Llamada[ id=" + id + " ]";
    }
    
}
