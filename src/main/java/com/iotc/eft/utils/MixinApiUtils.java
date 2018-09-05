package com.iotc.eft.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.iotc.eft.config.MixinConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @description mixin获取token解密加密信息工具
 */


@Slf4j
public class MixinApiUtils {

    /**
     * @description mixin 获取签名token
     * @param method http方法，POST/GET
     * @param url url路径，如/assets
     * @param body 请求内容
     * @param mixinConfig mixin相关配置信息
     * @return 返回签名token
     */
    public static String genUserToken(String method, String url, String body, MixinConfig mixinConfig) {

        //获取私钥
        PrivateKey privateKey = RsaUtils.getPrivateKeyOfPKCS1(mixinConfig.getMixin_privateKey());

        String sig = getUrlSig(method, url, body);
        if(StringUtils.isBlank(sig)){
            return null;
        }

        long ts = System.currentTimeMillis();
        String token =
                JWT
                        .create()
                        .withClaim("uid", mixinConfig.getMixin_uid())
                        .withClaim("sid", mixinConfig.getMixin_sid())
                        .withIssuedAt(new Date(ts))
                        .withExpiresAt(new Date(ts + 12 * 60 * 60 * 1000L))
                        .withClaim("sig", sig)
                        .withClaim("jti", UUID.randomUUID().toString())
                        .sign(Algorithm.RSA512(null, (RSAPrivateKey) privateKey));

        return token;
    }


    //对url进行签名
    private static String getUrlSig(String method, String url, String body) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return Hex.encodeHexString(md.digest((method + url + body).getBytes()));
        } catch (Exception e) {
            log.info("get sign error:{}",e);
            return null;
        }
    }

    /**
     * @description 获取到加密的支付密码
     * @return 返回加密后的pin
     */
    public static String getEncryptedPin(MixinConfig mixinConfig){
        try {
            if (mixinConfig == null ) {
                return null;
            }

            PrivateKey privateKey = RsaUtils.getPrivateKeyOfPKCS1(mixinConfig.getMixin_privateKey());

            String  key = decrypt(privateKey, mixinConfig.getMixin_pinToken(), mixinConfig.getMixin_sid());
            if(StringUtils.isBlank(key)){
                return null;
            }

            long itrator = System.currentTimeMillis();

            return aesEncrypt(key,mixinConfig.getMixin_pin(),itrator);

        }catch (Exception e){
            log.error("get encrypted pin error:{}",e);
        }
        return null;
    }


    //第一步方法，私钥PivateKey,pin_token,sessionId
    private static String decrypt(Key key, String pin_token, String sessionId) {
        try {
            Cipher c = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            c.init(Cipher.DECRYPT_MODE, key, new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256,
                    new PSource.PSpecified(sessionId.getBytes())));
            byte[] decodedBytes;
            byte[] in = Base64.getDecoder().decode(pin_token);
            decodedBytes = c.doFinal(in);
            return Base64.getEncoder().encodeToString(decodedBytes);
        } catch (Exception e) {
            log.error("descrypt pin error:{}",e);
        }
        return null;
    }

    //第二步，从pin_token中解析出来的K,pin,itrator
    private static String aesEncrypt(String key, String pin, Long itrator) {
        if (StringUtils.isBlank(key)) {
            return null;
        }

        SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
        byte[] iv = new byte[16];
        new Random().nextBytes(iv);

        byte[] pinByte = pin.getBytes();
        byte[] timeByte = longToByte(System.currentTimeMillis() / 1000);
        byte[] itratorByte = longToByte(itrator);

        int length = pinByte.length + timeByte.length + itratorByte.length;
        byte[] totalByte = new byte[length];
        copyOfRange(pinByte, totalByte, 0);
        copyOfRange(timeByte, totalByte, pinByte.length);
        copyOfRange(itratorByte, totalByte, pinByte.length + timeByte.length);

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
            byte[] result = cipher.doFinal(totalByte);

            byte[] newResult = new byte[iv.length + result.length];

            copyOfRange(iv, newResult, 0);
            copyOfRange(result, newResult, iv.length);
            String sss = Base64.getEncoder().encodeToString(newResult);
            return sss;
        } catch (Exception e) {
            log.error("aesEncrypt pin error:{}",e);
        }
        return null;
    }


    private static byte[] longToByte(long number) {
        long temp = number;
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    private static void copyOfRange(byte[] from, byte[] to, int index) {
        for (int i = 0; i < from.length; i++) {
            to[index + i] = from[i];
        }
    }

}
