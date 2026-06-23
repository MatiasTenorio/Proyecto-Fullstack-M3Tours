package com.M3Tours.itinerarios.Controller;

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
import com.M3Tours.itinerarios.DTO.ItinerarioDTO;
import com.M3Tours.itinerarios.DTO.TourDTO;
import com.M3Tours.itinerarios.Model.Itinerario;
import com.M3Tours.itinerarios.Service.ItinerarioService;

/**
 * Pruebas unitarias de la capa Web (Slice Test) para UsuarioController.
 * Utiliza anotaciones de Spring Boot 3.4+ y ventajas sintácticas de JDK 21.
 */
// Inicializa el entorno web de Spring de forma aislada
@WebMvcTest(ItinerarioController.class)
// Desactiva los filtros de seguridad (JWT) para no recibir error 401/403 en las pruebas
@AutoConfigureMockMvc(addFilters = false) 
public class ItinerarioControllerTest {

    // Inyecta la herramienta MockMvc para peticiones HTTP simuladas
    @Autowired
    private MockMvc mockMvc;

    // Crea un simulacro (mock) del servicio usando la nueva anotación de Spring Boot
    @MockitoBean
    private ItinerarioService itinerarioService;

    // ====================================================================
    // GET /api/v1/itinerario
    // ====================================================================

    @Test // GET ALL OK
    @DisplayName("GET /api/v1/itinerarios -> Retorna 200 y una lista con todos los itinerarios")
    public void buscarAll_CuandoNoEstaVacio_DeberiaRetornarAll() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var itinerarioFalso = new Itinerario();
        itinerarioFalso.setId(1);
        itinerarioFalso.setTourId(1);
        itinerarioFalso.setDia(12);
        itinerarioFalso.setDescripcion("Recorrido completo...");
        itinerarioFalso.setLugar("Entrada ruta Petrohue");

        var itinerarioFalso2 = new Itinerario();
        itinerarioFalso2.setId(2);
        itinerarioFalso2.setTourId(1);
        itinerarioFalso2.setDia(25);
        itinerarioFalso2.setDescripcion("Recorrido completo...");
        itinerarioFalso2.setLugar("Entrada ruta Estaquilla");

        List<Itinerario> listaFalsa = List.of(itinerarioFalso, itinerarioFalso2);

