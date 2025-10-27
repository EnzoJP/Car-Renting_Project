package com.clienteAdmin.clienteAdmin.converters;

import com.clienteAdmin.clienteAdmin.DTO.CaracteristicaVehiculoDTO;
import com.clienteAdmin.clienteAdmin.services.CaracteristicaVehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCaracteristicaVehiculoDTOConverter implements Converter<String, CaracteristicaVehiculoDTO> {
    @Autowired
    private CaracteristicaVehiculoService caracteristicaVehiculoService;

    @Override
    public CaracteristicaVehiculoDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return caracteristicaVehiculoService.obtener(Long.parseLong(id));
    }
}
