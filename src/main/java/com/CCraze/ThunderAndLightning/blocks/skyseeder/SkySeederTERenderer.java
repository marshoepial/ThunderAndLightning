package com.CCraze.ThunderAndLightning.blocks.skyseeder;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

//This block is interesting because I have it rendered in parts to make moving each individual part easier
//The blockstate file for this block is a "multipart". Depending on which "useModel" value you use, the model will be different
//This makes it easy to render the models individually while still rendering through BlockStates.

//Not sided because this should be called only on client.
public class SkySeederTERenderer extends TileEntityRenderer<SkySeederTile> {
    public SkySeederTERenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }
    public void renderPart(BlockState bs, double[] rotations, double[] offsets, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedLightOverlay){
        BlockRendererDispatcher renderer = Minecraft.getInstance().getBlockRendererDispatcher();

        //first, rotate it to where you want it to rotate around. then rotate it, and then move it to its final location in the world
        matrixStack.push();
        matrixStack.translate(offsets[0], offsets[1], offsets[2]);
        matrixStack.rotate(Vector3f.YP.rotationDegrees((float)rotations[0]));
        matrixStack.rotate(Vector3f.XP.rotationDegrees((float)rotations[1]));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees((float)rotations[2]));
        matrixStack.translate(-offsets[0], -offsets[1], -offsets[2]);

        renderer.renderBlock(bs, matrixStack, buffer, combinedLightIn, combinedLightOverlay);

        matrixStack.pop();
    }

    //v = partialTicks, i = combinedLightIn, i1=combinedOverlayIn
    @Override
    public void render(SkySeederTile skySeederTile, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int i, int i1) {
        //super.render(skySeederTile, v, matrixStack, iRenderTypeBuffer, i, i1);
        //get the blockstates that have the separate models
        BlockState headState = skySeederTile.getBlockState().with(SkySeederBlock.modelProperty, 1);
        BlockState fanState = skySeederTile.getBlockState().with(SkySeederBlock.modelProperty, 2);
        BlockState baseState = skySeederTile.getBlockState().with(SkySeederBlock.modelProperty, 3);
        
        double yaw = skySeederTile.realYaw > skySeederTile.yaw ? Math.max(skySeederTile.realYaw - 5*v, skySeederTile.yaw)
                : Math.min(skySeederTile.realYaw + 5*v, skySeederTile.yaw);
        double pitch = skySeederTile.realPitch > skySeederTile.pitch ? Math.max(skySeederTile.realPitch - 5*v, skySeederTile.pitch)
                : Math.min(skySeederTile.realPitch + 5*v, skySeederTile.pitch);
        skySeederTile.realFanRot += skySeederTile.realFanRPT*v;
        if (skySeederTile.realFanRot >= 360) skySeederTile.realFanRot -= 360;

        //System.out.println(skySeederTile.realPitch+", "+skySeederTile.realYaw);

        //actually render things. custom method to cut down on code. the first obj array is for the rotations, the second tells how each model should be offset after rotating
        renderPart(headState, new double[]{yaw, pitch, 0}, new double[]{0.5D, 0.5D, 0.5D}, matrixStack, iRenderTypeBuffer,i,  i1);
        renderPart(baseState, new double[]{yaw, 0, 0}, new double[]{0.5D, 0.5D, 0.5D}, matrixStack,iRenderTypeBuffer,i,  i1);
        renderPart(fanState, new double[]{yaw, pitch, skySeederTile.realFanRot}, new double[]{0.5D, 0.63D, 0.5D}, matrixStack, iRenderTypeBuffer, i, i1);
    }
}
