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
	    		desUtilisateur.get(i).getPasowrd().equals(pMotDePasse)   ) {
	    		ok = true;
		    }
		}
	    LOGGER.info("DAOUtilisateur->login( "+pCourriel+","+pMotDePasse+") : SUCCESS="+ok + " nbUser="+desUtilisateur.size() );
		return ok;
	}
    
    

    public Token creerUtilisateur(String pAlias, String pCourriel, String pPasword , int pIdAvatar) {
    	LOGGER.info("DAOUtilisateur->creerUtilisateur("+pAlias+","+ pCourriel+","+pPasword+")"  );
        
        // does't respect policy
        if (!Utilisateur.validateAlias(pAlias) || !Utilisateur.validateEMaill(pCourriel) || !Utilisateur.validatePasowrd(pPasword) ) {
        	String alias = (!Utilisateur.validateAlias(pAlias)?"Alias non compliant w/ policy! " :"");
        	String email = (!Utilisateur.validateEMaill(pCourriel)?"EMail non compliant w/ policy! " :"");
        	String pswd = (!Utilisateur.validatePasowrd(pPasword)?"Pswd non compliant w/ policy! " :"");
        	Token token = new Token(false, email + alias + pswd);
        	LOGGER.info("DAOUtilisateur->creerUtilisateur() ERROR : "+token.getAction()  );
       	 return token;
		}
        // email &&|| alias already used  &&|| Avatar.id non-existing
        boolean duplEmail  = (dao.querrySingle("SELECT u FROM Utilisateur u WHERE u.Courriel = "+pCourriel) != null);
        boolean duplAlias  = (dao.querrySingle("SELECT u FROM Utilisateur u WHERE u.Alias = "   +pAlias   ) != null);
        boolean AvatarExist = (dao.querrySingle("SELECT a FROM Avatar a WHERE a.id = "   +pIdAvatar   ) != null);
        if ( duplEmail || duplAlias || !AvatarExist) {
        	 Token token = new Token(false, (duplEmail?"EMail already used!":"")+
        			 						(duplAlias?"Alias already used!":"")+
        			 						(AvatarExist?"Avatar does'nt exist":"") );
        	 LOGGER.info("DAOUtilisateur->creerUtilisateur() ERROR : "+token.getAction()  );
        	 return token;
		}
        
        Token  token = new Token();
        token.setAction("creerUtilisateur");
        
        if ( token.getEtat() ) {
        	Utilisateur utilisateur = new Utilisateur(pAlias, pCourriel, pPasword);
            dao.creer(utilisateur);
		}
        
        
        token.setEtat(true);
        serviceCaptcha servCapt = new serviceCaptcha();
        String captchaStr =  servCapt.getCaptchaStr();
        token.setCaptchaStr(captchaStr  ) ;
        return dao.creer(token);
    }
    
    /**
     * 
     * @param pCourriel
     * @return ok , if user exist and has
     */
	public boolean activateUser(String pCourriel ) {
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

    public Utilisateur modifier(long pId, String pEMaill, String pPasword, String pAlias , int pAvatar) {
        Utilisateur utilisateur = dao.rechercher(Utilisateur.class, pId);
        if (utilisateur == null) {
            throw new IllegalArgumentException("MAJ id " + pId + " n\'existe pas!");
        }
         
        utilisateur.setPasowrd(pPasword);
        utilisateur.setEMaill(pAlias);
        return dao.modifier(utilisateur);
    }
    /**
    private String eMaill;
    private String pasword;
    private String alias;
    private int avatar;
    private boolean active;
    **/
    
}
