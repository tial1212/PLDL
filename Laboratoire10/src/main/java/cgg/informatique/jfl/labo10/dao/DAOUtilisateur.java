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

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;

import java.util.logging.Logger;

@Singleton
@Lock(LockType.READ)
public class DAOUtilisateur {

    @Inject
    private static DAO dao;
    
    @Inject
    private DAOToken daoToken;
    
    static Logger LOGGER = Logger.getLogger(Demarrage.class.getName()); 
    
    
    /**
     * Login as an existing user
     * 
     * to be able to login :<br>
     * <ul>
     * 	<li>User must exist</li>
     * 	<li>User must be activated</li>
     * 	<li>pswd must match</li>
     * </ul>
     * @param pCourriel
     * @param pMotDePasse
     * @return ok 
     */
    public Token login(String pCourriel, String pMotDePasse) {
    	LOGGER.info("DAOUtilisateur->login( "+pCourriel+","+pMotDePasse+")");
		
    	Utilisateur utilisateur = dao.querrySingle(""); //FIXME
	    if (utilisateur != null) {
	    	if (utilisateur.isActive() ) {
	    		if (utilisateur.getPasword().equals(pMotDePasse) ) {
		    		Token token = new Token(true, "login succeed");
			    	LOGGER.info("DAOUtilisateur->login() OK : "+token.getAction() );
			    	return token;
				}
		    	Token token2 = new Token(false, "login failled");
		    	LOGGER.info("DAOUtilisateur->login() ECHEC : user exist, wrong pswd" );
		    	return token2;
			}
	    	Token token3 = new Token(false, "user not activated");
	    	LOGGER.info("DAOUtilisateur->login() ECHEC : user exist, but not activated" );
	    	return token3;
		}
	    Token token4 = new Token(false, "login failled");
	    LOGGER.info("DAOUtilisateur->login() ECHEC : non existing user" );
	    return token4;
	}
    
    
    public Token logoff(String pCourriel) {
    	LOGGER.info("DAOUtilisateur->logoff( "+pCourriel+")");
		
    	Utilisateur utilisateur = dao.querrySingle("SELECT u FROM Utilisateur u WHERE u.Courriel = "+pCourriel );
	    if (utilisateur != null) {
	    	if (utilisateur.isActive() ) {
	    		dao.querry("DELETE t FROM Token t WHERE t.Courriel = "+pCourriel );
		    	Token token = new Token(false, "login failled");
		    	LOGGER.info("DAOUtilisateur->logoff() ECHEC : user exist, wrong pswd" );
		    	return token;
			}
	    	Token token2 = new Token(false, "user not activated");
	    	LOGGER.info("DAOUtilisateur->logoff() ECHEC : user exist, but not activated" );
	    	return token2;
		}
	    Token token3 = new Token(false, "login failled");
	    LOGGER.info("DAOUtilisateur->logoff() ECHEC : non existing user" );
	    return token3;
	}
    
