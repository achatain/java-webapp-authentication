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

package com.github.achatain.javawebappauthentication.service;

import com.github.achatain.javawebappauthentication.entity.AuthenticatedUser;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public interface SessionService {

    boolean isUserLoggedIn(HttpSession session, String redirectUrl);

    Optional<AuthenticatedUser> getUserFromSession(HttpSession session);

    void putUserInSession(HttpSession session, AuthenticatedUser authenticatedUser);

    void invalidateSession(HttpSession session);

    String popOriginalRequestUrl(HttpSession session);
}
