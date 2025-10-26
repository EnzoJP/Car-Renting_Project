package com.servidor.servidor.entities;

import com.servidor.servidor.enums.TipoTelefono;
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
public class ContactoTelefonico extends Contacto {
    private String telefono;
    private TipoTelefono tipoTelefono;
}
