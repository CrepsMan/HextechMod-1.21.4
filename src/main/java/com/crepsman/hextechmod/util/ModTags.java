package com.crepsman.hextechmod.util;

import com.crepsman.hextechmod.HextechMod;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.item.Item;

public class ModTags {
    public static final TagKey<Item> SMELT_TO_TITANIUM_INGOT = of("smelt_to_titanium_ingot");
    public static final TagKey<Item> TITANIUM_ORES = of("titanium_ores");
    public static final TagKey<Item> HEXTECH_ARMOR_MATERIALS = of("hextech_armor_materials");
    public static final TagKey<Item> HEXTECH_TOOL_MATERIALS = of("hextech_tool_materials");

    public static final TagKey<Item> MERCURY_HAMMER = of("mercury_hammer");
    public static final TagKey<Item> ATLAS_GAUNTLETS = of("atlas_gauntlets");

    public static final TagKey<Item> HEXTECH_POWERED = of("hextech_powered");


    private static TagKey<Item> of(String path) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(HextechMod.MOD_ID, path)) ;
    }

    private static  TagKey<Block> ofBlocks(String path) {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of(HextechMod.MOD_ID, path));
    }

    public static void init() {
    }

}