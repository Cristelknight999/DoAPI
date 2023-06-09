package de.cristelknight.doapi.mixin.armor;

import de.cristelknight.doapi.client.render.feature.CustomArmorFeatureRenderer;
import de.cristelknight.doapi.client.render.feature.CustomHatFeatureRenderer;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandRenderer.class)
public abstract class ArmorStandRendererMixin extends LivingEntityRenderer<ArmorStand, ArmorStandArmorModel> {
    @Inject(at = @At("RETURN"), method = "<init>")
    public void onConstruct(EntityRendererProvider.Context ctx, CallbackInfo ci) {
        addLayer(new CustomHatFeatureRenderer<>(this, ctx.getModelSet()));
        addLayer(new CustomArmorFeatureRenderer<>(this, ctx.getModelSet()));
    }
    public ArmorStandRendererMixin(EntityRendererProvider.Context ctx, ArmorStandArmorModel model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }
}