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

import net.logandhillon.controllerlink.ControllerLinkPacket;
import net.logandhillon.controllerlink.Header;
import net.logandhillon.controllerlink.gamepad.InputPacket;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler {
    private static final Logger LOG = LoggerContext.getContext().getLogger(ClientHandler.class);
    private static final ArrayList<Socket> connectedClients = new ArrayList<>();
    private static final Scanner STDIN = new Scanner(System.in);
    private final SSLSocket client;

    private ClientHandler(SSLSocket clientSocket) {
        this.client = clientSocket;
    }

    public static void handle(SSLSocket client) {
        System.out.print("Allow "+client.getInetAddress()+" to connect? [y/n] ");
        out:
        try {
            while (true) {
                switch (STDIN.nextLine().toLowerCase()) {
                    case "y" -> {
                        break out;
                    }
                    case "n" -> {
                        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                        out.println("Connection rejected");
                        LOG.info("Connection from {} has been rejected", client.getInetAddress());
                        client.close();
                        return;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        connectedClients.add(client);
        new Thread(() -> new ClientHandler(client).run(), "Client-Handler-" + connectedClients.size()).start();
    }

    private void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {
            InetAddress address = client.getInetAddress();
            LOG.info("Handling connection from " + address);
            String line;

            if ((line = in.readLine()).startsWith("ver:")) {
                Header remoteHeader = Header.fromString(line.split(":")[1]);
                if (remoteHeader == null || remoteHeader.isInvalid(Header.Environment.CLIENT)) {
                    LOG.warn("Unexpected or invalid client header: " + remoteHeader);
                    out.println("Unexpected or invalid client header");
                    LOG.info("Forcibly disconnecting " + address);
                    client.close();
                    return;
                }
                LOG.info("Accepted connection from {} client v{}", remoteHeader.brand, remoteHeader.version);
            } else {
                LOG.warn("{} did not send their version, forcibly disconnecting", address);
                out.println("Unexpected or invalid client header");
                client.close();
                return;
            }

            while ((line = in.readLine()) != null) {
                System.out.println(address + ": " + line);

                ControllerLinkPacket packet = ControllerLinkPacket.fromString(line);
                if (packet == null) {
                    out.println("Bad packet");
                    continue;
                }

                if (packet.command.equals("cmd")) { if (packet.content.equals("ver")) out.println(ServerMain.HEADER); }
                else if (packet.command.equals("in")) {
                    InputPacket input = InputPacket.fromString(packet.content);
                    if (input == null) {
                        out.println("Bad input packet");
                        return;
                    }
                    // TODO: 02-07-2024 Implement virtual gamepad
                    System.out.printf("(joystick #%s) button #%s has value %s%n", input.joystickId(), input.buttonId(), input.buttonVal());
                }
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
