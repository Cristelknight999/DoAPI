package de.cristelknight.doapi.fabric;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;
import com.terraformersmc.terraform.boat.impl.entity.TerraformBoatEntity;
import com.terraformersmc.terraform.boat.impl.entity.TerraformChestBoatEntity;
import com.terraformersmc.terraform.sign.SpriteIdentifierRegistry;
import com.terraformersmc.terraform.sign.block.TerraformSignBlock;
import com.terraformersmc.terraform.sign.block.TerraformWallSignBlock;
import de.cristelknight.doapi.fabric.terraform.DoApiBoatTypeHolder;
import de.cristelknight.doapi.terraform.boat.TerraformBoatType;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DoApiExpectPlatformImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static void registerBoatType(ResourceLocation boatTypeName, TerraformBoatType type) {
        DoApiBoatTypeHolder holder = new DoApiBoatTypeHolder(type.getItem(), type.getChestItem(), type.getPlanks());

        Registry.register(TerraformBoatTypeRegistry.INSTANCE, boatTypeName, holder);
    }


    public static Boat createBoat(ResourceLocation boatTypeName, Level world, double x, double y, double z, boolean chest) {
        com.terraformersmc.terraform.boat.api.TerraformBoatType boatType = TerraformBoatTypeRegistry.INSTANCE.get(boatTypeName);
        Boat boatEntity;
        if (chest) {
            TerraformChestBoatEntity chestBoat = new TerraformChestBoatEntity(world, x, y, z);
            chestBoat.setTerraformBoat(boatType);
            boatEntity = chestBoat;
        } else {
            TerraformBoatEntity boat = new TerraformBoatEntity(world, x, y, z);
            boat.setTerraformBoat(boatType);
            boatEntity = boat;
        }
        return boatEntity;
    }

    public static Set<ResourceLocation> getAllDoApiBoatTypeNames() {
        Set<ResourceLocation> boats = new HashSet<>();
        for(Map.Entry<ResourceKey<com.terraformersmc.terraform.boat.api.TerraformBoatType>, com.terraformersmc.terraform.boat.api.TerraformBoatType> entry : TerraformBoatTypeRegistry.INSTANCE.entrySet()){
            if(entry.getValue() instanceof DoApiBoatTypeHolder){
                boats.add(entry.getKey().location());
            }
        }
        return boats;
    }

    public static void addFlammable(int burnOdd, int igniteOdd, Block... blocks) {
        FlammableBlockRegistry registry = FlammableBlockRegistry.getDefaultInstance();
        for(Block b : blocks) registry.add(b, burnOdd, igniteOdd);
    }

    public static Block getSign(ResourceLocation signTextureId) {
        return new TerraformSignBlock(signTextureId, BlockBehaviour.Properties.copy(Blocks.OAK_SIGN));
    }

    public static Block getWallSign(ResourceLocation signTextureId) {
        return new TerraformWallSignBlock(signTextureId, BlockBehaviour.Properties.copy(Blocks.OAK_SIGN));
    }

    public static void addSignSprite(ResourceLocation signTextureId) {
        SpriteIdentifierRegistry.INSTANCE.addIdentifier(new Material(Sheets.SIGN_SHEET, signTextureId));
    }

    public static <T> List<Pair<List<String>, T>> findAPIs(Class<T> returnClazz, String name, Class<?> annotationClazz) {
        List<Pair<List<String>, T>> instances = Lists.newArrayList();

        FabricLoader.getInstance().getEntrypointContainers(name, returnClazz).forEach(entrypoint -> {
            String modId = entrypoint.getProvider().getMetadata().getId();
            T api = entrypoint.getEntrypoint();
            instances.add(new Pair<>(List.of(modId), api));
        });
        return instances;
    }
}
