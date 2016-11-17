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

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

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
	 * Detail information about what has happened
	 * indicate last action completed or specific error if there is.
	 */
    private String action;

	/**
	 * 
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
	 * 2 param constructor. 
	 * Create a token with : etat & action
	 */
	public Token( boolean pEtat , String pAction) {
		this.etat = pEtat;
		this.action = pAction;
		
	}
	
	
	public String getSalt() {
		return salt;
	}
	
	/**
	 * Set the salt.
	 * @param pSalt , the desired salt.
	 */
	public void setSalt(String pSalt) {
		this.salt = MD5Digest.getMD5(pSalt);
	}
	
	/**
	 * Set salt with the default "salt".
	 */
	public void setSalt() {
		this.salt = MD5Digest.getMD5("salt");
	}
	
	/**
	 * To get the Base64 call 
	 * @see serviceCaptcha#getCaptcha64()
	 * @return captchaStr
	 */
	public String getCaptchaVal() {
		return captchaStr;
	}

	public void setCaptchaVal(String captchaStr) {
		this.captchaStr = captchaStr;
	}

	
	 public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public Boolean getEtat() {
		return etat;
	}

	public void setEtat(Boolean etat) {
		this.etat = etat;
	}
    
}
