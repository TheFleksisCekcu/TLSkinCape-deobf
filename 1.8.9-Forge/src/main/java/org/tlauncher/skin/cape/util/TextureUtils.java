package org.tlauncher.skin.cape.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.annotation.Nullable;

public class TextureUtils {
   @Nullable
   public static BufferedImage getRightSkin(BufferedImage image) {
      if (image == null) {
         return null;
      } else if (image.getWidth() % 64 == 0 && image.getHeight() % 64 == 0 && image.getHeight() == image.getWidth()) {
         return image;
      } else {
         BufferedImage temp = new BufferedImage(image.getWidth(), image.getHeight() * 2, 6);
         int m = image.getWidth() / 64;

         for(int i = 0; i < image.getWidth(); ++i) {
            for(int j = 0; j < image.getHeight(); ++j) {
               if (j >= 16 * m && j < 32 * m) {
                  if (i < m * 4) {
                     temp.setRGB(m * 20 - i, 32 * m + j, image.getRGB(i, j));
                  } else if (i < m * 8) {
                     temp.setRGB(m * 28 - i, 32 * m + j, image.getRGB(i, j));
                  } else if (i < m * 12) {
                     temp.setRGB(m * 36 - i, 32 * m + j, image.getRGB(i, j));
                  } else if (i < m * 16) {
                     temp.setRGB(m * 44 - i, 32 * m + j, image.getRGB(i, j));
                  }

                  if (i >= 40 * m) {
                     if (i < 44 * m) {
                        temp.setRGB(76 * m - i, 32 * m + j, image.getRGB(i, j));
                     } else if (i < 48 * m) {
                        temp.setRGB(84 * m - i, 32 * m + j, image.getRGB(i, j));
                     } else if (i < 52 * m) {
                        temp.setRGB(92 * m - i, 32 * m + j, image.getRGB(i, j));
                     } else if (i < 56 * m) {
                        temp.setRGB(100 * m - i, 32 * m + j, image.getRGB(i, j));
                     }
                  }
               }

               temp.setRGB(i, j, image.getRGB(i, j));
            }
         }

         return temp;
      }
   }

   public static BufferedImage resize(BufferedImage img, int newW, int newH) {
      Image tmp = img.getScaledInstance(newW, newH, 4);
      BufferedImage dimg = new BufferedImage(newW, newH, 2);
      Graphics2D g2d = dimg.createGraphics();
      g2d.drawImage(tmp, 0, 0, (ImageObserver)null);
      g2d.dispose();
      return dimg;
   }
}
