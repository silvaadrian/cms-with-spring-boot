package br.edu.toledoprudente.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.edu.toledoprudente.pojo.Usuarios;

@Repository
public class UsuariosDAO extends AbstractDao<Usuarios, Integer> implements UserDetailsService {


    public Usuarios findByUserName(String email) {
        List<Usuarios> lista = this.createQuery("select u from Usuarios u where u.email like ?1", email);
        return lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuarios user = findByUserName(email);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getAppAuthorities().forEach(auth -> authorities.add(new SimpleGrantedAuthority(auth.getAuthority())));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getSenha(),
                authorities);
    }

}
