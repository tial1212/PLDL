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
    
    public boolean activerUser(long pIdToken , String pCaptcha , String pCourriel) {
    	LOGGER.info("DAOToken->activerUser("+pIdToken+","+pCaptcha+","+pCourriel+")");
    	
    	Token token = rechercher(pIdToken);
    	if ( token != null  && token.getCaptchaStr().equals( pCaptcha )              ) {
    		boolean ok = daoUser.activerUtil( pCourriel );
    		if(ok){
    			this.effacer(pIdToken);
    		}
    		return ok;
		} else {
			return false ;
		}
	}
    
    public List<Token> afficherListe(int pPremier, int pDernier) {
    	LOGGER.info("DAOToken->afficherListe("+pPremier+","+pDernier+")");
        return dao.rechercheParRequete(Token.class, "token.list", pPremier, pDernier);
    }
    
    public Token rechercher(long pId) {
    		LOGGER.info("DAOToken->rechercher("+pId+")");
        return dao.rechercher(Token.class, pId);
    }

    public void effacer(long pId) {
    		LOGGER.info("DAOToken->effacer("+pId+")");
        dao.effacer(Token.class, pId);
    }

    public Token modifier(long id, String pParam) {
    		LOGGER.info("DAOToken->modifier("+id+","+pParam+")");
    		Token token = dao.rechercher(Token.class, id);
    		//token.setParam(param);    TODO 
    	return dao.modifier(token);
    }
    
    
    public boolean confirmCanDoAction(long pIdToken, String pKey) {
		boolean ok =false;
		//TODO  real validation
	return ok;
}
}
