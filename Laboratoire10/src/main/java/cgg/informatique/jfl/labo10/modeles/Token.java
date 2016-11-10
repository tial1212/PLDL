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

@Entity
@NamedQueries({
   @NamedQuery(name = "token.list", query = "select t from Token t")
              })
@XmlRootElement(name = "token")
public class Token extends Modele {

	
	private String nom;

    private String couriel;
    
    private String mdp;
    
    private String captchaVal;
    
    
    private String action;
    
    private String salt;
    
	private Boolean etat;
	
	public Token() {
	    // must have a no-args constructor
	}
    


	public String getNom() {
		return nom;
	}
	
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getCouriel() {
		return couriel;
	}

	public void setCouriel(String couriel) {
		this.couriel = couriel;
	}

	public String getMdp() {
		return mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

	public String getCaptchaVal() {
		return captchaVal;
	}

	public void setCaptchaVal(String captchaImg) {
		this.captchaVal = captchaImg;
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
