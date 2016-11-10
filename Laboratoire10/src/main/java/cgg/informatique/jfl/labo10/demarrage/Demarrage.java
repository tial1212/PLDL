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
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cgg.informatique.jfl.labo10.demarrage;
 

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

@Startup
@DependsOn({ "DAOUtilisateur"})
@Singleton
@Lock(LockType.READ)
public class Demarrage {

    private static final Logger LOGGER = Logger.getLogger(Demarrage.class.getName());

    @PersistenceContext(unitName = "PERSISTENCE10")
    private EntityManager em;

    //@Inject
    //private DAOUtilisateur utilisateurs;
    
    //@Inject
    //private DAOFilm films;

    @PostConstruct
    public void createSomeData() {
        //final Utilisateur tomee = utilisateurs.creer("tomee", "tomee", "tomee@apache.org");
        //final Utilisateur openejb = utilisateurs.creer("alexandre", "arsenault", "openejb@apache.org");
        //final Film titi = films.creer("titre", "google.ca","google.ca" , "il etasi une fois ....");  
    }

    // Effacer les données dans la BD toutes les 30 minutes (pas très bien codé)
    @Schedule(second = "0", minute = "30", hour = "*", persistent = false)
    private void cleanData() {
        LOGGER.info("Cleaning data");
        deleteAll();
        createSomeData();
        LOGGER.info("Data reset");
    }

    private void deleteAll() {
        
        em.createQuery("delete From User").executeUpdate();
    }
}
