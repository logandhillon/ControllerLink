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

package net.logandhillon.controllerlink.client;

import net.logandhillon.controllerlink.Version;
import net.logandhillon.controllerlink.server.ServerMain;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public final class ClientMain {
    private static final Logger LOG = LoggerContext.getContext().getLogger(ClientMain.class);
    public static final Version VERSION = new Version("logandhillon","ControllerLink","client","0.1.0-dev");

    public static void start(String[] args) {
        LOG.info("Starting ControllerLink client");

        InetSocketAddress address = getSocketAddress(args);
        if (address == null) throw new NullPointerException("Could not find target! Do not forget to pass a target IP with --target xxx.xxx.xxx.xxx");

        try (Socket socket = new Socket()) {
            socket.connect(address);
            LOG.info("Connected to {}", address);

            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                out.println("ver");
                LOG.info("Server version: " + in.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the socket address from command-line args
     */
    private static InetSocketAddress getSocketAddress(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--target") && args.length > i+1) {
                String[] parts = args[i+1].split(":"); // host, port
                int port = (parts.length > 1) ? Integer.parseInt(parts[1]) : ServerMain.DEFAULT_PORT;
                return new InetSocketAddress(parts[0], port);
            }
        }
        return null;
    }
}
