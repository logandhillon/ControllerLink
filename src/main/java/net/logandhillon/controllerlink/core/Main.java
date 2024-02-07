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

package net.logandhillon.controllerlink.core;

import net.logandhillon.controllerlink.client.ClientMain;
import net.logandhillon.controllerlink.server.ServerMain;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

public final class Main {
    private static final Logger LOG = LoggerContext.getContext().getLogger(Main.class);

    public static void main(String[] args) {
        boolean missingEntrypoint = true;
        for (String arg: args) {
            if (arg.equals("--server") || arg.equals("-S")) {
                ServerMain.start(args);
                missingEntrypoint = false;
                break;
            } else if (arg.equals("--client") || arg.equals("-C")) {
                ClientMain.start(args);
                missingEntrypoint = false;
                break;
            }
        }

        if (missingEntrypoint)
            LOG.fatal("Couldn't find entrypoint. Do not forget to pass --client or --server as an argument.");
    }
}