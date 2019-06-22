package com.shaybox.straymod.proxy;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.shaybox.straymod.Configuration;
import com.shaybox.straymod.KeyBindings;
import com.shaybox.straymod.event.TwitchEventHandler;
import com.shaybox.straymod.timer.TimerHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.opengl.Display;

import java.util.LinkedList;
import java.util.Queue;

public class ClientProxy implements IProxy {
	public TimerHelper timer = new TimerHelper();

	private EntityPlayerMP player;
	private Queue<Integer> queue = new LinkedList<>();
	private TwitchClient twitch;

	@Override
	public void preinit(FMLPreInitializationEvent event) {
		Display.setTitle("#SAVEPICKLES");
		KeyBindings.register();
	}

	@Override
	public void init(FMLInitializationEvent event) {
	}

	@Override
	public void postinit(FMLPostInitializationEvent event) {
		OAuth2Credential oAuth2Credential = new OAuth2Credential("twitch", Configuration.secret.confirm.token);
		twitch = TwitchClientBuilder.builder()
			.withEnableHelix(true)
			.withEnableChat(true)
			.withChatAccount(oAuth2Credential)
			.build();

		twitch.getChat().joinChannel("straymav");
		twitch.getChat().connect();
		twitch.getChat().getEventManager().registerListener(new TwitchEventHandler());
	}

	public EntityPlayerMP getPlayer() {
		return player;
	}

	public void setPlayer(EntityPlayerMP player) {
		this.player = player;
	}

	public Queue<Integer> getQueue() {
		return queue;
	}
}
