/*
 * Controller Link, stream controller I/O data across machines.
 * Copyright (C) 2024 Logan Dhillon
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.logandhillon.controllerlink;

public final class Version {
    public final String vendor;
    public final String name;
    public final String environment;
    public final String version;

    private Version(String vendor, String name, String environment, String version) {
        this.vendor = vendor;
        this.name = name;
        this.environment = environment;
        this.version = version;
    }

    public Version(String environment) {
        this.vendor = "logandhillon";
        this.name = "ControllerLink";
        this.environment = environment;
        this.version = "0.1.0-dev";
    }

    public static class Environment {
        public static final String CLIENT = "client";
        public static final String SERVER = "server";
    }

    public Version fromString(String s) {
        String[] parts = s.split(",");
        if (parts.length < 3) return null;
        return new Version(parts[0], parts[1], parts[2], parts[3]);
    }

    @Override
    public String toString() {
        return vendor + "," + name + "," + environment + "," + version;
    }
}
