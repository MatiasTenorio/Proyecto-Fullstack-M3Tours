package com.M3Tours.reserva.Controller;

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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// Importaciones del dominio
import com.M3Tours.reserva.DTO.ReservaDTO;
import com.M3Tours.reserva.DTO.TourDTO;
import com.M3Tours.reserva.Model.Reserva;
import com.M3Tours.reserva.Service.ReservaService;

/**
 * Pruebas unitarias de la capa Web (Slice Test) para UsuarioController.
 * Utiliza anotaciones de Spring Boot 3.4+ y ventajas sintácticas de JDK 21.
 */
// Inicializa el entorno web de Spring de forma aislada
@WebMvcTest(ReservaController.class)
// Desactiva los filtros de seguridad (JWT) para no recibir error 401/403 en las pruebas
@AutoConfigureMockMvc(addFilters = false) 
public class ReservaControllerTest {

    // Inyecta la herramienta MockMvc para peticiones HTTP simuladas
    @Autowired
    private MockMvc mockMvc;

    // Crea un simulacro (mock) del servicio usando la nueva anotación de Spring Boot
    @MockitoBean
    private ReservaService reservaService;

    // ====================================================================
    // GET /api/v1/reservas
    // ====================================================================

    @Test // GET ALL OK
    @DisplayName("GET /api/v1/reservas -> Retorna 200 y una lista con todos las Reservas")
    public void buscarAll_CuandoNoEstaVacio_DeberiaRetornarAll() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var reservaFalsa = new Reserva();
        reservaFalsa.setId(1);
        reservaFalsa.setTourId(1);
        reservaFalsa.setNumeroAsiento("1A");

        var reservaFalsa2 = new Reserva();
        reservaFalsa2.setId(1);
        reservaFalsa2.setTourId(1);
        reservaFalsa2.setNumeroAsiento("2B");

        List<Reserva> listaFalsa = List.of(reservaFalsa, reservaFalsa2);

        // Confirgurar Mock
        when(reservaService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/reservas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test // GET ALL EMPTY
    @DisplayName("GET /api/v1/reservas -> Retorna 200 y una lista vacía si no hay registros")
    public void buscarAll_CuandoNoHayDatos_DeberiaRetornarListaVacia() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        List<Reserva> listaFalsa = List.of();

        when(reservaService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/reservas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // ====================================================================
    // GET /api/v1/reservas/{id}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/reservas/{id} -> Retorna 200 y JSON si el ID existe")
    public void buscarPorId_CuandoExiste_DeberiaRetornarReserva() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var reservaFalsa = new Reserva();
        reservaFalsa.setId(1);
        reservaFalsa.setTourId(1);
        reservaFalsa.setNumeroAsiento("1A");

        // Configura el mock
        when(reservaService.findById(anyInt())).thenReturn(Optional.of(reservaFalsa));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/reservas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/reservas/{id} -> Retorna 404 y mensaje si el ID no existe")
    public void buscarPorId_CuandoNoExiste_DeberiaRetornar404YMensaje() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(reservaService.findById(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/reservas/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ====================================================================
    // GET /api/v1/reservas/tour/{id}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/reservas/tour/{id} -> Retorna 200 y JSON si el ID existe")
    public void buscarPorTourId_CuandoExiste_DeberiaRetornarReserva() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var reservaFalsa = new Reserva();
        reservaFalsa.setId(1);
        reservaFalsa.setTourId(1);
        reservaFalsa.setNumeroAsiento("1A");

        // Configura el mock
        when(reservaService.findByTourId(anyInt())).thenReturn(Optional.of(reservaFalsa));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/reservas/tour/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.tourId").value(1));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/reservas/tour/{id} -> Retorna 404 y mensaje si el ID no existe")
    public void buscarPorTourId_CuandoNoExiste_DeberiaRetornar404YMensaje() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(reservaService.findByTourId(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/reservas/tour/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ====================================================================
    // POST /api/v1/reservas/agregar-reserva
    // ====================================================================

    @Test
    @DisplayName("POST /api/v1/reservas/agregar-reserva -> Retorna 201 y mensaje de éxito")
    public void crearReserva_DeberiaRetornarMensajeExito() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
            {
                "tourId": 1,
                "numeroAsiento": "1A"
            }
            """;

        when(reservaService.save(any(ReservaDTO.class))).thenReturn(true);

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/reservas/agregar-reserva")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isOk())                             
                .andExpect(content().string("Reserva guardada con exito!"));
}

    @Test // POST NOT FOUND
    @DisplayName("POST /api/v1/reservas/agregar-reserva -> Retorna 404 si el operador no existe")
    public void crearReserva_CuandoTourNoExiste_DeberiaRetornar404() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
                {
                    "tourId": 99
                }
                """;

        when(reservaService.save(any(ReservaDTO.class))).thenReturn(false);

        // ------------------------------------------------------------
        // 2. ACT & ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(post("/api/v1/reservas/agregar-reserva")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isNotFound())                                    // 404
                .andExpect(content().string("Error: Tour correspondiente no existe."));
    }

    // ====================================================================
    // DELETE /api/v1/reservas/{id}
    // ====================================================================

    @Test // DELETE BY ID OK 
    @DisplayName("DELETE /api/v1/reservas/{id} -> Retorna 200 si se elimina correctamente")
    public void eliminarReserva_CuandoExiste_DeberiaRetornarExito() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(reservaService.delete(1)).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reserva eliminada exitosamente"));        
    }   

    @Test // DELETE ID NOT FOUND
    @DisplayName("DELETE /api/v1/reservas/{id} -> Retorna 404 si la reserva no existe")
    public void eliminarReserva_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(reservaService.delete(99)).thenReturn(false);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/reservas/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Reserva con id '99' no encontrada"));
    }

}