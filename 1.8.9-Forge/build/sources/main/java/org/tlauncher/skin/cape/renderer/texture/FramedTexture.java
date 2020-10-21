//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9"!

package org.tlauncher.skin.cape.renderer.texture;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tlauncher.skin.cape.model.DynamicTexture;

public class FramedTexture {
   private static int frameIndex;
   private static final Logger LOGGER = LogManager.getLogger();
   private int currentFrameIndex;
   private long lastTimNano;
   private final long updatingTextureInNano;
   private boolean animated;
   private List<ResourceLocation> frames;

   public FramedTexture(long updatingTextureInNano, boolean animated) {
      this.lastTimNano = System.nanoTime();
      this.updatingTextureInNano = updatingTextureInNano;
      this.frames = new ArrayList();
      this.animated = animated;
   }

   public FramedTexture(int framesSize, long updatingTextureInNano, boolean animated) {
      this(updatingTextureInNano, animated);
      this.frames = new ArrayList(framesSize);
   }

   public static FramedTexture createOneFramedTexture(int[] data, int width, int height) {
      FramedTexture texture = new FramedTexture(1L, false);
      ResourceLocation resourceLocation = new ResourceLocation(String.format("dynamic/framedTexture%s.png", getNextFrameIndex()));
      DynamicTexture dynamicTexture = new DynamicTexture(width, height, data);
      Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, dynamicTexture);
      texture.frames.add(resourceLocation);
      return texture;
   }

   public void initByOneImage(int[] image, int width, int height) {
      ResourceLocation res = new ResourceLocation("dynamic/framedTexture" + getNextFrameIndex() + ".png");
      ITextureObject object = Minecraft.getMinecraft().getTextureManager().getTexture(res);
      if (object == null) {
         ITextureObject object2 = new DynamicTexture(width, height, image);
         Minecraft.getMinecraft().getTextureManager().loadTexture(res, object2);
      }

      this.frames.add(res);
   }

   private ResourceLocation getFrame(int index) {
      return (ResourceLocation)this.frames.get(index);
   }

   private static int getNextFrameIndex() {
      return ++frameIndex;
   }

   public ResourceLocation getFrame() {
      if (!this.animated) {
         return this.getFrame(0);
      } else {
         if (System.nanoTime() - this.lastTimNano >= this.updatingTextureInNano) {
            this.lastTimNano = System.nanoTime();
            if (this.currentFrameIndex + 1 < this.frames.size()) {
               ++this.currentFrameIndex;
            } else {
               this.currentFrameIndex = 0;
            }
         }

         return (ResourceLocation)this.frames.get(this.currentFrameIndex);
      }
   }

   public List<ResourceLocation> getFrames() {
      return this.frames;
   }
}
