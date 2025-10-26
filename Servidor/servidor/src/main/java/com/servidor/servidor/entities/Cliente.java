package com.servidor.servidor.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Cliente extends Persona {
    private String direccionEstadia;

    @ManyToOne
    private Nacionalidad nacionalidad;

}
