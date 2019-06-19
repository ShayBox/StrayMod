package com.shaybox.straymod;

import com.shaybox.straymod.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

import java.util.TimerTask;

public class CustomTask extends TimerTask {
	@Override
	public void run() {
		ClientProxy proxy = (ClientProxy) Main.PROXY;
		EntityPlayerMP player = proxy.getPlayer();

		Utilities.spawnBat(player);

		proxy.timer.updateTime();
		proxy.getQueue().remove();

		if (Minecraft.getMinecraft().isGamePaused()) {
			proxy.timer.pause();
			player.sendMessage(new TextComponentString("You were paused, I paused the timer for you"));
		}

		if (proxy.getQueue().size() == 0) {
			proxy.timer.stop();
			player.sendMessage(new TextComponentString("Queue ran out"));
		}
	}
}
