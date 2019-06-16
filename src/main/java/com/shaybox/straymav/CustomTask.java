package com.shaybox.straymav;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.time.LocalDateTime;
import java.util.TimerTask;

class CustomTask extends TimerTask {
	private Main main = Main.INSTANCE;
	private Minecraft minecraft = Minecraft.getMinecraft();

	@Override
	public void run() {
		EntityPlayer player = main.getPlayer();

		Utilities.spawnBat(player, minecraft);

		main.setTimerDateTime(LocalDateTime.now().plusMinutes(Configuration.timer.minutes));
		main.getQueue().remove();
		if (minecraft.isGamePaused() || main.getQueue().size() == 0) {
			main.restartTimer();
			main.setState("NOT_RUNNING");
			player.sendMessage(new TextComponentString("Queue has ran out"));
		}
	}
}
