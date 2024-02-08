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

import net.logandhillon.controllerlink.Header;
import net.logandhillon.controllerlink.config.UserConfig;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;

public final class ServerMain {
    private static final Logger LOG = LoggerContext.getContext().getLogger(ServerMain.class);
    public static final int DEFAULT_PORT = 4350;
    public static final Header HEADER = new Header(Header.Environment.SERVER);

    public static void start(String[] args) throws KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        int port = DEFAULT_PORT;

        for (int i = 0; i < args.length; i++)
            if (args[i].equals("--port") && args.length > i + 1)
                port = Integer.parseInt(args[i + 1]);

        char[] ksPass = UserConfig.INSTANCE.keystorePassword().toCharArray();
        KeyStore keyStore = KeyStore.getInstance("JKS");
        SSLContext sslContext;
        try (InputStream keystoreIS = new FileInputStream(UserConfig.INSTANCE.keystorePath())) {
            keyStore.load(keystoreIS, ksPass);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, ksPass);

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        SSLServerSocketFactory socketFactory = sslContext.getServerSocketFactory();

        try (SSLServerSocket socket = (SSLServerSocket) socketFactory.createServerSocket(port)) {
            LOG.info("Starting ControllerLink server v{} on {}", HEADER.version, new InetSocketAddress(DEFAULT_PORT));

            //noinspection InfiniteLoopStatement
            while (true) {
                if (!socket.isBound()) continue;
                SSLSocket client = (SSLSocket) socket.accept();
                ClientHandler.handle(client);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
