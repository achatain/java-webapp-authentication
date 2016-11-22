/*
 * https://github.com/achatain/java-webapp-authentication
 *
 * Copyright (C) 2016 Antoine Chatain (achatain [at] outlook [dot] com)
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class AuthenticationResponseTest {

    @Test
    public void shouldGetOriginalRequestUrl() throws Exception {
        assertThat(AuthenticationResponse.create().withOriginalRequestUrl("www.test.com").build().getOriginalRequestUrl(), equalTo("www.test.com"));
    }

    @Test
    public void shouldGeAuthenticatedUser() throws Exception {
        final AuthenticatedUser authenticatedUser = AuthenticatedUser.create().withId("1").build();
        assertThat(AuthenticationResponse.create().withAuthenticatedUser(authenticatedUser).build().getAuthenticatedUser(), sameInstance(authenticatedUser));
    }

}
