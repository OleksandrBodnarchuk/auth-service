package pl.alex.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  JwtUtil jwtUtil;
  Environment environment;
  UserDetailsService userDetailsService;
  ObjectMapper mapper;

  public AuthenticationFilter(AuthenticationManager authenticationManager,
      Environment environment,
      UserDetailsService userDetailsService) {
    super(authenticationManager);
    this.mapper = new ObjectMapper();
    this.environment = environment;
    this.jwtUtil = new JwtUtil(environment.getProperty("jwt.secret"));
    this.userDetailsService = userDetailsService;
  }

  @SneakyThrows
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    LoginDetails loginDetails = mapper.readValue(request.getInputStream(), LoginDetails.class);
    return getAuthenticationManager().authenticate(
        new UsernamePasswordAuthenticationToken(loginDetails.login, loginDetails.password,
            List.of()));
  }


  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException {
    val username = ((User) authResult.getPrincipal()).getUsername();
    val userDetails = userDetailsService.loadUserByUsername(username);

    val expiration = environment.getProperty("jwt.expiration");
    if (StringUtils.isBlank(expiration)) {
      throw new RuntimeException("expiration cannot be blank");
    }

    val tokenResponse = JwtUtil.prepareTokenResponse(userDetails.getUsername(),
        Long.valueOf(expiration));
    response.getWriter().write(new ObjectMapper().writeValueAsString(tokenResponse));
    response.getWriter().flush();
  }

  private record LoginDetails(String login, String password) {

  }
}
