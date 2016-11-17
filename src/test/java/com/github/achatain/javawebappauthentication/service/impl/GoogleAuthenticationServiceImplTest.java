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
import com.github.achatain.javawebappauthentication.entity.AuthenticationRequest;
import com.github.achatain.javawebappauthentication.exception.AuthenticationException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;

import static com.github.achatain.javawebappauthentication.service.impl.GoogleAuthenticationServiceImpl.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class GoogleAuthenticationServiceImplTest {

    @Mock
    private AuthenticationRequest authenticationRequest;

    @Mock
    private GoogleIdTokenVerifier googleIdTokenVerifier;

    @Mock
    private GoogleIdToken googleIdToken;

    @Spy
    private GoogleIdToken.Payload payload;

    @InjectMocks
    private GoogleAuthenticationServiceImpl googleAuthenticationService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = RuntimeException.class)
    public void shouldExceptWhenTheVerifierExcepts() throws Exception {
        final String token = "1234";
        when(authenticationRequest.getToken()).thenReturn(token);
        when(googleIdTokenVerifier.verify(token)).thenThrow(IOException.class);
        googleAuthenticationService.authenticate(authenticationRequest);
    }

    @Test(expected = AuthenticationException.class)
    public void shouldExceptWhenTheTokenIsInvalid() throws Exception {
        final String token = "invalid";
        when(authenticationRequest.getToken()).thenReturn(token);
        when(googleIdTokenVerifier.verify(token)).thenReturn(null);
        googleAuthenticationService.authenticate(authenticationRequest);
    }

    @Test
    public void shouldAuthenticateSuccessfully() throws Exception {
        final String token = "1234";
        when(authenticationRequest.getToken()).thenReturn(token);
        when(googleIdTokenVerifier.verify(token)).thenReturn(googleIdToken);
        when(googleIdToken.getPayload()).thenReturn(payload);
        doReturn("subject").when(payload).getSubject();
        doReturn("email").when(payload).getEmail();
        doReturn("name").when(payload).getOrDefault(NAME_KEY, DEFAULT);
        doReturn("given name").when(payload).getOrDefault(GIVEN_NAME_KEY, DEFAULT);
        doReturn("family name").when(payload).getOrDefault(FAMILY_NAME_KEY, DEFAULT);
        doReturn("hosted domain").when(payload).getHostedDomain();
        doReturn("picture").when(payload).getOrDefault(PICTURE_KEY, DEFAULT);

        final AuthenticatedUser authenticatedUser = googleAuthenticationService.authenticate(authenticationRequest);
        assertEquals(USER_ID_PREFIX + "subject", authenticatedUser.getId());
        assertEquals("email", authenticatedUser.getEmail());
        assertEquals("name", authenticatedUser.getName());
        assertEquals("given name", authenticatedUser.getGivenName());
        assertEquals("family name", authenticatedUser.getFamilyName());
        assertEquals("hosted domain", authenticatedUser.getHostedDomain());
        assertEquals("picture", authenticatedUser.getPicture());
    }
}
