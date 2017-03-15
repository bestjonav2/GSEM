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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JTextArea;
import static org.apache.commons.codec.binary.Base64.encodeBase64;


class archivoExiste extends Exception
{
    public archivoExiste(String msg)
    {
        super(msg);
    }
}

/**
 *
 * @author PROPIETARIO
 */
public class cifrar {
    private String leerImagen(String ruta) throws Exception
    {
        String encodedfile = null;  
        File imagen = new File(ruta);
        FileInputStream fileInputStreamReader = new FileInputStream(imagen);
        byte[] bytes = new byte[(int)imagen.length()];
        fileInputStreamReader.read(bytes);
        encodedfile = new String(encodeBase64(bytes), "UTF-8");             
        return encodedfile;
    }

    private  void blocDeNotas(String codigo, String nombre) throws IOException, archivoExiste 
    {
        new File("./Data").mkdirs();
        String ruta = "./Data/"+nombre+".txt";
        File archivo = new File(ruta);
        BufferedWriter bw = null;
        if(!archivo.exists())
        {
            bw = new BufferedWriter(new FileWriter(archivo, true));
            bw.write(""+codigo);
            bw.close();
        }
        else throw new archivoExiste("El archivo ya existe");       
    }
    
    private  String crearImagen(String imgCodificada,String nombre, JTextArea msg) throws IOException,archivoExiste 
    {
        long semilla = Long.parseLong(imgCodificada.split(";")[0]);
        int n;       
        String[] codigoDesordenado= imgCodificada.split(";")[1].split("°");
        n=codigoDesordenado.length;
        int[] secuencia = generarSecuencia(semilla, n);
        String codigoOrdenado=ordenarCodigo(secuencia, codigoDesordenado);
        String textoEmbebido = codigoOrdenado.split("#")[0];
        String base64Image = codigoOrdenado.split("#")[1];
        
        //System.out.println("El texto embebido es: "+textoEmbebido);
                
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
        
        new File("./Data").mkdirs();
        /*
        
        */
        String ruta = "./Data/"+nombre+".jpg";
        File archivo = new File(ruta);
        if(!archivo.exists())
        {
            BufferedWriter bw = null;
            ImageIO.write(img, "JPG", archivo); 
        }
        else
        {
            throw new archivoExiste("Este archivo ya existe");
        }
        msg.setText(textoEmbebido);
        return textoEmbebido;
            
    }
    
    private  int[] generarSecuencia(long semilla, int n)
    {
        int[] numeros = new int[n];
        int sigNumero;
        boolean existe=false;
        Random generador = new Random(semilla);
        
        for(int i=0;i<n;i++)
        {
            existe=false;
            sigNumero=(int)(generador.nextDouble() * (n));
            for(int x=0;x<i;x++)
            {
                if(numeros[x]==sigNumero)
                {
                    existe=true;
                    break;
                }
            }
            if(existe)i--;
            else numeros[i]=sigNumero; 
        }
        return numeros;
    }
    
    private  String desordenarCodigo(int[] secuencia, String codigo )
    {
        String codigoDesordenado="";
        int n = secuencia.length;
        String[] arregloCodigoOrdenado=new String[n];
        int largo=codigo.length()/n;
        
        for(int i=0;i<n;i++)
        {
            if( i==19 ) 
            {
                arregloCodigoOrdenado[i]=codigo.substring(i*largo);
            }
            else
            {
                arregloCodigoOrdenado[i]=codigo.substring(i*largo,i*largo+largo);
            }
        }
        
        for(int x=0;x<n;x++)
        {
            if(x==(n-1))
            {
                codigoDesordenado+=arregloCodigoOrdenado[ secuencia[x] ];
            }
            else 
            {
                codigoDesordenado+=arregloCodigoOrdenado[ secuencia[x] ]+"°";
            }    
        }       
        return codigoDesordenado;
    }
    
    private  String ordenarCodigo(int[] secuencia, String[] codigoDesordenado)
    {
        String codigoOrdenado="";
        int indexNumero=0;
        int n=secuencia.length;
        
        for(int i=0;i<n;i++)
        {
            for(int x=0;x<n;x++)
            {
                if(secuencia[x]==i)
                {
                    indexNumero=x;
                    break;
                }
            }
           codigoOrdenado+=codigoDesordenado[indexNumero];
        }
        return codigoOrdenado;
    }
    
    public void encriptarImagen(String TextoAEmbeber, int n, String ruta, String usuario, String password, String nombre, JTextArea mosCodigo)
            throws Exception
    {
        String codigoDesordenado;
        long semilla=System.currentTimeMillis();
        String codigo, codigoEnc;           
        int[] secuencia = generarSecuencia(semilla, n);
        
        String key=JgetKey.genKey(usuario, password);
        String iv=JgetKey.genIV(usuario, password);
        codigo=TextoAEmbeber+"#"+leerImagen(ruta);
        codigoDesordenado=semilla+";"+desordenarCodigo(secuencia, codigo);

        codigoEnc=StringEncrypt.encrypt(key, iv,codigoDesordenado);
        blocDeNotas(codigoEnc,nombre);
        mosCodigo.setText(codigoEnc);
    }
    
    public String desencriptarImagen(String ruta, String usuario, String password,String nombre, JTextArea orden, JTextArea msg) throws Exception{
        String key=JgetKey.genKey(usuario, password);
        String iv=JgetKey.genIV(usuario, password);
        File archivoEncriptado = new File(ruta);
        BufferedReader lector = new BufferedReader( new FileReader(archivoEncriptado) );
        String codigoEnc=lector.readLine();
        String codigo=StringEncrypt.decrypt(key, iv, codigoEnc);
        orden.setText(codigo);
        return crearImagen(codigo,nombre,msg);
    }
    
}
