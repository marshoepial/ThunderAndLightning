package com.CCraze.ThunderAndLightning.blocks.skyseeder;

import com.CCraze.ThunderAndLightning.setup.ModVals;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.Random;

//Not sided because this should be called only on client.
public class SkySeederTERenderer extends TileEntityRenderer<SkySeederTile> {
    public SkySeederTERenderer() {
        System.out.println("SkySeeder renderer has been created");
    }
    @Override
    public void render(SkySeederTile te, double x, double y, double z, float partialTicks, int destroyStage) {
        super.render(te, x, y, z, partialTicks, destroyStage);
        BlockState headState = te.getBlockState().with(SkySeederBlock.modelProperty, 1);
        BlockState fanState = te.getBlockState().with(SkySeederBlock.modelProperty, 2);
        BlockState baseState = te.getBlockState().with(SkySeederBlock.modelProperty, 3);

        double ticks = te.getWorld().getDayTime() + partialTicks;
        if (te.prevTicks == 0) te.prevTicks = ticks;
        double tickDiff = ticks - te.prevTicks;

        if (te.fanRPT > te.realFanRPT){
            double newRPT = te.realFanRPT + (1/te.spoolTicks)*tickDiff;
            te.realFanRPT = Math.min(te.fanRPT, newRPT);
        } else if (te.fanRPT < te.realFanRPT){
            double newRPT = te.realFanRPT - (1/te.spoolTicks)*tickDiff;
            te.realFanRPT = Math.max(newRPT, te.fanRPT);
        }
        //System.out.println(te.realFanRPT + " "+ te.fanRPT);

        if (te.pitch > te.realPitch){
            te.realPitch = Math.min(te.realPitch + tickDiff * 5, te.pitch);
        } else if (te.pitch < te.realPitch){
            te.realPitch = Math.max(te.realPitch - tickDiff * 5, te.pitch);
        }

        if (te.yaw > te.realYaw){
            te.realYaw = Math.min(te.realYaw + tickDiff * 5, te.yaw);
        } else if (te.yaw < te.realYaw){
            te.realYaw = Math.max(te.realYaw - tickDiff * 5, te.yaw);
        }

        te.realFanRot = te.realFanRPT*tickDiff + te.realFanRot;
        if (te.realFanRot >= 360) te.realFanRot = te.realFanRot - 360;
        renderPart(te, headState, x, y, z, new Object[][]{{te.realYaw, 0.0D, 1.0D, 0.0D},{te.realPitch, 1.0D, 0.0D, 0.0D}}, new Double[]{0.5D, 0.5D, 0.5D});
        renderPart(te, baseState, x, y, z, new Object[][]{{te.realYaw, 0.0D, 1.0D, 0.0D}}, new Double[]{0.5D, 0.5D, 0.5D});
        renderPart(te, fanState, x, y ,z, new Object[][]{{te.realYaw, 0.0D, 1.0D, 0.0D},{te.realPitch, 1.0D, 0.0D, 0.0D},{te.realFanRot, 0.0D, 0.0D, 1.0D}}, new Double[]{0.5D, 0.63D, 0.5D});
    }
    public void renderPart(SkySeederTile te, BlockState bs, double x, double y, double z, Object[][] rotations, Double[] offsets){
        BlockRendererDispatcher renderer = Minecraft.getInstance().getBlockRendererDispatcher();

        GlStateManager.pushMatrix();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        if (Minecraft.isAmbientOcclusionEnabled()) GlStateManager.shadeModel(7425);
        else GlStateManager.shadeModel(7424);

        BlockModelRenderer.enableCache();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        GlStateManager.translated(x + offsets[0], y + offsets[1], z+ offsets[2]);
        for (Object[] rotation : rotations) {
            GlStateManager.rotated((double)rotation[0], (double)rotation[1], (double)rotation[2], (double)rotation[3]);
        }
        GlStateManager.translated(-te.getPos().getX()-offsets[0], -te.getPos().getY()-offsets[1],
                -te.getPos().getZ()-offsets[2]);
        renderer.getBlockModelRenderer().renderModel(getWorld(), renderer.getModelForState(bs), bs, te.getPos(),
                bufferBuilder, false, new Random(), te.getBlockState().getPositionRandom(te.getPos()));
        bufferBuilder.setTranslation(0, 0, 0);
        tessellator.draw();
        BlockModelRenderer.disableCache();
        RenderHelper.enableStandardItemLighting();

        GlStateManager.popMatrix();
    }
}
