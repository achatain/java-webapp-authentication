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

package com.github.achatain.javawebappauthentication.filter;

import com.github.achatain.javawebappauthentication.service.SessionService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

public class SessionFilterTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private FilterConfig filterConfig;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private HttpSession httpSession;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private SessionFilter sessionFilter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToInitSessionFilterWithEmptyLoginRedirectUrl() throws Exception {
        when(filterConfig.getInitParameter(SessionFilter.LOGIN_URL_REDIRECT)).thenReturn("");
        sessionFilter.init(filterConfig);
    }

    @Test
    public void shouldDoFilterWhenUserIsLoggedIn() throws Exception {
        final String requestUrl = "www.test.com";
        final StringBuffer sb = new StringBuffer(requestUrl);
        when(httpServletRequest.getRequestURL()).thenReturn(sb);
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(sessionService.isUserLoggedIn(httpSession, requestUrl)).thenReturn(true);
        sessionFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }

    @Test
    public void shouldRedirectToLoginUrlWhenNoUserIsLoggedIn() throws Exception {
        final String loginUrlRedirect = "https://test.com/auth";
        when(filterConfig.getInitParameter(SessionFilter.LOGIN_URL_REDIRECT)).thenReturn(loginUrlRedirect);
        sessionFilter.init(filterConfig);
        final String requestUrl = "www.test.com";
        final StringBuffer sb = new StringBuffer(requestUrl);
        when(httpServletRequest.getRequestURL()).thenReturn(sb);
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(sessionService.isUserLoggedIn(httpSession, requestUrl)).thenReturn(false);
        sessionFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
        verify(httpServletResponse).sendRedirect(loginUrlRedirect);
    }

    @Test
    public void shouldDoNothingWhenInvokingDestroy() throws Exception {
        sessionFilter.destroy();
        verifyZeroInteractions(sessionService);
    }

}
