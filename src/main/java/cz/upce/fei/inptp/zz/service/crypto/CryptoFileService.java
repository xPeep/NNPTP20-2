/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.upce.fei.inptp.zz.service.crypto;

import cz.upce.fei.inptp.zz.entity.PasswordDatabase;
import cz.upce.fei.inptp.zz.service.json.JSONService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

/**
 * Service for creating encrypted files.
 *
 */
public class CryptoFileService implements CryptoService {

    private JSONService jsonService;

    @Inject
    public CryptoFileService(JSONService jsonService) {
        this.jsonService = jsonService;
    }

    @Override
    public String readFile(File file, String password) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            // TODO...
            Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");
            CipherInputStream cis = new CipherInputStream(fis, c);
            SecretKey secretKey = new SecretKeySpec(password.getBytes(), "DES");
            c.init(Cipher.DECRYPT_MODE, secretKey);
            
            DataInputStream dis = new DataInputStream(cis);
            String r = dis.readUTF();
            dis.close();
            c.doFinal();
            
            return r;        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CryptoFileService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoFileService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(CryptoFileService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(CryptoFileService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CryptoFileService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(CryptoFileService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(CryptoFileService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(CryptoFileService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return null;
    }
    
    @Override
    public void writeFile(File file, String password, String cnt) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");
            CipherOutputStream cis = new CipherOutputStream(fos, c);
            SecretKey secretKey = new SecretKeySpec(password.getBytes(), "DES");
            c.init(Cipher.ENCRYPT_MODE, secretKey);
            
            DataOutputStream dos = new DataOutputStream(cis);
            dos.writeUTF(cnt);
            dos.close();
            c.doFinal();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CryptoFileService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoFileService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(CryptoFileService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(CryptoFileService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CryptoFileService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(CryptoFileService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(CryptoFileService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(CryptoFileService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void writeFile(PasswordDatabase passwordDatabase) {
        writeFile(passwordDatabase.getFile(), passwordDatabase.getPasswd(), jsonService.toJson(passwordDatabase.getPasswords()));
    }
}
