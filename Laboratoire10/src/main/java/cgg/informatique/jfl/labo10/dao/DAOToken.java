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
        return dao.creer(pToken);
    }
    
    
    public Token rechercher(long pIdUser) {
    		LOGGER.info("DAOToken->rechercher("+pIdUser+")");
        return dao.rechercher(Token.class, pIdUser);
    }

    public void effacer(long pId) {
    		LOGGER.info("DAOToken->effacer("+pId+")");
        dao.effacer(Token.class, pId);
    }

    public Token modifier(long pIdUser, String pParam) {
    		LOGGER.info("DAOToken->modifier("+pIdUser+","+pParam+")");
    		Token token = dao.rechercher(Token.class, pIdUser);
    		//token.setParam(param);    TODO 
    	return dao.modifier(token);
    }
    
    
    public Token confirmCanDoAction(long pIdToken, String pKey) {
    	LOGGER.info("DAOToken->confirmCanDoAction("+pIdToken+","+pKey+")");
		Token token = dao.querrySingle("SELECT t FROM Token WHERE t.id ="+pIdToken);
		if (token != null) {
			if (token.getSalt().equals(pKey) ) { //FIXME
				Token token2 = new Token(false, "Token d'action");
				LOGGER.info("DAOToken->confirmCanDoAction() SUCCESS");
				return token2;
			}
			Token token3 = new Token(false, "Token inexistant");
			LOGGER.info("DAOToken->confirmCanDoAction() ECHEC : "+token3.getAction() );
			return token3;
		}
		Token token4 = new Token(false, "Token inexistant");
		LOGGER.info("DAOToken->confirmCanDoAction() ECHEC : "+token4.getAction() );
		return token4;
}
}
