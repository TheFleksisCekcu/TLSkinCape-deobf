//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "mcp_stable-22-1.8.9"!

package org.tlauncher.skin.cape.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tlauncher.skin.cape.model.MinecraftProfileTextureDTO;
import org.tlauncher.skin.cape.model.PreparedTextureData;

public class PreparedProfileManager {
   private static final Logger LOGGER = LogManager.getLogger();
   private final ExecutorService pool = Executors.newFixedThreadPool(1, (new ThreadFactoryBuilder()).setDaemon(true).build());
   private final Set<String> addedNames = new HashSet();
   private LinkedBlockingQueue<PreparedTextureData> preparedData = new LinkedBlockingQueue();
   private static final int UP_VERGE_LOAD_FRAME = 30;
   private static int machinePerformance = 1;

   public PreparedProfileManager() {
      this.calculateSizeCape();
      this.addNewName(Minecraft.getMinecraft().getSession().getUsername());
   }

   private void calculateSizeCape() {
      try {
         PreparedTextureData texture = new PreparedTextureData();
         final MinecraftProfileTextureDTO test = new MinecraftProfileTextureDTO();
         test.setCapeHeight(272);
         test.setAnimatedCape(true);
         texture.setProfileTextureDTO(new HashMap<Type, MinecraftProfileTextureDTO>() {
            {
               this.put(Type.CAPE, test);
            }
         });
         final BufferedImage image = ImageIO.read(PreparedProfileManager.class.getResource("cape_test_272.png"));
         texture.setImages(new HashMap<Type, BufferedImage>() {
            {
               this.put(Type.CAPE, image);
            }
         });
         List<BufferedImage> list = this.cutAnimatedCape(texture, test);
         long l = System.currentTimeMillis();
         Iterator var7 = list.iterator();

         while(var7.hasNext()) {
            BufferedImage b = (BufferedImage)var7.next();
            new DynamicTexture(b);
         }

         l = (System.currentTimeMillis() - l) / (long)list.size();
         if (l > 30L) {
            machinePerformance = 2;
         }

         LOGGER.debug(String.format("middle time to prepare frame of the cape is %s, performance %s", l, machinePerformance));
      } catch (Throwable var9) {
         LOGGER.error("couldn't calculate size of the cape", var9);
      }

   }

   public void addNewName(String playerName) {
      if (!this.addedNames.contains(playerName)) {
         this.addedNames.add(playerName);
         CompletableFuture.runAsync(() -> {
            PreparedTextureData texture = new PreparedTextureData();
            texture.setName(playerName);

            try {
               Map<Type, MinecraftProfileTextureDTO> profile = this.loadProfileData0(playerName);
               texture.setProfileTextureDTO(profile);
               this.downloadAndValidateImage(profile, texture);
               this.preparedFramesForCape(texture);
               this.preparedFrameForSkin(texture);
               LOGGER.debug("finished downloading data " + playerName);
            } catch (Throwable var4) {
               LOGGER.warn("error", var4);
            }

            this.preparedData.add(texture);
         }, this.pool);
      }

   }

   private void preparedFrameForSkin(PreparedTextureData texture) {
      BufferedImage image = (BufferedImage)texture.getImages().get(Type.SKIN);
      if (Objects.nonNull(image)) {
         int[] elm = new int[image.getWidth() * image.getHeight()];
         image.getRGB(0, 0, image.getWidth(), image.getHeight(), elm, 0, image.getWidth());
         texture.setSkin(elm);
      }

   }

