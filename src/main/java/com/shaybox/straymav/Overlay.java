package com.shaybox.straymav;

import me.guichaguri.tickratechanger.api.TickrateAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.time.Duration;
import java.time.LocalDateTime;

class Overlay extends Gui {
	private Main main = Main.INSTANCE;

	Overlay() {
		Minecraft minecraft = Minecraft.getMinecraft();
		ScaledResolution scaledResolution = new ScaledResolution(minecraft);

		// Top Left
		if (Configuration.overlay.framerate) drawString(minecraft.fontRenderer, "FPS: " + Minecraft.getDebugFPS(), 0, 0, 0xFFAA00);
		if (Configuration.overlay.tickrate) drawString(minecraft.fontRenderer, "TPS: " + TickrateAPI.getServerTickrate(), 0, 10, 0xFFAA00);
		int randomTickSpeed = main.getPlayer().getEntityWorld().getGameRules().getInt("randomTickSpeed");
		if (Configuration.overlay.randomTickSpeed) drawString(minecraft.fontRenderer, "RTS: " + randomTickSpeed, 0, 20, 0xFFAA00);

		// Bottom Left
		int queueSize = main.getQueue().size();
		String string = getTime() + " (" + queueSize + ")";
		if (main.getState().equals("PAUSED")) string = "Paused (" + queueSize + ")";
		if (main.getState().equals("NOT_RUNNING")) {
			if (queueSize == 0) string = "Queue empty";
			else string = "Press " + Keybindings.pause.getDisplayName() + " to start (" + queueSize + ")";
		}

		drawString(minecraft.fontRenderer, string, 0, scaledResolution.getScaledHeight() - 10, 0xFFAA00);
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

	private static long mean(long[] values)
	{
		long sum = 0L;
		for (long v : values)
			sum += v;
		return sum / values.length;
	}
}
