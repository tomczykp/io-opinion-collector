package pl.lodz.p.it.opinioncollector.userModule.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "app_user")
@Access(AccessType.FIELD)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String visibleName;

    @JsonIgnore
    @Column
    private String password;

    private boolean deleted = false;

    private boolean locked = false;

    private boolean active = false;

    @Enumerated(EnumType.STRING)
    private UserType role;

    @Enumerated(EnumType.STRING)
    private UserProvider provider;


    public User(String email, String visibleName, String password) {
        this.email = email;
        this.visibleName = visibleName;
        this.password = password;
        this.role = UserType.USER;
        this.provider = UserProvider.LOCAL;
    }

    public User(String email, String visibleName, String password, UserType role) {
        this.email = email;
        this.visibleName = visibleName;
        this.password = password;
        this.role = role;
        this.provider = UserProvider.LOCAL;
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
