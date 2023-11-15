package org.goldenalf.privatepr.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class CustomSecurityFilter extends OncePerRequestFilter {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
  private final AuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler("/auth/success");
  private final AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler("/auth/failed");

  private RequestMatcher processAuthenticationRequestMatcher;
  private AuthenticationManager authenticationManager;
  private SecurityContextRepository securityContextRepository;
  private MessageSource messageSource;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    if (notRequiredAuthenticationRequestMatcher(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      Authentication authentication = obtainBody(request);
      authentication = authenticationManager.authenticate(authentication);
      successfulAuthentication(request, response, filterChain, authentication);
    } catch (AuthenticationException exception) {
      unsuccessfulAuthentication(request, response, exception);
    }
  }

  private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) throws ServletException, IOException {
    SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
    context.setAuthentication(authentication);
    this.securityContextHolderStrategy.setContext(context);
    this.securityContextRepository.saveContext(context, request, response);
    this.successHandler.onAuthenticationSuccess(request, response, authentication);
  }

  private void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws ServletException, IOException {
    SecurityContext context = this.securityContextHolderStrategy.getContext();
    this.securityContextHolderStrategy.clearContext();
    context.setAuthentication(null);
    this.failureHandler.onAuthenticationFailure(request, response, exception);
  }

  protected Authentication obtainBody(HttpServletRequest request) {
    try {
      Map<String, String> map = this.objectMapper.readValue(request.getInputStream(), Map.class);
      String username = map.get("login");
      String password = map.get("password");
      if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
        throw new BadCredentialsException(messageSource
                .getMessage("validation.hotelBook.security.login-password.not-found", null, Locale.getDefault()));
      }

      return UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    } catch (IOException e) {
      throw new AuthenticationServiceException(e.getMessage(), e);
    }
  }

  private boolean notRequiredAuthenticationRequestMatcher(HttpServletRequest request) {
    return !this.processAuthenticationRequestMatcher.matches(request);
  }

  public void setProcessAuthenticationRequestMatcher(RequestMatcher processAuthenticationRequestMatcher) {
    this.processAuthenticationRequestMatcher = processAuthenticationRequestMatcher;
  }

  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  public void setSecurityContextRepository(SecurityContextRepository securityContextRepository) {
    this.securityContextRepository = securityContextRepository;
  }

  public void setMessageSource(@Autowired MessageSource messageSource) {
    this.messageSource = messageSource;
  }
}
