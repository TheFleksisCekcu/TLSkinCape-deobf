package gloomyfolken.hooklib.minecraft;

import gloomyfolken.hooklib.asm.AsmHook;
import gloomyfolken.hooklib.asm.ClassMetadataReader;
import gloomyfolken.hooklib.asm.HookClassTransformer;
import java.util.Map;
import net.minecraftforge.fml.common.asm.transformers.DeobfuscationTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public abstract class HookLoader implements IFMLLoadingPlugin {
   private static DeobfuscationTransformer deobfuscationTransformer;
   private static ClassMetadataReader deobfuscationMetadataReader = new DeobfuscationMetadataReader();

   public static HookClassTransformer getTransformer() {
      return (HookClassTransformer)(PrimaryClassTransformer.instance.registeredSecondTransformer ? MinecraftClassTransformer.instance : PrimaryClassTransformer.instance);
   }

   static DeobfuscationTransformer getDeobfuscationTransformer() {
      if (HookLibPlugin.getObfuscated() && deobfuscationTransformer == null) {
         deobfuscationTransformer = new DeobfuscationTransformer();
      }

      return deobfuscationTransformer;
   }

   public static void registerHook(AsmHook hook) {
      getTransformer().registerHook(hook);
   }

   public static void registerHookContainer(String className) {
      getTransformer().registerHookContainer(className);
   }

   public static ClassMetadataReader getDeobfuscationMetadataReader() {
      return deobfuscationMetadataReader;
   }

   public String[] getLibraryRequestClass() {
      return null;
   }

   public String getAccessTransformerClass() {
      return null;
   }

   public String[] getASMTransformerClass() {
      return null;
   }

   public String getModContainerClass() {
      return null;
   }

   public String getSetupClass() {
      return null;
   }

   public void injectData(Map<String, Object> data) {
      this.registerHooks();
   }

   protected abstract void registerHooks();
}
