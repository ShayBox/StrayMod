package com.shaybox.straymav;

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

class CustomTask extends TimerTask {
	private Main main = Main.INSTANCE;
	private Minecraft minecraft = Minecraft.getMinecraft();

	@Override
	public void run() {
		main.setTimerDateTime(LocalDateTime.now().plusMinutes(Configuration.timer));

		EntityPlayer player = main.getPlayer();
		if (player == null) return;

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

		if (minecraft.isGamePaused()) {
			main.getTimer().cancel();
			main.setPauseDateTime(LocalDateTime.now());
			main.setState("PAUSED");
		}
	}
}
