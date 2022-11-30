package pl.lodz.p.it.opinioncollector.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.opinioncollector.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthManager {

    private final UserRepository userRepository;

}
