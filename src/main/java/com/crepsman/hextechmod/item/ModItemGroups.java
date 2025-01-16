package com.crepsman.hextechmod.item;


import com.crepsman.hextechmod.HextechMod;
import com.crepsman.hextechmod.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class ModItemGroups {
    public static final ItemGroup HEXTECH_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.tryParse(HextechMod.MOD_ID, "hextech_items"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.hextechmod.hextech_items"))
                    .icon(() -> new ItemStack(ModItems.HEXTECH_HAMMER)) // Set the icon to one of your custom items
                    .entries((displayContext, entries) -> {
                        // Add your custom items here
                        entries.add(ModItems.HEXTECH_GAUNTLETS);
                        entries.add(ModItems.HEXTECH_HAMMER);
                        entries.add(ModItems.HEXTECH_SNIPER_RIFLE);
                        entries.add(ModItems.TITANIUM_INGOT);
                        entries.add(ModItems.RAW_TITANIUM);
                        entries.add(ModItems.HEXTECH_HANDLE);
                        entries.add(ModItems.HEXTECH_HAMMER_HEAD);
                        entries.add(ModItems.HEXTECH_HAMMER_COMBINER_SMITHING_TEMPLATE);

                    }).build());

    public static final ItemGroup HEXTECH_BLOCKS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.tryParse(HextechMod.MOD_ID, "hextech_blocks"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.hextechmod.hextech_blocks"))
                    .icon(() -> new ItemStack(ModBlocks.TITANIUM_BLOCK)) // Set the icon to one of your custom items
                    .entries((displayContext, entries) -> {
                        // Add your custom items here
                        entries.add(ModBlocks.TITANIUM_ORE_BLOCK);
                        entries.add(ModBlocks.TITANIUM_BLOCK);
                        entries.add(ModBlocks.RAW_TITANIUM_BLOCK);
                        entries.add(ModBlocks.TITANIUM_DEEPSLATE_ORE_BLOCK);
                    }).build());

    public static void registerItemGroups() {
        HextechMod.LOGGER.info("Registering An Item Group For " + HextechMod.MOD_ID);
    }
}