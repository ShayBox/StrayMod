package com.shaybox.chancecubetimer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.time.Duration;
import java.time.LocalDateTime;

class Overlay extends Gui {
	private Main main = Main.INSTANCE;
	
	Overlay(Minecraft mc) {
		ScaledResolution scaled = new ScaledResolution(mc);
		Duration duration = Duration.between(LocalDateTime.now(), main.getTimerDate());
		String string = main.getPaused() == 1 ? "PAUSED" : main.getPaused() == 2 ? "START TIMER (Home)" : format(duration);
		int width = scaled.getScaledWidth() - mc.fontRenderer.getStringWidth(string);
		int height = scaled.getScaledHeight() - 10;
		drawString(mc.fontRenderer, string, width, height, 0xFFAA00);
	}

	private static String format(Duration duration) {
		long hours = duration.toHours();
		duration = duration.minusHours(hours);

		long minutes = duration.toMinutes();
		duration = duration.minusMinutes(minutes);

		long seconds = duration.getSeconds();

		return (hours == 0 ? "" : hours + "h ") + (minutes == 0 ? "" : minutes + "m ") + (seconds == 0 ? "" : seconds + "s");
	}
}
