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

package com.github.achatain.javawebappauthentication.service.impl;

import com.github.achatain.javawebappauthentication.entity.AuthenticatedUser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpSession;

import static com.github.achatain.javawebappauthentication.service.impl.SessionServiceImpl.SESSION_IS_USER_LOGGED_IN;
import static com.github.achatain.javawebappauthentication.service.impl.SessionServiceImpl.SESSION_LOGGED_IN_USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class SessionServiceImplTest {

    @Mock
    private HttpSession session;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isUserLoggedIn() throws Exception {
        when(session.getAttribute(SESSION_IS_USER_LOGGED_IN)).thenReturn(true);
        assertTrue(new SessionServiceImpl().isUserLoggedIn(session));
    }

    @Test
    public void getUserFromSession() throws Exception {
        final AuthenticatedUser authenticatedUser = AuthenticatedUser.create().build();
        when(session.getAttribute(SESSION_LOGGED_IN_USER)).thenReturn(authenticatedUser);
        assertEquals(authenticatedUser, new SessionServiceImpl().getUserFromSession(session));
    }

}
