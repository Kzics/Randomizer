package fr.randomizer.utils;

import org.bukkit.Material;

import java.util.List;
import java.util.Locale;

public class MaterialChecker {



    private static boolean hasCheatMaterial(List<Material> materials){
        return materials.stream().anyMatch(mat-> mat.name().contains("COMMAND") || mat.name().startsWith("DEBUG")||mat.equals(Material.BEDROCK) || mat.equals(Material.KNOWLEDGE_BOOK)|| mat.equals(Material.LEGACY_KNOWLEDGE_BOOK));
    }

    private static boolean hasEndMiscMaterial(List<Material> materials){
        return materials.stream().anyMatch(mat-> mat.equals(Material.END_CRYSTAL) || mat.equals(Material.END_PORTAL) || mat.equals(Material.STRUCTURE_VOID));
    }

    private static boolean hasBannerMaterial(List<Material> materials){
        return materials.stream().anyMatch(mat-> mat.name().contains("BANNER"));
    }
    private static boolean hasEggMaterial(List<Material> materials){
        return materials.stream().anyMatch(mat-> mat.name().endsWith("EGG"));
    }

    private static boolean hasCheatMaterial(Material material){
        return material.name().contains("COMMAND") ||material.equals(Material.JIGSAW)|| material.equals(Material.ENCHANTED_BOOK) ||material.name().startsWith("DEBUG") ||material.equals(Material.BEDROCK) || material.equals(Material.KNOWLEDGE_BOOK) || material.equals(Material.LEGACY_KNOWLEDGE_BOOK);
    }

    private static boolean hasDiscMaterial(List<Material> materials){
        return materials.stream().anyMatch(mat-> mat.name().contains("DISC"));
    }

    private static boolean hasDiscMaterial(Material material){
        return material.name().contains("DISC");
    }

    private static boolean hasEndMiscMaterial(Material material){
        return material.equals(Material.END_CRYSTAL) || material.equals(Material.END_PORTAL);
    }

    private static boolean isAirMaterial(Material material){
        return material.equals(Material.AIR) || material.equals(Material.LEGACY_AIR) || material.equals(Material.CAVE_AIR) || material.equals(Material.VOID_AIR) || material.equals(Material.STRUCTURE_VOID);
    }
    private static boolean isAirMaterial(List<Material> material){
        return material.contains(Material.AIR) || material.contains(Material.LEGACY_AIR) || material.contains(Material.CAVE_AIR) || material.contains(Material.VOID_AIR) || material.contains(Material.STRUCTURE_VOID);
    }

    public static boolean hasMiscForbMaterial(Material material){
        return material.equals(Material.SCULK_SHRIEKER) || material.equals(Material.WRITTEN_BOOK) || material.equals(Material.FILLED_MAP) || material.equals(Material.TIPPED_ARROW);
    }

    private static boolean hasBannerMaterial(Material material){
        return material.name().contains("BANNER");
    }


    public static boolean hasEggMaterial(Material material){
        return material.name().toLowerCase(Locale.ROOT).endsWith("spawn_egg");
    }

    public static boolean hasCheatItems(List<Material> materials){
        return isAirMaterial(materials)|| hasCheatMaterial(materials) || hasDiscMaterial(materials) || hasEggMaterial(materials) || hasBannerMaterial(materials) || hasEndMiscMaterial(materials);
    }

    private static boolean hasBundleMaterial(Material mat){
        return mat.equals(Material.BUNDLE);
    }
    public static boolean hasCheatItems(Material material){
        return isAirMaterial(material)|| hasBundleMaterial(material) || hasMiscForbMaterial(material) ||hasCheatMaterial(material) || hasDiscMaterial(material) || hasEggMaterial(material) || hasBannerMaterial(material) || hasEndMiscMaterial(material);
    }
}
