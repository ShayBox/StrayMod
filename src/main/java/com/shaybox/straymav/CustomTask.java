package com.shaybox.straymav;

import net.minecraft.client.Minecraft;

import java.time.LocalDateTime;
import java.util.TimerTask;

class CustomTask extends TimerTask {
	private Main main = Main.INSTANCE;
	private Minecraft minecraft = Minecraft.getMinecraft();

	@Override
	public void run() {
		main.setTimerDateTime(LocalDateTime.now().plusMinutes(Configuration.timer));

		Utilities.spawnBat(main.getPlayer(), minecraft);

		if (minecraft.isGamePaused()) {
			main.getTimer().cancel();
			main.setPauseDateTime(LocalDateTime.now());
			main.setState("PAUSED");
		}
	}
}
