package com.CCraze.ThunderAndLightning.particles;

import net.minecraft.client.particle.*;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class SkySeederParticle extends SpriteTexturedParticle {
    public final IAnimatedSprite animatedSprite;
    private double xMove;
    private double yMove;
    private double zMove;

    public SkySeederParticle(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, IAnimatedSprite sprite) {
        super(world, x, y, z, 0, 0, 0);
        this.xMove = xSpeed;
        this.yMove = ySpeed;
        this.zMove = zSpeed;
        this.maxAge = (int) (Math.random()*5.0d+5.0d);
        this.animatedSprite = sprite;
        this.selectSpriteWithAge(sprite);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public void tick(){
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.age++;
        if (this.age >= this.maxAge) this.setExpired();

        motionX = xMove;
        motionY = yMove;
        motionZ = zMove;

        //System.out.println("Moving particle by"+ motionX + " " + motionY + " "+ motionZ);
        this.move(this.motionX, this.motionY, this.motionZ);

        this.xMove *= 1.1;
        this.zMove *= 1.1;
        this.yMove *= 1.1;
        //this.yMove = yMove - 0.05d;
        //this.zMove = zMove - 0.05d;

        this.selectSpriteWithAge(animatedSprite);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType>{
        private final IAnimatedSprite sprite;

        public Factory (IAnimatedSprite sprite){
            System.out.println("Particle factory initialized");
            this.sprite = sprite;
        }

        public Particle makeParticle(BasicParticleType skySeederParticleData, World world, double v, double v1, double v2, double v3, double v4, double v5) {
            return new SkySeederParticle(world, v, v1, v2, v3, v4, v5, sprite);
        }
    }
}
