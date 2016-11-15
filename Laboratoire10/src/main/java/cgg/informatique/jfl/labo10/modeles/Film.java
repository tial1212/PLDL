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

import javax.persistence.Column;
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
	
	/**
	 * The title.
	 */
    @NotNull
    @Size(min = 3, max = 15)
    private String title 		= new String();
    
    /**
	 * An artwork selling the movie.
	 */
    @NotNull
    @Column(name = "Affiche")
    private String coverArt 		= new String();
    
    /**
	 * An URL to the video of the trailler (a small teaser to incite to watch the movie ).
	 */
    @NotNull
    @Column(name = "BandeAnnonce")
    private String trailler = new String();
    
    /**
	 * A small text that explain the movie's plot.
	 */
    @NotNull
    @Column(name = "Resume")
    private String resume 		= new String();
    
    
    /**
     *  DO NOT USE it is useless 
     * "Unenhanced classes must have a public or protected no-args constructor"
     */
    public Film() { }
    
    
    /**
     * Use this constructor instead.
     * 4 param constructor.
     * 
     * @param pTitle
     * @param pCoverArt
     * @param pTrailler
     * @param pResume
     */
    public Film(String pTitle, String pCoverArt, String pTrailler, String pResume)
	{
    	
		this.title = pTitle;
		this.coverArt = pCoverArt;
		this.trailler = pTrailler;
		this.resume = pResume;
	}
    
    /**
     * Get the movie's title.
     * @return titre
     */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Set the movie's title.
	 * @param pTitle
	 */
	public void setTitle(String pTitle) {
		this.title = pTitle;
	}
	
	/**
	 * Get the movie's Cover Art.
	 * @return coverArt
	 */
	public String getCoverArt() {
		return this.coverArt;
	}
	
	/**
	 * Set the movie's Cover Art.
	 * @param pCoverArt
	 */
	public void setCoverArt(String pCoverArt) {
		this.coverArt = pCoverArt;
	}
	
	/**
	 * Get the movie's trailler.
	 * @return trailler
	 */
	public String getTrailler() {
		return this.trailler;
	}
	
	/**
	 * Set the movie's trailler.
	 * @param pTrailler
	 */
	public void setTrailler(String pTrailler) {
		this.trailler = pTrailler;
	}
	
	/**
	 * Get the movie's resume.
	 * @return
	 */
	public String getResume() {
		return this.resume;
	}
	
	/**
	 * Set the movie's resume.
	 * @param pResume
	 */
	public void setResume(String pResume) {
		this.resume = pResume;
	}
	
}
