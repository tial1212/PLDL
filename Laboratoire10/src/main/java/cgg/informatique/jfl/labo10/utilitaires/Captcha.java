package cgg.informatique.jfl.labo10.utilitaires;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;


public class Captcha {
	 int largeur = 0;
	 int hauteur = 0;
	 String captchaBase64 = new String();
	 
	 
	 public Captcha (String message, int taillePolice)
	 {
		 Font font = new Font("Tahoma", Font.PLAIN, taillePolice);
     
		 FontRenderContext frc = new FontRenderContext(null, true, true);
		 Rectangle2D bounds = font.getStringBounds(message, frc);
		 this.largeur = (int) bounds.getWidth();
		 this.hauteur = (int) bounds.getHeight();
      
		 BufferedImage image = new BufferedImage(this.largeur, this.hauteur,   BufferedImage.TYPE_INT_RGB);
         Graphics2D g = image.createGraphics();
     
         g.setColor(Color.WHITE);
         g.fillRect(0, 0, this.largeur, this.hauteur);
         g.setColor(Color.BLACK);
         g.setFont(font);
          
         g.drawString(message, (float) bounds.getX(), (float) -bounds.getY());
         g.dispose();
      
         ByteArrayOutputStream os = new ByteArrayOutputStream();

         try {
        	 ImageIO.write(image, "png", os);
        	 byte[] imageBytes = os.toByteArray();
        	 this.captchaBase64 = DatatypeConverter.printBase64Binary(imageBytes);

        	 os.close();
         } catch (IOException e) {
        	 e.printStackTrace();
         }
	 }
	 public String getCatpcha()
	 {
		 return this.captchaBase64;
	 }
	 public int getLargeur()
	 {
		 return this.largeur;
	 }
	 public int getHauteur()
	 {
		 return this.hauteur;
	 }

}
