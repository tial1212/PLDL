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
import cgg.informatique.jfl.labo10.modeles.ListesDelectureMusiques;
import cgg.informatique.jfl.labo10.modeles.Token;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;

import java.util.logging.Logger;

@Singleton
@Lock(LockType.READ)
public class DAOListesdelectureMusiques {

    @Inject
    private DAO dao;
    
    Logger LOGGER = Logger.getLogger(Demarrage.class.getName()); 
    
    
    //TODO lots of methods
    // find song in playlist
    
    public Token addSong(int idMusique, int idListeDeLecture) {
        dao.persist(new ListesDelectureMusiques(idMusique, idListeDeLecture));
        return new Token(true, "Ajout ok");
    }
    
    public Token removeSong(int idMusique, int idListeDeLecture) {
        String[] arrParams = new String[] { "liste", "musique" };
        int[] arrParamValues = new int[] { idMusique, idListeDeLecture };
        if (dao.querry("DELETE FROM ListesDelectureMusiques l WHERE l.song = :musique AND l.playList = :liste", arrParams, arrParamValues, true) == null)
            return new Token(false, "Suppression problème");
        else
            return new Token(true, "Suppression ok");
    }
    
    /**
     * @param id
     * @return
     */
    public ListesDeLecture rechercher(int id) {
        return dao.find(ListesDeLecture.class, id);
    }
    
    /**
     * @param id
     */
    public void effacer(int id) {
        dao.remove(ListesDeLecture.class, id);
    }
    
    
}
