/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifrado;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;
//import  org.apache.commons.codec.binary.Base64.decodeBase64(String);
//import  org.apache.commons.codec.binary.Base64.encodeBase64(byte[], boolean);

/**
 *
 * @author dany
 */
public class StringEncrypt {
    // Definición del tipo de algoritmo a utilizar (AES, DES, RSA)
    private final static String ALG = "AES";
    // Definición del modo de cifrado a utilizar
    private final static String CI = "AES/CBC/PKCS5Padding";
 
    public static String encrypt(String key, String iv, String cleartext) throws Exception {
            Cipher cipher = Cipher.getInstance(CI);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), ALG);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(cleartext.getBytes());
            return new String(encodeBase64(encrypted));
            //return new String(encrypted);
    }

    public static String decrypt(String key, String iv, String encrypted) throws Exception {
            Cipher cipher = Cipher.getInstance(CI);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), ALG);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            byte[] enc = decodeBase64(encrypted);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] decrypted = cipher.doFinal(enc);
            return new String(decrypted);
    }
}
