package com.CCraze.ThunderAndLightning.mixin;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public class SkyBrightnessMixin {
    private float partialTicks;

    /*@Inject(method = "getSunBrightness", at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void getPartialTicks(float partialTicks, CallbackInfoReturnable<Float> cir){
        this.partialTicks = partialTicks;
    }*/
    @ModifyVariable(method = "getSunBrightness", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getThunderStrength(F)F"), index = 3)
    private float modifySunBrightness(float brightness){
        //return brightness*(float)TempestWeatherClient.getBrightnessOffset(partialTicks);
        System.out.println(brightness);
        return 0;
    }
    @ModifyVariable(method = "getSunBrightness", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getThunderStrength(F)F", shift = At.Shift.AFTER), ordinal = 2)
    private float getSunBrightness(float brightness){
        //return brightness*(float)TempestWeatherClient.getBrightnessOffset(partialTicks);
        System.out.println(brightness);
        return brightness;
    }
    /*@Inject(method = "getLightFor", at = @At(value = "HEAD"))
    private void changeLightLevel(LightType lightTypeIn, BlockPos blockPosIn, CallbackInfoReturnable<Integer> cir){
        cir.setReturnValue(0);
    }*/
}
