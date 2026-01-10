package org.acabativa.rc.storage.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageProcessor {
    
    private static final int THUMBNAIL_WIDTH = 300;
    private static final int THUMBNAIL_HEIGHT = 300;
    
    public byte[] generateThumbnail(byte[] originalImage, String format) throws IOException {
        BufferedImage original = ImageIO.read(new ByteArrayInputStream(originalImage));
        
        // Calculate dimensions maintaining aspect ratio
        int originalWidth = original.getWidth();
        int originalHeight = original.getHeight();
        
        double widthRatio = (double) THUMBNAIL_WIDTH / originalWidth;
        double heightRatio = (double) THUMBNAIL_HEIGHT / originalHeight;
        double ratio = Math.min(widthRatio, heightRatio);
        
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);
        
        // Create thumbnail
        BufferedImage thumbnail = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = thumbnail.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(original, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        
        // Convert to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(thumbnail, format, baos);
        
        return baos.toByteArray();
    }
    
    public Dimension getImageDimensions(byte[] imageData) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
        return new Dimension(image.getWidth(), image.getHeight());
    }
}