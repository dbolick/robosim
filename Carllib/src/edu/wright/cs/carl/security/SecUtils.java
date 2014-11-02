
package edu.wright.cs.carl.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * This class is a utility class for obtaining cryptographic hashes of
 * plaintext.  It's used to hash plaintext passwords for storage in password
 * files, and for network transmission for remote login.
 *
 * @author Duane
 */

public class SecUtils
{
  
  private SecUtils(String hashAlgorithm)
  {
    
  }

  /**
   * Get a hash digest of plaintext.
   * 
   * @param plaintext Supplies the original plaintext.
   * @param hashAlgorithm Supplies the type of hash algorithm used to generate
   *                      the hash.
   * @return  The results of applying the hash function to the original
   *          plaintext.
   * @throws java.security.NoSuchAlgorithmException
   * @throws java.io.UnsupportedEncodingException
   */
  public static synchronized String getHash (String plaintext, String hashAlgorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException
  {
    MessageDigest digest = MessageDigest.getInstance(hashAlgorithm);
    digest.update(plaintext.getBytes("UTF-8"));
    String hash = new BigInteger(1,digest.digest()).toString(16);
    return hash;
  }
}
