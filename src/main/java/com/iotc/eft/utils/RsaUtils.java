package com.iotc.eft.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateCrtKeySpec;

/**
 * @description RSA公钥私钥工具
 */

@Slf4j
public class RsaUtils {

    /**
     * @description 将base64编码的私钥解码解析
     * @param privateKey 经过base64的私钥
     * @return 返回解码好的私钥
     */
    public static PrivateKey getPrivateKeyOfPKCS1(String privateKey){
        try {
            if (StringUtils.isBlank(privateKey)) {
                return null;
            }

            byte[] encoded = DatatypeConverter.parseBase64Binary(privateKey);
            DerParser parser = new DerParser(encoded);

            Asn1Object sequence = parser.read();
            if (sequence.getType() != DerParser.SEQUENCE) {
                throw new IOException("Invalid DER: not a sequence");
            }

            // Parse inside the sequence
            parser = sequence.getParser();

            parser.read(); // Skip version
            BigInteger modulus = parser.read().getInteger();
            BigInteger publicExp = parser.read().getInteger();
            BigInteger privateExp = parser.read().getInteger();
            BigInteger prime1 = parser.read().getInteger();
            BigInteger prime2 = parser.read().getInteger();
            BigInteger exp1 = parser.read().getInteger();
            BigInteger exp2 = parser.read().getInteger();
            BigInteger crtCoef = parser.read().getInteger();

            RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(
                    modulus, publicExp, privateExp, prime1, prime2,
                    exp1, exp2, crtCoef);

            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(keySpec);

        }catch (Exception e){
            log.error("get private key pkcs1 error:{}",e);
        }
        return null;
    }


}
