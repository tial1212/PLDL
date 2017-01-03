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
import cgg.informatique.jfl.labo10.modeles.ListeDeLecture;
import cgg.informatique.jfl.labo10.modeles.Token;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;

import java.util.logging.Logger;

@Singleton
@Lock(LockType.READ)
public class DAOListeLecture extends DAO {
  private static Logger LOGGER = Logger.getLogger(Demarrage.class.getName());

  /**
   * Create a playlist.
   *
   * @param idToken ID of token requesting creation.
   * @param nom Name of the playlist.
   * @param estPublique If playlist is public.
   * @param estActive If playlist is active.
   * 
   * @return token
   */
  public static Token creerListeLecture(int idProprietaire, String nom, boolean estPublique, boolean estActive) {
    LOGGER.info("DAOListesDeLecture.creerListeLecture(" + idProprietaire + ", " + nom + ", " + estPublique + ", " + estActive + ")");

    ListeDeLecture listeLectureCree = new ListeDeLecture(idProprietaire, nom, estPublique, estActive);
    listeLectureCree.setDate();
    persister(listeLectureCree);

    //verify that created object (in DB) are correct
    // delete all if error 
    if (!listeLectureCree.getNom().equals(nom)         || listeLectureCree.getIdProprietaire() != idProprietaire
      || listeLectureCree.estPublique() != estPublique || listeLectureCree.estActive() != estActive)
    {
      enlever(ListeDeLecture.class , listeLectureCree.getId());
      Token token = new Token(false, "erreur création playlist dans DB");
      LOGGER.warning("DAOListesDeLecture.creerListeLecture() ERROR : " + token.getAction());
      
      return token;
    }
    return new Token(true, "création ok");
  }

  /**
   * Modify a playlist
   * <ul>
   * 	<li>Playlist must exist</li>
   *  <li>Playlist's name must be public</li>
   *  <li>Playlist must belong to user</li>
   * </ul>
   * 
   * @param idToken ID of token requesting action
   * @param idListeLecture ID of playlist to modify
   * @param nom Name of the playlist.
   * @param estPublique If playlist is public.
   * @param estActive If playlist is active.
   * 
   * @return token
   */
  public static Token modifierListeLecture(int idListeLecture, String nom, Boolean estPublique, Boolean estActive) {
    LOGGER.info("DAOListesDeLecture.modifierListeLecture(" + idListeLecture + ", " + nom + ", " + estPublique + ", " + estActive + ")");

    ListeDeLecture listeLectureModifiee = trouverParId(ListeDeLecture.class, idListeLecture);
    listeLectureModifiee.setDate();
    if (nom != null)
      listeLectureModifiee.setNom(nom);
    if (estPublique != null)
      listeLectureModifiee.setPublique(estPublique);
    if (estActive != null)
      listeLectureModifiee.setActive(estActive);
    
    ListeDeLecture listeLectureRetour = modifier(listeLectureModifiee);

    // test if modification ok
    if (nom != null && !listeLectureRetour.getNom().equals(nom)
     || estPublique != null && listeLectureRetour.estPublique() != estPublique
     || estActive != null && listeLectureRetour.estActive() != estActive) {
      Token token = new Token(false, "erreur modification utilisateur dans DB");
      LOGGER.info("DAOListesDeLecture->modify() ERROR : " + token.getAction());

      return token;
    }

    return new Token(true, "Modifications à la liste de lecture effectuées");
  }
}
