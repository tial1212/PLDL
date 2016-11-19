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
package com.informatique.jfl.labo10.testdao;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.tomee.embedded.EmbeddedTomEEContainer;
import org.apache.ziplock.Archive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cgg.informatique.jfl.labo10.dao.DAOUtilisateur;
import cgg.informatique.jfl.labo10.modeles.Utilisateur;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.apache.openejb.loader.JarLocation.jarLocation;

public class ServiceUtilisateurTest {

    private static EJBContainer container;

    @BeforeClass
    public static void start() throws IOException {
        final File webApp = Archive.archive().copyTo("WEB-INF/classes", jarLocation(DAOUtilisateur.class)).asDir();
        final Properties p = new Properties();
        p.setProperty(EJBContainer.APP_NAME, "Laboratoire10");
        p.setProperty(EJBContainer.PROVIDER, "tomee-embedded"); // need web feature
        p.setProperty(EJBContainer.MODULES, webApp.getAbsolutePath());
        p.setProperty(EmbeddedTomEEContainer.TOMEE_EJBCONTAINER_HTTP_PORT, "-1"); // random port
        container = EJBContainer.createEJBContainer(p);
    }

    @AfterClass
    public static void stop() {
        if (container != null) {
            container.close();
        }
    }

    @Test
    public void create() throws NamingException {
        final DAOUtilisateur dao = (DAOUtilisateur) container.getContext().lookup("java:global/rest-example/UserDAO");
        final Utilisateur user = dao.creer("foo", "dummy", "foo@dummy.org");
        assertNotNull(dao.rechercher(user.getId()));

        final String uri = "http://127.0.0.1:" + System.getProperty(EmbeddedTomEEContainer.TOMEE_EJBCONTAINER_HTTP_PORT) + "/rest-example";
        final UserServiceClientAPI client = JAXRSClientFactory.create(uri, UserServiceClientAPI.class);
        final Utilisateur retrievedUser = client.show(user.getId());
        assertNotNull(retrievedUser);
        assertEquals("dummy", retrievedUser.getPasowrd());
        assertEquals("foo@dummy.org", retrievedUser.getEMaill() );
    }

    /**
     * a simple copy of the unique method i want to use from my service.
     * It allows to use cxf proxy to call remotely our rest service.
     * Any other way to do it is good.
     */
    @Path("/api/user")
    @Produces({"text/xml", "application/json"})
    public static interface UserServiceClientAPI {

        @Path("/show/{id}")
        @GET
        Utilisateur show(@PathParam("id") long id);
    }
}
