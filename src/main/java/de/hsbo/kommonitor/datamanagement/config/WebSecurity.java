package de.hsbo.kommonitor.datamanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import de.hsbo.kommonitor.datamanagement.api.impl.BasePathController;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, BasePathController.BASE_PATH_KOMMONITOR_API_V1 + "*").permitAll()
                .antMatchers("/", "/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui", "/swagger-resources/configuration/security", "/actuator", "/actuator/health").permitAll()
                .antMatchers(BasePathController.BASE_PATH_KOMMONITOR_API_V1 + "/api-docs", BasePathController.BASE_PATH_KOMMONITOR_API_V1 + "/configuration/ui", BasePathController.BASE_PATH_KOMMONITOR_API_V1 + "/swagger-resources", BasePathController.BASE_PATH_KOMMONITOR_API_V1 + "/configuration/security", BasePathController.BASE_PATH_KOMMONITOR_API_V1 + "/swagger-ui.html", BasePathController.BASE_PATH_KOMMONITOR_API_V1 + "/webjars/**", BasePathController.BASE_PATH_KOMMONITOR_API_V1 + "/swagger-resources/configuration/ui", BasePathController.BASE_PATH_KOMMONITOR_API_V1 + "/swagger-resources/configuration/security", "/actuator", "/actuator/health").permitAll()
//                .anyRequest().authenticated()
                .and()
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
    return source;
  }
}