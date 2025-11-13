package com.clientePromo.clientePromo.controllers;

import com.clientePromo.clientePromo.DTO.AlquilerDTO;
import com.clientePromo.clientePromo.DTO.PromocionDTO;
import com.clientePromo.clientePromo.DTO.EstadoAlquiler;
import com.clientePromo.clientePromo.services.AlquilerService;
import com.clientePromo.clientePromo.services.FacturaReportService;
import com.clientePromo.clientePromo.services.PagoService;
import com.clientePromo.clientePromo.services.PromocioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PagoController {

    private final PagoService testMP;
    private final PromocioService promService;
    private final AlquilerService alquilerService;
    private final FacturaReportService facturaReportService;

    public PagoController(PagoService testMP, PromocioService promService, AlquilerService alquilerService, FacturaReportService facturaReportService) {
        this.testMP = testMP;
        this.promService = promService;
        this.alquilerService = alquilerService;
        this.facturaReportService = facturaReportService;
    }

    @GetMapping("/pago-descuento")
    public String pagoDescuento(@RequestParam(required = false) String codigo,
                                @RequestParam(required = false) String total,
                                @RequestParam(required = false) Long alquilerId,
                                @RequestParam(required = false) String alquilerIds) {
        try {
            System.out.println("/pago-descuento invoked with codigo='" + codigo + "' total(raw)='" + total + "' alquilerId='" + alquilerId + "' alquilerIds='" + alquilerIds + "'");

            Double totalParsed = parseAmount(total);
            System.out.println("totalParsed=" + totalParsed);

            // si no viene código o está vacío, delegamos al endpoint /pago con el total original
            if (codigo == null || codigo.trim().isEmpty()) {
                // si total inválido -> redirigir a inicio
                if (totalParsed == null || totalParsed.isNaN() || totalParsed.isInfinite() || totalParsed <= 0.0) {
                    System.out.println("Total inválido o vacío al delegar a /pago. Redirigiendo a /inicio. totalParsed='" + totalParsed + "'");
                    return "redirect:/inicio";
                }
                System.out.println("Delegando a /pago con total=" + totalParsed);
                return "redirect:/pago?totalPago=" + totalParsed + (alquilerId != null ? "&alquilerId=" + alquilerId : (alquilerIds != null ? "&alquilerIds=" + alquilerIds : ""));
            }

            // obtener promociones activas
            List<PromocionDTO> promociones = promService.listarActivos();

            // buscar promoción que coincida con el código (ignorando mayúsculas/minúsculas)
            PromocionDTO matched = promociones.stream()
                    .filter(p -> p.getCodigoDescuento() != null && p.getCodigoDescuento().equalsIgnoreCase(codigo) && (p.getEliminado() == null || !p.getEliminado()))
                    .findFirst()
                    .orElse(null);

            Double precioOriginal = totalParsed != null ? totalParsed : 0.0;
            if (precioOriginal <= 0.0) {
                System.out.println("Precio original <= 0 (" + precioOriginal + "). Redirigiendo a /inicio");
                return "redirect:/inicio";
            }

            Double precioFinal = precioOriginal;

            if (matched != null && matched.getPorcentajeDescuento() != null) {
                precioFinal = precioOriginal - (precioOriginal * (matched.getPorcentajeDescuento() / 100.0));
                System.out.println("Promoción aplicada: codigo='" + matched.getCodigoDescuento() + "' porcentaje=" + matched.getPorcentajeDescuento() + " -> precioFinal=" + precioFinal);
            } else {
                System.out.println("No se encontró promoción para el código '" + codigo + "'. Precio final=" + precioFinal);
            }

            // si vienen alquilerIds, marcamos cada uno como PAGADO antes de intentar el pago
            if (alquilerIds != null && !alquilerIds.trim().isEmpty()) {
                String[] parts = alquilerIds.split(",");
                for (String p : parts) {
                    try {
                        long id = Long.parseLong(p.trim());
                        marcarAlquilerPagado(id);
                    } catch (Exception e) {
                        System.out.println("Id de alquiler inválido en alquilerIds: '" + p + "' -> " + e.getMessage());
                    }
                }
            }

            // si viene alquilerId individual, marcarlo también
            if (alquilerId != null) {
                marcarAlquilerPagado(alquilerId);
            }

            String initPoint = testMP.Pago(precioFinal);
            System.out.println("Redirigiendo a MercadoPago initPoint=" + initPoint);
            return "redirect:" + initPoint;

        } catch (Exception e) {
            System.out.println("Error en el pago con descuento: " + e.getMessage());
            return "redirect:/inicio";
        }
    }


    @GetMapping("/pago")
    public String pagar(@RequestParam(required = false) String totalPago,
                        @RequestParam(required = false) Long alquilerId,
                        @RequestParam(required = false) String alquilerIds) {
        try {
            System.out.println("/pago invoked with totalPago(raw)='" + totalPago + "' alquilerId='" + alquilerId + "' alquilerIds='" + alquilerIds + "'");
            Double totalParsed = parseAmount(totalPago);
            System.out.println("totalParsed=" + totalParsed);
            if (totalParsed == null || totalParsed.isNaN() || totalParsed.isInfinite() || totalParsed <= 0.0) {
                System.out.println("Total inválido en /pago: '" + totalParsed + "' -> redirigiendo a /inicio");
                return "redirect:/inicio";
            }

            // marcar multiples alquileres si vienen
            if (alquilerIds != null && !alquilerIds.trim().isEmpty()) {
                String[] parts = alquilerIds.split(",");
                for (String p : parts) {
                    try {
                        long id = Long.parseLong(p.trim());
                        marcarAlquilerPagado(id);
                    } catch (Exception e) {
                        System.out.println("Id de alquiler inválido en alquilerIds: '" + p + "' -> " + e.getMessage());
                    }
                }
            }

            // marcar alquiler individual si viene
            if (alquilerId != null) {
                marcarAlquilerPagado(alquilerId);
            }

            Double monto = totalParsed;
            String initPoint = testMP.Pago(monto);
            System.out.println("Redirigiendo a MercadoPago initPoint=" + initPoint);
            return "redirect:" + initPoint;

        } catch (Exception e) {
            System.out.println("Error en el pago: " + e.getMessage());
            return "redirect:/inicio";
        }
    }

    private void marcarAlquilerPagado(Long id) {
        try {
            AlquilerDTO a = alquilerService.obtener(id);
            if (a != null) {
                a.setEstadoAlquiler(EstadoAlquiler.PAGADO);
                alquilerService.modificar(id, a);
                System.out.println("Alquiler " + id + " marcado como PAGADO.");
            }
        } catch (Exception e) {
            System.out.println("No se pudo marcar alquiler " + id + " como PAGADO: " + e.getMessage());
        }
    }

    private Double parseAmount(String s) {
        if (s == null) return null;
        String trimmed = s.trim();
        if (trimmed.isEmpty()) return null;

        String only = trimmed;
        if (only.matches(".*[.,].*")) {
            int lastDot = only.lastIndexOf('.');
            int lastComma = only.lastIndexOf(',');
            if (lastComma > lastDot) {

                only = only.replace(".", ""); // remove thousands dots
                only = only.replace(',', '.');
            } else if (lastDot > lastComma) {

                only = only.replace(",", "");
            } else {

                if (only.indexOf(',') >= 0 && only.indexOf('.') < 0) {
                    only = only.replace(',', '.');
                } else {
                    only = only.replace(",", "");
                }
            }
        }

        only = only.replaceAll("[^0-9.\\-]", "");
        if (only.isEmpty() || only.equals(".") || only.equals("-")) return null;
        try {
            return Double.parseDouble(only);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @GetMapping("/pago-success")
    public String pagoExitoso(@RequestParam(required = false) Long alquilerId, @RequestParam(required = false) String alquilerIds, Model model) {
        try {
            System.out.println("=== PAGO EXITOSO ===");
            System.out.println("alquilerId: " + alquilerId);
            System.out.println("alquilerIds: " + alquilerIds);
            // Si es un solo alquiler
            if (alquilerId != null) {
                // Generar factura automáticamente
                byte[] factura = facturaReportService.descargarFacturaPorAlquiler(alquilerId);
                model.addAttribute("facturaGenerada", factura != null);
                model.addAttribute("alquilerId", alquilerId);
            }
            // Si son múltiples alquileres
            if (alquilerIds != null && !alquilerIds.isEmpty()) {
                List<Long> ids = Arrays.stream(alquilerIds.split(","))
                        .map(String::trim)
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

                byte[] factura = facturaReportService.descargarFacturaPorAlquileres(ids, null);
                model.addAttribute("facturaGenerada", factura != null);
                model.addAttribute("alquilerIds", alquilerIds);
            }
            return "view/pago-exitoso";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/cliente/dashboard?error=factura";
        }
    }

}
