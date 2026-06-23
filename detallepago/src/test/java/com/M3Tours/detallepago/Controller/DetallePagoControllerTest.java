package com.M3Tours.detallepago.Controller;

// Importaciones estáticas de Mockito para simular comportamientos
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

// Importaciones estáticas de Spring Test para construir las peticiones HTTP ficticias
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

// Importaciones estáticas de Spring Test para validar las respuestas
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;
// Importación de clases utilitarias de Java
import java.util.Optional;
// Importaciones de JUnit 5
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// Importaciones de Spring Framework
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// Importaciones del dominio
import com.M3Tours.detallepago.DTO.DetallePagoDTO;
import com.M3Tours.detallepago.Model.DetallePago;
import com.M3Tours.detallepago.Service.DetallePagoService;

/**
 * Pruebas unitarias de la capa Web (Slice Test) para UsuarioController.
 * Utiliza anotaciones de Spring Boot 3.4+ y ventajas sintácticas de JDK 21.
 */
// Inicializa el entorno web de Spring de forma aislada
@WebMvcTest(DetallePagoController.class)
// Desactiva los filtros de seguridad (JWT) para no recibir error 401/403 en las pruebas
@AutoConfigureMockMvc(addFilters = false) 
public class DetallePagoControllerTest {

    // Inyecta la herramienta MockMvc para peticiones HTTP simuladas
    @Autowired
    private MockMvc mockMvc;

    // Crea un simulacro (mock) del servicio usando la nueva anotación de Spring Boot
    @MockitoBean
    private DetallePagoService detallePagoService;

    // ====================================================================
    // GET /api/v1/detalle-pagos
    // ====================================================================

    @Test // GET ALL OK
    @DisplayName("GET /api/v1/detalle-pagos -> Retorna 200 y una lista con todos los Detalles de Pagos")
    public void buscarAll_CuandoNoEstaVacio_DeberiaRetornarAll() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var detallePagoFalso = new DetallePago();
        detallePagoFalso.setId(1);

        var detallePagoFalso2 = new DetallePago();
        detallePagoFalso2.setId(2);

        List<DetallePago> listaFalsa = List.of(detallePagoFalso, detallePagoFalso2);

