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
import cgg.informatique.jfl.labo10.modeles.Token;
import cgg.informatique.jfl.labo10.modeles.Utilisateur;
import cgg.informatique.jfl.labo10.services.serviceCaptcha;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@Singleton
@Lock(LockType.READ)
public class DAOUtilisateur {

    @Inject
    private DAO dao;
    
    Logger LOGGER = Logger.getLogger(Demarrage.class.getName()); 
    
    
    
    public boolean login(String pCourriel, String pMotDePasse) {
    	List<Utilisateur>  desUtilisateur = dao.rechercheParRequete(Utilisateur.class, "utilisateur.list", 0 , 100 );
	    for (int i = 0; i < desUtilisateur.size(); i++) {
	    	if (desUtilisateur.get(i).getEMaill().equals(pCourriel) && 
	    		desUtilisateur.get(i).getPassowrd().equals(pMotDePasse)   ) {
	    		LOGGER.info("DAO USER LOGIN -> SUCCESS "  );
		        return true;
		    }
		}
	    LOGGER.info("DAO USER LOGIN -> FAILLED , nb user->"+desUtilisateur.size() )  ;
		return false;
	}
    
    
    public Token creerUtilisateur(String nom, String motDePasse, String courriel) {
        Token  token = new Token();
        token.setAction("creerToken");
        
        List<Token> desToken = dao.rechercheParRequete(Token.class, "token.list", 0 ,100);
        
        for (int i = 0; i < desToken.size() ; i++) {
			if (  desToken.get(i).getCouriel().equals(courriel)  ) {
				LOGGER.info("DAO TOKEN CREER->TOKEN EXISTE DEJA");
				token.setEtat(false);
				token.setAction("token deja existant");
				return token;
			}
			
		}
        List<Utilisateur> desUtils = dao.rechercheParRequete(Utilisateur.class, "utilisateur.list", 0 ,100);
        for (int i = 0; i < desUtils.size() ; i++) {
			if (desUtils.get(i).getEMaill().equals(courriel)  ) {
				LOGGER.info("DAO TOKEN CREER->UTILISATEUR  EXISTE DEJA");
				token.setEtat(false);
				token.setAction("utilisateur deja existant");
				return token;
			}
			
		}
        
        token.setNom(nom);
        token.setMdp(motDePasse);
        token.setCouriel(courriel);
        token.setEtat(true);
        serviceCaptcha servCapt = new serviceCaptcha();
        String captchaStr =  servCapt.getCaptchaStr();
        token.setCaptchaVal(captchaStr  ) ;
        return dao.creer(token);
    }
    

	public Utilisateur creer(String nom, String motDePasse, String courriel) {

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPassowrd(motDePasse);
        utilisateur.setEMaill(courriel);
        
        LOGGER.info("DAO USER CREER " + utilisateur.getEMaill() );
        
        return dao.creer(utilisateur);
    }

    public List<Utilisateur> afficherListe(int premier, int dernier) {
    	Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    	LOGGER.info("DAO USER->LIST");
        return dao.rechercheParRequete(Utilisateur.class, "utilisateur.list", premier, dernier);
    }

    public Utilisateur rechercher(long id) {
        return dao.rechercher(Utilisateur.class, id);
    }

    public void effacer(long id) {
        dao.effacer(Utilisateur.class, id);
    }

    public Utilisateur modifier(long id, String nom, String motDePasse, String courriel) {
        Utilisateur utilisateur = dao.rechercher(Utilisateur.class, id);
        if (utilisateur == null) {
            throw new IllegalArgumentException("MAJ id " + id + " n\'existe pas!");
        }
         
        utilisateur.setPassowrd(motDePasse);
        utilisateur.setEMaill(courriel);
        return dao.modifier(utilisateur);
    }
}
