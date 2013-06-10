package architectgroup.fact.access;

import architectgroup.fact.access.license.KeyStatus;

import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 5/2/13
 * Time: 10:29 AM
 */
public class LicenseAccess {
    private static final String PUBLIC_KEY_FILE = "/cc83dedfcdf5dab2f7ca4331608b8319";

    private PublicKey loadPublicKey(InputStream stream) {
        try {
            DataInputStream dis = new DataInputStream(stream);
            byte[] keyBytes = new byte[stream.available()];
            dis.readFully(keyBytes);
            dis.close();

            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("DSA");
            return kf.generatePublic(spec);
        } catch (Exception err) {
            System.out.println("Can not verify the license because of broken public key");
            return null;
        }
    }

    /**
     * @param license
     * @return
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.NoSuchProviderException
     * @throws java.security.InvalidKeyException
     * @throws java.security.SignatureException
     * @throws javax.crypto.NoSuchPaddingException
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public KeyStatus verify(InputStream license) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, NoSuchPaddingException, FileNotFoundException, IOException {
        // now validate that we were the ones who shipped it
        PublicKey publicKey = loadPublicKey(getClass().getResourceAsStream(PUBLIC_KEY_FILE));
        if (publicKey == null) {
            return KeyStatus.KEY_INVALID;
        }

        Signature dsaSignature = Signature.getInstance("SHA1withDSA");
        dsaSignature.initVerify(publicKey);
        byte[] buffer = null;
        byte[] sig = null;
        byte[] sigToVerify = null;
        String tmp = "";
        try {
            ZipInputStream zis = new ZipInputStream(license);
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                if (ze.getName().equalsIgnoreCase("lic.dat")) {
                    int len;
                    buffer = new byte[1024];
                    while ((len = zis.read(buffer)) > 0) {
                        dsaSignature.update(buffer, 0, len);
                    }
                    zis.closeEntry();
                    continue;
                } else if (ze.getName().equalsIgnoreCase("sign")) {
                    int len;
                    buffer = new byte[1024];
                    len = zis.read(buffer);
                    sig = new byte[len];
                    for (int i = 0; i < len; i++) {
                        sig[i] = buffer[i];
                    }
                    zis.closeEntry();
                    continue;
                }
            }
            zis.close();
        } catch (Exception err) {
            System.out.println("Can not verify the license. Please check the key correctly");
        }

        sigToVerify = sig;
        if (sigToVerify != null) {
            if (dsaSignature.verify(sigToVerify)) {
                return KeyStatus.KEY_VALID;
            } else {
                return KeyStatus.KEY_INVALID;
            }
        }
        return KeyStatus.KEY_INVALID;
    }

    public String getData(InputStream is) {
        String tmp = "";
        try {
            ZipInputStream zis = new ZipInputStream(is);
            byte[] buffer = new byte[1024];
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                if (ze.getName().equalsIgnoreCase("lic.dat")) {
                    int numBytes;
                    while ((numBytes = zis.read(buffer, 0, buffer.length)) != -1) {
                        if (numBytes < buffer.length) {
                            String s = new String(buffer, 0, numBytes);
                            tmp += s;
                        } else {
                            String s = new String(buffer);
                            tmp += s;
                        }
                    }
                    break;
                }
            }
            zis.closeEntry();
            zis.close();
        } catch (Exception err) {
            System.out.println("Can not find the license key or the license key is invalid");
        }
        return tmp;
    }
}
