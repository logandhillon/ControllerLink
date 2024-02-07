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

package net.logandhillon.controllerlink.gamepad;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWJoystickCallback;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class GamepadInputHandler {
    public static void main(String[] args) {
        if (!GLFW.glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        GLFW.glfwSetJoystickCallback(new GLFWJoystickCallback() {
            @Override
            public void invoke(int jid, int event) {
                System.out.println(event);
                if (event == GLFW.GLFW_CONNECTED) {
                    System.out.println("Gamepad connected: " + jid);
                } else if (event == GLFW.GLFW_DISCONNECTED) {
                    System.out.println("Gamepad disconnected: " + jid);
                }
            }
        });

        while (true) {
            GLFW.glfwPollEvents();

            for (int jid = GLFW.GLFW_JOYSTICK_1; jid <= GLFW.GLFW_JOYSTICK_LAST; jid++) {
                if (GLFW.glfwJoystickPresent(jid)) {
                    FloatBuffer axes = GLFW.glfwGetJoystickAxes(jid);
                    ByteBuffer buttons = GLFW.glfwGetJoystickButtons(jid);

                    if ((buttons != null ? buttons.get(0) : 0) == 1) System.out.println("Button A is pressed");
                }
            }
        }
    }
}