    /**
     * Create an user
     * To be valid :<br>
     * <ul>
     * 	<li>User's param must respect policy</li>
     * 	<li>Email must be available</li>
     * 	<li>Alias must be available</li>
     *  <li>Avatar must exist</li>
     * </ul>
     * 
     * @param pAlias
     * @param pCourriel
     * @param pPasword
     * @param pIdAvatar
     * @return
     */
    public Token createUser(String pAlias, String pCourriel, String pPasword , int pIdAvatar) {
    	LOGGER.info("DAOUtilisateur->creerUtilisateur("+pAlias+","+ pCourriel+","+pPasword+","+pIdAvatar+")"  );
        // does't respect policy
        if (!Utilisateur.validateAlias(pAlias) || !Utilisateur.validateEMaill(pCourriel) || !Utilisateur.validatePasowrd(pPasword) ) {
        	String alias = (!Utilisateur.validateAlias(pAlias)    ?"Alias non compliant w/ policy! " :"");
        	String email = (!Utilisateur.validateEMaill(pCourriel)?"EMail non compliant w/ policy! " :"");
        	String pswd  = (!Utilisateur.validatePasowrd(pPasword)?"Pswd non compliant w/ policy! "  :"");
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
        // create objects
        Token  token = Token.generateConfirmUserToken(pCourriel );
        Utilisateur utilisateur = new Utilisateur(pAlias, pCourriel, pPasword , pIdAvatar);
        //persist objects
        Token  token2 = dao.persist(token);
        Utilisateur utilisateur2 = dao.persist(utilisateur);
        
        
        //verify that created object (in DB) are correct
        // delete all if error 
        if ( !token2.getEtat().equals(pCourriel) 		 || 
        	 !utilisateur2.getEMaill().equals(pCourriel) || 
        	 utilisateur2.getAvatar() != pIdAvatar 		 ||
        	 !utilisateur2.getAlias().equals(pAlias )		 ||
        	 !utilisateur2.getPasowrd().equals(pPasword)    ) {
        	Token token3 = new Token(false, "erreur création user dans DB");
        	LOGGER.info("DAOUtilisateur->creerUtilisateur() ERROR : "+token3.getAction()  );
        	return token3;
		}
        
        return token;
    }
    
    /**
     * Activate an user 
     * <ul><li>Token and User must exist</li>
     * <li>captcha must match.</li></ul>
     * @param pIdToken
     * @param pCaptcha
     * @param pCourriel
     * @return
     */
    public Token activateUser(long pIdToken , String pCaptcha ) {
    	LOGGER.info("DAOUtilisateur->activerUser("+pIdToken+","+pCaptcha+")");
    	
    	Token token = daoToken.rechercher(pIdToken);
    	
    	if (token != null && token.getCaptchaStr().equals(pCaptcha) ) {
    		if (token.getEMail() != null) {
    			Utilisateur utilisateurRequested = dao.querrySingle("SELECT u FROM Utilisateur u WHERE u.Courriel = "+token.getEMail() );
                if (utilisateurRequested != null) {
                	utilisateurRequested.setActive(true);
                	dao.modifier(utilisateurRequested);
                	daoToken.effacer(pIdToken);
                	return new Token(true, "activation réussie");
        		}
                Token token2 = new Token(false, "utilisateur demander non existant");
                LOGGER.info("DAOUtilisateur->activerUser() -> ECHEC : "+token2.getAction() );
                return token2;
			}
    		Token token3 = new Token(false, "token ne contien pas d'utilisateur ascocié");
            LOGGER.info("DAOUtilisateur->activerUser() -> ECHEC : "+token3.getAction() );
            return token3;
		}
    	Token token4 = new Token(false, "token demander non existant");
        LOGGER.info("DAOUtilisateur->activerUser() -> ECHEC : "+token4.getAction() );
        return token4;
	}


    public static  Utilisateur rechercher(long pId) {
    	LOGGER.info("DAOUtilisateur->rechercher("+pId+")");
    	return dao.find(Utilisateur.class, pId);
    }

    public void effacer(long pId) {
    	LOGGER.info("DAOUtilisateur->effacer("+pId+")");
    	dao.remove(Utilisateur.class, pId);
    }
    /**
     * Modify an user
     * <ul>
	 *  <li>User must exist</li>
     *  <li>Pswd must comply policy</li> 
     * 	<li>Alias must comply policy</li>
     *  <li>Avatar must exist</li>
     *  <li>Avatar must exist</li>
     * 	<li>Song must belong to the user</li>
     * </ul>
     * 
     * @param pIdUser
     * @param pPasword
     * @param pAlias
     * @param pAvatar
     * @return
     */
    public Token modifier(long pIdUser,  String pPasword, String pAlias , int pAvatar) {
    	LOGGER.info("DAOUtilisateur->modifier("+pIdUser+","+pPasword+","+pAlias+","+pAvatar+")");
    	Utilisateur utilisateur = dao.find(Utilisateur.class, pIdUser);
        if (utilisateur == null) {
            return null;
        }
        //FIXME 
        boolean okUserExist  = false;
        boolean okPswd  = Utilisateur.validatePasowrd(pPasword);
        boolean okAlias = Utilisateur.validateAlias(pAlias);
        boolean okAliasAvailable = false;
        boolean okAvatar = false;
        
        
        if ( !okUserExist || !okPswd || !okAlias || !okAliasAvailable || !okAvatar  ) {
        	String action = (okUserExist?"":"Utilisateur inexistant! ")+(okPswd?"":"Pswd non conforme! ") + 
		    (okAlias?"":"Alias non conforme! ")+(okAliasAvailable?"":"Alias déjà pris! ")+
		    (okAvatar?"":"Avatar non existant! ");
        	return new Token( false , action ) ;
		}
        else{
        	return null;
        	//dao.modifier(utilisateur);
        }
    }
    
    /**
     * Check if user is existing from an User Email,
     * @param pEMaill
     * @return ok
     */
    public static boolean isUserExisting(String pEMaill){
    	
    	Utilisateur utilisateurRequested = dao.querrySingle("SELECT u FROM Utilisateur u WHERE u.Courriel = "+pEMaill );
    	boolean ok = (utilisateurRequested != null);
        LOGGER.info("DAOUtilisateur->isUserExisting("+pEMaill+") : "+(ok?"YES" : "NO" ) );
    	
        return ok;
    }
    
    /**
     * Check if user is existing from an User ID,
     * @param pIdUser
     * @return ok
     */
    public static boolean isUserExisting(int pIdUser){
    	Utilisateur utilisateurRequested = rechercher(pIdUser);
    	boolean ok = (utilisateurRequested != null);
        LOGGER.info("DAOUtilisateur->isUserExisting("+pIdUser+") : "+(ok?"YES" : "NO" ) );
        return ok;
    }
    
    /**
     * Check if user is activated from an User Email,
     * @param pEMaill
     * @return ok
     */
    public static boolean isUserActivated(String pEMaill){
    	boolean okExist = isUserExisting(pEMaill);
    	if (!okExist) {
    		LOGGER.info("DAOUtilisateur->isUserActivated("+pEMaill+") : user DOESN'T exist" );
            return false;
		}
    	Utilisateur utilisateurRequested = dao.querrySingle("SELECT u FROM Utilisateur u WHERE u.Courriel = "+pEMaill );
    	boolean okActivated = utilisateurRequested.isActive();
        LOGGER.info("DAOUtilisateur->isUserActivated("+pEMaill+") : "+(okActivated?"YES" : "NO" ) );
        return okActivated;
    }
    
    
    /**
     * Check if user is activated from an User ID,
     * @param pIdUser
     * @return ok
     */
    public static boolean isUserActivated(int pIdUser){
    	boolean okExist = isUserExisting(pIdUser);
    	if (!okExist) {
    		LOGGER.info("DAOUtilisateur->isUserActivated("+pIdUser+") : user DOESN'T exist" );
            return false;
		}
    	Utilisateur utilisateurRequested = rechercher(pIdUser);
    	boolean okActivated = utilisateurRequested.isActive();
        LOGGER.info("DAOUtilisateur->isUserActivated("+pIdUser+") : "+(okActivated?"YES" : "NO" ) );
        return okActivated;
        
    }
    /**
     * Get the User.id for an EMail.
     * <br> !!!MIGTH!!!<br> return -1 if user doesn't exist
     * @param pEMaill
     * @return id
     */
    public static int getIdForUser(String pEMaill){
    	Utilisateur utilisateurRequested = dao.querrySingle("SELECT u FROM Utilisateur u WHERE u.Courriel = "+pEMaill );
    	if (utilisateurRequested != null ) {
    		utilisateurRequested.getId();
		}
    	return -1;
    	
        
    }
}
