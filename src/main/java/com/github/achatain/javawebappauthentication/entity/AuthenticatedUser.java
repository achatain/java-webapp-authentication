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

public class AuthenticatedUser {

    private String id;
    private String email;
    private String name;
    private String givenName;
    private String familyName;
    private String hostedDomain;
    private String picture;

    private AuthenticatedUser() {
        // no-op
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getHostedDomain() {
        return hostedDomain;
    }

    public String getPicture() {
        return picture;
    }

    @Override
    public String toString() {
        return "AuthenticatedUser{" + "id='" + id + '\'' + ", email='" + email + '\'' + ", name='" + name + '\'' + ", givenName='" + givenName + '\'' + ", familyName='" + familyName + '\'' + ", hostedDomain='" + hostedDomain + '\'' + ", picture='" + picture + '\'' + '}';
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String email;
        private String name;
        private String givenName;
        private String familyName;
        private String hostedDomain;
        private String picture;

        private Builder() {
        }

        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        public Builder withEmail(final String email) {
            this.email = email;
            return this;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withGivenName(final String givenName) {
            this.givenName = givenName;
            return this;
        }

        public Builder withFamilyName(final String familyName) {
            this.familyName = familyName;
            return this;
        }

        public Builder withHostedDomain(final String hostedDomain) {
            this.hostedDomain = hostedDomain;
            return this;
        }

        public Builder withPicture(final String picture) {
            this.picture = picture;
            return this;
        }

        public AuthenticatedUser build() {
            final AuthenticatedUser authenticatedUser = new AuthenticatedUser();
            authenticatedUser.id = id;
            authenticatedUser.email = email;
            authenticatedUser.name = name;
            authenticatedUser.givenName = givenName;
            authenticatedUser.familyName = familyName;
            authenticatedUser.hostedDomain = hostedDomain;
            authenticatedUser.picture = picture;
            return authenticatedUser;
        }
    }
}
