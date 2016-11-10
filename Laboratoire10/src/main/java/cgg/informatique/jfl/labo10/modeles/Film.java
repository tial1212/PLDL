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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
   @NamedQuery(name = "film.list", query = "select f from Film f")
              })
@XmlRootElement(name = "film")
public class Film extends Modele {

    @NotNull
    @Size(min = 3, max = 15)
    private String titre 		= new String();
    
    @NotNull
    private String affiche 		= new String();
    
    @NotNull
    private String bandeAnnonce = new String();
    
    @NotNull
	private String resume 		= new String();
    
    public Film() {
    // must have a no-args constructor
	}
    
    public Film(String titre, String affiche, String bandeAnnonce, String resume)
	{
		this.titre = titre;
		this.affiche = affiche;
		this.bandeAnnonce = bandeAnnonce;
		this.resume = resume;
	}
    

	public String getTitre() {
		return this.titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}

	
	
	public String getAffiche() {
		return this.affiche;
	}
	public void setAffiche(String affiche) {
		this.affiche = affiche;
	}
	
	
	public String getBandeAnnonce() {
		return this.bandeAnnonce;
	}
	public void setBandeAnnonce(String bandeAnnonce) {
		this.bandeAnnonce = bandeAnnonce;
	}
	
	
	public String getResume() {
		return this.resume;
	}
	public void setResume(String resume) {
		this.resume = resume;
	}
	
}
