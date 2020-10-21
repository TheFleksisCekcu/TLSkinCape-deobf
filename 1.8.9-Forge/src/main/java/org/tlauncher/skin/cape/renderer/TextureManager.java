//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9"!

package org.tlauncher.skin.cape.renderer;

import com.google.common.collect.Lists;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tlauncher.skin.cape.model.DynamicTexture;
import org.tlauncher.skin.cape.model.MinecraftProfileTextureDTO;
import org.tlauncher.skin.cape.model.PreparedTextureData;
import org.tlauncher.skin.cape.model.ProfileTexture;
import org.tlauncher.skin.cape.renderer.texture.FramedTexture;
import org.tlauncher.skin.cape.util.PreparedProfileManager;

public class TextureManager {
   private final Logger LOGGER = LogManager.getLogger(this.getClass());
   private final Map<String, ProfileTexture> textures = new HashMap();
   private static long TIME_GUP = 2000L;
   private PreparedProfileManager preparedProfileManager;
   private long updateTime;
   private long last;

   private void cleanOldTextures() {
      if (this.last < System.currentTimeMillis()) {
         long l = System.currentTimeMillis();
         long current = System.currentTimeMillis();
         List<String> removed = (List)this.textures.entrySet().stream().filter((e) -> {
            return current > ((ProfileTexture)e.getValue()).getTime();
         }).map(Entry::getKey).collect(Collectors.toList());
         removed.remove(Minecraft.getMinecraft().getSession().getUsername());
         Iterator var6 = removed.iterator();

         while(true) {
            String s;
            ProfileTexture profileTexture;
            do {
               if (!var6.hasNext()) {
                  Map var10001 = this.textures;
                  removed.forEach(var10001::remove);
                  this.LOGGER.debug(String.format("removed %s , during %s", String.join(",", Lists.newArrayList(removed)), System.currentTimeMillis() - l));
                  this.last = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1L);
                  return;
               }

               s = (String)var6.next();
               profileTexture = (ProfileTexture)this.textures.get(s);
               if (Objects.nonNull(profileTexture.getSkin())) {
                  Minecraft.getMinecraft().getTextureManager().deleteTexture(profileTexture.getSkin());
               }
            } while(!Objects.nonNull(profileTexture.getCape()));

            Iterator var9 = ((ProfileTexture)this.textures.get(s)).getCape().getFrames().iterator();

            while(var9.hasNext()) {
               ResourceLocation resourceLocation = (ResourceLocation)var9.next();
               Minecraft.getMinecraft().getTextureManager().deleteTexture(resourceLocation);
            }
         }
      }
   }

   public boolean isInit(String username, Type type) {
      ProfileTexture p = (ProfileTexture)this.textures.get(username);
      if (Objects.isNull(p)) {
         return false;
      } else {
         this.updateCache(username);
         switch(type) {
         case SKIN:
            return true;
         case CAPE:
            return p.isCapeReady();
         default:
            return false;
         }
      }
   }

   private void updateCache(String username) {
      if (this.updateTime < System.currentTimeMillis()) {
         this.updateTime = System.currentTimeMillis() + TIME_GUP;
         ProfileTexture profileTexture = (ProfileTexture)this.textures.get(username);
         if (Objects.nonNull(profileTexture)) {
            this.LOGGER.debug("updated cache value " + username);
            profileTexture.updateTime();
         }
      }

   }

   public void createFramedTextures() {
      long l = System.currentTimeMillis();
      this.cleanOldTextures();
      PreparedTextureData preparedTextureData = this.preparedProfileManager.peek();
      if (!Objects.isNull(preparedTextureData)) {
         String playerName = preparedTextureData.getName();
         ProfileTexture profile = (ProfileTexture)this.textures.get(playerName);

         try {
            Image image = (Image)preparedTextureData.getImages().get(Type.CAPE);
            if (Objects.isNull(profile)) {
               profile = new ProfileTexture();
               profile.updateTime();
               this.textures.put(playerName, profile);
               int[] image1 = preparedTextureData.getSkin();
               BufferedImage skin = (BufferedImage)preparedTextureData.getImages().get(Type.SKIN);
               ResourceLocation resourceLocation = null;
               if (Objects.nonNull(image1)) {
                  resourceLocation = new ResourceLocation(String.format("dynamic/skin_%s", playerName));
                  DynamicTexture textureObject = new DynamicTexture(skin.getWidth(), skin.getHeight(), preparedTextureData.getSkin());
                  Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, textureObject);
                  Map<String, String> map = ((MinecraftProfileTextureDTO)preparedTextureData.getProfileTextureDTO().get(Type.SKIN)).getMetadata();
                  if (Objects.nonNull(map)) {
                     profile.setSkinType((String)map.get("model"));
                  }
               }

               profile.setSkin(resourceLocation);
               if (Objects.nonNull(image)) {
                  return;
               }
            }

            MinecraftProfileTextureDTO p = (MinecraftProfileTextureDTO)preparedTextureData.getProfileTextureDTO().get(Type.CAPE);
            if (!Objects.isNull(p) && !preparedTextureData.getCapeFrames().isEmpty()) {
               if (Objects.equals(p.isAnimatedCape(), true)) {
                  FramedTexture cape = profile.getCape();
                  if (Objects.isNull(cape)) {
                     int fps = p.getFps() > 0 ? p.getFps() : 1;
                     cape = new FramedTexture(p.getCapeHeight(), 1000000000L / (long)fps, true);
                     profile.setCape(cape);
                  }

                  if (!this.fillCape(preparedTextureData, p, p.getCapeWidth(), cape)) {
                     return;
                  }
               } else {
                  profile.setCape(FramedTexture.createOneFramedTexture((int[])preparedTextureData.getCapeFrames().get(0), p.getCapeWidth(), p.getCapeHeight()));
               }
            } else {
               this.setReady(playerName, profile);
            }
         } catch (Throwable var12) {
            this.LOGGER.error(var12);
         }

         if (profile.getSkinType() == null) {
            profile.setSkinType("undefined");
         }

         this.setReady(playerName, profile);
         this.preparedProfileManager.poll();
         long t = System.currentTimeMillis() - l;
         if (preparedTextureData.getMaxTimeLoad() < t) {
            preparedTextureData.setMaxTimeLoad(t);
         }

         this.LOGGER.info(String.format("textures %s was added, skin: %s,cape: %s, max waiting: %s ,during : %s ", playerName, Objects.nonNull(profile.getSkin()), Objects.nonNull(profile.getCape()), preparedTextureData.getMaxTimeLoad(), System.currentTimeMillis() - preparedTextureData.getInitTime()));
      }
   }

   private boolean fillCape(PreparedTextureData preparedTextureData, MinecraftProfileTextureDTO p, int width, FramedTexture cape) {
      int size = preparedTextureData.getCapeFrames().size();

      for(int i = 1; i > 0 && cape.getFrames().size() < size; --i) {
         int index = cape.getFrames().size();
         cape.initByOneImage((int[])preparedTextureData.getCapeFrames().get(index), width / 22 * 64, p.getCapeHeight() / 17 * 32);
      }

      return size == cape.getFrames().size();
   }

   private void setReady(String playerName, ProfileTexture profile) {
      profile.setCapeReady(true);
      this.preparedProfileManager.removeByName(playerName);
   }

   public void setPreparedProfileManager(PreparedProfileManager preparedProfileManager) {
      this.preparedProfileManager = preparedProfileManager;
   }

   public ProfileTexture get(String username) {
      return (ProfileTexture)this.textures.get(username);
   }
}
