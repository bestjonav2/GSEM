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
import java.util.Random;
import java.util.Scanner;
//import java.util.Scanner;
import javax.imageio.ImageIO;
//import javax.swing.JOptionPane;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

/**
 *
 * @author dany
 */
public class Cifrado {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)  {
        // TODO code application logic here
        String key = "92AE31A79FEEB2A3"; //llave
        String iv = "0123456789ABCDEF"; // vector de inicialización
        String codigo=""; 
        String codigoEnc;
        String ruta;
        String nombre;
        String textoEmbebido;
        Scanner sc = new Scanner(System.in);
        char opcion=' ';
        
        System.out.println("Encripatar(e) o desencriptar(d)");
        opcion=sc.next().charAt(0);
        
        System.out.println("Ingrese la ruta del archivo");
        ruta=sc.next();
        
        System.out.println("Ingrese el nombre del archivo nuevo");
        nombre=sc.next();
        
        try{
            switch(opcion){
                case 'e':
                    System.out.println("Ingrese el texto a embeber");
                    sc.nextLine();
                    textoEmbebido=sc.nextLine();
                    String codigoDesordenado;
                    long semilla=System.currentTimeMillis();
                    int n=20;
                    int[] secuencia = generarSecuencia(semilla, n);
                    
                    codigo=textoEmbebido+"#"+leerImagen(ruta);
                    codigoDesordenado=semilla+";"+desordenarCodigo(secuencia, codigo);

                    codigoEnc=cifrado.StringEncrypt.encrypt(key, iv,codigoDesordenado);
                    blocDeNotas(codigoEnc,nombre);

                    System.out.println("El encriptado termino");
                    break;
                case 'd':
                    File archivoEncriptado = new File(ruta);
                    BufferedReader lector = new BufferedReader( new 
                        FileReader(archivoEncriptado) );
                    codigoEnc=lector.readLine();
                    codigo=cifrado.StringEncrypt.decrypt(key, iv, codigoEnc);
                    crearImagen(codigo,nombre);
                    System.out.println("La imagen ya acabo");
                    break;

                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        
        } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                System.out.println(""+e.getMessage());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println(""+e.getMessage());
            } catch(Exception e2){
                System.out.println(""+e2.getMessage());
            } 
    }
    
    public static String leerImagen(String ruta) throws Exception{
        
        String encodedfile = null;
            
        File imagen = new File(ruta);
        FileInputStream fileInputStreamReader = new FileInputStream(imagen);
        byte[] bytes = new byte[(int)imagen.length()];
        fileInputStreamReader.read(bytes);
        encodedfile = new String(encodeBase64(bytes), "UTF-8");
                
        return encodedfile;
    }

    public static void blocDeNotas(String codigo, String nombre) throws IOException, archivoExiste {
        new File("./Data").mkdirs();
        String ruta = "./Data/"+nombre+".txt";
        File archivo = new File(ruta);
        BufferedWriter bw = null;
        if(!archivo.exists()){
            bw = new BufferedWriter(new FileWriter(archivo, true));
            bw.write(""+codigo);
            bw.close();
        }
        else throw new archivoExiste("El archivo ya existe");
        
    }
    
    public static void crearImagen(String imgCodificada,String nombre) throws IOException,archivoExiste {
        long semilla = Long.parseLong(imgCodificada.split(";")[0]);
        int n;
        
        String[] codigoDesordenado= imgCodificada.split(";")[1].split(",");
        n=codigoDesordenado.length;
        int[] secuencia = generarSecuencia(semilla, n);
        String codigoOrdenado=ordenarCodigo(secuencia, codigoDesordenado);
        String textoEmbebido = codigoOrdenado.split("#")[0];
        String base64Image = codigoOrdenado.split("#")[1];
        
        
        System.out.println("El texto embebido es: "+textoEmbebido);
                
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
        
        new File("./Data").mkdirs();
        String ruta = "./Data/"+nombre+".jpg";
        File archivo = new File(ruta);
        if(!archivo.exists()){
            BufferedWriter bw = null;
            ImageIO.write(img, "JPG", archivo); 
        }else{
            throw new archivoExiste("Este archivo ya existe");
        }
        
            
    }
    
    public static int[] generarSecuencia(long semilla, int n){
        int[] numeros = new int[n];
        int sigNumero;
        boolean existe=false;
        Random generador = new Random(semilla);
        
        for(int i=0;i<n;i++){
            existe=false;
            sigNumero=(int)(generador.nextDouble() * (n));
            for(int x=0;x<i;x++){
                if(numeros[x]==sigNumero){
                    existe=true;
                    break;
                }
            }
            if(existe)i--;
            else numeros[i]=sigNumero; 
        }
        return numeros;
    }
    
    public static String desordenarCodigo(int[] secuencia, String codigo ){
        String codigoDesordenado="";
        int n = secuencia.length;
        String[] arregloCodigoOrdenado=new String[n];
        int largo=codigo.length()/n;
        
        for(int i=0;i<n;i++){
            if( i==19 ) arregloCodigoOrdenado[i]=codigo.substring(i*largo);
            else{
                    arregloCodigoOrdenado[i]=codigo.substring(i*largo,i*largo+largo);
            }
        }
        
        for(int x=0;x<n;x++){
            if(x==(n-1)) codigoDesordenado+=arregloCodigoOrdenado[ secuencia[x] ];
            else codigoDesordenado+=arregloCodigoOrdenado[ secuencia[x] ]+",";    
        }
        
        return codigoDesordenado;
    }
    
    public static String ordenarCodigo(int[] secuencia, String[] codigoDesordenado){
        String codigoOrdenado="";
        int indexNumero=0;
        int n=secuencia.length;
        
        for(int i=0;i<n;i++){
            for(int x=0;x<n;x++){
                if(secuencia[x]==i){
                    indexNumero=x;
                    break;
                }
            }
           codigoOrdenado+=codigoDesordenado[indexNumero];
        }
        return codigoOrdenado;
    }
}

class archivoExiste extends Exception{
    public archivoExiste(String msg){
        super(msg);
    }
}