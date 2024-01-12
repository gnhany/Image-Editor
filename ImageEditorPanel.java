import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ImageEditorPanel extends JPanel implements KeyListener{

    Color[][] pixels;
    Color[][] otherImage = null;

    public ImageEditorPanel() {
        BufferedImage imageIn = null;
        BufferedImage otherImageIn = null;
        try {
            imageIn = ImageIO.read(new File("dog.jpg"));
            otherImageIn = ImageIO.read(new File("fortnite.jpg"));

        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
        pixels = makeColorArray(imageIn);
        otherImage = makeNewColorArray(otherImageIn);
        setPreferredSize(new Dimension(pixels[0].length, pixels.length));
        setBackground(Color.BLACK);
        addKeyListener(this);
    }

    public void paintComponent(Graphics g) {
        // paints the array pixels onto the screen
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                g.setColor(pixels[row][col]);
                g.fillRect(col, row, 1, 1);
            }
        }
    }

    public void run() {
        // pixels = flipHorizontal(pixels);
        // pixels = flipVert(pixels);
        // pixels = grayScale(pixels);
        // pixels = blur(pixels);
        // pixels = symmetryHorz(pixels, otherImage);
        // call your image-processing methods here OR call them from keyboard event
        // handling methods
        // write image-processing methods as pure functions - for example: pixels =
        // flip(pixels);
        repaint();

    }

    public Color[][] makeColorArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Color[][] result = new Color[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color c = new Color(image.getRGB(col, row), true);
                result[row][col] = c;
            }
        }
        // System.out.println("Loaded image: width: " +width + " height: " + height);
        return result;
    }
    public Color[][] makeNewColorArray(BufferedImage image) {
        Color[][] result = new Color[pixels.length][pixels[0].length];

        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                Color c = new Color(image.getRGB(col, row), true);
                result[row][col] = c;
            }
        }
        return result;
    }

    public Color[][] symmetryHorz(Color[][] normal, Color[][] other){
        for (int row = 0; row < normal.length/2; row++) {
            for (int col = 0; col < normal[0].length; col++) {
                other[row][col] = normal[row][col];
            }
        }
        return other;
    }
    public Color[][] symmetryVert(Color[][] normal, Color[][] other){
        for (int row = 0; row < normal.length; row++) {
            for (int col = 0; col < normal[0].length/2; col++) {
                other[row][col] = normal[row][col];
            }
        }
        return other;
    }

    public Color[][] flipHorizontal(Color[][] origImage) {
        Color[][] horizImage = new Color[origImage.length][origImage[0].length];
        int horzPixel;
        for (int row = 0; row < origImage.length; row++) {
            horzPixel = origImage[0].length - 1;
            for (int col = 0; col < origImage[row].length; col++) {
                horizImage[row][col] = origImage[row][horzPixel];
                horzPixel--;

            }
        }
        return horizImage;
    }

    public Color[][] flipVert(Color[][] origImage) {
        Color[][] vertImage = new Color[origImage.length][origImage[0].length];
        int vertPixel;
        for (int col = 0; col < origImage[0].length; col++) {
            vertPixel = origImage.length - 1;
            for (int row = 0; row < origImage.length; row++) {
                vertImage[row][col] = origImage[vertPixel][col];
                vertPixel--;

            }
        }
        return vertImage;
    }

    public Color[][] grayScale(Color[][] image){
        Color[][] newArr = new Color[image.length][image[0].length];
        int gray;
        int red;
        int green;
        int blue;
        int numsOfColors = 3;
        for (int row = 0; row < newArr.length; row++) {
            for (int col = 0; col < newArr[0].length; col++) {
                red = image[row][col].getRed();
                green = image[row][col].getGreen();
                blue = image[row][col].getBlue();
                gray = (red + green + blue) / numsOfColors;
                newArr[row][col] = new Color(gray, gray, gray);
                
            }
        }
        
        return newArr;
    }
    public Color[][] brighten(Color[][] image){
        Color[][] newArr = new Color[image.length][image[0].length];
        for (int row = 0; row < newArr.length; row++) {
            for (int col = 0; col < newArr[0].length; col++) {
                newArr[row][col] = image[row][col].brighter();
                
            }
        }
        
        return newArr;
    }
    public Color[][] darken(Color[][] image){
        Color[][] newArr = new Color[image.length][image[0].length];
        for (int row = 0; row < newArr.length; row++) {
            for (int col = 0; col < newArr[0].length; col++) {
                newArr[row][col] = image[row][col].darker();
                
            }
        }
        
        return newArr;
    }
    public Color[][] blackAndWhite(Color[][] image){
        Color[][] newArr = new Color[image.length][image[0].length];
        for (int row = 0; row < newArr.length; row++) {
            for (int col = 0; col < newArr[0].length; col++){
                int red = image[row][col].getRed();
                int green = image[row][col].getGreen();
                int blue = image[row][col].getBlue();
                if((red + green + blue) >= 382){
                    newArr[row][col] = new Color(255,255,255);
                }else{
                    newArr[row][col] = new Color(0,0,0);
                }
                
            }
        }
        
        return newArr;
    }

    public Color[][] blur(Color[][] image){
        Color[][] newArr = new Color[image.length][image[0].length];
        final int BLUR_SIZE = 9;
        int numPixels = 0;
        int redValue = 0;
        int greenValue = 0;
        int blueValue = 0;
        for (int row = 0; row < newArr.length; row++){
            for (int col = 0; col < newArr[0].length; col++){
                redValue = 0;
                blueValue = 0;
                greenValue = 0;
                numPixels = 0;
                for (int i = row - BLUR_SIZE; i < row + BLUR_SIZE; i++) {
                    for (int j = col - BLUR_SIZE ; j < col + BLUR_SIZE; j++) {
                        if (i < image.length && j < image[0].length && j >=0 && i >=0){
                            redValue += image[i][j].getRed();
                            blueValue += image[i][j].getBlue();
                            greenValue += image[i][j].getGreen();
                            numPixels++;  
                        }else{}
                        
                    }
                }
               redValue = redValue / numPixels; 
               blueValue = blueValue / numPixels; 
               greenValue = greenValue / numPixels; 
               newArr[row][col] = new Color(redValue, greenValue, blueValue);
               
            }
        }
        return newArr;
    }


    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar()=='+'){
            pixels = symmetryHorz(pixels, otherImage);
        }else if(e.getKeyChar()== '-'){
            pixels = symmetryVert(pixels, otherImage);
        }else if(e.getKeyChar()== '/'){
            pixels = grayScale(pixels);
        }else if(e.getKeyChar()== '1'){
            pixels = flipHorizontal(pixels);
        }else if(e.getKeyChar()== '2'){
            pixels = flipVert(pixels);
        }else if(e.getKeyChar()== '3'){
            pixels = brighten(pixels);
        }else if(e.getKeyChar()== '4'){
            pixels = darken(pixels);
        }else if(e.getKeyChar()== '5'){
            pixels = blur(pixels);
        }else if(e.getKeyChar()== '6'){
            pixels = blackAndWhite(pixels);
        }else if(e.getKeyChar()== '7'){
            pixels = blur(pixels);
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
