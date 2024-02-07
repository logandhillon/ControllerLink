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

package net.logandhillon.controllerlink.testing;

import net.java.games.input.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * Seeing if I can get JInput to work without running the dedicated client-server
 */
public class JInputTestMain {
	private static final Logger LOG = LoggerContext.getContext().getLogger(JInputTestMain.class);

	public static void main(String[] args) throws InterruptedException {
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		Controller controller = null;

		System.out.println("Selected Gamepad: ");
		for (Controller c: controllers) if (c.getType() == Controller.Type.GAMEPAD) {
			controller = c;
			System.out.printf("Port #%s (%s): %s%n", c.getPortNumber(), c.getPortType(), c.getName());
			break;
		}

		if (controller == null) System.exit(0);
		EventQueue queue = controller.getEventQueue();
		Event event = new Event();

		while (true) {
			controller.poll();
			queue.getNextEvent(event);

			Component component = event.getComponent();

			if (component != null) {
				Component.Identifier id = component.getIdentifier();
				if (id == Component.Identifier.Button._0) System.out.println("Button 0 was pressed");
			}

			// polling rate so the CPU doesn't max out
			Thread.sleep(10);
		}
	}
}
