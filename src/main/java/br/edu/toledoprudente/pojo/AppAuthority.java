package br.edu.toledoprudente.pojo;

import java.io.Serializable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "authorities")
public class AppAuthority extends AbstractEntity<Integer> implements GrantedAuthority, Serializable {
    // ~ Instance fields
    // ================================================================================================
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "authority", nullable = false)
    private String authority;
    // Here comes the buggy attribute. It is supposed to repesent the
    // association username<->username, but I just don't know how to
    // implement it
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "appuser_fk")
    private Usuarios appUser;

    // ~ Constructors
    // ===================================================================================================
    public AppAuthority(String email, String authority) {
        Assert.hasText(authority,
                "A granted authority textual representation is required");
        this.email = email;
        this.authority = authority;
    }

    public AppAuthority() {
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public String getUsername() {
        return email;
    }

    public void setUsername(String email) {
        this.email = email;
    }

    public Usuarios getAppUser() {
        return appUser;
    }

    public void setAppUser(Usuarios appUser) {
        this.appUser = appUser;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
