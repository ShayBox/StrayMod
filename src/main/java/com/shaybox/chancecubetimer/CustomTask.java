package com.shaybox.chancecubetimer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.time.LocalDateTime;
import java.util.TimerTask;

public class CustomTask extends TimerTask {
	@Override
	public void run() {
		Main.INSTANCE.setTimerDate(LocalDateTime.now().plusMinutes(Configuration.timer));

		EntityPlayer entityPlayer = Main.INSTANCE.getPlayer();
		if (entityPlayer == null) return;

		World world = entityPlayer.getEntityWorld();

		EntityBat entityBat = new EntityBat(world);
		entityBat.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
		entityBat.setHealth(100);

		world.spawnEntity(entityBat);
		if (Configuration.sound) entityPlayer.playSound(SoundEvents.BLOCK_NOTE_PLING, 1, 2);

		Utilities.setTimeout(() -> {
			if (entityBat.isDead) return;
			entityBat.setDead();

			BlockPos blockPos = entityBat.getPosition();
			IBlockState iBlockState = world.getBlockState(blockPos);

			Block block = Block.getBlockFromName("chancecubes:chance_cube");
			if (block == null) {
				entityPlayer.sendMessage(new TextComponentString("There seems to be a problem"));
				return;
			}

			if (iBlockState.getBlock() == Blocks.AIR) world.setBlockState(blockPos, block.getDefaultState());
			else {
				boolean succeeded = entityPlayer.inventory.addItemStackToInventory(new ItemStack(block, 1));
				if (succeeded) entityPlayer.sendMessage(new TextComponentString("I couldn't place a block, I gave you one instead"));
				else entityPlayer.sendMessage(new TextComponentString("I couldn't place or give you a block"));

				if (Configuration.sound) entityPlayer.playSound(SoundEvents.BLOCK_NOTE_PLING, 1, 0);
			}
		}, 1000 * Configuration.bat);

		if (Minecraft.getMinecraft().isGamePaused()) Main.INSTANCE.setPaused(1);
	}
}
