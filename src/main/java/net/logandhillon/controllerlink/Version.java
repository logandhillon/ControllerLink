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

public record Version(String author, String name, String environment, String version) {
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
        return author + "," + name + "," + environment + "," + version;
    }
}
