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
import com.github.achatain.javawebappauthentication.entity.AuthenticationRequest;
import com.github.achatain.javawebappauthentication.exception.AuthenticationException;
import com.github.achatain.javawebappauthentication.service.AuthenticationService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import javax.inject.Inject;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.Objects.isNull;

public class GoogleAuthenticationServiceImpl implements AuthenticationService {

    private static final Logger LOG = Logger.getLogger(GoogleAuthenticationServiceImpl.class.getName());

    private final GoogleIdTokenVerifier verifier;

    static final String USER_ID_PREFIX = "goog-";
    static final String NAME_KEY = "name";
    static final String GIVEN_NAME_KEY = "given_name";
    static final String FAMILY_NAME_KEY = "family_name";
    static final String PICTURE_KEY = "picture";
    static final String DEFAULT = "";

    @Inject
    private GoogleAuthenticationServiceImpl(final GoogleIdTokenVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public AuthenticatedUser authenticate(final AuthenticationRequest authenticationRequest) throws AuthenticationException {
        final String token = authenticationRequest.getToken();

        GoogleIdToken idToken;

        try {
            LOG.info(format("Attempting to verify token [%s]", token));
            idToken = verifier.verify(token);
        }
        catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(format("Could not verify the token [%s]", token), e);
        }

        if (isNull(idToken)) {
            throw new AuthenticationException(format("Invalid authentication token [%s]", token));
        }

        final GoogleIdToken.Payload payload = idToken.getPayload();

        AuthenticatedUser authenticatedUser = AuthenticatedUser
                .create()
                .withId(USER_ID_PREFIX + payload.getSubject())
                .withEmail(payload.getEmail())
                .withName((String) payload.getOrDefault(NAME_KEY, DEFAULT))
                .withGivenName((String) payload.getOrDefault(GIVEN_NAME_KEY, DEFAULT))
                .withFamilyName((String) payload.getOrDefault(FAMILY_NAME_KEY, DEFAULT))
                .withHostedDomain(payload.getHostedDomain())
                .withPicture((String) payload.getOrDefault(PICTURE_KEY, DEFAULT))
                .build();
        LOG.info(format("Token successfully verified, matching user is [%s]", authenticatedUser));

        return authenticatedUser;
    }
}
