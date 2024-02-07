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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    private static final Logger LOG = LoggerContext.getContext().getLogger(ServerMain.class);
    public static final int DEFAULT_PORT = 4350;

    public static void start(String[] args) {
        try (ServerSocket socket = new ServerSocket(DEFAULT_PORT)) {
            LOG.info("Starting ControllerLink server on {}", new InetSocketAddress(DEFAULT_PORT));

            while (true) {
                if (!socket.isBound()) continue;
                Socket client = socket.accept();
                LOG.info("Accepted connection from " + client.getInetAddress());

                new Thread(new ClientHandler(client)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final class ClientHandler implements Runnable {
        private final Socket client;

        public ClientHandler(Socket clientSocket) {
            this.client = clientSocket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true)
            ) {
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    LOG.info("Received from client: " + inputLine);
                    out.println("Server says: " + inputLine);
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
}
