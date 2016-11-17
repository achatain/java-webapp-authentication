/*
 * https://github.com/achatain/java-webapp-authentication
 *
 * Copyright (C) 2016 Antoine R. "achatain" (achatain [at] outlook [dot] com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.achatain.javawebappauthentication.entity;

import org.junit.Test;

import static org.junit.Assert.*;

public class AuthenticatedUserTest {

    @Test
    public void getId() throws Exception {
        assertEquals("id", AuthenticatedUser.create().withId("id").build().getId());
    }

    @Test
    public void getEmail() throws Exception {
        assertEquals("email", AuthenticatedUser.create().withEmail("email").build().getEmail());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("name", AuthenticatedUser.create().withName("name").build().getName());
    }

    @Test
    public void getGivenName() throws Exception {
        assertEquals("given name", AuthenticatedUser.create().withGivenName("given name").build().getGivenName());
    }

    @Test
    public void getFamilyName() throws Exception {
        assertEquals("family name", AuthenticatedUser.create().withFamilyName("family name").build().getFamilyName());
    }

    @Test
    public void getHostedDomain() throws Exception {
        assertEquals("hosted domain", AuthenticatedUser.create().withHostedDomain("hosted domain").build().getHostedDomain());
    }

    @Test
    public void getPicture() throws Exception {
        assertEquals("picture", AuthenticatedUser.create().withPicture("picture").build().getPicture());
    }

}
