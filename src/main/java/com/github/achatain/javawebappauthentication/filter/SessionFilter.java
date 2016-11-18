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

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.Objects.nonNull;

@Singleton
public class SessionFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(SessionFilter.class.getName());

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
        Preconditions.checkArgument(nonNull(loginUrlInitParameter) && !loginUrlInitParameter.trim().isEmpty(), format("The login redirect URL [%s] is empty", loginUrlInitParameter));
        loginUrlRedirect = loginUrlInitParameter;
        LOG.info(format("SessionFilter initialised with the following login url to redirect to when no user is logged in [%s]", loginUrlRedirect));
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        final boolean userLoggedIn = sessionService.isUserLoggedIn(httpServletRequest.getSession(), httpServletRequest.getRequestURI());

        if (userLoggedIn) {
            LOG.info("User is logged in, hence the request can proceed.");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            LOG.info(format("No user is logged in, hence redirecting to the login url [%s]", loginUrlRedirect));
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.sendRedirect(loginUrlRedirect);
        }
    }

    @Override
    public void destroy() {
        // no-op
    }
}
