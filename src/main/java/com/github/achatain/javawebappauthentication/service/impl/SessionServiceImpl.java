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
import com.github.achatain.javawebappauthentication.service.SessionService;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.logging.Logger;

import static java.lang.String.format;

public class SessionServiceImpl implements SessionService {

    private static final Logger LOG = Logger.getLogger(SessionServiceImpl.class.getName());

    static final String SESSION_IS_USER_LOGGED_IN = "IsUserLoggedIn";
    static final String SESSION_LOGGED_IN_USER = "LoggedInUser";
    static final String SESSION_REDIRECT_URL = "RedirectUrl";

    @Override
    public boolean isUserLoggedIn(final HttpSession session, final String redirectUrl) {
        LOG.info("Checking if a user is found in the session...");
        final Optional<Boolean> attribute = Optional.ofNullable((Boolean) session.getAttribute(SESSION_IS_USER_LOGGED_IN));
        final Boolean userLoggedIn = attribute.orElse(false);
        if (userLoggedIn) {
            LOG.info("A user was found in the session");
        } else {
            LOG.info(format("No user was found in the session. Upon successful login, user will be redirected to [%s]", redirectUrl));
            session.setAttribute(SESSION_REDIRECT_URL, redirectUrl);
        }
        return userLoggedIn;
    }

    @Override
    public Optional<AuthenticatedUser> getUserFromSession(final HttpSession session) {
        final Optional<AuthenticatedUser> optionalAuthenticatedUser = Optional.ofNullable((AuthenticatedUser) session.getAttribute(SESSION_LOGGED_IN_USER));
        optionalAuthenticatedUser.ifPresent(user -> LOG.info(format("User found in session is [%s]", user)));
        return optionalAuthenticatedUser;
    }

    @Override
    public void putUserInSession(final HttpSession session, final AuthenticatedUser authenticatedUser) {
        LOG.info(format("Putting user in session [%s]", authenticatedUser));
        session.setAttribute(SESSION_IS_USER_LOGGED_IN, true);
        session.setAttribute(SESSION_LOGGED_IN_USER, authenticatedUser);
    }

    @Override
    public void invalidateSession(final HttpSession session) {
        LOG.info("Invalidating the session");
        session.invalidate();
    }

    @Override
    public String popOriginalRequestUrl(final HttpSession session) {
        String sessionRedirectUrl = (String) session.getAttribute(SESSION_REDIRECT_URL);
        LOG.info(format("Pop original request out of session [%s]", sessionRedirectUrl));
        session.removeAttribute(SESSION_REDIRECT_URL);
        return sessionRedirectUrl;
    }
}
