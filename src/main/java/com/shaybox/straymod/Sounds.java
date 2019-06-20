package com.shaybox.straymod;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

@Mod.EventBusSubscriber
@GameRegistry.ObjectHolder(Main.MOD_ID)
public class Sounds {
	private static Map<ResourceLocation, SoundEvent> sound_events;

	private static Map.Entry<ResourceLocation, SoundEvent> createSoundEvent(String name) {
		final ResourceLocation id = new ResourceLocation(Main.MOD_NAME, name);
		final SoundEvent sound = new SoundEvent(id).setRegistryName(id);

		return Pair.of(id, sound);
	}

	static void initialize() {
		Preconditions.checkState(sound_events == null, "Attempted to reinitialize Sound Events");

		sound_events = new ImmutableMap.Builder<ResourceLocation, SoundEvent>()
			.put(createSoundEvent("tuck_and_roll"))
			.put(createSoundEvent("boop"))
			.build();
	}

	static SoundEvent getSoundEvent(String name) {
		ResourceLocation id = new ResourceLocation(Main.MOD_NAME, name);
		Preconditions.checkState(sound_events != null, "Attempted to get Sound Events before initialization");
		Preconditions.checkState(sound_events.containsKey(id), "Attempted to get a non-existent Sound Event %s", id);

		return sound_events.get(id);
	}

	@SubscribeEvent
	public static void onRegisterSoundEvent(RegistryEvent.Register<SoundEvent> event) {
		Preconditions.checkState(sound_events != null, "Sound events weren't initialized before registration");

		sound_events.values().forEach(event.getRegistry()::register);
	}
}
