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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
   @NamedQuery(name = "utilisateur.list", query = "select u from Utilisateur u")
              })
@XmlRootElement(name = "utilisateur")
public class Utilisateur extends ModeleDate {
	/**
     * The E-mail of the user.
     */
    @Column(name = "Courriel", length=50 , nullable = false , unique=true )
    private String eMaill;
    
    /**
     * The pasword (MD5 encrypted ) of the user.
     */
    @Column(name = "MotDePasse", columnDefinition = "text")
    private String pasword;
    
    /**
     * The displayed name of the user (NickName)
     */
    @Column(name = "Alias", length=50)
    private String alias;
    
    /**
     * The ID of the selected Avatar.
     * @see Avatar#id
     */
    @Column(name = "Avatar", length=11)
    private int avatar;
    
    /**
     * If the user has been activated 
     * (has validate captcha ) 
     */
    @Column(name = "Actif" , columnDefinition   ="TINYINT(1)")
    private boolean active;
    
    
	/**
     * DO NOT USE it is useless 
     * "Unenhanced classes must have a public or protected no-args constructor"
     */
    public Utilisateur() {
	}
    
	/**
	 * Create an unactivated user.
	 * 
	 * @param pAlias
	 * @param pEMaill
	 * @param pPasword
	 * @param pAvatar
	 */
    public Utilisateur(String pAlias , String pEMaill, String pPasword , int pAvatar ) {
    	super();
    	if (validateAlias(pAlias) && validateEMaill(pEMaill) && validatePasowrd(pPasword) ) {
    		this.alias = pAlias;
    		this.eMaill = pEMaill;
    		this.pasword = pPasword;
    		this.active = false;
		}
    	else{
    		System.err.println("Utilisateur.constructor("+pAlias+""+pEMaill+""+pPasword +") -> INVALIDE");
    	}
	}
    
	/**
     * Get the e-mail of the user.
     * @return eMaill The E-mail of the user.
     */
	public String getEMaill() {
		return eMaill;
	}
	
	/**
	 * Set the e-mail of the user.
	 * @param pEMaill,   The e-mail to be set
	 * @return ok if email has been changed.
	 */
	public boolean setEMaill(String pEMaill) {
		boolean ok = validateEMaill(pEMaill);
		this.eMaill = (ok?pEMaill:this.eMaill);
		return ok;
	}
	
	/**
	 * Validate an e-mail adress.<br>
	 * 
	 * 
	 * <p>The policy for an user's e-mail is:</p>
	 * <ul>
	 *  <li><p>At least 4 chars</p></li>
	 *  <li><p>No longer than 50 chars</p></li>
	 *	<li><p> Must be in a valid form ( name@compagny.domain ).</p></li>
	 *	</ul>
	 * @param pEMaill The e-mail to validate
	 * @return ok
	 */
	public static boolean validateEMaill(String pEMaill) {
		Pattern VALID_EMAIL_ADDRESS_REGEX =  Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(pEMaill);
		int length = pEMaill.length();
		boolean ok = matcher.find() && length>=4 && length<=50 ;
		if (!ok) {
			System.err.println("Utilisateur.validateEMaill("+pEMaill+") ->INVALIDE");
		}
        return ok;
	}
	
	
	/**
	 * Get the user's pasword.
	 * @return pasword
	 */
	public String getPasowrd() {
		return pasword;
	}
	
	/**
	 * 
	 * Set the user's pasword.
	 * @param pPasword The pasword to be set.
	 * @return ok if pasword has been changed.
	 */
	public boolean setPasowrd(String pPasword) {
		boolean ok = validatePasowrd(pPasword);
		this.pasword = (ok?pPasword:this.pasword);
		return ok;
	}
	
	/**
	 * Validate a  pasword.<br>
	 * <p>The policy for an user's password is:</p>
	 * <ul>
	 *  <li><p>At least 8 chars</p></li>
	 *  <li><p>No longer than 50 chars</p></li>
	 *	<li><p>Contains at least one digit</p></li>
	 *	<li><p>Contains at least one lower alpha char and one upper alpha char</p></li>
	 *	<li><p>Contains at least one char within a set of special chars (<code>@#%$^</code> etc.)</p></li>
	 *	<li><p>Does not contain space, tab, etc.</p></li>
	 *	</ul>
	 * @param pPasword The pasword to validate.
	 * @return ok 
	 */
	public static boolean validatePasowrd(String pPasword) {
		Pattern VALID_EMAIL_ADDRESS_REGEX =  Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(pPasword);
		int length = pPasword.length();
		boolean ok = matcher.find() && length<=50;
		if (!ok) {
			System.err.println("Utilisateur.validatePassowrd("+pPasword+") ->INVALIDE");
		}
        return ok;
	}
	
	/**
	 * Get the user's alias.
	 * @return alias The display name of the user 
	 */
	public String getAlias() {
		return alias;
	}
	
	/**
	 * Set the user's alias.
	 * @param pAlias The new display name to be set.
	 * @return ok if alias has been changed.
	 */
	public boolean setAlias(String pAlias) {
		boolean ok = validateAlias(pAlias);
		this.alias = (ok?pAlias:this.alias);
		return ok;
	}
	
	/**
	 * Validate an alias.<br>
	 * <p>The policy for an user's alias is:</p>
	 * <ul>
	 *  <li><p>At least 4 chars</p></li>
	 *  <li><p>No longer than 50 chars</p></li>
	 *	</ul>
	 * @param pAlias The alias to validate
	 * @return ok
	 */
	public static boolean validateAlias(String pAlias) {
		int length = pAlias.length();
		boolean ok = length>=4 && length<=50;
		if (!ok) {
			System.err.println("Utilisateur.validateAlias("+pAlias+") ->INVALIDE");
		}
        return ok;
	}
	
	/**
	 * If the user has been activated
	 * @return active
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Set if the user is activated
	 * @param pActive
	 */
	public void setActive(boolean pActive) {
		this.active = pActive;
	}
	//TODO javadoc
	
	
	/**
	 * Get the avatar id for the user.
	 * 
	 * @return avatar
	 * @see Avatar#id
	 */
	public int getAvatar() {
		return avatar;
	}
	
	/**
	 * Set the avatar id.
	 * 
	 * @param pIdAvatar
	 * @see Avatar#id
	 */
	public void setAvatar(int pIdAvatar) {
		this.avatar = pIdAvatar;
	}

	@Override
	public String toString() {
		return "User '"+this.eMaill+"' goes by '"+this.alias+"'It has :"+(this.active?" ":" not " ) +" been activated ";
	}
}
