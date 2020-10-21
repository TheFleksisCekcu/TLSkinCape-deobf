package gloomyfolken.hooklib.example;

import gloomyfolken.hooklib.asm.Hook;
import gloomyfolken.hooklib.asm.ReturnCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.ForgeHooks;

public class AnnotationHooks {
   @Hook
   public static void resize(Minecraft mc, int x, int y) {
      System.out.println("Resize, x=" + x + ", y=" + y);
   }

   @Hook(
      injectOnExit = true,
      returnCondition = ReturnCondition.ALWAYS
   )
   public static int getTotalArmorValue(ForgeHooks fh, EntityPlayer player, @Hook.ReturnValue int returnValue) {
      return returnValue / 2;
   }

   @Hook(
      returnCondition = ReturnCondition.ON_TRUE,
      intReturnConstant = 100
   )
   public static boolean getPortalCooldown(EntityPlayer player) {
      return player.dimension == 0;
   }

   @Hook(
      injectOnExit = true,
      returnCondition = ReturnCondition.ON_TRUE,
      returnAnotherMethod = "getBrightness"
   )
   public static boolean getBrightnessForRender(Entity entity, float f) {
      return entity.height > 1.5F;
   }

   public static int getBrightness(Entity entity, float f) {
      int oldValue = 0;
      int j = (oldValue >> 20 & 15) / 2;
      int k = (oldValue >> 4 & 15) / 2;
      return j << 20 | k << 4;
   }
}
