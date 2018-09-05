package com.iotc.eft.utils;


import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

@Slf4j
public class AesUtils {

    /**
     * @description 采用AES加密
     * @param password 加密的密钥
     * @param content 需要加密的内容
     * @return 返回加密后的base64串
     */
    public static String encode(String password,String content){
        try {
            KeyGenerator keygen=KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());
            keygen.init(128, random);
            SecretKey original_key=keygen.generateKey();
            byte [] raw=original_key.getEncoded();
            SecretKey key=new SecretKeySpec(raw, "AES");
            Cipher cipher=Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte [] byte_encode=content.getBytes("utf-8");
            byte [] byte_AES=cipher.doFinal(byte_encode);
            String AES_encode=new String(new BASE64Encoder().encode(byte_AES));
            return AES_encode;
        } catch (Exception e) {
            log.error("AES encode error:{}",e);
        }
        return null;
    }

    /**
     * @description 采用AES解密
     * @param password 解密的密码
     * @param content 已经加密的内容
     * @return 返回解密后的内容
     */
    public static String decode(String password,String content){
        try {
            KeyGenerator keygen=KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());
            keygen.init(128, random);
            SecretKey original_key=keygen.generateKey();
            byte [] raw=original_key.getEncoded();
            SecretKey key=new SecretKeySpec(raw, "AES");
            Cipher cipher=Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte [] byte_content= new BASE64Decoder().decodeBuffer(content);
            byte [] byte_decode=cipher.doFinal(byte_content);
            String AES_decode=new String(byte_decode,"utf-8");
            return AES_decode;
        } catch (Exception e) {
            log.error("AES decode error:{}",e);
        }
        return null;
    }

}

