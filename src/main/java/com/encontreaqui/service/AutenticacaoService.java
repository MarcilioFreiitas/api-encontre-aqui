package com.encontreaqui.service;

import com.encontreaqui.config.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class AutenticacaoService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // Método auxiliar para extrair o token da requisição atual
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    // Recupera o ID do usuário logado a partir do token JWT
    public Long obterUsuarioLogadoId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String jwt = parseJwt(request);
            if (jwt != null && jwtTokenUtil.validateJwtToken(jwt)) {
                String userIdStr = jwtTokenUtil.getUserIdFromJwtToken(jwt);
                return Long.valueOf(userIdStr);
            }
        }
        throw new RuntimeException("Usuário não autenticado.");
    }
}
