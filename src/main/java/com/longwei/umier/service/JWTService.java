package com.longwei.umier.service;

import com.longwei.umier.vo.WxMpUserInfoVo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Map;

@Service
public class JWTService {

    @Value("${jwt.key}")
    private String keyStr;

    private Key key;

    @PostConstruct
    public void init(){
        key = Keys.hmacShaKeyFor(keyStr.getBytes());
    }

    public String generateJWTToken(WxMpUserInfoVo userInfoVo) {
        return Jwts.builder().addClaims(userInfoVo.toMap()).signWith(key).compact();
    }

    public WxMpUserInfoVo decryptJWTToken(String jws) {
        Map<String, Object> map = Jwts.parser().setSigningKey(key).parseClaimsJws(jws).getBody();
        return WxMpUserInfoVo.parse(map);
    }
}
