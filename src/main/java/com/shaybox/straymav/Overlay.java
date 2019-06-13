package com.shaybox.straymav;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.time.Duration;
import java.time.LocalDateTime;

class Overlay extends Gui {
	private Main main = Main.INSTANCE;

	Overlay() {
		String string = "Press " + Keybindings.pause.getDisplayName() + " To start the timer";
		if (main.getState().equals("PAUSED")) string = "Paused";
		if (main.getState().equals("RUNNING")) string = getTime();

		Minecraft minecraft = Minecraft.getMinecraft();
		ScaledResolution scaledResolution = new ScaledResolution(minecraft);
		int stringWidth = minecraft.fontRenderer.getStringWidth(string);
		int width = scaledResolution.getScaledWidth() - stringWidth;
		int height = scaledResolution.getScaledHeight() - 10;

		drawString(minecraft.fontRenderer, string, width, height, 0xFFAA00);
	}

	private String getTime() {
		Duration duration = Duration.between(LocalDateTime.now(), main.getTimerDateTime());

		long hours = duration.toHours();
		duration = duration.minusHours(hours);

		long minutes = duration.toMinutes();
		duration = duration.minusMinutes(minutes);

		long seconds = duration.getSeconds();

		return (hours == 0 ? "" : hours + "h ") + (minutes == 0 ? "" : minutes + "m ") + (seconds == 0 ? "" : seconds + "s");
	}
}
