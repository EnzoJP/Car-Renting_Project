package com.servidor.servidor.entities;

import com.servidor.servidor.enums.RolUsuario;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario extends Persona {
    private String nombreUsuario;
    private String clave;
    private RolUsuario rol;
}
