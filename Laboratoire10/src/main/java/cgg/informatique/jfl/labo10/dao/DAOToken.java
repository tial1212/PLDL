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

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.swing.JPanel;

@Singleton
@Lock(LockType.READ)
public class DAOToken {

    @Inject
    private DAO dao;
    
    @Inject
    DAOUtilisateur daoUser;
    
    Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    
    
    public List<Token> afficherListe(int pPremier, int pDernier) {
    	LOGGER.info("DAOToken->afficherListe("+pPremier+","+pDernier+")");
        return dao.rechercheParRequete(Token.class, "token.list", pPremier, pDernier);
    }
    
    /**
     * Create (persist) a token in database 
     * @param pToken
     * @return
     */
    public Token persistToken(Token pToken ) {
    	LOGGER.info("DAOToken->persistToken("+pToken.toString()+")"  );
        return dao.persist(pToken);
    }
    
    public Token rechercher(int pIdUser) {
    		LOGGER.info("DAOToken->rechercher("+pIdUser+")");
        return dao.find(Token.class, pIdUser);
    }

    public void effacer(int pId) {
    		LOGGER.info("DAOToken->effacer("+pId+")");
        dao.remove(Token.class, pId);
    }
    
    //TODO  is it realy usefull
    public Token modifier(int pIdUser, String pParam) {
    		LOGGER.info("DAOToken->modifier("+pIdUser+","+pParam+")");
    		Token token = dao.find(Token.class, pIdUser);
    		
    		//validation
    	return dao.modifier(token);
    }
    
    /**
     * Confirm that the action can be executed
     *  <ul>
     * 	<li>Token must exist</li>
     *  <li>key must match </li>
     *  <li>Token must be an ActionToken </li>
     * </ul>
     * key = psdw.salted<br>
     * 
     * @param pIdToken
     * @param pKey
     * @return
     */
    public Token confirmCanDoAction(int pIdToken, String pKey) {
    	LOGGER.info("DAOToken->confirmCanDoAction("+pIdToken+","+pKey+")");
		Token token = dao.querrySingle("SELECT t FROM Token WHERE t.id ="+pIdToken);
		if (token != null) {
			if ( !token.getAction().equals(Token.txtActionToken ) ) { 
				Token token2 = new Token(false, "Token is not an action token");
				LOGGER.info("DAOToken->confirmCanDoAction() SUCCESS : "+token2.getAction());
				return token2;
			}
			
			if (token.getSalt().equals(pKey) ) { //FIXME
				Token token3 = new Token(false, "Token can do action");
				LOGGER.info("DAOToken->confirmCanDoAction() SUCCESS");
				return token3;
			}
			else{
				Token token4 = new Token(false, "Key non valide");
				LOGGER.info("DAOToken->confirmCanDoAction() ECHEC : "+token4.getAction() );
				return token4;
			}
			
		}
		Token token5 = new Token(false, "Token inexistant");
		LOGGER.info("DAOToken->confirmCanDoAction() ECHEC : "+token5.getAction() );
		return token5;
    }
    
    /**
     * Get an user ID from a Token ?????
     * @param pIdToken
     * @param pKey
     * @return
     */
    
    public Utilisateur getUserForToken(org.apache.taglibs.standard.lang.jstl.parser.Token pIdToken) {
    	LOGGER.info("DAOToken->getUserForToken("+pIdToken.toString()+")");
    	// FIXME 
    	LOGGER.info("DAOToken->getUserForToken() NOT PROGRAMMED YET");
		return new Utilisateur();
    }
}
