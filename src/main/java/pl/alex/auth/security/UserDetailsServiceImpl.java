package pl.alex.auth.security;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.alex.auth.user.persistable.JpaUsersDao;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class UserDetailsServiceImpl implements UserDetailsService {

  JpaUsersDao jpaUsersDao;

  @Override
  public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
    val userEntity = jpaUsersDao.findByEmail(login)
        .orElseThrow(() -> new UsernameNotFoundException(login));
    return new User(userEntity.getEmail(), userEntity.getPassword(), List.of());
  }
}
