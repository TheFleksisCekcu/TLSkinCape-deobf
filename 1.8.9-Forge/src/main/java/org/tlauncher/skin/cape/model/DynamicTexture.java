//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9"!

package org.tlauncher.skin.cape.model;

import javax.annotation.Nonnull;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

public class DynamicTexture extends AbstractTexture {
   private final int[] dynamicTextureData;
   private final int width;
   private final int height;

   public DynamicTexture(int textureWidth, int textureHeight, int[] data) {
      this.width = textureWidth;
      this.height = textureHeight;
      this.dynamicTextureData = data;
      TextureUtil.allocateTexture(this.getGlTextureId(), textureWidth, textureHeight);
      this.updateDynamicTexture();
   }

   public void loadTexture(@Nonnull IResourceManager resourceManager) {
   }

   private void updateDynamicTexture() {
      TextureUtil.uploadTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height);
   }
}
