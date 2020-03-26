package com.CCraze.ThunderAndLightning.weather;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;


public class TempestWeatherClient {
    private static boolean transitioning = false;
    private static boolean colorsChanged = false;

    private static long ticksAtChange = 0;
    private static double ticksAtUpdateFog = 0;
    private static double ticksAtUpdateSky = 0;
    private static double ticksAtUpdateBrightness = 0;

    private static double[] fogColorsTempest = {0.56, 0.08, 0.46};
    private static double[] fogColorsClear = {0.71164376, 0.81895363, 1.0};
    private static double[] currFogColors = new double[3];

    private static double[] skyColorsTempest = {0.83, 0.08, 0.4};
    private static double[] skyColorsTempestND = new double[3]; //adjusted for night/day cycle
    private static double[] skyColorsClear = new double[3];
    private static double[] currSkyColors = new double[3];

    private static double currBrightnessOffset = 1.0;

    private static ClientWorld world;
    private static boolean wasWorldNull;

    public static void handleFogColor(EntityViewRenderEvent.FogColors event, ClientWorld world){
        if (wasWorldNull){
            setTempestActive(true, false);
            wasWorldNull = false;
        }
        TempestWeatherClient.world = world;
        //System.out.println("changefogColor fires!");
        if (transitioning){
            double currTicks = world.getGameTime() + event.getRenderPartialTicks();
            if (ticksAtUpdateFog < ticksAtChange) ticksAtUpdateFog = ticksAtChange;
            double tickDiff = currTicks - ticksAtUpdateFog;
            //System.out.println(tickDiff);

            double[] selectedFogColors;
            if (colorsChanged) selectedFogColors = fogColorsTempest;
            else selectedFogColors = fogColorsClear;

            for (int i = 0; i < 3; i++){
                if (currFogColors[i] < selectedFogColors[i]) currFogColors[i] = Math.min(selectedFogColors[i], currFogColors[i]+tickDiff/40);
                else if (currFogColors[i] > selectedFogColors[i]) currFogColors[i] = Math.max(selectedFogColors[i], currFogColors[i]-tickDiff/40);
            }

            if (currFogColors[0] == selectedFogColors[0] && currFogColors[1] == selectedFogColors[1] && currFogColors[2] == selectedFogColors[2]) transitioning = false;
            else {
                event.setRed((float) currFogColors[0]);
                event.setGreen((float) currFogColors[1]);
                event.setBlue((float) currFogColors[2]);
                //System.out.println((currFogColors[0]-selectedFogColors[0])+", "+(currFogColors[1] - selectedFogColors[1])+", "+(currFogColors[2]-selectedFogColors[2]));
            }
            ticksAtUpdateFog = currTicks;
        }
        if (colorsChanged && !transitioning){
            //System.out.println("Tempest is active, changing fog color");
            event.setRed((float)fogColorsTempest[0]);
            event.setGreen((float) fogColorsTempest[1]);
            event.setBlue((float) fogColorsTempest[2]);
        }
    }
    public static double[] getSkyColor(double partialTicks){
        if (world == null) return new double[]{0, 0, 0};
        float nightDayMod = MathHelper.clamp(MathHelper.cos(world.getCelestialAngle(1) * ((float)Math.PI * 2)) * 0.5f + 0.75f, 0, 1);
        for (int i = 0; i < 3; i++) skyColorsTempestND[i]  = skyColorsTempest[i] * nightDayMod;
        if (transitioning){
            double totalTicks = partialTicks + world.getGameTime();
            if (ticksAtUpdateSky < ticksAtChange) ticksAtUpdateSky = ticksAtChange;
            double tickDiff = totalTicks - ticksAtUpdateSky;

            double[] selectedColors;
            if (colorsChanged) selectedColors = skyColorsTempestND;
            else selectedColors = skyColorsClear;
            
            for (int i = 0; i < 3; i++) {
                if (currSkyColors[i] < selectedColors[i])
                    currSkyColors[i] = Math.min(selectedColors[i], currSkyColors[i] + tickDiff / 40);
                else if (currSkyColors[i] > selectedColors[i])
                    currSkyColors[i] = Math.max(selectedColors[i], currSkyColors[i] - tickDiff / 40);
            }

            ticksAtUpdateSky = totalTicks;

            return currSkyColors;
        }
        if (!colorsChanged) return new double[]{-1, -1, -1};
        else return skyColorsTempestND;
    }
    public static double getBrightnessOffset(float partialTicks){
        if (world == null) return 1.0;
        if (transitioning){
            double totalTicks = partialTicks + world.getGameTime();
            if (ticksAtUpdateBrightness < ticksAtChange) ticksAtUpdateBrightness = ticksAtChange;
            double tickDiff = totalTicks - ticksAtUpdateBrightness;

            double selectedOffset;
            if (colorsChanged) selectedOffset = 0.5;
            else selectedOffset = 1.0;

            if (currBrightnessOffset < selectedOffset) currBrightnessOffset = Math.min(selectedOffset, currBrightnessOffset + tickDiff / 40);
            else if (currBrightnessOffset > selectedOffset) currBrightnessOffset = Math.max(selectedOffset, currBrightnessOffset - tickDiff / 40);

            ticksAtUpdateBrightness = totalTicks;

            return currBrightnessOffset;
        }
        if (!colorsChanged) return 1;
        else return 0.7;
    }

    public static void setTempestActive(boolean setTempest, boolean transition) {
        if (world == null) { wasWorldNull = true; return; }
        if (setTempest){
            ticksAtChange = world.getGameTime();
            Vec3d fogColors = world.getFogColor(1);
            currFogColors[0] = fogColors.x; currFogColors[1] = fogColors.y; currFogColors[2] = fogColors.z;
            transitioning = transition;
            colorsChanged = true;
        } else {
            ticksAtChange = world.getGameTime();
            Vec3d skyColors = world.getSkyColor(Minecraft.getInstance().player.getPosition(), 1);
            currSkyColors[0] = skyColors.x; currSkyColors[1] = skyColors.y; currSkyColors[2] = skyColors.z;
            transitioning = transition;
            colorsChanged = false;
        }

    }


    public static Boolean isTempestActive() { return colorsChanged; }

    public static void tempestClientOnLoad(){
        currFogColors = new double[3];
        currSkyColors = new double[3];
        currBrightnessOffset = 0;
        colorsChanged = false;
    }
    public static void setSkyColorsClear(double[] in) {skyColorsClear = in;}
    public static void setFogColorsClear(double[] in) {fogColorsClear = in;}
}
