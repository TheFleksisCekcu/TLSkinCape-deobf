package org.tlauncher.skin.cape.model;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreparedTextureData {
   private String name;
   private volatile Map<Type, MinecraftProfileTextureDTO> profileTextureDTO;
   private Map<Type, BufferedImage> images = Collections.synchronizedMap(new HashMap());
   private List<int[]> capeFrames = new ArrayList();
   private int[] skin;
   private int preparedIndexFrame;
   private long maxTimeLoad;
   private long initTime = System.currentTimeMillis();

   public Map<Type, MinecraftProfileTextureDTO> getProfileTextureDTO() {
      return this.profileTextureDTO;
   }

   public void setProfileTextureDTO(Map<Type, MinecraftProfileTextureDTO> profileTextureDTO) {
      this.profileTextureDTO = profileTextureDTO;
   }

   public Map<Type, BufferedImage> getImages() {
      return this.images;
   }

   public void setImages(Map<Type, BufferedImage> images) {
      this.images = images;
   }

   public boolean hasFrame() {
      return this.preparedIndexFrame < this.capeFrames.size();
   }

   public List<int[]> getCapeFrames() {
      return this.capeFrames;
   }

   public void setCapeFrames(List<int[]> capeFrames) {
      this.capeFrames = capeFrames;
   }

   public int getPreparedIndexFrame() {
      return this.preparedIndexFrame;
   }

   public int getPreparedIndexFrameAndIncrease() {
      return this.preparedIndexFrame++;
   }

   public void setPreparedIndexFrame(int preparedIndexFrame) {
      this.preparedIndexFrame = preparedIndexFrame;
   }

   public int[] getSkin() {
      return this.skin;
   }

   public void setSkin(int[] skin) {
      this.skin = skin;
   }

   public long getMaxTimeLoad() {
      return this.maxTimeLoad;
   }

   public void setMaxTimeLoad(long maxTimeLoad) {
      this.maxTimeLoad = maxTimeLoad;
   }

   public long getInitTime() {
      return this.initTime;
   }

   public void setInitTime(long initTime) {
      this.initTime = initTime;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
