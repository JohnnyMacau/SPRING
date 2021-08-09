package com.macauslot.recruitment_ms.service.impl;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.macauslot.recruitment_ms.service.ITokenService;
import org.springframework.stereotype.Service;




@Service
public class TokenServiceImpl implements ITokenService {
    @Override
    public String getToken(String token) {
        return JWT.create().sign(Algorithm.HMAC256(token));
    }
}
