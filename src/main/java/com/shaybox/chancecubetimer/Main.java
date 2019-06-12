package com.shaybox.chancecubetimer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.time.LocalDateTime;
import java.util.Timer;

@Mod(modid = Main.MOD_ID, name = Main.MOD_NAME, version = Main.VERSION, dependencies = "required-after:chancecubes")
public class Main {
	static final String MOD_ID = "chancecubetimer";
	static final String MOD_NAME = "ChanceCubeTimer";
	static final String VERSION = "0.0.2";

	@Mod.Instance(MOD_ID)
	static Main INSTANCE;

	private Timer timer = new Timer();
	private LocalDateTime timerDate = LocalDateTime.now();
	private LocalDateTime pauseDate = LocalDateTime.now();
	private EntityPlayer player;
	private int paused = 2;

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		Keybinds.register();
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

	int getPaused() {
		return paused;
	}

	void setPaused(int paused) {
		this.paused = paused;
	}
}
