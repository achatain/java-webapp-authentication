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

package com.github.achatain.javawebappauthentication.servlet;

import com.github.achatain.javawebappauthentication.entity.AuthenticatedUser;
import com.github.achatain.javawebappauthentication.entity.AuthenticationRequest;
import com.github.achatain.javawebappauthentication.service.AuthenticationService;
import com.github.achatain.javawebappauthentication.service.SessionService;
import com.google.api.client.util.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static com.google.common.io.Resources.getResource;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GoogleSigninServletTest {

    @Captor
    ArgumentCaptor<AuthenticationRequest> authenticationRequestCaptor;

    @Spy
    private Gson gson;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private SessionService sessionService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private GoogleSigninServlet googleSigninServlet;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldAuthenticateAndRespondWithAnAuthenticatedUserInstance() throws Exception {
        final String json = Resources.toString(getResource("authenticationRequest.json"), Charsets.UTF_8);
        when(httpServletRequest.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        final AuthenticatedUser authenticatedUser = AuthenticatedUser.create().withId("test_id").build();
        when(authenticationService.authenticate(authenticationRequestCaptor.capture())).thenReturn(authenticatedUser);
        final StringWriter stringWriter = new StringWriter();
        when(httpServletResponse.getWriter()).thenReturn(new PrintWriter(stringWriter));
        googleSigninServlet.doPost(httpServletRequest, httpServletResponse);
        verify(sessionService).putUserInSession(httpServletRequest.getSession(), authenticatedUser);
        assertThat(authenticationRequestCaptor.getValue().getToken(), equalTo("1234"));
        assertThat(stringWriter.toString(), equalTo("{\"id\":\"test_id\"}"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldExceptWhenTheRequestBodyIsInvalid() throws Exception {
        when(httpServletRequest.getReader()).thenReturn(new BufferedReader(new StringReader("")));
        googleSigninServlet.doPost(httpServletRequest, httpServletResponse);
    }
}
