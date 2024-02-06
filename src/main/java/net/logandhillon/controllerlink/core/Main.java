package net.logandhillon.controllerlink.core;

import net.logandhillon.controllerlink.client.ClientMain;
import net.logandhillon.controllerlink.server.ServerMain;

public class Main {
    public static void main(String[] args) {
        for (String arg: args) {
            if (arg.equals("--server") || arg.equals("-S")) {
                ServerMain.start(args);
                break;
            } else if (arg.equals("--client") || arg.equals("-C")) {
                ClientMain.start(args);
                break;
            }
        }
    }
}