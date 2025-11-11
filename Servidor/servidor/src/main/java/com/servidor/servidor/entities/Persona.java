package com.servidor.servidor.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.servidor.servidor.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
public class Persona extends BaseEntity<Long> {
    private String nombre;
    private String apellido;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Contacto> contactos = new ArrayList<>();

    @ManyToOne
    private Direccion direccion;

    @ManyToOne(cascade = CascadeType.ALL)
    private Imagen imagen;

    @OneToMany
    private java.util.List<Usuario> usuarios;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Boolean getEliminado() {
        return this.eliminado;
    }

    @Override
    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
}
