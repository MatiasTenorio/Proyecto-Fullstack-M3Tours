package com.M3Tours.tour.Controller;

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
import com.M3Tours.tour.DTO.TourDTO;
import com.M3Tours.tour.Model.Tour;
import com.M3Tours.tour.Service.TourService;

/**
 * Pruebas unitarias de la capa Web (Slice Test) para UsuarioController.
 * Utiliza anotaciones de Spring Boot 3.4+ y ventajas sintácticas de JDK 21.
 */
// Inicializa el entorno web de Spring de forma aislada
@WebMvcTest(TourController.class)
// Desactiva los filtros de seguridad (JWT) para no recibir error 401/403 en las pruebas
@AutoConfigureMockMvc(addFilters = false) 
public class TourControllerTest {

    // Inyecta la herramienta MockMvc para peticiones HTTP simuladas
    @Autowired
    private MockMvc mockMvc;

    // Crea un simulacro (mock) del servicio usando la nueva anotación de Spring Boot
    @MockitoBean
    private TourService tourService;

    // ====================================================================
    // GET /api/v1/tours
    // ====================================================================

    @Test // GET ALL OK
    @DisplayName("GET /api/v1/tours -> Retorna 200 y una lista con todos los Tours")
    public void buscarAll_CuandoNoEstaVacio_DeberiaRetornarAll() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var tourFalso = new Tour();
        tourFalso.setOperadorId(1);
        tourFalso.setNumeroReservas(1);
        tourFalso.setUbicacionInicial("PuertoMontt");

        var tourFalso2 = new Tour();
        tourFalso2.setOperadorId(1);
        tourFalso2.setNumeroReservas(5);
        tourFalso2.setUbicacionInicial("LosMuermos");

        List<Tour> listaFalsa = List.of(tourFalso, tourFalso2);

        // Confirgurar Mock
        when(tourService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/tours")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test // GET ALL EMPTY
    @DisplayName("GET /api/v1/tours -> Retorna 200 y una lista vacía si no hay registros")
    public void buscarAll_CuandoNoHayDatos_DeberiaRetornarListaVacia() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        List<Tour> listaFalsa = List.of();

        when(tourService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/tours")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // ====================================================================
    // GET /api/v1/tours/{id}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/tours/{id} -> Retorna 200 y JSON si el ID existe")
    public void buscarPorId_CuandoExiste_DeberiaRetornarPago() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var tourFalso = new Tour();
        tourFalso.setId(1);

        // Configura el mock
        when(tourService.findById(anyInt())).thenReturn(Optional.of(tourFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/tours/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/pagos/{id} -> Retorna 404 y mensaje si el ID no existe")
    public void buscarPorId_CuandoNoExiste_DeberiaRetornar404YMensaje() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(tourService.findById(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/tours/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ====================================================================
    // GET /api/v1/tours/operador/{id}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/tours/operador/{id} -> Retorna 200 y JSON si el ID existe")
    public void buscarPorOperadorId_CuandoExiste_DeberiaRetornarReserva() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var tourFalso = new Tour();
        tourFalso.setId(1);
        tourFalso.setOperadorId(1);

        // Configura el mock
        when(tourService.findByOperadorId(anyInt())).thenReturn(Optional.of(tourFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/tours/operador/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.operadorId").value(1));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/tours/operador/{id} -> Retorna 404 y mensaje si el ID no existe")
    public void buscarPorTourId_CuandoNoExiste_DeberiaRetornar404YMensaje() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(tourService.findByOperadorId(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/tours/operador/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ====================================================================
    // GET /api/v1/tours/reserva/{numeroReservas}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/tours/reserva/{numeroReservas} -> Retorna 200 y JSON si el ID existe")
    public void buscarPorReservaId_CuandoExiste_DeberiaRetornarReserva() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var tourFalso = new Tour();
        tourFalso.setId(1);
        tourFalso.setNumeroReservas(10);

        // Configura el mock
        when(tourService.findByNumeroReservas(anyInt())).thenReturn(Optional.of(tourFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/tours/reservas/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.numeroReservas").value(10));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/tours/reservas/{numeroReservas} -> Retorna 404 y mensaje si el Numero de Reservas no existe")
    public void buscarPorNumeroReservas_CuandoNoExiste_DeberiaRetornar404YMensaje() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(tourService.findByNumeroReservas(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/tours/reservas/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ====================================================================
    // GET /api/v1/tours/ubicacion/{id}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/tours/ubicacion/{ubicacionInicial} -> Retorna 200 y JSON si la ubicacion inicial existe")
    public void buscarPorUbicacionInicial_CuandoExiste_DeberiaRetornarUbiacionInicial() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var tourFalso = new Tour();
        tourFalso.setId(1);
        tourFalso.setUbicacionInicial("PuertoMontt");

        // Configura el mock
        when(tourService.findByUbicacionInicial(anyString())).thenReturn(Optional.of(tourFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/tours/ubicacion/PuertoMontt")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.ubicacionInicial").value("PuertoMontt"));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/tours/ubicacion/{ubicacionInicial} -> Retorna 404 y mensaje si el Numero de Reservas no existe")
    public void buscarPorUbicacionInicial_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/tours/ubicacion/LosMuermos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ====================================================================
    // POST /api/v1/tours/agregar-tour
    // ====================================================================

    @Test // POST OK
    @DisplayName("POST /api/v1/tours/agregar-tour -> Retorna 201 y mensaje de éxito")
    public void crearTour_DeberiaRetornarMensajeExito() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
            {
                "operadorId": 1,
                "numeroReservas": 20,
                "ubicacionInicial": "PuertoMontt, Chile",
                "fechaInicial": "2025-07-15"
            }
            """;

        when(tourService.save(any(TourDTO.class))).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & ASSERT
        // ------------------------------------------------------------
        
        mockMvc.perform(post("/api/v1/tours/agregar-tour")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isOk())                             
                .andExpect(content().string("Tour guardado con exito!"));
}

    @Test // POST NOT FOUND
    @DisplayName("POST /api/v1/tours/agregar-tour -> Retorna 404 si el operador no existe")
    public void crearTour_CuandoOperadorNoExiste_DeberiaRetornar404() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
                {
                    "operadorId": 99
                }
                """;

        when(tourService.save(any(TourDTO.class))).thenReturn(false);

        // ------------------------------------------------------------
        // 2. ACT & ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(post("/api/v1/tours/agregar-tour")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isNotFound())                                    // 404
                .andExpect(content().string("Error: Operador correspondiente no existe."));
    }

    // ====================================================================
    // DELETE /api/v1/tours/{id}
    // ====================================================================

    @Test // DELETE BY ID OK 
    @DisplayName("DELETE /api/v1/tours/{id} -> Retorna 200 si se elimina correctamente")
    public void eliminarTour_CuandoExiste_DeberiaRetornarExito() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(tourService.delete(1)).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/tours/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tour eliminado exitosamente"));        
    }   

    @Test // DELETE ID NOT FOUND
    @DisplayName("DELETE /api/v1/reservas/{id} -> Retorna 404 si la reserva no existe")
    public void eliminarReserva_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(tourService.delete(99)).thenReturn(false);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/tours/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tour con id '99' no encontrado"));
    }
}
