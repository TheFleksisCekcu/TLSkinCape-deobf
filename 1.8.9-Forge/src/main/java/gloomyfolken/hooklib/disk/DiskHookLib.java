package gloomyfolken.hooklib.disk;

import gloomyfolken.hooklib.asm.HookClassTransformer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class DiskHookLib {
   File untransformedDir = new File("untransformed");
   File transformedDir = new File("transformed");
   File hooksDir = new File("hooks");

   public static void main(String[] args) throws IOException {
      (new DiskHookLib()).process();
   }

   void process() throws IOException {
      HookClassTransformer transformer = new HookClassTransformer();
      Iterator var2 = getFiles(".class", this.hooksDir).iterator();

      File file;
      while(var2.hasNext()) {
         file = (File)var2.next();
         transformer.registerHookContainer(FileUtils.readFileToByteArray(file));
      }

      var2 = getFiles(".class", this.untransformedDir).iterator();

      while(var2.hasNext()) {
         file = (File)var2.next();
         byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
         String className = "";
         transformer.transform(className, bytes);
      }

   }

   private static List<File> getFiles(String postfix, File dir) throws IOException {
      ArrayList<File> files = new ArrayList();
      File[] filesArray = dir.listFiles();
      if (filesArray != null) {
         File[] var4 = dir.listFiles();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File file = var4[var6];
            if (file.isDirectory()) {
               files.addAll(getFiles(postfix, file));
            } else if (file.getName().toLowerCase().endsWith(postfix)) {
               files.add(file);
            }
         }
      }

      return files;
   }
}
