/*
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
package cgg.informatique.jfl.labo10.modeles;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.RandomStringUtils;

import cgg.informatique.jfl.labo10.services.serviceCaptcha;
import cgg.informatique.jfl.labo10.utilitaires.MD5Digest;

@Entity
@NamedQueries({
   @NamedQuery(name = "token.list", query = "select t from Token t")
              })
@XmlRootElement(name = "token")
public class Token extends Modele {
	
	/**
	 * The string value of the captcha 
	 */
    private String captchaStr;

	/**
	 * Detailed information about last transaction.<br>
	 * Can also indicate last action completed or specific error message if there is.
	 */
    private String action;

	/**
	 * A salt used to secure the user's pasowrd <br>
	 * The salt is save in the database in MD5
	 */
    private String salt;

	/**
	 * If transaction was succesful.
	 */
	private Boolean etat;
	
	
	/**
     * DO NOT USE it is useless 
     *  "Unenhanced classes must have a public or protected no-args constructor"
     */
	public Token() { }
	
	
	/**
	 * 2 param constructor. (Error Token)<br> 
	 * Create a token with : etat & action
	 * @param pEtat
	 * @param pAction
	 */
	public Token( boolean pEtat , String pAction) {
		if ( validateAction(pAction) ) {
			this.etat = pEtat;
			this.action = pAction;
		}
		else {
			System.err.println("Token.constructor("+pEtat+""+pAction+") -> INVALIDE");
		}
	}
	
	/**
	 * 3 param constructor. (Action Token)<br>
	 * Create a token with : etat , action & salt
	 * @param pEtat
	 * @param pAction
	 * @param pSalt
	 */
	public Token( boolean pEtat , String pAction, String pSalt) {
		if ( validateAction(pAction) && validateSalt(pSalt) ) {
			this.etat = pEtat;
			this.action = pAction;
			this.salt = pSalt;
		}
		else {
			System.err.println("Token.constructor("+pEtat+","+pAction+","+pSalt+") -> INVALIDE");
		}
	}
	
	/**
	 * Generate a random salt that comply with the folowing policy 
	 * <p>The policy for a token's salt is:</p>
	 * <ul>
	 *  <li><p>At least 8 chars</p></li>
	 *  <li><p>No longer than 50 chars</p></li>
	 *	</ul>
	 * @return salt
	 */
	public static String generateRdmSalt() {
		int length = ThreadLocalRandom.current().nextInt(8, 50 + 1);
		return RandomStringUtils.randomAlphabetic( length );
	}
	
	
	/**
	 * Generate a token with a random salt
	 * @return Token
	 */
	public static Token generateConfirmationToken() {
		Token token = new Token();
		//token.
	}
	
	/**
	 * 
	 * @param pEmail
	 * @return token
	 */
	public static Token generateConfirmUserToken(String pEmail) {
		Token token = new Token();
		//token.
	}
	
	
	/**
	 * Get the token salt
	 * @return salt
	 */
	public String getSalt() {
		return salt;
	}
	
	/**
	 * Set salt with the default "longAssSalt".
	 * @return ok If the salt has been change to the default.
	 */
	public boolean setSalt() {
		String defaultSalt = "longAssSalt";
		boolean ok = validateSalt(defaultSalt);
		this.salt = (ok?MD5Digest.getMD5("salt") :this.salt);
		return ok;
	}


	/**
	 * Set a specific salt.
	 * @param pSalt The salt to be set.
	 * @return ok If the salt has been change.
	 */
	public boolean setSalt(String pSalt) {
		this.salt = pSalt;
		boolean ok = validateSalt(pSalt);
		this.salt = (ok?MD5Digest.getMD5("salt") :this.salt);
		return ok;
	}
	
	/**
	 * Validate a salt.<br>
	 * <p>The policy for a token's salt is:</p>
	 * <ul>
	 *  <li><p>At least 8 chars</p></li>
	 *  <li><p>No longer than 50 chars</p></li>
	 *	</ul>
	 *@param pSalt The salt to validate.
	 * @return ok
	 */
	public boolean validateSalt(String pSalt){
		int length = pSalt.length();
		boolean ok = length>=8 && length<=50;
		if (!ok) {
			System.err.println("Token.validateSalt("+pSalt+") ->INVALIDE");
		}
        return ok;
	}


	/**
	 * Get the string captcha<br>
	 * To get a Base 64 img call "serviceCaptcha"
	 * @see serviceCaptcha#getCaptcha64
	 * @return captchaStr
	 */
	public String getCaptchaStr() {
		return captchaStr;
	}
	
	/**
	 * Set the string captcha 
	 * @see serviceCaptcha#getCaptcha64()
	 * @param pCaptchaStr The captcha to be set.
	 * @return ok
	 */
	public boolean setCaptchaStr(String pCaptchaStr) {
		boolean ok = validateCaptchaStr(pCaptchaStr);
		this.captchaStr = (ok? pCaptchaStr :this.captchaStr);
		return ok;
	}
	
	/**
	 * Validate a captcha.<br>
	 * <p>The policy for a token's captcha is:</p>
	 * <ul>
	 *  <li><p>At least 5 chars</p></li>
	 *  <li><p>No longer than 15 chars</p></li>
	 *	</ul>
	 *@param pCaptchaStr The salt to validate.
	 * @return ok
	 */
	public boolean validateCaptchaStr(String pCaptchaStr){
		int length = pCaptchaStr.length();
		boolean ok = length>=5 && length<=15;
		if (!ok) {
			System.err.println("Token.validateCaptchaStr("+pCaptchaStr+") ->INVALIDE");
		}
        return ok;
	}

	/**
	 * Get the token's action.
	 * @return action
	 */
	 public String getAction() {
		return action;
	}
	 
	 
	 /**
	  * Set the token's action.
	  * 
	  * @param pAction The action to be set.
	  * @return ok If the action has been change.
	  */
	public boolean setAction(String pAction) {
		boolean ok = validateAction(pAction);
		this.action = (ok? pAction :this.action);
		return ok;
	}
	
	/**
	 * Validate an action/error message.<br>
	 * <p>The policy for a token's action is:</p>
	 * <ul>
	 *  <li><p>At least 5 chars</p></li>
	 *  <li><p>No longer than 50 chars</p></li>
	 *	</ul>
	 *@param pAction The action to validate.
	 * @return ok
	 */
	public boolean validateAction(String pAction){
		int length = pAction.length();
		boolean ok = length>=5 && length<=50;
		if (!ok) {
			System.err.println("Token.validateAction("+pAction+") ->INVALIDE");
		}
        return ok;
	}
	
	/**
	 * Get the token's action state (succes/faillure)
	 * @return etat
	 */
	public Boolean getEtat() {
		return etat;
	}

	/**
	 * Set the token's action state (succes/faillure)
	 */
	public void setEtat(Boolean etat) {
		this.etat = etat;
	}
	
	@Override
	public String toString() {
		String captchaStr = (this.captchaStr != null?"captcha = "+this.captchaStr+" ":"");
		String action = (this.action != null?"action = "+this.action+" ":"");
		String salt = (this.salt != null?"salt = "+this.salt+" ":"");
		String etat = (this.captchaStr != null?"etat = "+this.etat.toString()+" ":"");
		return "Token #"+this.id+" "+captchaStr+action+salt+etat;
	}
    
}
