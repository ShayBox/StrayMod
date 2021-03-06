package com.shaybox.straymod;

import com.shaybox.straymod.proxy.ClientProxy;
import com.shaybox.straymod.timer.State;
import me.guichaguri.tickratechanger.api.TickrateAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class Overlay extends Gui {
	public Overlay() {
		ClientProxy proxy = (ClientProxy) Main.PROXY;
		Minecraft minecraft = Minecraft.getMinecraft();
		ScaledResolution scaledResolution = new ScaledResolution(minecraft);

		// Top-Left Corner
		int fps = Minecraft.getDebugFPS();
		int color = Color.HSBtoRGB(((120.0f / 360.0f) / 144.0f) * Math.min(fps, 144), 1.0f, 1.0f);

		if (Configuration.overlay.framerate)
			drawString(minecraft.fontRenderer, "FPS: " + fps, 0, 10, color);

		if (Configuration.overlay.tickrate)
			drawString(minecraft.fontRenderer, "TPS: " + TickrateAPI.getServerTickrate(), 0, 0, color);

		if (Configuration.overlay.randomTickSpeed) {
			int randomTickSpeed = proxy.getPlayer().getEntityWorld().getGameRules().getInt("randomTickSpeed");
			drawString(minecraft.fontRenderer, "RTS: " + randomTickSpeed, 0, 20, color);
		}

		// Bottom-Left Corner
		int queueSize = proxy.getQueue().size();
		String string = getTime(proxy.timer.getTimerDateTime()) + " (" + queueSize + ")";
		if (proxy.timer.getState() == State.PAUSED) string = "Paused (" + queueSize + ")";
		if (proxy.timer.getState() == State.NOT_RUNNING) {
			if (queueSize == 0) string = "Press " + KeyBindings.add.getDisplayName() + " to add to queue";
			else string = "Press " + KeyBindings.action.getDisplayName() + " to start (" + queueSize + ")";
		}

		drawString(minecraft.fontRenderer, string, 0, scaledResolution.getScaledHeight() - 10, 0xFFAA00);
	}

	private String getTime(LocalDateTime localDateTime) {
		Duration duration = Duration.between(LocalDateTime.now(), localDateTime);

		long hours = duration.toHours();
		duration = duration.minusHours(hours);

		long minutes = duration.toMinutes();
		duration = duration.minusMinutes(minutes);

		long seconds = duration.getSeconds();

		return (hours == 0 ? "" : hours + "h ") + (minutes == 0 ? "" : minutes + "m ") + (seconds == 0 ? "" : seconds + "s");
	}
}
