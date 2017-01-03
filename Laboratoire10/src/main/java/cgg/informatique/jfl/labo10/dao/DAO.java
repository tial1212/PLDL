package cgg.informatique.jfl.labo10.dao;

import cgg.informatique.jfl.labo10.demarrage.Demarrage;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 * Simply maps the entitymanager.
 * It simplifies refactoring (unitName change) and wraps some logic (limited queries).
 */
@Singleton
@Lock(LockType.READ)
public class DAO {
  private static final Logger LOGGER = Logger.getLogger(Demarrage.class.getName());

  @PersistenceContext(unitName = "PERSISTENCE10")
  private static EntityManager em;

  /**
   * remove an object in DB
   * @param classe The class of desired object
   * @param id
   */
  public static <E> void enlever(Class<E> classe, int id) {
    LOGGER.info("DAO->remove(" + classe + ", " + id + ")");

    em.remove(em.find(classe, id));
  }
  
  /**
   * Find an object List by id
   * @param classe The class of desired objects
   * @param requete
   * @return
   */
  public static <E> int executerRequete(Class<E> classe, String requete, String parametre, Object valeurParametre) {
    LOGGER.info("DAO.trouverParRequete(" + classe + ", " + requete + ")");
    
    Query requeteCree = em.createQuery(requete, classe);
    
    requeteCree.setParameter(parametre, valeurParametre);

    return requeteCree.executeUpdate();
  }
  
  public static <E> int executerRequete(Class<E> classe, String requete, String[] parametres, Object[] valeursParametres) {
    LOGGER.info("DAO.executerRequete(" + classe + ", " + requete + ")");
    
    Query requeteCree = em.createQuery(requete, classe);
    
    for (int i = 0; i < parametres.length && i < valeursParametres.length; i++) {
      LOGGER.info(".setParameter(" + parametres[i] + ", " + valeursParametres[i] + ")");
      requeteCree.setParameter(parametres[i], valeursParametres[i]);
    }

    return requeteCree.executeUpdate();
  }

  public static <E> E modifier(E e) {
    LOGGER.info("DAO.modifier(" + e + ")");

    return em.merge(e);
  }

  /**
   * Persist an object in DB
   * @param e The object to persist
   * @return e The persisted object
   */
  public static <E> E persister(E e) {
    LOGGER.info("DAO.persister(" + e + ")");

    em.persist(e);
    return e;
  }

  /**
   * Find an object by id
   * @param classe The class of desired object
   * @param id 
   * @return
   */
  public static <E> E trouverParId(Class<E> classe, int id) {
    LOGGER.info("DAO.trouverParId(" + classe + ", " + id + ")");

    return em.find(classe, id);
  }

  /**
   * Find an object List by id
   * @param classe The class of desired objects
   * @param requete
   * @return
   */
  public static <E> List<E> trouverParRequete(Class<E> classe, String requete) {
    LOGGER.info("DAO.trouverParRequete(" + classe + ", " + requete + ")");

    return em.createQuery(requete, classe).getResultList();
  }
  
  /**
   * Find an object List by id
   * @param classe The class of desired objects
   * @param requete
   * @return
   */
  public static <E> List<E> trouverParRequete(Class<E> classe, String requete, String parametre, Object valeurParametre) {
    LOGGER.info("DAO.trouverParRequete(" + classe + ", " + requete + ")");
    
    Query requeteCree = em.createQuery(requete, classe);
    
    requeteCree.setParameter(parametre, valeurParametre);

    return em.createQuery(requete, classe).getResultList();
  }
  
  /**
   * Find an object List by id
   * @param classe The class of desired objects
   * @param requete
   * @return
   */
  public static <E> List<E> trouverParRequete(Class<E> classe, String requete, String[] parametres, Object[] valeursParametres) {
    LOGGER.info("DAO.trouverParRequete(" + classe + ", " + requete + ")");
    
    Query requeteCree = em.createQuery(requete, classe);
    
    for (int i = 0; i < parametres.length && i < valeursParametres.length; i++) {
      LOGGER.info(".setParameter(" + parametres[i] + ", " + valeursParametres[i] + ")");
      requeteCree.setParameter(parametres[i], valeursParametres[i]);
    }

    return em.createQuery(requete, classe).getResultList();
  }

  /**
   * Find an object List by id
   * @param classe The class of desired objects
   * @param requete
   * @param premier First index from result
   * @param dernier Last index from result
   * @return
   */
  public static <E> List<E> trouverParRequete(Class<E> classe, String requete, int premier, int dernier) {
    LOGGER.info("DAO.trouverParRequete(" + classe + ", " + requete + ", " + premier + ", " + dernier + ")");

    if (premier < 0)
      LOGGER.warning("DAO.find(...) Paramètre \"premier\" (" + premier + ") plus petit que 0");
    else if (dernier < 0)
      LOGGER.warning("DAO.find(...) Paramètre \"dernier\" (" + dernier + ") plus petit que 0");
    else if (premier > dernier)
      LOGGER.warning("DAO.find(...) Paramètre \"premier\" (" + premier + ")"
                   + " est plus grand que paramètre \"dernier\" (" + dernier + ")");

    return em.createQuery(requete, classe).setFirstResult(premier).setMaxResults(dernier - premier).getResultList();
  }

  public static <E> E trouverParRequeteUnResultat(Class<E> classe, String requete) {
    LOGGER.info("DAO.find(" + classe + ", " + requete + ")");

    return em.createQuery(requete, classe).getSingleResult();
  }
  
  /**
   * Find an object List by id
   * @param classe The class of desired objects
   * @param requete
   * @return
   */
  public static <E> E trouverParRequeteUnResultat(Class<E> classe, String requete, String parametre, Object valeurParametre) {
    LOGGER.info("DAO.trouverParRequeteUnResultat(" + classe + ", " + requete + ")");
    
    Query requeteCree = em.createQuery(requete, classe);
    
    requeteCree.setParameter(parametre, valeurParametre);

    return em.createQuery(requete, classe).getSingleResult();
  }
  
  /**
   * Find an object List by id
   * @param classe The class of desired objects
   * @param requete
   * @return
   */
  public static <E> E trouverParRequeteUnResultat(Class<E> classe, String requete, String[] parametres, Object[] valeursParametres) {
    LOGGER.info("DAO.trouverParRequeteUnResultat(" + classe + ", " + requete + ")");
    
    Query requeteCree = em.createQuery(requete, classe);
    
    for (int i = 0; i < parametres.length && i < valeursParametres.length; i++) {
      LOGGER.info(".setParameter(" + parametres[i] + ", " + valeursParametres[i] + ")");
      requeteCree.setParameter(parametres[i], valeursParametres[i]);
    }

    return em.createQuery(requete, classe).getSingleResult();
  }
}