package gloomyfolken.hooklib.asm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class HookInjectorClassVisitor extends ClassVisitor {
   List<AsmHook> hooks;
   List<AsmHook> injectedHooks = new ArrayList(1);
   boolean visitingHook;
   HookClassTransformer transformer;
   String superName;

   public HookInjectorClassVisitor(HookClassTransformer transformer, ClassWriter cv, List<AsmHook> hooks) {
      super(327680, cv);
      this.hooks = hooks;
      this.transformer = transformer;
   }

   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
      this.superName = superName;
      super.visit(version, access, name, signature, superName, interfaces);
   }

   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
      Iterator var7 = this.hooks.iterator();

      while(var7.hasNext()) {
         AsmHook hook = (AsmHook)var7.next();
         if (this.isTargetMethod(hook, name, desc) && !this.injectedHooks.contains(hook)) {
            mv = hook.getInjectorFactory().createHookInjector((MethodVisitor)mv, access, name, desc, hook, this);
            this.injectedHooks.add(hook);
         }
      }

      return (MethodVisitor)mv;
   }

   public void visitEnd() {
      Iterator var1 = this.hooks.iterator();

      while(var1.hasNext()) {
         AsmHook hook = (AsmHook)var1.next();
         if (hook.getCreateMethod() && !this.injectedHooks.contains(hook)) {
            hook.createMethod(this);
         }
      }

      super.visitEnd();
   }

   protected boolean isTargetMethod(AsmHook hook, String name, String desc) {
      return hook.isTargetMethod(name, desc);
   }
}
