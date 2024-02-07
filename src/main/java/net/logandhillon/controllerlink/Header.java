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

import net.logandhillon.controllerlink.core.Main;

public final class Header {
    public final String vendor;
    public final String brand;
    public final String environment;
    public final String version;

    private Header(String vendor, String brand, String environment, String version) {
        this.vendor = vendor;
        this.brand = brand;
        this.environment = environment;
        this.version = version;
    }

    public Header(String environment) {
        this.vendor = "logandhillon";
        this.brand = "ControllerLink";
        this.environment = environment;
        this.version = "0.1.0-dev";
    }

    public static class Environment {
        public static final String CLIENT = "client";
        public static final String SERVER = "server";
    }

    public static Header fromString(String s) {
        String[] parts = s.split(",");
        if (parts.length < 3) return null;
        return new Header(parts[0], parts[1], parts[2], parts[3]);
    }

    @Override
    public String toString() {
        return vendor + "," + brand + "," + environment + "," + version;
    }

    public boolean isInvalid(String expectedEnvironment) {
        if (Main.strictHeaders) {
            return !vendor.equals("logandhillon") || !brand.equals("ControllerLink") || !environment.equals(expectedEnvironment);
        } else {
            return !environment.equals(expectedEnvironment);
        }
    }
}
