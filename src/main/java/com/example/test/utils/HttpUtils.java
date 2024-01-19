package com.example.test.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public class HttpUtils {
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDtel2Dl3/7EiliPy8rTDiUelO/uQgFR1QSBucdZ25eaSf9bh73EN7TXrq5n9a3ND6WIaKX0XnCg7e1YqpawpFfxL/jrREId+soIlZidgIJseIDvseRKVVkG0ADZilSLqzyv5P3ZuEHNtZWGRQBy7UzTpFsQLGWfyFg2CR5r+vgNQIDAQAB";
    private static String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAO16XYOXf/sSKWI/LytMOJR6U7+5CAVHVBIG5x1nbl5pJ/1uHvcQ3tNeurmf1rc0PpYhopfRecKDt7ViqlrCkV/Ev+OtEQh36ygiVmJ2Agmx4gO+x5EpVWQbQANmKVIurPK/k/dm4Qc21lYZFAHLtTNOkWxAsZZ/IWDYJHmv6+A1AgMBAAECgYAWIurmok+murmq09L0yUUY6hI2c+wAefanO4d9oS+Tk1/L6uDuDN+IenMTbIfaSf9vhTEfz8H21MpiGRFiJnLgeYGznahyPAn6tCXOViNBEVBoB/UO3Rh33QuBoNsn0Zm/reT6iBy3/QL55dlNFtf1IBvSUgODS0tWBF/hedeSIQJBAPgI2L8Uv2l1a8f/knalKkGflWCe/EPUcg9mIuOWGZi7HyzvHja5hODtAX9pzncceHLe1vL2C98fQ87lCVIxy1kCQQD1GrsAQO5PnSM5rGenzDTxj0VpN8JGCi/2ALyqb6gworKGQRiA2oZT8YF8/kPrPpe9G9+b2ej2TpPkVVyZMEw9AkBNSUxodgmSzdDQ1/UGXT7GhfhgzAllBVypKbUzX0EIkz4KnVO3z6T1BUTTM9/uw4NQn9kwibR3SOGzJTD7WKK5AkAExqGQLrO2D2zJSee3KE57ynviwfhiHv6yP29trsFLOVBbN1d/40Iszo7kXZv7MHwKbkmcItHcQsm08Ejen0dNAkAo20wN/y8b1bkNGaUt3LaglhuhsXjcx8a7+yv8cKe88jnwNqZ2q2JxNZZkR0IMn6tf0Z2XWClC4cgr5I/oCF9s";
    public static Boolean checkSign(String sign, String content) throws Exception{
        byte[] decode = Base64.decode(publicKey);
        X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(decode);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PublicKey publicKey = keyFactory.generatePublic(encodedKeySpec);

        Signature signature = Signature.getInstance("SHA256WithRSA");

        signature.initVerify(publicKey);

        signature.update(content.getBytes(StandardCharsets.UTF_8));
        boolean verify = false;
        try{
           verify = signature.verify(Base64.decode(sign));
        }catch (Exception e){
            e.printStackTrace();
        }

        return verify;

    }

    public static String sign(String string){
        RSA rsa = new RSA(privateKey, null);
        return rsa.encryptBase64(string, KeyType.PrivateKey);
    }

    public static String signHex(String string){
        RSA rsa = new RSA(privateKey, null);
        return rsa.encryptHex(string, KeyType.PrivateKey);
    }

}
