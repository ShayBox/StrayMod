package com.shaybox.straymav;

import me.guichaguri.tickratechanger.api.TickrateAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.time.Duration;
import java.time.LocalDateTime;

@Mod.EventBusSubscriber
public class EventHandler {

	// Render overlay
	@SubscribeEvent
	public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.EXPERIENCE) new Overlay();
	}

	// Set Main#player and Extend reach
	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		if (world.isRemote) return;

		Entity entity = event.getEntity();
		if (entity instanceof EntityPlayer) {
			// Set Main#player
			EntityPlayer player = (EntityPlayer) entity;
			Main.INSTANCE.setPlayer(player);

			// Extended reach
			if (Configuration.reach.extend)
				player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(Configuration.reach.range);
		}
	}

	// Guide
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (Configuration.misc.guide) {
			event.player.sendMessage(new TextComponentString(Keybindings.add.getDisplayName() + " to one to queue"));
			event.player.sendMessage(new TextComponentString(Keybindings.remove.getDisplayName() + " to remove one from queue"));
			event.player.sendMessage(new TextComponentString(Keybindings.pause.getDisplayName() + " to start and pause queue"));
			event.player.sendMessage(new TextComponentString(Keybindings.skip.getDisplayName() + " to immediately skip a cycle"));
			event.player.sendMessage(new TextComponentString(Keybindings.tickrateMax.getDisplayName() + " to set tickrate to max"));
			event.player.sendMessage(new TextComponentString(Keybindings.tickrateNormal.getDisplayName() + " to set tickrate to normal"));
			event.player.sendMessage(new TextComponentString(Keybindings.give.getDisplayName() + " to get a chance cube"));
			event.player.sendMessage(new TextComponentString(Keybindings.bat.getDisplayName() + " to spawn a bat"));
		}
	}

	// Modify animals
	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event) {
		for (Entity entity : event.world.getLoadedEntityList()) {
			if (entity instanceof EntityAnimal) {
				EntityAnimal animal = (EntityAnimal) entity;
				if (Configuration.animal.breeding) {
					int breedingCooldown = 20 * Configuration.animal.breedingCooldown;
					if (animal.getGrowingAge() > breedingCooldown) animal.setGrowingAge(breedingCooldown);
				}
				if (Configuration.animal.ageing) {
					int ageingCooldown = -(20 * Configuration.animal.ageingCooldown);
					if (animal.getGrowingAge() < ageingCooldown) animal.setGrowingAge(ageingCooldown);
				}
			}
		}
	}

	// Keybindings
	@SubscribeEvent
	public static void onKeyInput(InputEvent.KeyInputEvent event) {
		Main main = Main.INSTANCE;

		if (Keybindings.pause.isPressed()) {
			switch (main.getState()) {
				case "NOT_RUNNING": {
					if (main.getQueue().size() == 0) {
						main.getPlayer().sendMessage(new TextComponentString("Queue is empty"));
						return;
					}
					main.getTimer().scheduleAtFixedRate(new CustomTask(), 0, 1000 * 60 * Configuration.timer.minutes);
					main.setState("RUNNING");
					break;
				}
				case "RUNNING": {
					main.restartTimer();
					main.setPauseDateTime(LocalDateTime.now());
					main.setState("PAUSED");
					break;
				}
				case "PAUSED": {
					Duration pauseDuration = Duration.between(main.getPauseDateTime(), LocalDateTime.now());
					LocalDateTime localDateTime = main.getTimerDateTime().plusSeconds(pauseDuration.getSeconds());
					Duration delayDuration = Duration.between(LocalDateTime.now(), localDateTime);
					main.getTimer().scheduleAtFixedRate(new CustomTask(), 1000 * delayDuration.getSeconds(), 1000 * 60 * Configuration.timer.minutes);
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
			main.restartTimer().scheduleAtFixedRate(new CustomTask(), 0, 1000 * 60 * Configuration.timer.minutes);
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

	// #SAVEPICKLES
	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof EntityMooshroom && entity.getName().equals("Pickles")) {
			event.setCanceled(true);
			EntityMooshroom entityMooshroom = (EntityMooshroom) entity;
			EntityPlayer player = Main.INSTANCE.getPlayer();
			entityMooshroom.setHealth(20);
			entityMooshroom.attemptTeleport(player.posX, player.posY, player.posZ);
			player.sendMessage(new TextComponentString("#SAVEPICKLES"));
		}
	}
}
