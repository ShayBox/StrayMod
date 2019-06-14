package com.shaybox.straymav;

import me.guichaguri.tickratechanger.api.TickrateAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.UUID;

class EventHandler {
	private Main main = Main.INSTANCE;

	// Render overlay
	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.EXPERIENCE) new Overlay();
	}

	// Keep Main#player up to date
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		if (world.isRemote) return;

		Entity entity = event.getEntity();
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			main.setPlayer(player);

			// Extend reach
			IAttributeInstance iAttributeInstance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
			UUID uuid = UUID.fromString("0DD5A1AD-CA11-ADD5-1CED-C0FFEEEFFEC7");
			AttributeModifier attributeModifier = new AttributeModifier(uuid, "extend_reach", 100, 0);
			if (Configuration.reach) {
				if (!iAttributeInstance.hasModifier(attributeModifier))
					player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).applyModifier(attributeModifier);
			} else {
				if (iAttributeInstance.hasModifier(attributeModifier))
					player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).removeModifier(attributeModifier);
			}
		}
	}

	// Keybindings Handler
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (Keybindings.pause.isPressed()) {
			switch (main.getState()) {
				// Start timer
				case "NOT_RUNNING": {
					int delay = 1000 * 60 * Configuration.timer;
					main.setTimer(new Timer());
					main.getTimer().scheduleAtFixedRate(new CustomTask(), delay, delay);
					main.setTimerDateTime(LocalDateTime.now().plusMinutes(Configuration.timer));
					main.setState("RUNNING");
					break;
				}
				// Pause timer
				case "RUNNING": {
					main.getTimer().cancel();
					main.setPauseDateTime(LocalDateTime.now());
					main.setState("PAUSED");
					break;
				}
				// Resume timer
				case "PAUSED": {
					Duration duration = Duration.between(main.getPauseDateTime(), LocalDateTime.now());
					LocalDateTime localDateTime = main.getTimerDateTime().plusSeconds(duration.getSeconds());
					Duration duration1 = Duration.between(LocalDateTime.now(), localDateTime);
					main.setTimer(new Timer());
					main.getTimer().scheduleAtFixedRate(new CustomTask(), 1000 * duration1.getSeconds(), 1000 * 60 * Configuration.timer);
					main.setTimerDateTime(localDateTime);
					main.setState("RUNNING");
					break;
				}
			}
		}
		if (Keybindings.reset.isPressed()) {
			int delay = 1000 * 60 * Configuration.timer;
			main.getTimer().cancel();
			main.setTimer(new Timer());
			main.getTimer().scheduleAtFixedRate(new CustomTask(), delay, delay);
			main.setTimerDateTime(LocalDateTime.now().plusMinutes(Configuration.timer));
			main.setState("RUNNING");
		}
		if (Keybindings.give.isPressed()) Utilities.giveChanceCube(main.getPlayer());
		if (Keybindings.bat.isPressed()) Utilities.spawnBat(main.getPlayer(), Minecraft.getMinecraft());
		if (Keybindings.tickrateUp.isPressed()) TickrateAPI.changeTickrate(Float.MAX_VALUE, true);
		if (Keybindings.tickrateDown.isPressed()) TickrateAPI.changeTickrate(20, true);
	}

	// SAVE PICKLES!
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof EntityMooshroom && entity.getName().equals("Pickles")) {
			event.setCanceled(true);
			EntityMooshroom entityMooshroom = (EntityMooshroom) entity;
			EntityPlayer player = main.getPlayer();
			entityMooshroom.setHealth(20);
			entityMooshroom.attemptTeleport(player.posX, player.posY, player.posZ);
			player.sendMessage(new TextComponentString("#SAVEPICKLES"));
		}
	}
}
