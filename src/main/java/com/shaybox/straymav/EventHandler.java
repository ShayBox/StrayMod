package com.shaybox.straymav;

import me.guichaguri.tickratechanger.api.TickrateAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
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
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.time.Duration;
import java.time.LocalDateTime;

import static net.minecraft.entity.player.EntityPlayer.REACH_DISTANCE;

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
			player.getEntityAttribute(REACH_DISTANCE).setBaseValue(Configuration.reach ? Configuration.range : 5.0D);
		}
	}

	// Login chat guide
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!Configuration.guide) return;
		event.player.sendMessage(new TextComponentString("Quick guide:"));
		event.player.sendMessage(new TextComponentString(Keybindings.add.getDisplayName() + " to one to queue"));
		event.player.sendMessage(new TextComponentString(Keybindings.remove.getDisplayName() + " to remove one from queue"));
		event.player.sendMessage(new TextComponentString(Keybindings.pause.getDisplayName() + " to start and pause queue"));
		event.player.sendMessage(new TextComponentString(Keybindings.skip.getDisplayName() + " to immediately skip a cycle"));
		event.player.sendMessage(new TextComponentString(Keybindings.tickrateMax.getDisplayName() + " to set tickrate to max"));
		event.player.sendMessage(new TextComponentString(Keybindings.tickrateNormal.getDisplayName() + " to set tickrate to normal"));
		event.player.sendMessage(new TextComponentString(Keybindings.give.getDisplayName() + " to get a chance cube"));
		event.player.sendMessage(new TextComponentString(Keybindings.bat.getDisplayName() + " to spawn a bat"));
		event.player.sendMessage(new TextComponentString("All of these can be changed in Controls, #SAVEPICKLES"));
	}

	// Keybindings Handler
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (Keybindings.pause.isPressed()) {
			switch (main.getState()) {
				// Start timer
				case "NOT_RUNNING": {
					if (main.getQueue().size() == 0) {
						main.getPlayer().sendMessage(new TextComponentString("Queue is empty"));
						return;
					}
					main.getTimer().scheduleAtFixedRate(new CustomTask(), 0, 1000 * 60 * Configuration.timer);
					main.setState("RUNNING");
					break;
				}
				// Pause timer
				case "RUNNING": {
					main.restartTimer();
					main.setPauseDateTime(LocalDateTime.now());
					main.setState("PAUSED");
					break;
				}
				// Resume timer
				case "PAUSED": {
					Duration pauseDuration = Duration.between(main.getPauseDateTime(), LocalDateTime.now());
					LocalDateTime localDateTime = main.getTimerDateTime().plusSeconds(pauseDuration.getSeconds());
					Duration delayDuration = Duration.between(LocalDateTime.now(), localDateTime);
					main.getTimer().scheduleAtFixedRate(new CustomTask(), 1000 * delayDuration.getSeconds(), 1000 * 60 * Configuration.timer);
					main.setTimerDateTime(localDateTime);
					main.setState("RUNNING");
					break;
				}
			}
		}
		if (Keybindings.skip.isPressed()) {
			if (main.getQueue().size() == 0) {
				main.getPlayer().sendMessage(new TextComponentString("Queue is empty"));
				return;
			}
			main.restartTimer().scheduleAtFixedRate(new CustomTask(), 0, 1000 * 60 * Configuration.timer);
			main.setState("RUNNING");
		}
		if (Keybindings.add.isPressed()) {
			main.getQueue().add(1);
			main.getPlayer().sendMessage(new TextComponentString("Added one to queue"));
		}
		if (Keybindings.remove.isPressed()) {
			if (main.getQueue().size() == 0) {
				main.getPlayer().sendMessage(new TextComponentString("Queue is empty"));
				return;
			}
			main.getQueue().remove();
			main.getPlayer().sendMessage(new TextComponentString("removed one to queue"));
		}
		if (Keybindings.give.isPressed()) {
			Utilities.giveChanceCube(main.getPlayer());
			main.getPlayer().sendMessage(new TextComponentString("Gave chancecube"));
		}
		if (Keybindings.bat.isPressed()) {
			Utilities.spawnBat(main.getPlayer(), Minecraft.getMinecraft());
			main.getPlayer().sendMessage(new TextComponentString("Spawned bat"));
		}
		if (Keybindings.tickrateMax.isPressed()) {
			TickrateAPI.changeTickrate(Float.MAX_VALUE);
			main.getPlayer().sendMessage(new TextComponentString("Set tickrate to Infinite"));
		}
		if (Keybindings.tickrateNormal.isPressed()) {
			TickrateAPI.changeTickrate(20);
			main.getPlayer().sendMessage(new TextComponentString("Set tickrate to 20"));
		}
		if (Keybindings.randomDouble.isPressed()) {
			int randomTickSpeed = main.getPlayer().getEntityWorld().getGameRules().getInt("randomTickSpeed");
			main.getPlayer().getEntityWorld().getGameRules().setOrCreateGameRule("randomTickSpeed", String.valueOf(randomTickSpeed * 2));
			main.getPlayer().sendMessage(new TextComponentString("Random Tick Speed is now " + randomTickSpeed * 2));
		}
		if (Keybindings.randomNormal.isPressed()) {
			main.getPlayer().getEntityWorld().getGameRules().setOrCreateGameRule("randomTickSpeed", "3");
			main.getPlayer().sendMessage(new TextComponentString("Set random tick speed to 3"));
		}
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
