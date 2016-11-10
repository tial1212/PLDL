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

    public <E> E creer(E e) {
        em.persist(e);
       
        return e;
    }

    public <E> E modifier(E e) {
        return em.merge(e);
    }

    public <E> void effacer(Class<E> clazz, long id) {
    	 
        
        em.remove(em.find(clazz, id));
    }

    public <E> E rechercher(Class<E> clazz, long id) {
        return em.find(clazz, id);
    }

    public <E> List<E> find(Class<E> clazz, String query, int premier, int dernier) {
    	
        return queryRange(em.createQuery(query, clazz), premier, dernier).getResultList();
    }

    public <E> List<E> rechercheParRequete(Class<E> clazz, String query, int premier, int dernier) {
    	Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    	LOGGER.info("DAO->LIST");
    	
        return queryRange(em.createNamedQuery(query, clazz), premier, dernier).getResultList();
    }

    private static Query queryRange(Query query, int premier, int dernier) {
        if (dernier >= 0) {
            query.setMaxResults(dernier);
        }
        if (premier >= 0) {
            query.setFirstResult(premier);
        }
        return query;
    }
}
