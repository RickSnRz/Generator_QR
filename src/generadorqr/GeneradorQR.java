package generadorqr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Usuario
 */
public class GeneradorQR {

    public void Generador(JFrame frame, JTextField URL){
        int size = 1000;
        String FileType = "png";
        String codigo = URL.getText().trim();
        
        //Choose image path
        String filePath = "";
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION){
            filePath = chooser.getSelectedFile().getAbsolutePath();
        }
        
        //Generate the name
        UUID uuid = UUID.randomUUID();
        String randomName = uuid.toString();
        
        //Generate the QR
        QRCodeWriter qrcode = new QRCodeWriter();
        try {
            BitMatrix matrix = qrcode.encode(codigo, BarcodeFormat.QR_CODE, size, size);
            File f = new File(filePath + "/" + randomName + "." + FileType);
            int matrixWidth = matrix.getWidth();
            BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_BGR);
            image.createGraphics();
            
            Graphics2D gd = (Graphics2D) image.getGraphics();
            gd.setColor(Color.WHITE);//>background
            gd.fillRect(0, 0, size, size);
            gd.setColor(Color.black);//>QR
            
            for (int b = 0; b < matrixWidth; b++){
                for (int j = 0; j < matrixWidth; j++){
                    if (matrix.get(b, j)){
                        gd.fillRect(b, j, 1, 1);
                    }
                }
            }
            
            QR q = new QR();
            
            //Show generated image
            ImageIO.write(image, FileType, f);
            Image mImagen = new ImageIcon(filePath + "/" +  randomName + "." + FileType).getImage();
            ImageIcon mIcono = new ImageIcon(mImagen.getScaledInstance(q.lblQR.getWidth(), q.lblQR.getHeight(), 0));
            if (frame instanceof QR) {
                QR qrFrame = (QR) frame;
                JLabel lblQR = qrFrame.lblQR;
                lblQR.setIcon(mIcono);
            }
            
            
        } catch (WriterException ex){
            Logger.getLogger(QR.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GeneradorQR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Nuevo(JFrame frame, JTextField URL) {
        URL.setText(""); // Limpiar el contenido del JTextField
        
        if (frame instanceof QR) {
            QR qrFrame = (QR) frame;
            JLabel lblQR = qrFrame.lblQR;
            lblQR.setIcon(null); // Eliminar el icono del JLabel
        }
    }
}
