package com.CCraze.ThunderAndLightning.mixin;

import com.CCraze.ThunderAndLightning.weather.TempestWeatherClient;
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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientWorld.class)
public class SkyBrightnessMixin {
    @Inject(method = "getSunBrightness", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void modifySunBrightness(float partialTicks, CallbackInfoReturnable<Float> cir, float f, float f1){ //f1 is the brightness that is returning
        cir.setReturnValue((float) (TempestWeatherClient.getBrightnessOffset(partialTicks)*f1));
    }
}
