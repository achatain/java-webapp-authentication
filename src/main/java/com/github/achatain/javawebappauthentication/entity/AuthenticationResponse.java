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

package com.github.achatain.javawebappauthentication.entity;

public class AuthenticationResponse {
    private String originalRequestUrl;
    private AuthenticatedUser authenticatedUser;

    public String getOriginalRequestUrl() {
        return originalRequestUrl;
    }

    public AuthenticatedUser getAuthenticatedUser() {
        return authenticatedUser;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private String originalRequestUrl;
        private AuthenticatedUser authenticatedUser;

        private Builder() {
        }

        public Builder withOriginalRequestUrl(final String originalRequestUrl) {
            this.originalRequestUrl = originalRequestUrl;
            return this;
        }

        public Builder withAuthenticatedUser(final AuthenticatedUser authenticatedUser) {
            this.authenticatedUser = authenticatedUser;
            return this;
        }

        public AuthenticationResponse build() {
            final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.originalRequestUrl = originalRequestUrl;
            authenticationResponse.authenticatedUser = authenticatedUser;
            return authenticationResponse;
        }
    }
}
