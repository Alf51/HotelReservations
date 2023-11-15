package org.goldenalf.privatepr.config.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomAuthenticationProvider implements AuthenticationProvider {
  private PasswordEncoder passwordEncoder;
  private UserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();

    UserDetails userDetails;
    try {
      userDetails = this.userDetailsService.loadUserByUsername(username);
    } catch (UsernameNotFoundException exception) {
      throw new BadCredentialsException("{validation.hotelBook.security.bad-credential}");
    }

    if (!this.passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
      throw new BadCredentialsException("{validation.hotelBook.security.bad-credential}");
    }

    UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken.authenticated(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
    result.setDetails(authentication.getDetails());
    return result;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }

  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  public void setUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }
}