        // Confirgurar Mock
        when(itinerarioService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/itinerarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test // GET ALL EMPTY
    @DisplayName("GET /api/v1/itinerarios -> Retorna 404 si no hay registros")
    public void buscarAll_CuandoNoHayDatos_DeberiaRetornarListaVacia() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        List<Itinerario> listaFalsa = List.of();

        when(itinerarioService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/itinerarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No hay itinerarios en este momento"));
    }

    // ====================================================================
    // GET /api/v1/itinerarios/{id}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/itinerarios/{id} -> Retorna 200 y JSON si el ID existe")
    public void buscarPorId_CuandoExiste_DeberiaRetornarItinerario() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var itinerarioFalso = new Itinerario();
        itinerarioFalso.setId(1);

        // Configura el mock
        when(itinerarioService.findById(anyInt())).thenReturn(Optional.of(itinerarioFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/itinerarios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/itinerarios/{id} -> Retorna 404 y mensaje si el ID no existe")
    public void buscarPorId_CuandoNoExiste_DeberiaRetornar404YMensaje() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(itinerarioService.findById(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/itinerarios/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe itinerario con id '99'"));
    }

    // ====================================================================
    // POST /api/v1/itinerarios
    // ====================================================================

    @Test // POST OK
    @DisplayName("POST /api/v1/itinerarios -> Retorna 201 y mensaje de éxito")
    public void crearItinerario_DeberiaRetornarMensajeExito() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
            {
                "tourId": 1,
                "destinoId": 1,
                "dia": 1,
                "descripcion": "Llegada al destino y recorrido inicial",
                "horaInicio": "08:00:00",
                "horaFin": "18:00:00",
                "lugar": "Entrada pasarelas estaquilla"
            }
            """;

        when(itinerarioService.save(any(ItinerarioDTO.class))).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & ASSERT
        // ------------------------------------------------------------
        
        mockMvc.perform(post("/api/v1/itinerarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isCreated())                             
                .andExpect(content().string("Itinerario creado con exito!"));
}

    @Test // POST NOT_FOUND
    @DisplayName("POST /api/v1/itinerarios -> Retorna 500 si falla la creación del usuario")
    public void crearItinerario_CuandoFalla_DeberiaRetornar500() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
                {
                    "tourId": 99
                }
                """;

        when(itinerarioService.save(any(ItinerarioDTO.class))).thenReturn(false);

        // ------------------------------------------------------------
        // 2. ACT & ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(post("/api/v1/itinerarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isNotFound())                                    // 404
                .andExpect(content().string("Tour no encontrado"));
    }

    // ====================================================================
    // PUT /api/v1/itinerarios/{id}
    // ====================================================================

    @Test // UPDATE OK
    @DisplayName("PUT /api/v1/itinerarios/{id} -> Retorna 200 y el objeto actualizado si el ID existe")
    public void actualizaritinerarios_CuandoExiste_DeberiaRetornar200() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
                {              
                    "nombre": "Estaquilla",
                    "pais": "Chile",
                    "ciudad": "Los Muermos",
                    "descripcion": "Pasarelas de Estaquilla y alrededores",
                    "imagenUrl": "https://example.com/estaquilla.jpg"
                }
                """;

        var itinerarioAntiguo = new Itinerario();
        itinerarioAntiguo.setId(1);
        itinerarioAntiguo.setTourId(1);
        itinerarioAntiguo.setDia(12);
        itinerarioAntiguo.setDescripcion("Recorrido completo...");
        itinerarioAntiguo.setLugar("Entrada ruta Petrohue");

        var itinerarioActualizado = new Itinerario();
        itinerarioActualizado.setId(1);
        itinerarioActualizado.setTourId(1);
        itinerarioActualizado.setDia(14);
        itinerarioActualizado.setDescripcion("Recorrido completo...");
        itinerarioActualizado.setLugar("Entrada ruta Petrohue");

        // Configuracion del Mock
        when(itinerarioService.findById(1)).thenReturn(Optional.of(itinerarioActualizado));

        // update recibe el id y el DTO, retorna el Operador ya modificado
        when(itinerarioService.update(eq(1), any(ItinerarioDTO.class))).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(put("/api/v1/itinerarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Itinerario actualizado con exito!"));
    }

    @Test // PUT FAILED
    @DisplayName("PUT /api/v1/itinerarios/{id} -> Retorna 404 si el ID no existe")
    public void actualizarItinerario_CuandoNoExiste_DeberiaRetornar404() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
                {              
                    "nombre": "Estaquilla",
                    "pais": "Chile",
                    "ciudad": "Los Muermos",
                    "descripcion": "Pasarelas de Estaquilla y alrededores",
                    "imagenUrl": "https://example.com/estaquilla.jpg"
                }
                """;

        when(itinerarioService.findById(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(put("/api/v1/itinerarios/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe itinerario con id '99'"));
    }

    // ====================================================================
    // DELETE /api/v1/itinerarios/{id}
    // ====================================================================

    @Test // DELETE BY ID OK 
    @DisplayName("DELETE /api/v1/itinerarios/{id} -> Retorna 200 si se elimina correctamente")
    public void eliminarItinerario_CuandoExiste_DeberiaRetornarExito() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var itinerarioExistente = new Itinerario();
        itinerarioExistente.setId(1);

        when(itinerarioService.findById(1)).thenReturn(Optional.of(itinerarioExistente));
        when(itinerarioService.delete(1)).thenReturn(true);
        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/itinerarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Itinerario eliminado exitosamente"));        
    }   

    @Test // DELETE ID NOT FOUND
    @DisplayName("DELETE /api/v1/itinerarios/{id} -> Retorna 404 si el Itinerario no existe")
    public void eliminarItinerario_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(itinerarioService.delete(99)).thenReturn(false);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/itinerarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe itinerario con id '99'"));
    }
}
