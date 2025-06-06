package com.crepsman.hextechmod;

import com.crepsman.hextechmod.block.ModBlocks;
import com.crepsman.hextechmod.block.entity.ModBlockEntities;
import com.crepsman.hextechmod.event.ModEventHandlers;
import com.crepsman.hextechmod.item.ModItemGroups;
import com.crepsman.hextechmod.item.ModItems;
import com.crepsman.hextechmod.network.NetworkHandler;
import com.crepsman.hextechmod.util.GauntlestsUsageEvent;
import com.crepsman.hextechmod.util.KeyBindingHandler;
import com.crepsman.hextechmod.component.ModDataComponentTypes;
import com.crepsman.hextechmod.util.ModLootTableModifiers;
import com.crepsman.hextechmod.util.ModTags;
import com.crepsman.hextechmod.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HextechMod implements ModInitializer {
	public static final String MOD_ID = "hextechmod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlockEntities.registerModBlockEntities();
		ModItems.registerModItem();
		ModBlocks.registerModBlock();
		ModItemGroups.registerItemGroups();
		ModWorldGeneration.generateModWorldGen();
		ModTags.init();
		ModDataComponentTypes.registerDataComponentTypes();
		PlayerBlockBreakEvents.BEFORE.register(new GauntlestsUsageEvent());
		KeyBindingHandler.register();
		ModScreenHandlers.registerAll();
		NetworkHandler.registerC2SPackets();
		ModEventHandlers.register();
		ModLootTableModifiers.modifyLootTables();

	}
	public static Identifier identifierOf(String name) {
		return Identifier.of(MOD_ID, name);

	}
}