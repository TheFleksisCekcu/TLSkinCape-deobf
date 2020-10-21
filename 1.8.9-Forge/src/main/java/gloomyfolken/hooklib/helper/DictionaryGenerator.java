package gloomyfolken.hooklib.helper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.apache.commons.io.FileUtils;

public class DictionaryGenerator {
   public static void main(String[] args) throws Exception {
      List<String> lines = FileUtils.readLines(new File("methods.csv"));
      lines.remove(0);
      HashMap<Integer, String> map = new HashMap();
      Iterator var3 = lines.iterator();

      while(var3.hasNext()) {
         String str = (String)var3.next();
         String[] splitted = str.split(",");
         int first = splitted[0].indexOf(95);
         int second = splitted[0].indexOf(95, first + 1);
         int id = Integer.valueOf(splitted[0].substring(first + 1, second));
         map.put(id, splitted[1]);
      }

      DataOutputStream out = new DataOutputStream(new FileOutputStream("methods.bin"));
      out.writeInt(map.size());
      Iterator var10 = map.entrySet().iterator();

      while(var10.hasNext()) {
         Entry<Integer, String> entry = (Entry)var10.next();
         out.writeInt((Integer)entry.getKey());
         out.writeUTF((String)entry.getValue());
      }

      out.close();
   }
}
