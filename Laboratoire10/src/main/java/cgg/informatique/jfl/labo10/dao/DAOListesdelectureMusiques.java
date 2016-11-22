/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cgg.informatique.jfl.labo10.dao;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.ListesDeLecture;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@Singleton
@Lock(LockType.READ)
public class DAOListesdelectureMusiques {

    @Inject
    private DAO dao;
    
    Logger LOGGER = Logger.getLogger(Demarrage.class.getName()); 
    
    
    
    public List<ListesDeLecture> afficherListe( int pPremier, int pDernier ) {
    	LOGGER.info("DAOUtilisateur->afficherListe("+pPremier+","+pDernier+")");
        return dao.rechercheParRequete(ListesDeLecture.class, "utilisateur.list", pPremier, pDernier);
    }
    
    /**
     * @param id
     * @return
     */
    public ListesDeLecture rechercher(long id) {
        return dao.find(ListesDeLecture.class, id);
    }
    
    /**
     * @param id
     */
    public void effacer(long id) {
        dao.remove(ListesDeLecture.class, id);
    }

    
    /**
     * @param pOwner  (Utilisateur.id)
     * @param pName
     * @param pIsPublic
     * @param pIsActive
     * @return
     */
    public ListesDeLecture modifier(int pOwner, String pName, boolean pIsPublic, boolean pIsActive ) {
    	 ListesDeLecture playlist = dao.find(ListesDeLecture.class, pOwner);
        if (playlist == null) {
            throw new IllegalArgumentException("MAJ id " + pOwner + " n\'existe pas!");
        }
         
        playlist.setOwner(pOwner);
        playlist.setName(pName);
        playlist.setPublic(pIsPublic);
        playlist.setActive(pIsActive);
        return dao.modifier(playlist);
    }
    /**
    private String eMaill;
    private String pasword;
    private String alias;
    private int avatar;
    private boolean active;
    **/
    
}
