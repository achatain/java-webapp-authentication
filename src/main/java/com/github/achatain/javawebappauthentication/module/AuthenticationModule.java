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

package com.github.achatain.javawebappauthentication.module;

import com.github.achatain.javawebappauthentication.service.AuthenticationService;
import com.github.achatain.javawebappauthentication.service.SessionService;
import com.github.achatain.javawebappauthentication.service.impl.GoogleAuthenticationServiceImpl;
import com.github.achatain.javawebappauthentication.service.impl.SessionServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AuthenticationModule extends AbstractModule {

    private static final String ISSUER = "accounts.google.com";
    private static final GsonBuilder GSON_BUILDER = new GsonBuilder();

    @Override
    protected void configure() {
        bind(AuthenticationService.class).to(GoogleAuthenticationServiceImpl.class);
        bind(SessionService.class).to(SessionServiceImpl.class);
    }

    @Provides
    @Singleton
    private Gson provideGson() {
        return GSON_BUILDER.create();
    }

    @Provides
    private GoogleIdTokenVerifier provideGoogleIdTokenVerifier() {
        try {
            final HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            return new GoogleIdTokenVerifier.Builder(transport, jsonFactory).setIssuer(ISSUER).build();
        }
        catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Could not create a GoogleIdTokenVerifier", e);
        }
    }
}
