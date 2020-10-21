package org.tlauncher.skin.cape.asm;

import gloomyfolken.hooklib.minecraft.HookLoader;
import gloomyfolken.hooklib.minecraft.PrimaryClassTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@MCVersion("1.8.9")
public class TLSkinCapeHookLoader extends HookLoader {
   public String[] getASMTransformerClass() {
      return new String[]{PrimaryClassTransformer.class.getName()};
   }

   public void registerHooks() {
      registerHookContainer("org.tlauncher.skin.cape.asm.TLSkinCapeHooks");
   }
}
