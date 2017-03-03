/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifrado;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
//import java.util.Scanner;
import javax.imageio.ImageIO;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

/**
 *
 * @author dany
 */
public class Cifrado {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        String key = "92AE31A79FEEB2A3"; //llave
        String iv = "0123456789ABCDEF"; // vector de inicialización
        String codigo=""; 
        String codigoEnc;
        String ruta;
        Scanner sc = new Scanner(System.in);
        char opcion=' ';
        
        System.out.println("Encripatar(e) o desencriptar(d)");
        opcion=sc.next().charAt(0);
        
        System.out.println("Ingrese la ruta del archivo");
        ruta=sc.next();
        
        switch(opcion){
            case 'e':
                codigo=leerImagen(ruta);
                /*Pueba*/
                //blocDeNotas(codigo,"pru");
                /**/
                
                codigoEnc=cifrado.StringEncrypt.encrypt(key, iv,codigo);
                blocDeNotas(codigoEnc,"enc");
                
                System.out.println("El encriptado termino");
                break;
            case 'd':
                File archivoEncriptado = new File(ruta);
                BufferedReader lector = new BufferedReader( new 
                    FileReader(archivoEncriptado) );
                codigoEnc=lector.readLine();
                codigo=cifrado.StringEncrypt.decrypt(key, iv, codigoEnc);
                crearImagen(codigo);
                System.out.println("La imagen ya acabo");
                break;
            
            default:
                System.out.println("Opcion no valida");
                break;
        }
       
        
        //System.out.println("Texto encriptado: " + cifrado.StringEncrypt.encrypt(key, iv, cleartext));
        //System.out.println("Texto desencriptado: " + cifrado.StringEncrypt.decrypt(key, iv, cifrado.StringEncrypt.encrypt(key, iv, cleartext)));
        //String dec=cifrado.StringEncrypt.decrypt(key, iv, t1);
        //crearImagen(dec);
    }
    
    public static String leerImagen(String ruta){
        File imagen = new File(ruta);
        
        String encodedfile = null;
            try {
                FileInputStream fileInputStreamReader = new FileInputStream(imagen);
                byte[] bytes = new byte[(int)imagen.length()];
                fileInputStreamReader.read(bytes);
                encodedfile = new String(encodeBase64(bytes), "UTF-8");
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return encodedfile;
    }

    public static void blocDeNotas(String codigo, String nombre) {
        new File("./Data").mkdirs();
        String ruta = "./Data/"+nombre+".txt";
        File archivo = new File(ruta);
        BufferedWriter bw = null;
        
        try {
            bw = new BufferedWriter(new FileWriter(archivo, true));
            bw.write(""+codigo);
            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void crearImagen(String imgCodificada) throws IOException{
        String base64Image = imgCodificada;
                //.split(",")[1];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
        
        new File("./Data").mkdirs();
        String ruta = "./Data/d.jpg";
        File archivo = new File(ruta);
        BufferedWriter bw = null;
        
        ImageIO.write(img, "JPG", archivo);     
    }
}
