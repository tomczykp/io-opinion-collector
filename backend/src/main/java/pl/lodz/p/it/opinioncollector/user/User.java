package pl.lodz.p.it.opinioncollector.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor
@Data
@Entity
@Table(name = "app_user")
@Access(AccessType.FIELD)
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String visibleName;


    @JsonIgnore
    private String password;


    private boolean locked = false;

    private boolean active = false;

    @Enumerated(EnumType.STRING)
    private UserType role;

    public User(String email, String visibleName, String password) {
        this.email = email;
        this.visibleName = visibleName;
        this.password = password;
        this.role = UserType.USER;
    }

    public User(String email, String visibleName, String password, UserType role) {
        this.email = email;
        this.visibleName = visibleName;
        this.password = password;
        this.role = role;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }


    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return active;
    }
}
