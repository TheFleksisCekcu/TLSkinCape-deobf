package gloomyfolken.hooklib.example;

import gloomyfolken.hooklib.minecraft.HookLoader;
import gloomyfolken.hooklib.minecraft.PrimaryClassTransformer;

public class ExampleHookLoader extends HookLoader {
   public String[] getASMTransformerClass() {
      return new String[]{PrimaryClassTransformer.class.getName()};
   }

   public void registerHooks() {
      registerHookContainer("gloomyfolken.hooklib.example.AnnotationHooks");
   }
}
