package com.shaybox.straymod;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

class Utilities {
	private static Block chanceCubeBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("chancecubes:chance_cube"));

	private static void setTimeout(Runnable runnable, int delay) {
		new Thread(() -> {
			try {
				Thread.sleep(delay);
				runnable.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	static void giveChanceCube(EntityPlayer player) {
		boolean succeeded = player.inventory.addItemStackToInventory(new ItemStack(chanceCubeBlock, 1));
		if (!succeeded) player.sendMessage(new TextComponentString("I couldn't give you a block"));
	}

	static void spawnBat(EntityPlayer player) {
		World world = player.getEntityWorld();

		EntityBat entityBat = new EntityBat(world);
		entityBat.setPosition(player.posX, player.posY, player.posZ);
		entityBat.setHealth(Configuration.timer.batHealth);
		world.spawnEntity(entityBat);

		setTimeout(() -> {
			if (entityBat.isDead) player.sendMessage(new TextComponentString("You're safe... this time"));
			else {
				entityBat.setDead();

				BlockPos blockPos = entityBat.getPosition();
				IBlockState iBlockState = world.getBlockState(blockPos);

				if (iBlockState.getBlock() == Blocks.AIR)
					world.setBlockState(blockPos, chanceCubeBlock.getDefaultState());
				else {
					player.sendMessage(new TextComponentString("I was unable to place a block, have a cube"));
					giveChanceCube(player);
				}
			}

			if (Configuration.misc.sound)
				world.playSound(null, entityBat.getPosition(), Sounds.getSoundEvent("boop"), SoundCategory.MASTER, 1, 1);
		}, 1000 * Configuration.timer.batLifetime);
	}
}
