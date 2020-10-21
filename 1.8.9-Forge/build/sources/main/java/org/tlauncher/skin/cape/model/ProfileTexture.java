package org.tlauncher.skin.cape.model;

import net.minecraft.util.ResourceLocation;
import org.tlauncher.skin.cape.renderer.texture.FramedTexture;

public class ProfileTexture {
   private static final long REMOVAL_TIME_MILLS = 85000L;
   private ResourceLocation skin;
   private FramedTexture cape;
   private boolean capeReady;
   private long time;
   private String skinType;

   public String getSkinType() {
      return this.skinType;
   }

   public void setSkinType(String skinType) {
      this.skinType = skinType;
   }

   public ResourceLocation getSkin() {
      return this.skin;
   }

   public void setSkin(ResourceLocation skin) {
      this.skin = skin;
   }

   public FramedTexture getCape() {
      return this.cape;
   }

   public void setCape(FramedTexture cape) {
      this.cape = cape;
   }

   public long getTime() {
      return this.time;
   }

   public void updateTime() {
      this.updateTime(System.currentTimeMillis() + 85000L);
   }

   public void updateTime(long l) {
      this.time = l;
   }

   public boolean isCapeReady() {
      return this.capeReady;
   }

   public void setCapeReady(boolean capeReady) {
      this.capeReady = capeReady;
   }
}
