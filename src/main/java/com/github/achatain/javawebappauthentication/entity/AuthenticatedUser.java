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

package com.github.achatain.javawebappauthentication.entity;

public class AuthenticatedUser {

    private String id;
    private String email;

    private AuthenticatedUser() {
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String email;

        private Builder() {
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public AuthenticatedUser build() {
            AuthenticatedUser authenticatedUser = new AuthenticatedUser();
            authenticatedUser.id = id;
            authenticatedUser.email = email;
            return authenticatedUser;
        }
    }
}
