package com.shaybox.straymod.event;

import com.shaybox.straymod.Configuration;
import com.shaybox.straymod.KeyBindings;
import com.shaybox.straymod.Main;
import com.shaybox.straymod.Overlay;
import com.shaybox.straymod.proxy.ClientProxy;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT)
public class ClientEventHandler {
	@SubscribeEvent
	public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		// Render Overlay
		if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) new Overlay();
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		// Logical server side only
		if (world.isRemote) return;

		Entity entity = event.getEntity();
		if (entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) entity;

			// Update ClientProxy#player
			ClientProxy proxy = (ClientProxy) Main.PROXY;
			proxy.setPlayer(player);

			// Extended reach
			if (Configuration.reach.extend)
				player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(Configuration.reach.range);
		}
	}

	// Modify animals
	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event) {
		for (Entity entity : event.world.getLoadedEntityList()) {
			if (entity instanceof EntityAnimal) {
				EntityAnimal animal = (EntityAnimal) entity;
				if (Configuration.animal.breeding) {
					int breedingCooldown = 20 * Configuration.animal.breedingCooldown;
					if (animal.getGrowingAge() > breedingCooldown) animal.setGrowingAge(breedingCooldown);
				}
				if (Configuration.animal.ageing) {
					int ageingCooldown = -(20 * Configuration.animal.ageingCooldown);
					if (animal.getGrowingAge() < ageingCooldown) animal.setGrowingAge(ageingCooldown);
				}
			}
		}
	}

	// Guide
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!Configuration.misc.guide) return;
		for (KeyBinding key : KeyBindings.keys)
			event.player.sendMessage(new TextComponentString(key.getDisplayName() + " " + key.getKeyDescription()));
	}

	// #SAVEPICKLES
	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof EntityMooshroom && entity.getName().equals("Pickles")) {
			event.setCanceled(true);
			EntityMooshroom entityMooshroom = (EntityMooshroom) entity;
			ClientProxy proxy = (ClientProxy) Main.PROXY;
			EntityPlayer player = proxy.getPlayer();
			entityMooshroom.setHealth(20);
			entityMooshroom.attemptTeleport(player.posX, player.posY, player.posZ);
			player.sendMessage(new TextComponentString("#SAVEPICKLES"));
		}
	}
}
