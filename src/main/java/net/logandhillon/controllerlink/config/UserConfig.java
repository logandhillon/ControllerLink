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

package net.logandhillon.controllerlink.config;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public record UserConfig(String keystorePath, String keystorePassword) {
    private static final String PATH = "user_config.json";
    public static final UserConfig INSTANCE;

    static {
        try {
            INSTANCE = get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the user config from the constant file location
     * @return {@link UserConfig} object
     * @throws IOException Failed to read config JSON file
     */
    private static UserConfig get() throws IOException {
        return new Gson().fromJson(FileUtils.readFileToString(new File(PATH), StandardCharsets.UTF_8), UserConfig.class);
    }

    /**
     * Validates this {@link UserConfig} object
     * @return if all the keys in this config are valid
     */
    public boolean validate() {
        return keystorePath != null && new File(keystorePath).exists()
                && keystorePassword != null;
    }
}
