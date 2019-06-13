package com.shaybox.straymav;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;

class EventHandler {
	private Main main = Main.INSTANCE;

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.EXPERIENCE) new Overlay();
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		if (world.isRemote) return;

		Entity entity = event.getEntity();
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			main.setPlayer(player);
		}
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (Keybindings.pause.isPressed()) {
			switch(main.getState()) {
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
		if (Keybindings.give.isPressed()) {
			EntityPlayer entityPlayer = main.getPlayer();

			Block block = Block.getBlockFromName("chancecubes:chance_cube");
			if (block == null) {
				entityPlayer.sendMessage(new TextComponentString("Could not find chancecubes:chance_cube"));
				return;
			}

			boolean succeeded = entityPlayer.inventory.addItemStackToInventory(new ItemStack(block, 1));
			if (succeeded) entityPlayer.sendMessage(new TextComponentString("CHANCECUBE!"));
			else entityPlayer.sendMessage(new TextComponentString("I couldn't give you a block"));

			if (Configuration.sound) entityPlayer.playSound(SoundEvents.BLOCK_NOTE_PLING, 1, 0);
		}
	}
}
