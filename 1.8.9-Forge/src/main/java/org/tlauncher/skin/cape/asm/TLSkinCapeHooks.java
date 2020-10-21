//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9"!

package org.tlauncher.skin.cape.asm;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import gloomyfolken.hooklib.asm.Hook;
import gloomyfolken.hooklib.asm.ReturnCondition;
import java.util.Objects;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;
import org.tlauncher.skin.cape.TLSkinCape;
import org.tlauncher.skin.cape.model.ProfileTexture;
import org.tlauncher.skin.cape.renderer.texture.FramedTexture;

public class TLSkinCapeHooks {
   @Hook(
      injectOnExit = true,
      returnCondition = ReturnCondition.ALWAYS
   )
   public static ResourceLocation getLocationSkin(AbstractClientPlayer abstractClientPlayer, @Hook.ReturnValue ResourceLocation sourceValue) {
      String name = abstractClientPlayer.getName();
      if (TLSkinCape.instance.getTextureManager().isInit(name, Type.SKIN)) {
         ResourceLocation resourceLocation = TLSkinCape.instance.getTextureManager().get(name).getSkin();
         if (Objects.nonNull(resourceLocation)) {
            return resourceLocation;
         }
      } else {
         TLSkinCape.instance.createTexture(name);
      }

      return sourceValue;
   }

   @Hook(
      injectOnExit = true,
      returnCondition = ReturnCondition.ALWAYS
   )
   public static ResourceLocation getLocationCape(AbstractClientPlayer abstractClientPlayer, @Hook.ReturnValue ResourceLocation sourceValue) {
      String name = abstractClientPlayer.getName();
      if (TLSkinCape.instance.getTextureManager().isInit(name, Type.CAPE)) {
         FramedTexture framedTexture = TLSkinCape.instance.getTextureManager().get(name).getCape();
         if (Objects.nonNull(framedTexture)) {
            return framedTexture.getFrame();
         }
      } else {
         TLSkinCape.instance.createTexture(name);
      }

      return sourceValue;
   }

   @Hook(
      injectOnExit = true,
      returnCondition = ReturnCondition.ALWAYS
   )
   public static boolean isWearing(EntityPlayer entityPlayer, EnumPlayerModelParts part) {
      return true;
   }

   @Hook(
      injectOnExit = true,
      returnCondition = ReturnCondition.ALWAYS
   )
   public static String getSkinType(AbstractClientPlayer abstractClientPlayer) {
      String name = abstractClientPlayer.getName();
      ProfileTexture profileTexture = TLSkinCape.instance.getTextureManager().get(name);
      if (Objects.nonNull(profileTexture)) {
         String skinType = profileTexture.getSkinType();
         if ("slim".equals(profileTexture.getSkinType())) {
            return skinType;
         }
      }

      return "default";
   }
}
