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

package com.github.achatain.javawebappauthentication.filter;

import com.github.achatain.javawebappauthentication.service.SessionService;
import com.google.api.client.util.Preconditions;
import org.apache.commons.validator.routines.UrlValidator;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;

@Singleton
public class SessionFilter implements Filter {

    private final SessionService sessionService;
    private String loginUrlRedirect;

    public static final String LOGIN_URL_REDIRECT = "loginUrlRedirect";

    @Inject
    public SessionFilter(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        final String loginUrlInitParameter = filterConfig.getInitParameter(LOGIN_URL_REDIRECT);

        Preconditions.checkArgument(UrlValidator.getInstance().isValid(loginUrlInitParameter), format("The login redirect URL [%s] is invalid", loginUrlInitParameter));

        loginUrlRedirect = loginUrlInitParameter;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        final boolean userLoggedIn = sessionService.isUserLoggedIn(httpServletRequest.getSession());

        if (userLoggedIn) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.sendRedirect(loginUrlRedirect);
        }
    }

    @Override
    public void destroy() {
        // no-op
    }
}
