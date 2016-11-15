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
    	boolean ok = false;
    	List<Utilisateur>  desUtilisateur = dao.rechercheParRequete(Utilisateur.class, "utilisateur.list", 0 , 100 );
	    for (int i = 0; i < desUtilisateur.size(); i++) {
	    	if (desUtilisateur.get(i).getEMaill().equals(pCourriel) && 
	    		desUtilisateur.get(i).getPassowrd().equals(pMotDePasse)   ) {
	    		ok = true;
		    }
		}
	    LOGGER.info("DAOUtilisateur->login( "+pCourriel+","+pMotDePasse+") : SUCCESS="+ok + " nbUser="+desUtilisateur.size() );
		return ok;
	}
    
    
    public Token creerUtilisateur(String pAlias, String pCourriel, String pPasword) {
    	LOGGER.info("DAOUtilisateur->creerUtilisateur("+pAlias+","+ pCourriel+","+pPasword+")"  );
        Token  token = new Token();
        token.setAction("creerUtilisateur");
        
        //FIXME use querry to look up for email && alias are free
        List<Utilisateur> desUtils = dao.rechercheParRequete(Utilisateur.class, "utilisateur.list", 0 ,100);
        for (int i = 0; i < desUtils.size() ; i++) {
			if ( false ) {  //FIXME
				LOGGER.info("DAOUtilisateur->creerUtilisateur-> COURRIEL EXISTE DEJA ");
				token.setAction("courriel deja pris");
				LOGGER.info("DAOUtilisateur->creerUtilisateur-> ALIAS DEJA PRIS");
				token.setAction("alias deja pris");
				
				
				token.setEtat(false);
				return token;
			}
			
		}
        if ( token.getEtat() ) {
        	Utilisateur utilisateur = new Utilisateur(pAlias, pCourriel, pPasword);
            dao.creer(utilisateur);
		}
        
        
        token.setEtat(true);
        serviceCaptcha servCapt = new serviceCaptcha();
        String captchaStr =  servCapt.getCaptchaStr();
        token.setCaptchaVal(captchaStr  ) ;
        return dao.creer(token);
    }
    
    /**
     * 
     * @param pCourriel
     * @return ok , if user exist and has
     */
	public boolean activerUtil(String pCourriel ) {
		LOGGER.info("DAOUtilisateur->activerUtil(" + pCourriel + ")" );
        //FIXME use querry
		Utilisateur utilisateur;
		List<Utilisateur> desUtils = dao.rechercheParRequete(Utilisateur.class, "utilisateur.list", 0 ,100);
        for (int i = 0; i < desUtils.size() ; i++) {
			if ( desUtils.get(i).getEMaill().equals(pCourriel)  ) { 
				utilisateur = desUtils.get(i);
				utilisateur.setActive(true);
				break;
			}
			
		}
		
        return false;
    }

    public List<Utilisateur> afficherListe( int pPremier, int pDernier ) {
    	LOGGER.info("DAOUtilisateur->afficherListe("+pPremier+","+pDernier+")");
        return dao.rechercheParRequete(Utilisateur.class, "utilisateur.list", pPremier, pDernier);
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
