package com.example.user_verification.serviceimpl;

import com.example.user_verification.model.MsfCompany;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

@Getter
@Setter
public class UserDetailsImpl implements UserDetails {

    private final MsfCompany msfCompany;

    public UserDetailsImpl(MsfCompany msfCompany) {
        this.msfCompany = msfCompany;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return msfCompany.getPassword();
    }

    @Override
    public String getUsername() {
        return msfCompany.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserDetailsImpl build(MsfCompany msfCompany) {
        return new UserDetailsImpl(msfCompany);
    }


}
