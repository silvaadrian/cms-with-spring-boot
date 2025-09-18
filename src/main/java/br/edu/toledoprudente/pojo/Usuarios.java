package br.edu.toledoprudente.pojo;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.InheritanceType;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuarios extends Pessoas implements UserDetails {

	@NotBlank(message = "E-mail é obrigatório")
	@Column(length = 50, nullable = false)
	private String email;

	@NotBlank(message = "Senha é obrigatório")
	@Column(length = 350, nullable = false)
	private String senha;

	@Column(columnDefinition = "DATE", nullable = false)
	private LocalDate data_reg;

	private Boolean admin;

	private Boolean usuario_ativo;

	@OneToMany(mappedBy = "usuarios")
	private List<Sites> sites;

	public List<Sites> getSites() {
		return sites;
	}

	public void setSites(List<Sites> sites) {
		this.sites = sites;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public LocalDate getData_reg() {
		return data_reg;
	}

	public void setData_reg(LocalDate data_reg) {
		this.data_reg = data_reg;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Boolean getUsuario_ativo() {
		return usuario_ativo;
	}

	public void setUsuario_ativo(Boolean usuario_ativo) {
		this.usuario_ativo = usuario_ativo;
	}

	public Set<AppAuthority> getAppAuthorities() {
		return appAuthorities;
	}

	public void setAppAuthorities(Set<AppAuthority> appAuthorities) {
		this.appAuthorities = appAuthorities;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "appUser")
	private Set<AppAuthority> appAuthorities;

	public Usuarios(
			String email,
			String senha,
			boolean admin,
			boolean accountNonExpired,
			boolean credentialsNonExpired,
			boolean accountNonLocked,

			Collection<? extends AppAuthority> authorities) {
		if (((email == null) || "".equals(email)) || (senha == null)) {
			throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
		}
		this.email = email;
		this.senha = senha;
		this.admin = admin;
	}

	public Usuarios() {
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getUsername'");
	}

}
