package org.tlauncher.skin.cape.model;

import java.util.Map;

public class MinecraftProfileTextureDTO {
   private String url;
   private Map<String, String> metadata;
   private Boolean animatedCape;
   private Integer capeHeight;
   private Integer capeWidth;
   private Integer fps;

   public String getUrl() {
      return this.url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public Map<String, String> getMetadata() {
      return this.metadata;
   }

   public void setMetadata(Map<String, String> metadata) {
      this.metadata = metadata;
   }

   public Boolean isAnimatedCape() {
      return this.animatedCape;
   }

   public void setAnimatedCape(boolean animatedCape) {
      this.animatedCape = animatedCape;
   }

   public Integer getCapeHeight() {
      return this.capeHeight;
   }

   public int getFps() {
      return this.fps;
   }

   public void setFps(int fps) {
      this.fps = fps;
   }

   public void setCapeHeight(Integer capeHeight) {
      this.capeHeight = capeHeight;
   }

   public Integer getCapeWidth() {
      return this.capeWidth;
   }

   public void setCapeWidth(Integer capeWidth) {
      this.capeWidth = capeWidth;
   }

   public String toString() {
      return "MinecraftProfileTextureDTO{url='" + this.url + '\'' + ", metadata=" + this.metadata + ", animatedCape=" + this.animatedCape + ", capeHeight=" + this.capeHeight + ", fps=" + this.fps + '}';
   }
}
