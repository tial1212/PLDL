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
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
@Lock(LockType.READ)
public class DAOToken {

    @Inject
    private DAO dao;
    
    @Inject
    DAOUtilisateur daoUser;
    
    Logger LOGGER = Logger.getLogger(Demarrage.class.getName());


    
    public boolean activerUser(long idToken , String captcha) {
    	
    	
    	Token token = rechercher(idToken);
    	if ( token != null  && token.getCaptchaVal().equals( captcha ) )  {
    		daoUser.creerUtilisateur(token.getNom(), token.getMdp(), token.getCouriel() ); 
    		this.effacer(idToken);
    		return true;
		} else {
			return false ;
		}
    	
	}
    
    
    public List<Token> afficherListe(int premier, int dernier) {
    	
    	LOGGER.info("DAO TOKEN->LIST");
        return dao.rechercheParRequete(Token.class, "token.list", premier, dernier);
    }
    
    public Token rechercher(long id) {
        return dao.rechercher(Token.class, id);
    }

    public void effacer(long id) {
        dao.effacer(Token.class, id);
    }

    public Token modifier(long id, String param) {
    	Token token = dao.rechercher(Token.class, id);
         
    	//token.setParam(param);
    	
    	return dao.modifier(token);
    }
}
