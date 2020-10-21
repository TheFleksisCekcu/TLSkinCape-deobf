package gloomyfolken.hooklib.asm;

import org.objectweb.asm.MethodVisitor;

public abstract class HookInjectorFactory {
   protected boolean isPriorityInverted = false;

   abstract HookInjectorMethodVisitor createHookInjector(MethodVisitor var1, int var2, String var3, String var4, AsmHook var5, HookInjectorClassVisitor var6);

   static class LineNumber extends HookInjectorFactory {
      private int lineNumber;

      public LineNumber(int lineNumber) {
         this.lineNumber = lineNumber;
      }

      public HookInjectorMethodVisitor createHookInjector(MethodVisitor mv, int access, String name, String desc, AsmHook hook, HookInjectorClassVisitor cv) {
         return new HookInjectorMethodVisitor.LineNumber(mv, access, name, desc, hook, cv, this.lineNumber);
      }
   }

   static class MethodExit extends HookInjectorFactory {
      public static final HookInjectorFactory.MethodExit INSTANCE = new HookInjectorFactory.MethodExit();

      private MethodExit() {
         this.isPriorityInverted = true;
      }

      public HookInjectorMethodVisitor createHookInjector(MethodVisitor mv, int access, String name, String desc, AsmHook hook, HookInjectorClassVisitor cv) {
         return new HookInjectorMethodVisitor.MethodExit(mv, access, name, desc, hook, cv);
      }
   }

   static class MethodEnter extends HookInjectorFactory {
      public static final HookInjectorFactory.MethodEnter INSTANCE = new HookInjectorFactory.MethodEnter();

      private MethodEnter() {
      }

      public HookInjectorMethodVisitor createHookInjector(MethodVisitor mv, int access, String name, String desc, AsmHook hook, HookInjectorClassVisitor cv) {
         return new HookInjectorMethodVisitor.MethodEnter(mv, access, name, desc, hook, cv);
      }
   }
}
