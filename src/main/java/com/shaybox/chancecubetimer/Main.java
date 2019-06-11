package com.shaybox.chancecubetimer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Timer;

@Mod(modid = Main.MOD_ID, name = Main.MOD_NAME, version = Main.VERSION, dependencies = "required-after:chancecubes")
public class Main {
	static final String MOD_ID = "chancecubetimer";
	static final String MOD_NAME = "ChanceCubeTimer";
	static final String VERSION = "0.0.0";

	@Mod.Instance(MOD_ID)
	static Main INSTANCE;

	private ZonedDateTime zonedDateTime = ZonedDateTime.now();
	private EntityPlayer player;

	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new CustomTask(), new Date(), 1000 * 60 * Configuration.minutes);

		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	ZonedDateTime getZonedDateTime() {
		return zonedDateTime;
	}

	void setZonedDateTime(ZonedDateTime zonedDateTime) {
		this.zonedDateTime = zonedDateTime;
	}

	EntityPlayer getPlayer() {
		return player;
	}

	void setPlayer(EntityPlayer player) {
		this.player = player;
	}
}
