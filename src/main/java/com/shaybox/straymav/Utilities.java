package com.shaybox.straymav;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

class Utilities {
	private Main main = Main.INSTANCE;
	private static Block chanceCubeBlock = Block.getBlockFromName("chancecubes:chance_cube");

	static void setTimeout(Runnable runnable, int delay) {
		new Thread(() -> {
			try {
				Thread.sleep(delay);
				runnable.run();
			} catch (Exception e) {
				System.err.println(e);
			}
		}).start();
	}

	static void giveChanceCube(EntityPlayer player) {
		if (player == null) return;

		if (chanceCubeBlock == null) {
			player.sendMessage(new TextComponentString("Could not find chancecubes:chance_cube"));
			return;
		}

		boolean succeeded = player.inventory.addItemStackToInventory(new ItemStack(chanceCubeBlock, 1));
		if (!succeeded) player.sendMessage(new TextComponentString("I couldn't give you a block"));
	}

	static void spawnBat(EntityPlayer player) {
		World world = player.getEntityWorld();

		EntityBat entityBat = new EntityBat(world);
		entityBat.setPosition(player.posX, player.posY, player.posZ);
		entityBat.setHealth(Configuration.health);
		world.spawnEntity(entityBat);

		setTimeout(() -> {
			if (entityBat.isDead) player.sendMessage(new TextComponentString("You're safe... this time"));
			else {
				entityBat.setDead();

				BlockPos blockPos = entityBat.getPosition();
				IBlockState iBlockState = world.getBlockState(blockPos);

				if (chanceCubeBlock == null) {
					player.sendMessage(new TextComponentString("Could not find chancecubes:chance_cube"));
					return;
				}

				if (iBlockState.getBlock() == Blocks.AIR) world.setBlockState(blockPos, chanceCubeBlock.getDefaultState());
				else {
					player.sendMessage(new TextComponentString("I was unable to place a block, have a cube"));
					giveChanceCube(player);
				}
			}
		}, 1000 * Configuration.lifetime);
	}
}
