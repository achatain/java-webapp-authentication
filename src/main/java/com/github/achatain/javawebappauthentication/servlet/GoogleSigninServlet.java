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

package com.github.achatain.javawebappauthentication.servlet;

import com.github.achatain.javawebappauthentication.entity.AuthenticatedUser;
import com.github.achatain.javawebappauthentication.entity.AuthenticationRequest;
import com.github.achatain.javawebappauthentication.service.AuthenticationService;
import com.github.achatain.javawebappauthentication.service.SessionService;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Singleton
public class GoogleSigninServlet extends HttpServlet {

    private final transient Gson gson;
    private final transient AuthenticationService authenticationService;
    private final transient SessionService sessionService;

    @Inject
    private GoogleSigninServlet(final Gson gson, final AuthenticationService authenticationService, final SessionService sessionService) {
        this.gson = gson;
        this.authenticationService = authenticationService;
        this.sessionService = sessionService;
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final AuthenticationRequest authenticationRequest = gson.fromJson(req.getReader(), AuthenticationRequest.class);
        Preconditions.checkArgument(Objects.nonNull(authenticationRequest), "Invalid request");
        final AuthenticatedUser authenticatedUser = authenticationService.authenticate(authenticationRequest);
        sessionService.putUserInSession(req.getSession(), authenticatedUser);
        resp.getWriter().write(gson.toJson(authenticatedUser));
    }
}
