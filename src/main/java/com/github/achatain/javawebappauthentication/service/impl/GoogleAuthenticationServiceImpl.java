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
import com.github.achatain.javawebappauthentication.service.AuthenticationService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static java.util.Objects.isNull;

public class GoogleAuthenticationServiceImpl implements AuthenticationService {

    private static final String ISSUER = "accounts.google.com";
    private static final String USER_ID_PREFIX = "goog-";
    private static final String NAME_KEY = "name";
    private static final String GIVEN_NAME_KEY = "given_name";
    private static final String FAMILY_NAME_KEY = "family_name";
    private static final String PICTURE_KEY = "picture";
    private static final String DEFAULT = "";

    @Override
    public AuthenticatedUser authenticate(final AuthenticationRequest authenticationRequest) throws AuthenticationException {
        GoogleIdToken idToken;

        try {
            final HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory).setIssuer(ISSUER).build();

            idToken = verifier.verify(authenticationRequest.getToken());
        }
        catch (GeneralSecurityException | IOException e) {
            throw new AuthenticationException("Authentication failed", e);
        }

        if (isNull(idToken)) {
            throw new AuthenticationException("Invalid authentication token");
        }

        final GoogleIdToken.Payload payload = idToken.getPayload();

        return AuthenticatedUser
                .create()
                .withId(USER_ID_PREFIX + payload.getSubject())
                .withEmail(payload.getEmail())
                .withName((String) payload.getOrDefault(NAME_KEY, DEFAULT))
                .withGivenName((String) payload.getOrDefault(GIVEN_NAME_KEY, DEFAULT))
                .withFamilyName((String) payload.getOrDefault(FAMILY_NAME_KEY, DEFAULT))
                .withHostedDomain(payload.getHostedDomain())
                .withPicture((String) payload.getOrDefault(PICTURE_KEY, DEFAULT))
                .build();
    }
}
