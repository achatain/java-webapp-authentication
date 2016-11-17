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
import com.github.achatain.javawebappauthentication.service.SessionService;

import javax.servlet.http.HttpSession;

public class SessionServiceImpl implements SessionService {

    static final String SESSION_IS_USER_LOGGED_IN = "IsUserLoggedIn";
    static final String SESSION_LOGGED_IN_USER = "LoggedInUser";

    @Override
    public boolean isUserLoggedIn(final HttpSession session) {
        return (boolean) session.getAttribute(SESSION_IS_USER_LOGGED_IN);
    }

    @Override
    public AuthenticatedUser getUserFromSession(final HttpSession session) {
        return (AuthenticatedUser) session.getAttribute(SESSION_LOGGED_IN_USER);
    }
}
