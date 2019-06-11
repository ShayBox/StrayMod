package com.shaybox.chancecubetimer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.time.ZonedDateTime;
import java.util.TimerTask;

public class CustomTask extends TimerTask {
	@Override
	public void run() {
		Main.INSTANCE.setZonedDateTime(ZonedDateTime.now().plusMinutes(Configuration.minutes));

		EntityPlayer entityPlayer = Main.INSTANCE.getPlayer();
		if (entityPlayer == null) return;

		World world = entityPlayer.getEntityWorld();

		BlockPos feetBlockPos = entityPlayer.getPosition();
		IBlockState feetBlockState = world.getBlockState(feetBlockPos);

		BlockPos headBlockPos = entityPlayer.getPosition();
		IBlockState headBlockState = world.getBlockState(headBlockPos);

		Block block = Block.getBlockFromName("chancecubes:chance_cube");
		if (block == null) return;

		if (feetBlockState.getBlock() == Blocks.AIR) world.setBlockState(feetBlockPos, block.getDefaultState());
		else if (headBlockState.getBlock() == Blocks.AIR) world.setBlockState(headBlockPos, block.getDefaultState());
		else {
			boolean succeeded = entityPlayer.inventory.addItemStackToInventory(new ItemStack(block, 1));
			if (succeeded) {
				entityPlayer.sendMessage(new TextComponentString("You were given a chance cube"));
				if (Configuration.sound) entityPlayer.playSound(SoundEvents.BLOCK_NOTE_PLING, 1, 2);
			} else {
				entityPlayer.sendMessage(new TextComponentString("I was not able to give you a chance cube, sorry :("));
				if (Configuration.sound) entityPlayer.playSound(SoundEvents.BLOCK_NOTE_PLING, 1, 0);
			}
		}
	}
}
