package com.crepsman.hextechmod.item;


import com.crepsman.hextechmod.HextechMod;
import com.crepsman.hextechmod.item.smithing_templates.HextechHammerCombinerSmithingTemplateItem;
import com.crepsman.hextechmod.item.weapons.AtlasGauntlets;
import com.crepsman.hextechmod.item.weapons.HextechHammer;
import com.crepsman.hextechmod.item.weapons.HextechHammerBlasterMode;
import net.minecraft.item.*;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.item.ArmorItem;

import java.util.function.Function;

public class ModItems {
    // Weapons
    public static final Item ATLAS_GAUNTLETS = registerItem("atlas_gauntlets",
            settings -> new AtlasGauntlets(5.0F, 3.0F, settings),
            new Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1));
    public static final Item HEXTECH_HAMMER = registerItem("hextech_hammer", settings -> new HextechHammer(ToolMaterial.NETHERITE, 11.0F, -3.5F, settings), new Item.Settings().fireproof().rarity(Rarity.EPIC));
    public static final Item HEXTECH_HAMMER_BLASTER_MODE = registerItem("hextech_hammer_blaster_mode", HextechHammerBlasterMode::new, new Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1));
    // Materials
    public static final Item TITANIUM_INGOT = registerItem("titanium_ingot", Item::new, new Item.Settings().fireproof().rarity(Rarity.RARE));
    public static final Item RAW_TITANIUM = registerItem("raw_titanium", Item::new, new Item.Settings().fireproof().rarity(Rarity.RARE));

    //Parts
    public static final Item HEXTECH_HANDLE = registerItem("hextech_handle", Item::new, new Item.Settings().fireproof().rarity(Rarity.RARE));
    public static final Item HEXTECH_HAMMER_HEAD = registerItem("hextech_hammer_head", Item::new, new Item.Settings().fireproof().rarity(Rarity.EPIC));

    //Smithing templates
    public static final Item HEXTECH_HAMMER_COMBINER_SMITHING_TEMPLATE =registerItem("hextech_hammer_combiner_smithing_template", HextechHammerCombinerSmithingTemplateItem::createNetheriteUpgrade, (new Item.Settings().fireproof().rarity(Rarity.UNCOMMON)).rarity(Rarity.EPIC));

    // Armor
    public static final Identifier HEXTECH_ARMOR_PATH = Identifier.of(HextechMod.MOD_ID, "hextech");

    public static final Item HEXTECH_HELMET = Items.register(of("hextech_helmet"), settings -> new ArmorItem(
            ModArmorMaterials.HEXTECH, EquipmentType.HELMET, settings), new Item.Settings().maxCount(1).fireproof().maxDamage(ModArmorMaterials.HEXTECH.durability())
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, HEXTECH_ARMOR_PATH))
            .rarity(Rarity.EPIC));

    public static final Item HEXTECH_CHESTPLATE = Items.register(of("hextech_chestplate"), settings -> new ArmorItem(
            ModArmorMaterials.HEXTECH, EquipmentType.CHESTPLATE, settings), new Item.Settings().maxCount(1).fireproof().maxDamage(ModArmorMaterials.HEXTECH.durability())
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, HEXTECH_ARMOR_PATH))
            .rarity(Rarity.EPIC));

    public static final Item HEXTECH_LEGGINGS = Items.register(of("hextech_leggings"), settings -> new ArmorItem(
            ModArmorMaterials.HEXTECH, EquipmentType.LEGGINGS, settings), new Item.Settings().maxCount(1).fireproof().maxDamage(ModArmorMaterials.HEXTECH.durability())
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, HEXTECH_ARMOR_PATH))
            .rarity(Rarity.EPIC));

    public static final Item HEXTECH_BOOTS = Items.register(of("hextech_boots"), settings -> new ArmorItem(
            ModArmorMaterials.HEXTECH, EquipmentType.BOOTS, settings), new Item.Settings().maxCount(1).fireproof().maxDamage(ModArmorMaterials.HEXTECH.durability())
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, HEXTECH_ARMOR_PATH))
            .rarity(Rarity.EPIC));

    public static final Item UNREFINED_HEXTECH_CRYSTAL = registerItem("unrefined_hextech_crystal", Item::new, new Item.Settings().fireproof().rarity(Rarity.EPIC));
    public static final Item HEXTECH_CRYSTAL = registerItem("hextech_crystal", Item::new, new Item.Settings().fireproof().rarity(Rarity.EPIC));
    public static final Item UNREFINED_HEXTECH_CRYSTAL_SHARD = registerItem("unrefined_hextech_crystal_shard", Item::new, new Item.Settings().fireproof().rarity(Rarity.RARE));



    public static Item registerItem(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(HextechMod.MOD_ID, id));
        Item item = factory.apply(settings.registryKey(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }
        return Registry.register(Registries.ITEM, key, item);
    }

    public static RegistryKey<Item> of(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(HextechMod.MOD_ID, id));
    }

    public static void registerModItem() {
        HextechMod.LOGGER.info("Registering Mod Items for " + HextechMod.MOD_ID);
    }
}