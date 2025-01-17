package com.crepsman.hextechmod.item;

import com.crepsman.hextechmod.HextechMod;
import com.crepsman.hextechmod.util.ModTags;
import com.google.common.base.Supplier;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.EnumMap;

public interface ModArmorMaterials {
    ArmorMaterial HEXTECH = new ArmorMaterial(50, Util.make(new EnumMap<>(EquipmentType.class), map -> {
            map.put(EquipmentType.HELMET, 4);
            map.put(EquipmentType.CHESTPLATE, 9);
            map.put(EquipmentType.LEGGINGS, 7);
            map.put(EquipmentType.BOOTS, 4);
            map.put(EquipmentType.BODY,12);

    }), 40, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 4F, 0.2F, ModTags.HEXTECH_ARMOR_MATERIALS, ModItems.HEXTECH_ARMOR_PATH);
}