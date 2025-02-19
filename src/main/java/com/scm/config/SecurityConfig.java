package com.scm.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.Services.Impl.SecurityCustomUserDetailsService;


@Configuration
public class SecurityConfig {


    //user create and login using java code with in memory service
    //@Bean
    // @Bean
    // public UserDetailsService userDetailsService(){
      
          
    //   UserDetails userDetails = User
    //  .withDefaultPasswordEncoder()
    //   .username("admin123")
    //   .password("admin123")
    //   .roles("ADMIN","USER")
    //  .build();
  





    //    var inMemoryUserDetailsManager =  new InMemoryUserDetailsManager();
    //     return  inMemoryUserDetailsManager;
    // }
     
    @Autowired
    private OAuthAuthenicationSuccessHandler handler;
    @Autowired
  private SecurityCustomUserDetailsService userDetailsService;

//Configuration
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
  
  //Configuration 

  // urls configuration public or protect 
  httpSecurity.authorizeHttpRequests(authorize->{
  //authorize.requestMatchers("/home","/register", "/services").permitAll();
authorize.requestMatchers("/user/**").authenticated();
authorize.anyRequest().permitAll();
  } );
//form default login 
  httpSecurity.formLogin(formLogin->{
 
  formLogin.loginPage("/login");
  formLogin.loginProcessingUrl("/authenticate");
  formLogin.successForwardUrl("/user/dashboard");
  //formLogin.failureForwardUrl("/login?error=true");
  formLogin.usernameParameter("email");
  formLogin.passwordParameter("password");
 
  /*
  formLogin.failureHandler(new AuthenticationFailureHandler() {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'");
    }
    
  });
formLogin.successHandler(new AuthenticationSuccessHandler() {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'");
  }
  
});

 */



  });

  httpSecurity.csrf(AbstractHttpConfigurer::disable);
  httpSecurity.logout(logoutForm->{
 logoutForm.logoutUrl("/do-logout");
  logoutForm.logoutSuccessUrl("/login?logout=true");

  });


  //Oauth2 Configuration google and github
  httpSecurity.oauth2Login(oauth2->{
    oauth2.loginPage("/login");
    oauth2.successHandler(handler);

  });

  

  return httpSecurity.build();

}



// User Authentication

    @Bean
public DaoAuthenticationProvider authenticationProvider(){

    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
  
  //user details service subject
    daoAuthenticationProvider.setUserDetailsService(userDetailsService);
  
  //password Encoder
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return daoAuthenticationProvider;
}



@Bean
public PasswordEncoder passwordEncoder(){
    return  new BCryptPasswordEncoder();
}

}
