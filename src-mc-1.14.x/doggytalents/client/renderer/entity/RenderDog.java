package doggytalents.client.renderer.entity;

import com.mojang.blaze3d.platform.GlStateManager;

import doggytalents.ModTalents;
import doggytalents.client.model.entity.ModelChest;
import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.model.entity.ModelSaddle;
import doggytalents.client.model.entity.ModelWings;
import doggytalents.client.renderer.RenderUtil;
import doggytalents.client.renderer.entity.layer.LayerBone;
import doggytalents.client.renderer.entity.layer.LayerCape;
import doggytalents.client.renderer.entity.layer.LayerCover;
import doggytalents.client.renderer.entity.layer.LayerDogCollar;
import doggytalents.client.renderer.entity.layer.LayerDogHurt;
import doggytalents.client.renderer.entity.layer.LayerModel;
import doggytalents.client.renderer.entity.layer.LayerRadioCollar;
import doggytalents.entity.EntityDog;
import doggytalents.lib.Constants;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author ProPercivalalb
 */
@OnlyIn(Dist.CLIENT)
public class RenderDog extends LivingRenderer<EntityDog, ModelDog> {
	
    public RenderDog(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelDog(0.0F), 0.5F);
        this.addLayer(new LayerCape(this));
        this.addLayer(new LayerRadioCollar(this));
        this.addLayer(new LayerDogCollar(this));
        this.addLayer(new LayerDogHurt(this));
        this.addLayer(new LayerBone(this));
        
        this.addLayer(new LayerCover(this, new ModelDog(0.4F), ResourceLib.MOB_LAYER_SUNGLASSES, EntityDog::hasSunglasses));
        
        this.addLayer(new LayerModel(this, new ModelDog(0.4F), ResourceLib.MOB_LAYER_ARMOR, dog -> Constants.DOGGY_ARMOUR && dog.TALENTS.getLevel(ModTalents.GUARD_DOG) > 0));
        this.addLayer(new LayerModel(this, new ModelWings(), ResourceLib.MOB_LAYER_WINGS, dog -> Constants.DOGGY_WINGS && dog.TALENTS.getLevel(ModTalents.PILLOW_PAW) == 5));
        this.addLayer(new LayerModel(this, new ModelSaddle(0.0F), ResourceLib.MOB_LAYER_SADDLE, dog -> Constants.DOGGY_SADDLE && dog.TALENTS.getLevel(ModTalents.WOLF_MOUNT) > 0));
        this.addLayer(new LayerModel(this, new ModelChest(0.0F), ResourceLib.MOB_LAYER_CHEST, dog -> Constants.DOGGY_CHEST && dog.TALENTS.getLevel(ModTalents.PACK_PUPPY) > 0));
    }

    @Override
    protected float handleRotationFloat(EntityDog dog, float partialTickTime) {
        return dog.getTailRotation();
    }
    
    @Override
    protected void renderLivingAt(EntityDog dog, double x, double y, double z) {
        if(dog.isAlive() && dog.isPlayerSleeping())
            super.renderLivingAt(dog, x, y + 0.5F, z);
        else
            super.renderLivingAt(dog, x, y, z);
    }
    
    @Override
    public void doRender(EntityDog dog, double x, double y, double z, float entityYaw, float partialTicks) {
    	if(dog.isDogWet()) {
            float f2 = dog.getBrightness() * dog.getShadingWhileWet(partialTicks);
            GlStateManager.color3f(f2, f2, f2);
        }

        super.doRender(dog, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDog dog) {
    	if(Constants.USE_DT_TEXTURES)
     		return ResourceLib.getTameSkin(dog.getTameSkin());
     	else
     		return ResourceLib.MOB_DOG_TAME;
    }
    
    @Override
    public void renderName(EntityDog dog, double x, double y, double z) {
        if(this.canRenderName(dog)) {
        	GlStateManager.alphaFunc(516, 0.1F);
            double d0 = dog.getDistanceSq(this.field_76990_c.field_217783_c.func_216785_c());
            
            y += (double)((float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * 0.016666668F * 0.7F);
        	
        	String tip = dog.MODE.getMode().getTip();
                
        	if(dog.isIncapacicated())
        		tip = "dog.mode.incapacitated.indicator";
        	
        	String label = String.format("%s(%d)%s", 
        			new TranslationTextComponent(tip).getFormattedText(), 
        			dog.getDogHunger(), 
        			new TranslationTextComponent(dog.GENDER.getGenderTip()).getFormattedText());
        	if(d0 <= (double)(64 * 64)) {
        		boolean flag = dog.isSneaking();
        		float f = this.field_76990_c.playerViewY;
        		float f1 = this.field_76990_c.playerViewX;
        		boolean flag1 = this.field_76990_c.options.thirdPersonView == 2;
        		float f2 = dog.getSize(dog.getPose()).height + 0.42F - (flag ? 0.25F : 0.0F) - (dog.isPlayerSleeping() ? 0.5F : 0);
        
        		RenderUtil.renderLabelWithScale(this.getFontRendererFromRenderManager(), label, (float)x, (float)y + f2, (float)z, 0, f, f1, flag1, flag, 0.01F);
        		RenderUtil.renderLabelWithScale(this.getFontRendererFromRenderManager(), dog.getDisplayName().getFormattedText(), (float)x, (float)y + f2 - 0.12F, (float)z, 0, f, f1, flag1, flag, 0.026F);
        		
        		if(d0 <= (double)(5 * 5)) {
        			//TODO Sneaking if(this.field_76990_c.field_217783_c.isSneaking()) {
    	    			//String ownerName = dog.getOwnersName().getFormattedText();
    	    			
    	    			
    	    			//RenderUtil.renderLabelWithScale(this.getFontRendererFromRenderManager(), ownerName, (float)x, (float)y + f2 - 0.34F, (float)z, 0, f, f1, flag1, flag, 0.01F);
    	    	//	}
        		}
        	}
        }
    }
    
    @Override
    protected void preRenderCallback(EntityDog entitylivingbaseIn, float partialTickTime) {
    	EntityDog dog = (EntityDog)entitylivingbaseIn;
    	float size = dog.getDogSize() * 0.3F + 0.1F;
    	GlStateManager.scalef(size, size, size);
    	this.shadowSize = size * 0.5F;
	}
}