package br.edu.toledoprudente.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.edu.toledoprudente.dao.UsuariosDAO;
import br.edu.toledoprudente.pojo.Usuarios;

@Component
public class StringToUsuariosConverter implements Converter<String, Usuarios> {

    @Autowired
    private UsuariosDAO dao;

    @Override
    public Usuarios convert(String idTexto) {
        if (idTexto.isEmpty())
            return null;

        return dao.findById(Integer.parseInt(idTexto));
    }
}
