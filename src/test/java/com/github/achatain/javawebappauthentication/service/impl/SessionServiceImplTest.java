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

package com.github.achatain.javawebappauthentication.service.impl;

import com.github.achatain.javawebappauthentication.entity.AuthenticatedUser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpSession;

import java.util.Optional;

import static com.github.achatain.javawebappauthentication.service.impl.SessionServiceImpl.SESSION_IS_USER_LOGGED_IN;
import static com.github.achatain.javawebappauthentication.service.impl.SessionServiceImpl.SESSION_LOGGED_IN_USER;
import static com.github.achatain.javawebappauthentication.service.impl.SessionServiceImpl.SESSION_REDIRECT_URL;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SessionServiceImplTest {

    @Mock
    private HttpSession session;

    private SessionServiceImpl sessionService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sessionService = new SessionServiceImpl();
    }

    @Test
    public void shouldCheckIfUserIsLoggedIn() throws Exception {
        when(session.getAttribute(SESSION_IS_USER_LOGGED_IN)).thenReturn(true);
        assertThat(sessionService.isUserLoggedIn(session, ""), is(true));
    }

    @Test
    public void shouldReturnFalseWhenNoUserLoggedInAttributeIsFoundInSession() throws Exception {
        when(session.getAttribute(SESSION_IS_USER_LOGGED_IN)).thenReturn(null);
        assertThat(sessionService.isUserLoggedIn(session, ""), is(false));
    }

    @Test
    public void shouldGetUserFromSession() throws Exception {
        final AuthenticatedUser authenticatedUser = AuthenticatedUser.create().build();
        when(session.getAttribute(SESSION_LOGGED_IN_USER)).thenReturn(authenticatedUser);
        final Optional<AuthenticatedUser> optionalUser = sessionService.getUserFromSession(session);
        assertThat(optionalUser.isPresent(), is(true));
        assertThat(optionalUser.get(), sameInstance(authenticatedUser));
    }

    @Test
    public void shouldNotGetUserFromSession() throws Exception {
        when(session.getAttribute(SESSION_LOGGED_IN_USER)).thenReturn(null);
        final Optional<AuthenticatedUser> optionalUser = sessionService.getUserFromSession(session);
        assertThat(optionalUser.isPresent(), is(false));
    }

    @Test
    public void shouldPutUserInSession() throws Exception {
        final AuthenticatedUser authenticatedUser = AuthenticatedUser.create().withId("1234").build();
        sessionService.putUserInSession(session, authenticatedUser);
        verify(session).setAttribute(SESSION_IS_USER_LOGGED_IN, true);
        verify(session).setAttribute(SESSION_LOGGED_IN_USER, authenticatedUser);
    }

    @Test
    public void shouldInvalidateSession() throws Exception {
        sessionService.invalidateSession(session);
        verify(session).invalidate();
    }

    @Test
    public void shouldPopOriginalRequestUrl() throws Exception {
        final String originalRequestUrl = "www.myresource.com";
        when(session.getAttribute(SESSION_REDIRECT_URL)).thenReturn(originalRequestUrl);
        assertThat(sessionService.popOriginalRequestUrl(session), equalTo(originalRequestUrl));
        verify(session).getAttribute(SESSION_REDIRECT_URL);
        verify(session).removeAttribute(SESSION_REDIRECT_URL);
    }

}
