package com.so.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

public class JWTHelper {
	/*public static void main(String[] args) throws Exception {
		Claims claims = parseJWT("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyZWFsTmFtZSI6IuW-kOmtj-mtjyIsInVzZXJfbmFtZSI6IjE3NzY4MDg5ODcwIiwic2NvcGUiOlsiYXBwIl0sInVzZXJObyI6IjUwRW9sSWRVUUljODVBNGNoVnhaaE4iLCJleHAiOjE1NTI3OTE1MzAsInVzZXJOYW1lIjoiMTc3NjgwODk4NzAiLCJ1c2VySWQiOiIzMCIsImF1dGhvcml0aWVzIjpbIlJPTEVfQU5PTllNT1VTIl0sImp0aSI6ImMwYmJmN2Y1LTcwZWItNGNiNS04NmE2LTEwODAyMjExYjM4OCIsImNsaWVudF9pZCI6InRlc3RfYyJ9.JaTxufAh2vWlu7ZZCXTHMV5yl33RkwMYTAqVybTjaoA", "123456");
		String json = new Gson().toJson(claims);
		System.out.println(json);
	}*/
	/**
     * 由字符串生成加密key
     *
     * @return
	 * @throws NoSuchAlgorithmException 
     */
    public static SecretKey generalKey(String stringKey) throws NoSuchAlgorithmException {
        SecretKeySpec secret_key = new SecretKeySpec(stringKey.getBytes(), "HmacSHA256");
        return secret_key;
    }
  
	   /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt, String stringKey) throws Exception {
        SecretKey key = generalKey(stringKey);  //签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(key)                 //设置签名的秘钥
                .parseClaimsJws(jwt).getBody();     //设置需要解析的jwt
        return claims;
    }
}
