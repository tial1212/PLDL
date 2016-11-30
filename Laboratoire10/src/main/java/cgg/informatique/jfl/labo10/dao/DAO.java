/*
 *     Licensed to the Apache Software Foundation (ASF) under one or more
 *     contributor license agreements.  See the NOTICE file distributed with
 *     this work for additional information regarding copyright ownership.
 *     The ASF licenses this file to You under the Apache License, Version 2.0
 *     (the "License"); you may not use this file except in compliance with
 *     the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package cgg.informatique.jfl.labo10.dao;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cgg.informatique.jfl.labo10.demarrage.Demarrage;

import java.util.List;
import java.util.logging.Logger;

/**
 * Simply maps the entitymanager.
 * It simplifies refactoring (unitName change) and wraps some logic (limited queries).
 */
@Singleton
@Lock(LockType.READ)
public class DAO {

    @PersistenceContext(unitName = "PERSISTENCE10")
    private EntityManager em;
    
    private Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    private static Logger LOGGER2 = Logger.getLogger(Demarrage.class.getName());
    
    
    /**
     * Persist an object in DB
     * @param e The object to persist
     * @return e The persisted object
     */
    public <E> E persist(E e) {
    	LOGGER.info("DAO->persist("+e+")" );
        em.persist(e);
       
        return e;
    }
    
    public <E> E modifier(E e) {
    	LOGGER.info("DAO->modifier("+e+")" );
        return em.merge(e);
    }
    
    /**
     * remove an object in DB
     * @param clazz The class of desired object
     * @param pId
     */
    public <E> void remove(Class<E> clazz, int pId) {
    	LOGGER.info("DAO->remove("+clazz+","+pId+")" );
        em.remove(em.find(clazz, pId));
    }
    
    /**
     * Find an object by id
     * @param clazz The class of desired object
     * @param id 
     * @return
     */
    public <E> E find(Class<E> clazz, int id) {
    	LOGGER.info("DAO->find("+clazz+","+id+")" );
        return em.find(clazz, id);
    }
    
    /**
     * Find an object List by id
     * @param clazz The class of desired objects
     * @param query
     * @param premier First index from result
     * @param dernier Last index from result
     * @return
     */
    public <E> List<E> find(Class<E> clazz, String query, int premier, int dernier) {
    	LOGGER.info("DAO->find("+clazz+","+query+","+premier+","+dernier+")" );
        return queryRange(em.createQuery(query, clazz), premier, dernier).getResultList();
    }
    
    public <E> List<E> rechercheParRequete(Class<E> clazz, String query, int premier, int dernier) {
    	LOGGER.info("DAO->rechercheParRequete("+clazz+","+query+","+premier+","+dernier+")" );
    	
        return queryRange(em.createNamedQuery(query, clazz), premier, dernier).getResultList();
    }
    
    private static Query queryRange(Query query, int premier, int dernier) {
    	LOGGER2.info("DAO->queryRange("+query+","+premier+","+dernier+")" );
        if (dernier >= 0) {
            query.setMaxResults(dernier);
        }
        if (premier >= 0) {
            query.setFirstResult(premier);
        }
        return query;
    }
    
    /**
     * Execute a desired querry.<br>
     * ***MAKE SURE TO VERIFY THE RESULT***<br>
     *  Might return empty list or even cause a roolback
     *
     *@param pQuerry , a querry to execute.
     * @return result , the result of the querry
     */
    public <E> List<E> querry(String pQuerry) {
    	LOGGER.info("DAO->querry("+pQuerry+")" );
    	Query query = em.createQuery(pQuerry);
    	List<E> results = query.getResultList();
    	return results;
    }
    
    public <E> E querry(String pQuerry, String pParam, String pParamValue) {
    	LOGGER.info("DAO->querry("+pQuerry+")" );
    	Query query = em.createQuery(pQuerry).setParameter(pParam, pParamValue);
    	E results = null;
        try {
          results = (E) ( query.getResultList().isEmpty() ? null : query.getResultList() );
        } catch (Exception e) {
          LOGGER.info("DAO->getResultList() found nothing");
        }
    	return results;
    }
    
    public <E> E querry(String pQuerry, String pParam, String pParamValue, boolean doUpdate) {
    	LOGGER.info("DAO->querry("+pQuerry+")" );
    	Query query = em.createQuery(pQuerry).setParameter(pParam, pParamValue);
    	E results = null;
        try {
          if (doUpdate)
            results = (E) ( query.executeUpdate() == 0 ? null : query.executeUpdate() );
          else
            results = (E) ( query.getResultList().isEmpty() ? null : query.getResultList() );
        } catch (Exception e) {
          LOGGER.info("DAO->getResultList() found nothing");
        }
    	return results;
    }
    
