package com.shaybox.chancecubetimer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.time.Duration;
import java.time.ZonedDateTime;

class Overlay extends Gui {
	Overlay(Minecraft mc) {
		ScaledResolution scaled = new ScaledResolution(mc);
		Duration duration = Duration.between(ZonedDateTime.now(), Main.INSTANCE.getZonedDateTime());
		String string = format(duration);
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
