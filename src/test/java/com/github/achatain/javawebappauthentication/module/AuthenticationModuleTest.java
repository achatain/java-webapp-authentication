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
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class AuthenticationModuleTest {

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new AuthenticationModule());
    }

    @Test
    public void shouldEnsureDIBindingsAreAsExpected() throws Exception {
        assertThat(injector.getProvider(AuthenticationService.class).get(), instanceOf(GoogleAuthenticationServiceImpl.class));
        assertThat(injector.getProvider(SessionService.class).get(), instanceOf(SessionServiceImpl.class));
    }

    @Test
    public void shouldEnsureGsonProviderReturnsASingleton() throws Exception {
        final Gson gson1 = injector.getProvider(Gson.class).get();
        final Gson gson2 = injector.getProvider(Gson.class).get();
        assertThat(gson1, sameInstance(gson2));
    }

    @Test
    public void shouldProvideGoogleIdTokenVerifier() throws Exception {
        final GoogleIdTokenVerifier verifier = injector.getProvider(GoogleIdTokenVerifier.class).get();
        assertThat(verifier.getTransport(), instanceOf(NetHttpTransport.class));
        assertThat(verifier.getJsonFactory(), instanceOf(JacksonFactory.class));
    }
}
