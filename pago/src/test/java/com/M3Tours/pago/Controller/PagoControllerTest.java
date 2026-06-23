package com.M3Tours.pago.Controller;

// Importaciones estáticas de Mockito para simular comportamientos
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

// Importaciones estáticas de Spring Test para construir las peticiones HTTP ficticias
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

// Importaciones estáticas de Spring Test para validar las respuestas
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
import com.M3Tours.pago.DTO.PagoDTO;
import com.M3Tours.pago.DTO.ReservaDTO;
import com.M3Tours.pago.Model.Pago;
import com.M3Tours.pago.Service.PagoService;

/**
 * Pruebas unitarias de la capa Web (Slice Test) para UsuarioController.
 * Utiliza anotaciones de Spring Boot 3.4+ y ventajas sintácticas de JDK 21.
 */
// Inicializa el entorno web de Spring de forma aislada
@WebMvcTest(PagoController.class)
// Desactiva los filtros de seguridad (JWT) para no recibir error 401/403 en las pruebas
@AutoConfigureMockMvc(addFilters = false) 
public class PagoControllerTest {

    // Inyecta la herramienta MockMvc para peticiones HTTP simuladas
    @Autowired
    private MockMvc mockMvc;

    // Crea un simulacro (mock) del servicio usando la nueva anotación de Spring Boot
    @MockitoBean
    private PagoService pagoService;

    // ====================================================================
    // GET /api/v1/empresas
    // ====================================================================

    @Test // GET ALL OK
    @DisplayName("GET /api/v1/pago -> Retorna 200 y una lista con todos los Usuarios")
    public void buscarAll_CuandoNoEstaVacio_DeberiaRetornarAll() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var pagoFalso = new Pago();
        pagoFalso.setId(1);
        pagoFalso.setOrdenCompra(1);
        pagoFalso.setUsuarioId(1);
        pagoFalso.setReservaId(1);
        pagoFalso.setCosto(990.0);

        var pagoFalso2 = new Pago();
        pagoFalso2.setId(2);
        pagoFalso2.setOrdenCompra(2);
        pagoFalso2.setUsuarioId(2);
        pagoFalso2.setReservaId(1);
        pagoFalso2.setCosto(990.0);


        List<Pago> listaFalsa = List.of(pagoFalso, pagoFalso2);

        // Configura el mock
        when(pagoService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/pagos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test // GET ALL EMPTY
    @DisplayName("GET /api/v1/pagos -> Retorna 200 y una lista vacía si no hay registros")
    public void buscarAll_CuandoNoHayDatos_DeberiaRetornarListaVacia() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        List<Pago> listaFalsa = List.of();

        when(pagoService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/pagos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verificamos que el tamaño del Array de respuesta sea exactamente 0
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ====================================================================
    // GET /api/v1/pagos/{id}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/pagos/{id} -> Retorna 200 y JSON si el ID existe")
    public void buscarPorId_CuandoExiste_DeberiaRetornarPago() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var pagoFalso = new Pago();
        pagoFalso.setId(1);
        // pagoFalso.setOrdenCompra(1);
        // pagoFalso.setUsuarioId(1);
        // pagoFalso.setReservaId(1);
        // pagoFalso.setCosto(990.0);

        // Configura el mock
        when(pagoService.findById(anyInt())).thenReturn(Optional.of(pagoFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/pagos/1")
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
        when(pagoService.findById(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/pagos/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pago con id '99' no encontrado")); 
    }

    // ====================================================================
    // GET /api/v1/pagos/usuario/{id}
    // ====================================================================

    @Test // GET USUARIO_ID OK
    @DisplayName("GET /api/v1/pagos/usuario/{id} -> Retorna 200 y JSON si el ID de Usuario existe")
    public void buscarPorUsuarioId_CuandoExiste_DeberiaRetornarPago() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var pagoFalso = new Pago();
        pagoFalso.setUsuarioId(1);

        // Configura el mock
        when(pagoService.findByUserId(anyInt())).thenReturn(Optional.of(pagoFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/pagos/usuario/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.usuarioId").value(1));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/pagos/usuario/{id} -> Retorna 404 y mensaje si el ID de Usuario no existe")
    public void buscarPorUsuarioId_CuandoNoExiste_DeberiaRetornar404YMensaje() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(pagoService.findByUserId(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/pagos/usuario/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pago de usuario con id '99' no encontrado"));
    }

    // ====================================================================
    // GET /api/v1/pagos/reserva/{id}
    // ====================================================================

    @Test // GET RESERVA_ID OK
    @DisplayName("GET /api/v1/pagos/reserva/{id} -> Retorna 200 y JSON si el ID de Reserva existe")
    public void buscarPorReservaId_CuandoExiste_DeberiaRetornarPago() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var pagoFalso = new Pago();
        pagoFalso.setReservaId(1);

        // Configura el mock
        when(pagoService.findByReservaId(anyInt())).thenReturn(Optional.of(pagoFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/pagos/reserva/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.reservaId").value(1));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/pagos/reserva/{id} -> Retorna 404 y mensaje si el ID de Reserva no existe")
    public void buscarPorReservaId_CuandoNoExiste_DeberiaRetornar404YMensaje() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(pagoService.findByReservaId(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/pagos/reserva/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pago de reserva con id '99' no encontrado"));
    }

    // ====================================================================
    // POST /api/v1/pagos/agregar-pago
    // ====================================================================

    @Test
    @DisplayName("POST /api/v1/pagos/agregar-pago -> Retorna 201 y mensaje de éxito")
    public void crearPago_DeberiaRetornarMensajeExito() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
            {
                "ordenCompra": 1001,
                "usuarioId": 1,
                "reservaId": 1,
                "costo": 150000.00,
                "fechaPago": "2025-06-01"
            }
            """;

        when(pagoService.save(any(PagoDTO.class))).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(post("/api/v1/pagos/ejecutar-pago")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isOk())                             
                .andExpect(content().string("Pago ejecutado con exito!"));
}

    @Test // POST NOT FOUND
    @DisplayName("POST /api/v1/pagos/ejecutar-pago -> Retorna 404 si el Usuario o Reserva no existe")
    public void crearPago_CuandoUsuarioOrReservaNoExiste_DeberiaRetornar404() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
                {
                    "usuarioId": 99,
                    "reservaId": 99
                }
                """;

        when(pagoService.save(any(PagoDTO.class))).thenReturn(false);

        // ------------------------------------------------------------
        // 2. ACT & ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(post("/api/v1/pagos/ejecutar-pago")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isNotFound())                                  
                .andExpect(content().string("Error: Usuario o Reserva correspondientes no existen."));
    }

    // ====================================================================
    // DELETE /api/v1/pagos/{id}
    // ====================================================================

    @Test // DELETE BY ID OK 
    @DisplayName("DELETE /api/v1/pagos/{id} -> Retorna 200 si se elimina correctamente")
    public void eliminarPago_CuandoExiste_DeberiaRetornarExito() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(pagoService.delete(1)).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Pago eliminado exitosamente"));        
    }   

    @Test // DELETE ID NOT FOUND
    @DisplayName("DELETE /api/v1/pagos/{id} -> Retorna 404 si el pago no existe")
    public void eliminarPago_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(pagoService.delete(99)).thenReturn(false);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/pagos/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pago con id '99' no encontrado"));
    }
}
