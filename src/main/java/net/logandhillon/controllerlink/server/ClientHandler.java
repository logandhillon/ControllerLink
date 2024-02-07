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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandler {
    private static final Logger LOG = LoggerContext.getContext().getLogger(ClientHandler.class);
    private final Socket client;

    private ClientHandler(Socket clientSocket) {
        this.client = clientSocket;
    }

    public static void handle(Socket client) {
        new Thread(() -> new ClientHandler(client).run()).start();
    }

    private void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {
            InetAddress address = client.getInetAddress();
            LOG.info("Accepted connection from " + address);
            String line;

            while ((line = in.readLine()) != null) {
                LOG.info("Received from {}: {}", address, line);

                if (line.equals("ver")) out.println(ServerMain.VERSION);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                client.close();
                LOG.info("Client disconnected");
            } catch (IOException e) {
                LOG.warn("Exception thrown while closing client connection, ignoring", e);
            }
        }
    }
}
