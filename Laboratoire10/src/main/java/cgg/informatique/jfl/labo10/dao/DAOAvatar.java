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
import cgg.informatique.jfl.labo10.modeles.Avatar;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@Singleton
@Lock(LockType.READ)
public class DAOAvatar {

    @Inject
    private DAO dao;
    
    private Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
	
    public Avatar creer(String pNom, String pAvatar ) {
    	LOGGER.info("DAOAvatar->creer("+pNom+","+pAvatar+")" );
    	Avatar avatar = new Avatar(pNom, pAvatar);
    	return dao.persist(avatar);
    }
    
    public List<Avatar> afficherListe(int pPremier, int pDernier) {
    	LOGGER.info("DAOAvatar->afficherListe("+pPremier+","+pDernier+")" );
        return dao.rechercheParRequete(Avatar.class, "avatar.list", pPremier, pDernier);
    }
    
    public Avatar rechercher(int pId) {
    	LOGGER.info("DAOAvatar->rechercher("+pId+")" );
        return dao.find(Avatar.class, pId);
    }
    
    public void effacer(int pId) {
    	LOGGER.info("DAOAvatar->effacer("+pId+")" );
        dao.remove(Avatar.class, pId);
    }
    
    public Avatar modifier(long pId, String pNom , String pAvatar ) {
    	LOGGER.info("DAOAvatar->modifier("+pId+","+pNom+","+pAvatar+")" );
    	Avatar avatar = dao.find(Avatar.class, pId);
        if (avatar == null) {
            throw new IllegalArgumentException("DAOAvatar->modifier("+pId+","+pNom+","+pAvatar+") :" + pId + " n\'existe pas!");
        }
        avatar.setName(pNom);
        avatar.setAvatar(pAvatar);
        return dao.modifier(avatar);
    }
}
