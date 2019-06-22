package com.shaybox.straymod.event;

import com.github.philippheuer.events4j.annotation.EventSubscriber;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.CheerEvent;
import com.github.twitch4j.chat.events.channel.DonationEvent;
import com.github.twitch4j.common.events.domain.EventUser;
import com.shaybox.straymod.Configuration;
import com.shaybox.straymod.Main;
import com.shaybox.straymod.proxy.ClientProxy;
import com.shaybox.straymod.timer.State;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class TwitchEventHandler {
	private Map<EventUser, Integer> users = new HashMap<>();

	@EventSubscriber
	public void onCheer(CheerEvent event) {
		ClientProxy proxy = (ClientProxy) Main.PROXY;
		EntityPlayerMP player = proxy.getPlayer();

		EventUser user = event.getUser();
		Integer cents = event.getBits();

		users.putIfAbsent(user, 0);
		int total = users.get(user) + cents;

		if (player != null)
			player.sendMessage(new TextComponentString(user.getName() + " has donated " + cents + " bits!"));

		int centsAmount = Configuration.twitch.dollars / 100;
		while (total >= centsAmount) {
			total -= centsAmount;

			add(proxy, player);
		}

		users.put(user, total);
	}

	@EventSubscriber
	public void onDonation(DonationEvent event) {
		ClientProxy proxy = (ClientProxy) Main.PROXY;
		EntityPlayerMP player = proxy.getPlayer();

		Currency currency = event.getCurrency();
		if (!currency.getCurrencyCode().equals("USD")) {
			if (player != null)
				player.sendMessage(new TextComponentString("Sorry, I don't handle " + currency.getDisplayName()));
			return;
		}

		EventUser user = event.getUser();
		int cents = (int) Math.round(100 * event.getAmount());

		users.putIfAbsent(user, 0);
		int total = users.get(user) + cents;

		if (player != null)
			player.sendMessage(new TextComponentString(user.getName() + " has donated " + event.getAmount()));

		int centsAmount = Configuration.twitch.dollars / 100;
		while (total >= centsAmount) {
			total -= centsAmount;

			add(proxy, player);
		}

		users.put(user, total);
	}

	@EventSubscriber
	public void onSubscribe(SubscribeEvent event) {
		ClientProxy proxy = (ClientProxy) Main.PROXY;
		EntityPlayerMP player = proxy.getPlayer();

		if (player != null) player.sendMessage(new TextComponentString("Someone subscribed!"));

		add(proxy, player);
	}

	@EventSubscriber
	public void onChannelMessage(ChannelMessageEvent event) {
		ClientProxy proxy = (ClientProxy) Main.PROXY;
		EntityPlayerMP player = proxy.getPlayer();

		EventUser user = event.getUser();
		String message = event.getMessage();
		if (user.getId() == 73442975 && message.startsWith("|") && player != null) {
			player.sendMessage(new TextComponentString(message.replace("|", "")));
		}
	}

	private void add(ClientProxy proxy, EntityPlayerMP player) {
		proxy.getQueue().add(1);
		if (player != null) {
			if (proxy.timer.getState() == State.NOT_RUNNING) {
				proxy.timer.start();
				player.sendMessage(new TextComponentString("The timer has started"));
			}
			player.sendMessage(new TextComponentString("Added a block"));
		}
	}
}
