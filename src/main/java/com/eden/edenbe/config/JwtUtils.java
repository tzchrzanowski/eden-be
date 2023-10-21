package com.eden.edenbe.config;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.MACSigner;

import java.util.Date;
import java.util.Calendar;

public class JwtUtils {
    // TODO replace with strong key
    private static final String SECRET_KEY = "v$E*j1OaccyWU*Gdc4oC^7O5ZiESKdvmuFbGd!nhqrnB4ukFmw$iW6$!xT5YsNw@";

//    @Bean
//    public static Claims parseToken(String token) {
//        return Jwts.parser()
//                .setSigningKey(SECRET_KEY)
//                .getClass().get
//    }

    public static String generateToken(String username) throws JOSEException {
        Calendar expirationTime = Calendar.getInstance();
        expirationTime.add(Calendar.MINUTE, 720);

        // Create a new JWT Claims Set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .expirationTime(expirationTime.getTime())
                .build();

        // Sign the JWT with the HMAC SHA-256 algorithm
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(new MACSigner(SECRET_KEY));

        // Serialize to a compact format
        return signedJWT.serialize();
    }
}
