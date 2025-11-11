package com.clientePromo.clientePromo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromocionDTO {
    private Long id;
    private Double porcentajeDescuento;
    private String codigoDescuento;
    private String descripcionDescuento;
    private Date fechaInicioPromocion;
    private Date fechaFinPromocion;
    private Boolean eliminado;
}