    public <E> E querry(String pQuerry, String pParam, int pParamValue) {
    	LOGGER.info("DAO->querry("+pQuerry+")" );
    	Query query = em.createQuery(pQuerry).setParameter(pParam, pParamValue);
        E results = null;
        try {
          results = (E) ( query.getResultList().isEmpty() ? null : query.getResultList() );
        } catch (Exception e) {
          LOGGER.info("DAO->getResultList() found nothing");
        }
    	return results;
    }
    
    public <E> E querry(String pQuerry, String[] pParams, int[] pParamValues, boolean doUpdate) {
    	LOGGER.info("DAO->querry("+pQuerry+")" );
    	Query query = em.createQuery(pQuerry);
        for (int i = 0; i < pParams.length && i < pParamValues.length; i++) {
            query.setParameter(pParams[i], pParamValues[i]);
            LOGGER.info("DAO->querry() Parameter " + pParams[i] + " : " + pParamValues[i]);
        }
    	E results = null;
        try {
          if (doUpdate)
            results = (E) ( query.executeUpdate() == 0 ? null : query.executeUpdate() );
          else
            results = (E) ( query.getResultList().isEmpty() ? null : query.getResultList() );
        } catch (Exception e) {
          LOGGER.info("DAO->getSingleResult() found nothing");
        }
    	return results;
    }
    
    /**
     * Execute a desired querry.<br>
     * RETURNING 1 OBJECT.<br>
     * ***MAKE SURE TO VERIFY THE RESULT***<br>
     *  Might return empty or even cause a roolback
     *
     *@param pQuerry , a querry to execute.
     * @return 
     * @return result , the result of the querry
     */
    public <E> E querrySingle(String pQuerry) {
    	LOGGER.info("DAO->querry("+pQuerry+")" );
    	Query query = em.createQuery(pQuerry);
    	E results = (E) query.getSingleResult();
    	return results;
    }
    
    public <E> E querrySingle(String pQuerry, String pParam, String pParamValue) {
    	LOGGER.info("DAO->querry("+pQuerry+")" + "[" + pParam + ", " + pParamValue + "]" );
    	Query query = em.createQuery(pQuerry).setParameter(pParam, pParamValue);
    	E results = null;
        try {
          results = (E) ( query.getResultList().isEmpty() ? null : query.getSingleResult() );
        } catch (Exception e) {
          LOGGER.info("DAO->getSingleResult() found nothing");
        }
    	return results;
    }
    
    public <E> E querrySingle(String pQuerry, String[] pParams, String[] pParamValues) {
    	LOGGER.info("DAO->querry("+pQuerry+")" );
    	Query query = em.createQuery(pQuerry);
        for (int i = 0; i < pParams.length && i < pParamValues.length; i++)
            query.setParameter(pParams[i], pParamValues[i]);
    	E results = null;
        try {
          results = (E) ( query.getResultList().isEmpty() ? null : query.getSingleResult() );
        } catch (Exception e) {
          LOGGER.info("DAO->getSingleResult() found nothing");
        }
    	return results;
    }
    
    public <E> E querrySingle(String pQuerry, String pParam, int pParamValue) {
    	LOGGER.info("DAO->querry("+pQuerry+")" + "[" + pParam + ", " + pParamValue + "]" );
    	Query query = em.createQuery(pQuerry).setParameter(pParam, pParamValue);
        E results = null;
        try {
          results = (E) ( query.getResultList().isEmpty() ? null : query.getSingleResult() );
        } catch (Exception e) {
          LOGGER.info("DAO->getSingleResult() found nothing");
        }
    	return results;
    }
    
    public <E> E querrySingle(String pQuerry, String[] pParams, int[] pParamValues) {
    	LOGGER.info("DAO->querry("+pQuerry+")" );
    	Query query = em.createQuery(pQuerry);
        for (int i = 0; i < pParams.length && i < pParamValues.length; i++) {
            query.setParameter(pParams[i], pParamValues[i]);
            LOGGER.info("DAO->querrySingle() Parameter " + pParams[i] + " : " + pParamValues[i]);
        }
    	E results = null;
        try {
          results = (E) ( query.getResultList().isEmpty() ? null : query.getSingleResult() );
        } catch (Exception e) {
          LOGGER.info("DAO->getSingleResult() found nothing");
        }
    	return results;
    }
}
