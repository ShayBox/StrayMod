package com.shaybox.straymod;

import com.shaybox.straymod.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.MOD_ID, name = Main.MOD_NAME, dependencies = "required-after:chancecubes;required-after:tickratechanger")
public class Main {
	static final String MOD_ID = "straymod";
	static final String MOD_NAME = "StrayMod";

	@Mod.Instance(MOD_ID)
	static Main INSTANCE;

	@SidedProxy(
		clientSide = "com.shaybox.straymod.proxy.ClientProxy",
		serverSide = "com.shaybox.straymod.proxy.ServerProxy"
	)
	public static IProxy PROXY;

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		PROXY.preinit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		PROXY.init(event);
	}

	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		PROXY.postinit(event);
	}
}
