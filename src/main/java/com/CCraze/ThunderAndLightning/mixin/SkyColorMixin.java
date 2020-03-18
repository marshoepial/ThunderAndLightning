package com.CCraze.ThunderAndLightning.mixin;

import com.CCraze.ThunderAndLightning.weather.TempestWeatherClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientWorld.class)
public class SkyColorMixin {
    private double[] returnColors = new double[3];
    private double[] clearColors = new double[3];
    //Modifying floats f2, f3, f4 in ClientWorld.getSkyColor.
    //This jumps to before the invocation of getRainStrength - so rain and thunder will still affect sky color, but not night/day cycle.
    //We therefore create our own math for night/day cycle (in TempestWeatherClient.class) and make it brighter than normal
    @Inject(method = "getSkyColor", at=@At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void getPartialTicks(BlockPos arg0, float arg1, CallbackInfoReturnable<Vec3d> cir) {
        TempestWeatherClient.setSkyColorsClear(clearColors);
        try{
            returnColors = TempestWeatherClient.getSkyColor(arg1);
        } catch (Exception e){
            System.out.println("Exception where mixin attempts to read partialticks: "+e);
        }
    }
    @ModifyVariable(method = "getSkyColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainStrength(F)F"), index = 7)
    private float f2Handler (float f2){
        try {
            if (TempestWeatherClient.isTempestActive() == null || !TempestWeatherClient.isTempestActive()) clearColors[0] = f2;
            if (returnColors[0] == -1) return f2;
            else return (float) returnColors[0];
        } catch (Exception e){
            System.out.println("Exception where mixin attempts to read sky color: "+e);
            return (float)returnColors[0];
        }
    }
    @ModifyVariable(method = "getSkyColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainStrength(F)F"), index = 8)
    private float f3Handler (float f3){
        try {
            if (TempestWeatherClient.isTempestActive() == null || !TempestWeatherClient.isTempestActive()) clearColors[1] = f3;
            if (returnColors[1] == -1) return f3;
            else return (float) returnColors[1];
        } catch (Exception e){
            System.out.println("Exception where mixin attempts to read sky color: "+e);
            return (float)returnColors[0];
        }
    }
    @ModifyVariable(method = "getSkyColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainStrength(F)F"), index = 9)
    private float f4Handler (float f4){
        try {
            if (TempestWeatherClient.isTempestActive() == null || !TempestWeatherClient.isTempestActive()) clearColors[2] = f4;
            if (returnColors[2] == -1) return f4;
            else return (float) returnColors[2];
        } catch (Exception e){
            System.out.println("Exception where mixin attempts to read sky color: "+e);
            return (float)returnColors[0];
        }
    }
}
