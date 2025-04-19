package codigo.ms_auth.config;

import codigo.ms_auth.aggregates.constants.Constants;
import codigo.ms_auth.entity.Rol;
import codigo.ms_auth.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UsuarioService usuarioService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers(Constants.ENDPOINTS_PERMIT_AUTH).permitAll()
                                .requestMatchers(Constants.ENDPOINTS_PERMIT_TEST_SUPERADMIN).hasAnyAuthority(Rol.SUPERADMIN.name())
                                .requestMatchers(Constants.ENDPOINTS_PERMIT_TEST_ADMIN).hasAnyAuthority(Rol.ADMIN.name())
                                .requestMatchers(Constants.ENDPOINTS_PERMIT_USER).hasAnyAuthority(Rol.USUARIO.name())
                                .requestMatchers(Constants.ENDPOINTS_PERMIT_PRODUCTO).hasAnyAuthority(Rol.ADMIN.name(),Rol.SUPERADMIN.name())
                                .requestMatchers(HttpMethod.POST,Constants.ENDPOINTS_PERMIT_ORDENES).hasAnyAuthority(Rol.USUARIO.name())
                                .requestMatchers(HttpMethod.GET,Constants.ENDPOINTS_PERMIT_ORDENES).hasAnyAuthority(Rol.ADMIN.name(),Rol.SUPERADMIN.name())
                                .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(usuarioService.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
