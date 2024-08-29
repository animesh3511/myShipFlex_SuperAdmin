package com.example.user_verification.serviceimpl;

import com.example.user_verification.model.Token;
import com.example.user_verification.repository.TokenRepository;
import com.example.user_verification.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private MsfCompanyServiceImpl msfCompanyService;
    @Autowired
    private UserToken userToken;
    @Autowired
    private TokenRepository tokenRepository;


    @Override
    public Object saveToken(Long companyId, String token) {

        Token token1 = new Token();
        token1.setCompanyId(companyId);
        token1.setToken(token);
        tokenRepository.save(token1);
        return "Token Saved";

    }

    @Override
    public Object getTokenByCompanyId(Long companyId) {
        return null;
    }
}
