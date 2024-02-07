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

package net.logandhillon.controllerlink.server;

import net.logandhillon.controllerlink.Version;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public final class ServerMain {
    private static final Logger LOG = LoggerContext.getContext().getLogger(ServerMain.class);
    public static final int DEFAULT_PORT = 4350;
    public static final Version VERSION = new Version(Version.Environment.SERVER);

    public static void start(String[] args) {
        int port = DEFAULT_PORT;

        for (int i = 0; i < args.length; i++)
            if (args[i].equals("--port") && args.length > i + 1)
                port = Integer.parseInt(args[i + 1]);

        try (ServerSocket socket = new ServerSocket(port)) {
            LOG.info("Starting ControllerLink server v{} on {}", VERSION.version, new InetSocketAddress(DEFAULT_PORT));

            //noinspection InfiniteLoopStatement
            while (true) {
                if (!socket.isBound()) continue;
                Socket client = socket.accept();
                ClientHandler.handle(client);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
