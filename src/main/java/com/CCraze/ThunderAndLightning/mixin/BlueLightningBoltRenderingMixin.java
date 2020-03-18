package com.CCraze.ThunderAndLightning.mixin;

import com.CCraze.ThunderAndLightning.entity.ModEntities;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.entity.effect.LightningBoltEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

//this is made so we don't have to copy code for rendering the blue lightning bolts

@Mixin(LightningBoltRenderer.class)
public class BlueLightningBoltRenderingMixin {
    private static boolean isBlueLightningBolt;
    @Inject(method = "render", at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void isBlueLightningBolt(LightningBoltEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, CallbackInfo ci){
        isBlueLightningBolt = entityIn.getType() == ModEntities.BLUEBOLT;
    }

    //Red float: 8, Green float: 9, Blue float: 10
    @ModifyVariable(method = "func_229116_a_", at = @At(value = "HEAD"), ordinal = 4)
    private static float modifyRed(float red){
        if (isBlueLightningBolt) return 0.1f;
        else return red;
    }
    @ModifyVariable(method = "func_229116_a_", at = @At(value = "HEAD"), ordinal = 5)
    private static float modifyGreen(float green){
        if (isBlueLightningBolt) return 0.1f;
        else return green;
    }
    @ModifyVariable(method = "func_229116_a_", at = @At(value = "HEAD"), ordinal = 6)
    private static float modifyBlue(float blue){
        if (isBlueLightningBolt) return 0.9f;
        else return blue;
    }
}
