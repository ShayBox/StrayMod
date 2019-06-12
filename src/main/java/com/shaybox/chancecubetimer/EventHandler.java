package com.shaybox.chancecubetimer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
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

	// Show overlay
	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == EXPERIENCE) new Overlay(Minecraft.getMinecraft());
	}

	// Set player variable
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		if (world.isRemote) return;

		Entity entity = event.getEntity();
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			Main.INSTANCE.setPlayer(player);
		}
	}

	// Keybind handler
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (Keybinds.pause.isPressed()) {
			if (main.getPaused() == 0) {
				main.setPaused(1);
				main.getTimer().cancel();
				main.setPauseDate(LocalDateTime.now());
			} else if (main.getPaused() == 1) {
				Duration duration = Duration.between(main.getPauseDate(), LocalDateTime.now());
				LocalDateTime localDateTime = main.getTimerDate().plusSeconds(duration.getSeconds());

				main.setTimerDate(localDateTime);

				Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
				main.setTimer(new Timer());
				main.getTimer().scheduleAtFixedRate(new CustomTask(), date, 1000 * 60 * Configuration.timer);

				main.setPaused(0);
			} else {
				LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(Configuration.timer);

				main.setTimerDate(localDateTime);

				Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
				main.setTimer(new Timer());
				main.getTimer().scheduleAtFixedRate(new CustomTask(), date, 1000 * 60 * Configuration.timer);

				main.setPaused(0);
			}
		}

		if (Keybinds.reset.isPressed()) {
			main.getTimer().cancel();

			LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(Configuration.timer);

			main.setTimerDate(localDateTime);

			Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
			main.setTimer(new Timer());
			main.getTimer().scheduleAtFixedRate(new CustomTask(), date, 1000 * 60 * Configuration.timer);
		}

		if (Keybinds.give.isPressed()) {
			EntityPlayer entityPlayer = main.getPlayer();

			Block block = Block.getBlockFromName("chancecubes:chance_cube");
			if (block == null) {
				entityPlayer.sendMessage(new TextComponentString("There seems to be a problem"));
				return;
			}

			boolean succeeded = entityPlayer.inventory.addItemStackToInventory(new ItemStack(block, 1));
			if (succeeded) entityPlayer.sendMessage(new TextComponentString("CHANCECUBE!"));
			else entityPlayer.sendMessage(new TextComponentString("I couldn't give you a block"));

			if (Configuration.sound) entityPlayer.playSound(SoundEvents.BLOCK_NOTE_PLING, 1, 0);
		}
	}
}
