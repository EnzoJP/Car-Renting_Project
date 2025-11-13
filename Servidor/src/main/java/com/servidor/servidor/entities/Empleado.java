package com.servidor.servidor.entities;

import com.servidor.servidor.enums.TipoEmpleado;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Empleado extends Persona {
    private TipoEmpleado tipoEmpleado;
}
