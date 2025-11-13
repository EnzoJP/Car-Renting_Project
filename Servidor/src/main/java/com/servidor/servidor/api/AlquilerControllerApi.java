package com.servidor.servidor.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servidor.servidor.entities.Alquiler;
import com.servidor.servidor.entities.Documentacion;
import com.servidor.servidor.entities.Vehiculo;
import com.servidor.servidor.enums.TipoDocumentacion;
import com.servidor.servidor.exceptions.ErrorServiceException;
import com.servidor.servidor.services.AlquilerService;
import com.servidor.servidor.services.DocumentacionService;
import com.servidor.servidor.services.FileStorageService;
import com.servidor.servidor.services.VehiculoService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "api/v1/alquileres")
public class AlquilerControllerApi extends BaseControllerApi<Alquiler, Long> {

    private final AlquilerService alquilerService;
    private final FileStorageService fileStorageService;
    private final DocumentacionService documentacionService;
    private final ObjectMapper objectMapper;
    private final VehiculoService vehiculoService;

    public AlquilerControllerApi(AlquilerService service, FileStorageService fileStorageService, DocumentacionService documentacionService, VehiculoService vehiculoService) {
        super(service);
        this.alquilerService = service;
        this.fileStorageService = fileStorageService;
        this.documentacionService = documentacionService;
        this.objectMapper = new ObjectMapper();
        this.vehiculoService = vehiculoService;
    }

    @PostMapping(value = "/save-with-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveWithFile(
            @RequestParam("alquiler") String alquilerJson,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            System.out.println("=== RECIBIENDO ALQUILER CON ARCHIVO ===");
            System.out.println("JSON recibido: " + alquilerJson);
            System.out.println("Archivo: " + (file != null ? file.getOriginalFilename() : "ninguno"));

            // 1. Convertir JSON a Alquiler
            Alquiler alquiler = objectMapper.readValue(alquilerJson, Alquiler.class);

            System.out.println("Alquiler parseado - Cliente ID: " +
                    (alquiler.getCliente() != null ? alquiler.getCliente().getId() : "null"));
            System.out.println("Alquiler parseado - Vehículo ID: " +
                    (alquiler.getVehiculo() != null ? alquiler.getVehiculo().getId() : "null"));

            // 2. Procesar archivo
            if (file != null && !file.isEmpty()) {
                String baseName = "doc_cliente_" + alquiler.getCliente().getId();
                String fileName = fileStorageService.storeFile(file, baseName);

                System.out.println("Archivo guardado con nombre: " + fileName);

                // Crear documentación
                Documentacion doc = new Documentacion();
                doc.setNombreArchivo(fileName);
                doc.setPathArchivo(fileStorageService.getUploadDir());
                doc.setTipoDocumentacion(
                        alquiler.getDocumentacion() != null && alquiler.getDocumentacion().getTipoDocumentacion() != null ?
                                alquiler.getDocumentacion().getTipoDocumentacion() :
                                TipoDocumentacion.CARNET_DE_CONDUCIR
                );
                doc.setObservacion(file.getOriginalFilename());
                doc.setEliminado(false);

                // Guardar documentación primero
                Documentacion savedDoc = documentacionService.alta(doc);
                System.out.println("Documentación guardada con ID: " + savedDoc.getId());

                alquiler.setDocumentacion(savedDoc);

            } else if (alquiler.getId() == null) {
                throw new ErrorServiceException("Debe adjuntar un archivo para un nuevo alquiler.");
            }

            // 3. Guardar alquiler
            Alquiler savedAlquiler;
            if (alquiler.getId() == null) {
                // ALTA
                System.out.println("Guardando nuevo alquiler...");
                savedAlquiler = alquilerService.alta(alquiler);
                System.out.println("Alquiler guardado con ID: " + savedAlquiler.getId());

            } else {
                // MODIFICACIÓN
                System.out.println("Modificando alquiler existente: " + alquiler.getId());
                Alquiler existing = alquilerService.getOne(alquiler.getId());
                existing.setFechaDesde(alquiler.getFechaDesde());
                existing.setFechaHasta(alquiler.getFechaHasta());
                existing.setCliente(alquiler.getCliente());
                existing.setVehiculo(alquiler.getVehiculo());
                existing.setEstadoAlquiler(alquiler.getEstadoAlquiler());
                existing.setCosto(alquiler.getCosto());

                if (alquiler.getDocumentacion() != null) {
                    existing.setDocumentacion(alquiler.getDocumentacion());
                }

                savedAlquiler = alquilerService.modificar(alquiler.getId(), existing)
                        .orElseThrow(() -> new ErrorServiceException("Error al actualizar el alquiler"));
            }

            System.out.println("=== ALQUILER GUARDADO EXITOSAMENTE ===");
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAlquiler);

        } catch (ErrorServiceException e) {
            System.err.println("Error de validación: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            System.err.println("Error inesperado:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al guardar: " + e.getMessage()));
        }
    }

    // --- ¡NUEVO ENDPOINT PARA DESCARGAR EL ARCHIVO! ---
    @GetMapping("/documentacion/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        try {
            // 1. Buscar el Alquiler, luego su Documentacion
            Alquiler alquiler = alquilerService.getOne(id);
            if (alquiler.getDocumentacion() == null || alquiler.getDocumentacion().getNombreArchivo() == null) {
                return ResponseEntity.notFound().build();
            }

            String fileName = alquiler.getDocumentacion().getNombreArchivo();
            Resource resource = fileStorageService.loadFileAsResource(fileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF) // Asumimos PDF, o puedes guardar el MIME
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestBody String nuevoEstado) {
        try {
            String estadoLimpio = nuevoEstado.replaceAll("\"", "");
            Vehiculo vehiculo = vehiculoService.cambiarEstado(id, estadoLimpio);
            return ResponseEntity.ok(vehiculo);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage()));
        }
    }
}
