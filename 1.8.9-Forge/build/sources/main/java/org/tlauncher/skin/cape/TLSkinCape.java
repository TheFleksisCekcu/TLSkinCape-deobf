package org.tlauncher.skin.cape;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tlauncher.skin.cape.renderer.TextureManager;
import org.tlauncher.skin.cape.util.PreparedProfileManager;

@Mod(
   modid = "tlauncher_custom_cape_skin",
   name = "TLSkinCape",
   version = "1.4",
   acceptedMinecraftVersions = "[1.8.9]"
)
public class TLSkinCape {
   static final String NAME = "TLSkinCape";
   static final String MODID = "tlauncher_custom_cape_skin";
   @Instance("tlauncher_custom_cape_skin")
   public static TLSkinCape instance;
   private static final Logger LOGGER = LogManager.getLogger();
   private TextureManager textureManager;
   private PreparedProfileManager preparedProfileManager;
   private static long lastTimeMills;

   @EventHandler
   public void event(FMLPostInitializationEvent event) {
      this.preparedProfileManager = new PreparedProfileManager();
      this.textureManager = new TextureManager();
      this.textureManager.setPreparedProfileManager(this.preparedProfileManager);
      MinecraftForge.EVENT_BUS.register(this);
   }

   public void createTexture(String name) {
      if (System.currentTimeMillis() - lastTimeMills >= 90L) {
         this.preparedProfileManager.addNewName(name);
         this.textureManager.createFramedTextures();
         lastTimeMills = System.currentTimeMillis();
      }

   }

   public TextureManager getTextureManager() {
      return this.textureManager;
   }
}
