package com.shaybox.straymav;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
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
			EntityPlayer player = main.getPlayer();

			Block block = Block.getBlockFromName("chancecubes:chance_cube");
			if (block == null) {
				player.sendMessage(new TextComponentString("Could not find chancecubes:chance_cube"));
				return;
			}

			boolean succeeded = player.inventory.addItemStackToInventory(new ItemStack(block, 1));
			if (succeeded) player.sendMessage(new TextComponentString("CHANCECUBE!"));
			else player.sendMessage(new TextComponentString("I couldn't give you a block"));

			if (Configuration.sound) player.playSound(SoundEvents.BLOCK_NOTE_PLING, 1, 0);
		}
		if (Keybindings.bat.isPressed()) {
			EntityPlayer player = main.getPlayer();
			World world = player.getEntityWorld();

			EntityBat entityBat = new EntityBat(world);
			entityBat.setPosition(player.posX, player.posY, player.posZ);
			entityBat.setHealth(Configuration.health);
			world.spawnEntity(entityBat);

			if (Configuration.sound) player.playSound(SoundEvents.BLOCK_NOTE_PLING, 1, 2);

			try {
				Thread.sleep(1000 * Configuration.lifetime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (entityBat.isDead) return;
			entityBat.setDead();

			BlockPos blockPos = entityBat.getPosition();
			IBlockState iBlockState = world.getBlockState(blockPos);

			Block block = Block.getBlockFromName("chancecubes:chance_cube");
			if (block == null) {
				player.sendMessage(new TextComponentString("Could not find chancecubes:chance_cube"));
				return;
			}

			if (iBlockState.getBlock() == Blocks.AIR) world.setBlockState(blockPos, block.getDefaultState());
			else {
				boolean succeeded = player.inventory.addItemStackToInventory(new ItemStack(block, 1));
				if (succeeded) player.sendMessage(new TextComponentString("I couldn't place a block, I gave you one instead"));
				else player.sendMessage(new TextComponentString("I couldn't place or give you a block"));

				if (Configuration.sound) player.playSound(SoundEvents.BLOCK_NOTE_PLING, 1, 0);
			}
		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof EntityMooshroom && entity.getName().equals("Pickles")) {
			event.setCanceled(true);
			EntityMooshroom entityMooshroom = (EntityMooshroom) entity;
			EntityPlayer player = main.getPlayer();
			entityMooshroom.setHealth(20);
			entityMooshroom.attemptTeleport(player.posX, player.posY, player.posZ);
			player.sendMessage(new TextComponentString("no"));
		}
	}
}