   private void preparedFramesForCape(PreparedTextureData texture) {
      MinecraftProfileTextureDTO p = (MinecraftProfileTextureDTO)texture.getProfileTextureDTO().get(Type.CAPE);
      if (!Objects.isNull(p)) {
         if (!Objects.equals(p.isAnimatedCape(), true)) {
            BufferedImage bufferedImage = (BufferedImage)texture.getImages().get(Type.CAPE);
            p.setCapeWidth(bufferedImage.getWidth());
            p.setCapeHeight(bufferedImage.getHeight());
            texture.getCapeFrames().add(bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), (int[])null, 0, bufferedImage.getWidth()));
         } else {
            Iterator var3 = this.cutAnimatedCape(texture, p).iterator();

            while(var3.hasNext()) {
               BufferedImage image = (BufferedImage)var3.next();
               texture.getCapeFrames().add(image.getRGB(0, 0, image.getWidth(), image.getHeight(), (int[])null, 0, image.getWidth()));
            }

         }
      }
   }

   private List<BufferedImage> cutAnimatedCape(PreparedTextureData texture, MinecraftProfileTextureDTO p) {
      BufferedImage image = (BufferedImage)texture.getImages().get(Type.CAPE);
      int frameHeight = p.getCapeHeight();
      int frameWidth = p.getCapeHeight() / 17 * 22;
      p.setCapeWidth(frameWidth);
      int xFrames = image.getWidth() / frameWidth;
      int yFrames = image.getHeight() / frameHeight;
      List<BufferedImage> list = new ArrayList();

      for(int j = 0; j < xFrames; ++j) {
         for(int i = 0; i < yFrames; ++i) {
            BufferedImage image1 = new BufferedImage(frameWidth / 22 * 64, frameHeight / 17 * 32, 6);
            boolean flag = false;

            for(int y = 0; y < frameHeight; ++y) {
               for(int x = 0; x < frameWidth; ++x) {
                  int color = image.getRGB(j * frameWidth + x, i * frameHeight + y);
                  if (!flag) {
                     int alpha = color >>> 24 & 255;
                     if (alpha != 0) {
                        flag = true;
                     }
                  }

                  try {
                     image1.setRGB(x, y, color);
                  } catch (ArrayIndexOutOfBoundsException var17) {
                     LOGGER.error("Coordinates: x " + x + " y " + y + " color " + color, var17);
                  }
               }
            }

            if (!flag) {
               return list;
            }

            list.add(image1);
         }
      }

      return list;
   }

   private Map<Type, MinecraftProfileTextureDTO> loadProfileData0(String playerName) throws IOException {
      IOException exception = new IOException("can't download profile");
      int i = 0;

      while(i < 3) {
         try {
            return this.loadProfileData(playerName);
         } catch (IOException var7) {
            exception = var7;

            try {
               Thread.sleep(1500L);
            } catch (InterruptedException var6) {
            }

            ++i;
         }
      }

      throw exception;
   }

   private Map<Type, MinecraftProfileTextureDTO> loadProfileData(String playerName) throws IOException {
      Gson gson = new Gson();
      playerName = URLEncoder.encode(playerName, "UTF-8");
      URL url = new URL(String.format("http://auth.tlauncher.org/skin/profile/texture/login/%s", playerName));
      URLConnection urlConnection = url.openConnection();
      urlConnection.setConnectTimeout(40000);
      urlConnection.setReadTimeout(30000);
      StringWriter writer = new StringWriter();
      InputStream inputStream = urlConnection.getInputStream();
      IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
      String response = writer.toString();
      Map<Type, MinecraftProfileTextureDTO> textureMap = (Map)gson.fromJson(response, (new TypeToken<HashMap<Type, MinecraftProfileTextureDTO>>() {
      }).getType());
      inputStream.close();
      return textureMap;
   }

   private void downloadAndValidateImage(Map<Type, MinecraftProfileTextureDTO> profile, PreparedTextureData texture) throws IOException {
      Iterator var3 = profile.entrySet().iterator();

      while(true) {
         Entry entry;
         BufferedImage image;
         do {
            if (!var3.hasNext()) {
               return;
            }

            entry = (Entry)var3.next();
            image = this.downloadImage0(entry);
            if (((Type)entry.getKey()).equals(Type.SKIN)) {
               image = TextureUtils.getRightSkin(image);
            }
         } while(((Type)entry.getKey()).equals(Type.CAPE) && (image.getWidth() % 22 != 0 || image.getHeight() % 17 != 0) && (image.getWidth() % 64 != 0 || image.getHeight() % 32 != 0));

         if (((Type)entry.getKey()).equals(Type.CAPE) && machinePerformance == 2) {
            ((MinecraftProfileTextureDTO)profile.get(Type.CAPE)).setCapeHeight(136);
            image = TextureUtils.resize(image, image.getWidth() / 2, image.getHeight() / 2);
         }

         texture.getImages().put((Type) entry.getKey(), image);
      }
   }

   private BufferedImage downloadImage0(Entry<Type, MinecraftProfileTextureDTO> entry) throws IOException {
      IOException exception = new IOException("can't load image");
      int i = 0;

      while(i < 3) {
         try {
            return ImageIO.read(new URL(((MinecraftProfileTextureDTO)entry.getValue()).getUrl()));
         } catch (IOException var7) {
            exception = var7;

            try {
               Thread.sleep(1500L);
            } catch (InterruptedException var6) {
            }

            ++i;
         }
      }

      throw exception;
   }

   public void removeByName(String name) {
      this.addedNames.remove(name);
   }

   public PreparedTextureData peek() {
      return (PreparedTextureData)this.preparedData.peek();
   }

   public void poll() {
      this.preparedData.poll();
   }
}
