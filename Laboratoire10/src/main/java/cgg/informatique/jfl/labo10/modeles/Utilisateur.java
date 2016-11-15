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

import java.util.Date;
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
public class Utilisateur extends Modele {
	/**
     * The E-mail of the user.
     */
    @Column(name = "COURRIEL", length=50 , nullable = false , unique=true )
    private String eMaill;
    
    /**
     * The pasword (MD5 encrypted ) of the user.
     */
    @Column(name = "MOT_DE_PASSE", columnDefinition = "text")
    private String pasword;
    
    /**
     * The displayed name of the user (NickName)
     */
    @Column(name = "ALIAS", length=50)
    private String alias;
    
    /**
     * The ID of the selected Avatar.
     * @see Avatar#id
     */
    @Column(name = "AVATAR", length=11)
    private int avatar;
    
    /**
     * If the user has been activated 
     * (has validate captcha ) 
     */
    @Column(name = "ACTIF" , columnDefinition   ="TINYINT(1)")
    private boolean active;
    
    /**
     * The modification date
     * (user expire if never confirm )
     */
    @Column(name = "DATE") 
    private Date date; 

    
    
    
    
    public Utilisateur(String pAlias , String pEMaill, String pPasword ) {
		super();
		this.alias = pAlias;
		this.eMaill = pEMaill;
		this.pasword = pPasword;
	}

	/**
     * Get the e-mail of the user.
     * @return eMaill, The E-mail of the user.
     */
	public String getEMaill() {
		return eMaill;
	}
	
	/**
	 * Set the e-mail of the user.
	 * @param pEMaill,   The e-mail to be set
	 */
	public void setEMaill(String pEMaill) {
		this.eMaill = pEMaill;
	}
	
	/**
	 * Get the user's pasword.
	 * @return pasword
	 */
	public String getPassowrd() {
		return pasword;
	}
	
	/**
	 * Set the user's pasword.
	 * @param pPassowrd, The pasword to be set.
	 */
	public void setPassowrd(String pPassowrd) {
		this.pasword = pPassowrd;
	}
	
	/**
	 * Get the user's alias.
	 * @return�alias, The display name of the user 
	 */
	public String getAlias() {
		return alias;
	}
	
	/**
	 * Set the user's alias.
	 * @param alias , The new display name to be set.
	 */
	public void setAlias(String pAlias) {
		this.alias = pAlias;
	}
	
	/**
	 * If the user has been activated
	 * @return active , boolean
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Set if the user is activated
	 * @param pActive , boolean
	 */
	public void setActive(boolean pActive) {
		this.active = pActive;
	}
	
	/**
	 * Get the last modification date  date.
	 * @return date , The creation date
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Set the last modification date to the current date.
	 */
	public void setDate() {
		this.date = new Date();
	}
}
