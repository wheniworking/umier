package com.longwei.umier.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.security.Key;

public class JWTService {

    public String generateJWTToken() {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jws = Jwts.builder().setSubject("Joe").setSubject("abc").signWith(key).compact();
        return jws;
    }

    public static void main(String[] args) {
        String jws = new JWTService().generateJWTToken();
        System.out.println(jws);
    }
}