        // Confirgurar Mock
        when(detallePagoService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/detalle-pagos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test // GET ALL EMPTY
    @DisplayName("GET /api/v1/detalle-pagos -> Retorna 200 y una lista vacía si no hay registros")
    public void buscarAll_CuandoNoHayDatos_DeberiaRetornarListaVacia() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        List<DetallePago> listaFalsa = List.of();

        when(detallePagoService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/detalle-pagos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // ====================================================================
    // GET /api/v1/detalle-pagos/{id}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/detalle-pagos/{id} -> Retorna 200 y JSON si el ID existe")
    public void buscarPorId_CuandoExiste_DeberiaRetornarDetallePago() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var detallePagoFalso = new DetallePago();
        detallePagoFalso.setId(1);

        // Configura el mock
        when(detallePagoService.findById(anyInt())).thenReturn(Optional.of(detallePagoFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/detalle-pagos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/detalle-pagos/{id} -> Retorna 404 y mensaje si el ID no existe")
    public void buscarPorId_CuandoNoExiste_DeberiaRetornar404() throws Exception {
       
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(detallePagoService.findById(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/detalle-pagos/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ====================================================================
    // GET /api/v1/detalle-pagos/boleta/{numeroBoleta}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/detalle-pagos/boleta/{bumeroBolñeta} -> Retorna 200 y JSON si el ID existe")
    public void buscarPorNumeroBoleta_CuandoExiste_DeberiaRetornarDetallePago() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var detallePagoFalso = new DetallePago();
        detallePagoFalso.setId(1);
        detallePagoFalso.setNumeroBoleta("9909");

        // Configura el mock
        when(detallePagoService.findByNumeroBoleta(anyString())).thenReturn(Optional.of(detallePagoFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/detalle-pagos/boleta/9909")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.numeroBoleta").value("9909"));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/detalle-pagos/boleta/{numeroBoleta} -> Retorna 404 y mensaje si el Numero de Boleta no existe")
    public void buscarPorNumeroBoleta_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(detallePagoService.findByNumeroBoleta("99")).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/detalle-pagos/boleta/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ====================================================================
    // GET /api/v1/detalle-pagos/tipo-paog/{tipoPago}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/detalle-pagos/tipo-pago/{tipoPago} -> Retorna 200 y JSON si el Tipo de Pago existe")
    public void buscarPorTipoPago_CuandoExiste_DeberiaRetornarDetallePago() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var detallePagoFalso = new DetallePago();
        detallePagoFalso.setId(1);
        detallePagoFalso.setTipoPago("DEBITO");

        // Configura el mock
        when(detallePagoService.findByTipoPago(anyString())).thenReturn(Optional.of(detallePagoFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/detalle-pagos/tipo-pago/DEBITO")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.tipoPago").value("DEBITO"));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/detalle-pagos/tipo-pago/{tipoPago} -> Retorna 404 si el Tipo de Pago no existe")
    public void buscarPorTipoPago_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(detallePagoService.findByTipoPago("CREDITO")).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/detalle-pagos/tipo-pago/CREDITO")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ====================================================================
    // GET /api/v1/detalle-pagos/estado/{estado}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/detalle-pagos/estado/{estado} -> Retorna 200 y JSON si el Estado existe")
    public void buscarPorEstado_CuandoExiste_DeberiaRetornarDetallePago() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var detallePagoFalso = new DetallePago();
        detallePagoFalso.setId(1);
        detallePagoFalso.setEstado("APROBADO");

        // Configura el mock
        when(detallePagoService.findByEstado(anyString())).thenReturn(Optional.of(detallePagoFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/detalle-pagos/estado/APROBADO")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.estado").value("APROBADO"));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/detalle-pagos/estado/{estado} -> Retorna 404 si el Estado no existe")
    public void buscarPorEstado_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(detallePagoService.findByEstado("ANULADO")).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/detalle-pagos/estado/ANULADO")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ====================================================================
    // GET /api/v1/detalle-pagos/tour/{nombreTour}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/detalle-pagos/tour/{nombreTour} -> Retorna 200 y JSON si el Nombre del Tour existe")
    public void buscarPorNombreTour_CuandoExiste_DeberiaRetornarDetallePago() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var detallePagoFalso = new DetallePago();
        detallePagoFalso.setId(1);
        detallePagoFalso.setNombreTour("Tour Estaquilla");

        // Configura el mock
        when(detallePagoService.findByNombreTour(anyString())).thenReturn(Optional.of(detallePagoFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/detalle-pagos/tour/Tour Estaquilla")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombreTour").value("Tour Estaquilla"));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/detalle-pagos/tour/{nombreTour} -> Retorna 404 si el Nombre del Tour no existe")
    public void buscarPorNombreTour_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(detallePagoService.findByNombreTour("Tour Las Quemas")).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/detalle-pagos/tour/Tour Las Quemas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ====================================================================
    // POST /api/v1/detalle-pagos
    // ====================================================================

    @Test // POST OK
    @DisplayName("POST /api/v1/detalle-pagos -> Retorna 201 y mensaje de éxito")
    public void crearDetallePago_DeberiaRetornarMensajeExito() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
            {
                "pagoId": 1,
                "numeroBoleta": "B-000123",
                "tipoPago": "Tarjeta Credito",
                "estado": "Pagado",
                "nombreTour": "TourPatagonia",
                "numeroAsiento": "A12",
                "subtotal": 126050.00,
                "impuesto": 23950.00,
                "total": 150000.00
            }
            """;

        when(detallePagoService.save(any(DetallePagoDTO.class))).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & ASSERT
        // ------------------------------------------------------------
        
        mockMvc.perform(post("/api/v1/detalle-pagos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isOk())                             
                .andExpect(content().string("Detalle de pago guardado con exito!"));
}

    @Test // POST NOT FOUND
    @DisplayName("POST /api/v1/detalle-pagos -> Retorna 404 si el operador no existe")
    public void crearDetallePago_CuandoPagoNoExiste_DeberiaRetornar404() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
                {
                    "pagoId": 99
                }
                """;

        when(detallePagoService.save(any(DetallePagoDTO.class))).thenReturn(false);

        // ------------------------------------------------------------
        // 2. ACT & ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(post("/api/v1/detalle-pagos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isNotFound())                                    // 404
                .andExpect(content().string("Error: Pago correspondiente no existe."));
    }

    // ====================================================================
    // DELETE /api/v1/reservas/{id}
    // ====================================================================

    @Test // DELETE BY ID OK 
    @DisplayName("DELETE /api/v1/detalle-pagos/{id} -> Retorna 200 si se elimina correctamente")
    public void eliminarDetallePago_CuandoExiste_DeberiaRetornarExito() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(detallePagoService.delete(1)).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/detalle-pagos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Detalle de pago eliminado exitosamente"));        
    }   

    @Test // DELETE ID NOT FOUND
    @DisplayName("DELETE /api/v1/detalle-pagos/{id} -> Retorna 404 si el Detalle de Pago no existe")
    public void eliminarDetallePago_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(detallePagoService.delete(99)).thenReturn(false);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/detalle-pagos/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Detalle de pago con id '99' no encontrado"));
    }     
}
