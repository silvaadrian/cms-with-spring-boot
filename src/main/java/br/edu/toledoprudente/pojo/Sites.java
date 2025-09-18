package br.edu.toledoprudente.pojo;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "sites")
@Inheritance(strategy = InheritanceType.JOINED)
public class Sites extends AbstractEntity<Integer> {
    private String nome;

    private String url;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuarios;

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    @OneToMany(mappedBy = "sites")
    private List<Paginas> paginas;

    public List<Paginas> getPaginas() {
        return paginas;
    }

    public void setPaginas(List<Paginas> paginas) {
        this.paginas = paginas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}