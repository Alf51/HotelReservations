package org.goldenalf.privatepr.config.security;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class CustomConfigurer<B extends HttpSecurityBuilder<B>, T extends CustomConfigurer<B, T>> extends AbstractHttpConfigurer<T, B> {
  private CustomSecurityFilter securityFilter;

  @Override
  public void init(B builder) throws Exception {
    super.init(builder);
    this.securityFilter = new CustomSecurityFilter();
  }

  @Override
  public void configure(B builder) throws Exception {
    super.configure(builder);

    ApplicationContext applicationContext = builder.getSharedObject(ApplicationContext.class);
    CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider();
    authenticationProvider.setPasswordEncoder(applicationContext.getBean(PasswordEncoder.class));
    authenticationProvider.setUserDetailsService(applicationContext.getBean(UserDetailsService.class));

    AuthenticationManagerBuilder authenticationManagerBuilder = builder.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(authenticationProvider);

    this.securityFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
    this.securityFilter.setSecurityContextRepository(builder.getSharedObject(SecurityContextRepository.class));
    this.securityFilter.setProcessAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", HttpMethod.POST.name()));
    builder.addFilterBefore(this.securityFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
