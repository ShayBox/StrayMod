package com.shaybox.chancecubetimer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Timer;

@Mod(modid = Main.MOD_ID, name = Main.MOD_NAME, version = Main.VERSION, dependencies = "required-after:chancecubes")
public class Main {
	static final String MOD_ID = "chancecubetimer";
	static final String MOD_NAME = "ChanceCubeTimer";
	static final String VERSION = "0.0.1";

	@Mod.Instance(MOD_ID)
	static Main INSTANCE;

	private Timer timer = new Timer();
	private LocalDateTime timerDate = LocalDateTime.now();
	private LocalDateTime pauseDate = LocalDateTime.now();
	private EntityPlayer player;
	private boolean paused = false;

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		Keybinds.register();
	}

	@Mod.EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {
		timer.scheduleAtFixedRate(new CustomTask(), new Date(), 1000 * 60 * Configuration.minutes);

		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	Timer getTimer() {
		return timer;
	}

	void setTimer(Timer timer) {
		this.timer = timer;
	}

	LocalDateTime getTimerDate() {
		return timerDate;
	}

	void setTimerDate(LocalDateTime date) {
		this.timerDate = date;
	}

	LocalDateTime getPauseDate() {
		return pauseDate;
	}

	void setPauseDate(LocalDateTime date) {
		this.pauseDate = date;
	}

	EntityPlayer getPlayer() {
		return player;
	}

	void setPlayer(EntityPlayer player) {
		this.player = player;
	}

	boolean isPaused() {
		return paused;
	}

	void setPaused(boolean paused) {
		this.paused = paused;
	}
}
