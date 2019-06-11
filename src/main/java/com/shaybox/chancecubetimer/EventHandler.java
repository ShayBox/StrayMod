package com.shaybox.chancecubetimer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.EXPERIENCE;

class EventHandler {
	private Main main = Main.INSTANCE;

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == EXPERIENCE) new Overlay(Minecraft.getMinecraft());
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getWorld().isRemote) return;

		Entity entity = event.getEntity();
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			Main.INSTANCE.setPlayer(player);
		}
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (Keybinds.pause.isPressed()) {
			if (main.isPaused()) {
				// Calculate duration since paused
				Duration duration = Duration.between(main.getPauseDate(), LocalDateTime.now());
				System.out.println(duration.getSeconds());

				// Add duration to time
				LocalDateTime localDateTime = main.getTimerDate().plusSeconds(duration.getSeconds());

				// Set timer
				main.setTimerDate(localDateTime);

				// Create timer with date
				Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
				main.setTimer(new Timer());
				main.getTimer().scheduleAtFixedRate(new CustomTask(), date, 1000 * 60 * Configuration.minutes);

				// Unpause
				main.setPaused(false);
			} else {
				// Pause
				main.setPaused(true);

				// Cancel timer
				main.getTimer().cancel();

				// Save paused time
				main.setPauseDate(LocalDateTime.now());
			}
		}
		if (Keybinds.reset.isPressed()) {
			// Cancel timer
			main.getTimer().cancel();

			// Add minutes to time
			LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(Configuration.minutes);

			// Set timer
			main.setTimerDate(localDateTime);

			// Create timer with date
			Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
			main.setTimer(new Timer());
			main.getTimer().scheduleAtFixedRate(new CustomTask(), date, 1000 * 60 * Configuration.minutes);
		}
	}
}
