package br.edu.toledoprudente.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.edu.toledoprudente.pojo.AppAuthority;

@Repository
public class AppAuthorityDAO extends AbstractDao<AppAuthority, Integer> {

    public AppAuthority findByUserName(String email) {
        List<AppAuthority> lista = this.createQuery("select u from AppAuthority u where u.email like ?1", email);
        return lista.isEmpty() ? null : lista.get(0);
    }

}
