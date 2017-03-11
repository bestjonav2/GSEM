import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class PasswordHash
{
    public  final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    
    private int SALT_BYTES = 4;
    private int HASH_BYTES = 24;
    private int PBKDF2_ITERATIONS = 1000;

	private String passwordGuardada="";
	private String saltGuardada="";
	
	public PasswordHash(){
		
	}
	
	public PasswordHash(int salt_bytes,int hash_bytes,int iterations){
		SALT_BYTES=salt_bytes;
		HASH_BYTES=hash_bytes;
		PBKDF2_ITERATIONS=iterations;
	}
	
	
	public String getHash(){
		return passwordGuardada;
	}
	
	public String getSalt(){
		return saltGuardada;
	}

    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param   password    the password to hash
     */
    public  void createHash(String password)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
         createHash(password.toCharArray());
    }

    /**
     * Asigna el Hahsh de la contraseña y la salt
     *
     * @param   password    contraseña a encriptar
     *             
     */
    public  void createHash(char[] password)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        // Genera el salt al azar
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);

        // 	Genera el hash de la contraseña
        byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTES);
       
		saltGuardada=toHex(salt);
		passwordGuardada=toHex(hash);
    }

    /**
     * Validates a password using a hash.
     *
     * @param   password    the password to check
     * @param   goodHash    the hash of the valid password
     * @return              true if the password is correct, false if not
     */
    public boolean validatePassword(String password, String goodHash, String salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return validatePassword(password.toCharArray(), goodHash, salt);
    }

    /**
     * Validates a password using a hash.
     *
     * @param   password    the password to check
     * @param   goodHash    the hash of the valid password
     * @return              true if the password is correct, false if not
     */
    public boolean validatePassword(char[] password, String goodHash, String salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        // Decode the hash into its parameters
        String[] params = goodHash.split(":");
        int iterations = PBKDF2_ITERATIONS;
        byte[] salByte = fromHex(salt);
        byte[] hash = fromHex(goodHash);
        // Compute the hash of the provided password, using the same salt, 
        // iteration count, and hash length
        byte[] testHash = pbkdf2(password, salByte, iterations, hash.length);
        // Compare the hashes in constant time. The password is correct if
        // both hashes match.
        return slowEquals(hash, testHash);
    }

    /**
     * Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line 
     * system using a timing attack and then attacked off-line.
     * 
     * @param   a       the first byte array
     * @param   b       the second byte array 
     * @return          true if both byte arrays are the same, false if not
     */
    private boolean slowEquals(byte[] a, byte[] b)
    {
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }

    /**
     *  Computes the PBKDF2 hash of a password.
     *
     * @param   password    the password to hash.
     * @param   salt        the salt
     * @param   iterations  the iteration count (slowness factor)
     * @param   bytes       the length of the hash to compute in bytes
     * @return              the PBDKF2 hash of the password
     */
    private byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    /**
     * Converts a string of hexadecimal characters into a byte array.
     *
     * @param   hex         the hex string
     * @return              the hex string decoded into a byte array
     */
    private byte[] fromHex(String hex)
    {
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++)
        {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }

    /**
     * Converts a byte array into a hexadecimal string.
     *
     * @param   array       the byte array to convert
     * @return              a length*2 character string encoding the byte array
     */
    private String toHex(byte[] array)
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) 
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

    
    

}

