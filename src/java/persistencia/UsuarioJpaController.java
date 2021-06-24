/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Llamada;
import DTO.Usuario;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;

/**
 *
 * @author USER
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public UsuarioJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Examen-PGWEBPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getLlamadaCollection() == null) {
            usuario.setLlamadaCollection(new ArrayList<Llamada>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Llamada> attachedLlamadaCollection = new ArrayList<Llamada>();
            for (Llamada llamadaCollectionLlamadaToAttach : usuario.getLlamadaCollection()) {
                llamadaCollectionLlamadaToAttach = em.getReference(llamadaCollectionLlamadaToAttach.getClass(), llamadaCollectionLlamadaToAttach.getId());
                attachedLlamadaCollection.add(llamadaCollectionLlamadaToAttach);
            }
            usuario.setLlamadaCollection(attachedLlamadaCollection);
            em.persist(usuario);
            for (Llamada llamadaCollectionLlamada : usuario.getLlamadaCollection()) {
                Usuario oldCedulaOfLlamadaCollectionLlamada = llamadaCollectionLlamada.getCedula();
                llamadaCollectionLlamada.setCedula(usuario);
                llamadaCollectionLlamada = em.merge(llamadaCollectionLlamada);
                if (oldCedulaOfLlamadaCollectionLlamada != null) {
                    oldCedulaOfLlamadaCollectionLlamada.getLlamadaCollection().remove(llamadaCollectionLlamada);
                    oldCedulaOfLlamadaCollectionLlamada = em.merge(oldCedulaOfLlamadaCollectionLlamada);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getCedula()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getCedula());
            Collection<Llamada> llamadaCollectionOld = persistentUsuario.getLlamadaCollection();
            Collection<Llamada> llamadaCollectionNew = usuario.getLlamadaCollection();
            List<String> illegalOrphanMessages = null;
            for (Llamada llamadaCollectionOldLlamada : llamadaCollectionOld) {
                if (!llamadaCollectionNew.contains(llamadaCollectionOldLlamada)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Llamada " + llamadaCollectionOldLlamada + " since its cedula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Llamada> attachedLlamadaCollectionNew = new ArrayList<Llamada>();
            for (Llamada llamadaCollectionNewLlamadaToAttach : llamadaCollectionNew) {
                llamadaCollectionNewLlamadaToAttach = em.getReference(llamadaCollectionNewLlamadaToAttach.getClass(), llamadaCollectionNewLlamadaToAttach.getId());
                attachedLlamadaCollectionNew.add(llamadaCollectionNewLlamadaToAttach);
            }
            llamadaCollectionNew = attachedLlamadaCollectionNew;
            usuario.setLlamadaCollection(llamadaCollectionNew);
            usuario = em.merge(usuario);
            for (Llamada llamadaCollectionNewLlamada : llamadaCollectionNew) {
                if (!llamadaCollectionOld.contains(llamadaCollectionNewLlamada)) {
                    Usuario oldCedulaOfLlamadaCollectionNewLlamada = llamadaCollectionNewLlamada.getCedula();
                    llamadaCollectionNewLlamada.setCedula(usuario);
                    llamadaCollectionNewLlamada = em.merge(llamadaCollectionNewLlamada);
                    if (oldCedulaOfLlamadaCollectionNewLlamada != null && !oldCedulaOfLlamadaCollectionNewLlamada.equals(usuario)) {
                        oldCedulaOfLlamadaCollectionNewLlamada.getLlamadaCollection().remove(llamadaCollectionNewLlamada);
                        oldCedulaOfLlamadaCollectionNewLlamada = em.merge(oldCedulaOfLlamadaCollectionNewLlamada);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getCedula();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Llamada> llamadaCollectionOrphanCheck = usuario.getLlamadaCollection();
            for (Llamada llamadaCollectionOrphanCheckLlamada : llamadaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Llamada " + llamadaCollectionOrphanCheckLlamada + " in its llamadaCollection field has a non-nullable cedula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
