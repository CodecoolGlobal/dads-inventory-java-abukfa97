package com.codecool.dadsinventory.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = "";
        Cookie[] cookies = request.getCookies();
        // Get token cookie if exist
        if (cookies != null){
            for (Cookie cookie: cookies){
                if (cookie.getName().equals("token")){
                    token = cookie.getValue();
                }
            }
        }

        if (request.getServletPath().equals("/login")){
            filterChain.doFilter(request,response);
        } else {
            if (!token.equals("")){
                try {
                    Algorithm algorithm = Algorithm.HMAC256("D4d5|nv3nt0ry");
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("role").asArray(String.class);
                    List<SimpleGrantedAuthority> authorities = stream(roles)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,null,authorities);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    filterChain.doFilter(request,response);
                } catch (Exception e){
                    log.error("*****ERROR***** -> "+e.getMessage());
                    response.setHeader("error",e.getMessage());
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                }
            } else {
              filterChain.doFilter(request,response);
            }
        }
    }
}
