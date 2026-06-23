package com.M3Tours.destinos.Controller;

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
import com.M3Tours.destinos.DTO.DestinoDTO;
import com.M3Tours.destinos.Model.Destino;
import com.M3Tours.destinos.Service.DestinoService;

/**
 * Pruebas unitarias de la capa Web (Slice Test) para UsuarioController.
 * Utiliza anotaciones de Spring Boot 3.4+ y ventajas sintácticas de JDK 21.
 */
// Inicializa el entorno web de Spring de forma aislada
@WebMvcTest(DestinoController.class)
// Desactiva los filtros de seguridad (JWT) para no recibir error 401/403 en las pruebas
@AutoConfigureMockMvc(addFilters = false) 
public class DestinoControllerTest {

    // Inyecta la herramienta MockMvc para peticiones HTTP simuladas
    @Autowired
    private MockMvc mockMvc;

    // Crea un simulacro (mock) del servicio usando la nueva anotación de Spring Boot
    @MockitoBean
    private DestinoService destinoService;

    // ====================================================================
    // GET /api/v1/destinos
    // ====================================================================

    @Test // GET ALL OK
    @DisplayName("GET /api/v1/destinos -> Retorna 200 y una lista con todos los Destinos")
    public void buscarAll_CuandoNoEstaVacio_DeberiaRetornarAll() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var destinoFalso = new Destino();
        destinoFalso.setId(1);
        destinoFalso.setNombre("Saltos del Petrohue");
        destinoFalso.setPais("Chile");
        destinoFalso.setCiudad("Puerto Varas");
        destinoFalso.setDescripcion("Tour hacia Saltos del Petrohue");
        destinoFalso.setImagenUrl("imagen epicarda aqui");

       var destinoFalso2 = new Destino();
        destinoFalso2.setId(2);
        destinoFalso2.setNombre("Maullin");
        destinoFalso2.setPais("Chile");
        destinoFalso2.setCiudad("Maullin");
        destinoFalso2.setDescripcion("TODOS A LA CASA DE NICOLÁ");
        destinoFalso2.setImagenUrl("otra imagen epicarda aqui");

        List<Destino> listaFalsa = List.of(destinoFalso, destinoFalso2);

        // Confirgurar Mock
        when(destinoService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/destinos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test // GET ALL EMPTY
    @DisplayName("GET /api/v1/destinos -> Retorna 404 si no hay registros")
    public void buscarAll_CuandoNoHayDatos_DeberiaRetornarListaVacia() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        List<Destino> listaFalsa = List.of();

        when(destinoService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/destinos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No hay destinos en este momento"));
    }

    // ====================================================================
    // GET /api/v1/destinos/{id}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/destinos/{id} -> Retorna 200 y JSON si el ID existe")
    public void buscarPorId_CuandoExiste_DeberiaRetornarDestino() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var destinoFalso = new Destino();
        destinoFalso.setId(1);

        // Configura el mock
        when(destinoService.findById(anyInt())).thenReturn(Optional.of(destinoFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/destinos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/destinos/{id} -> Retorna 404 y mensaje si el ID no existe")
    public void buscarPorId_CuandoNoExiste_DeberiaRetornar404YMensaje() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(destinoService.findById(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/destinos/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe destino con id '99'"));
    }

    // ====================================================================
    // POST /api/v1/destinos
    // ====================================================================

    @Test // POST OK
    @DisplayName("POST /api/v1/destinos -> Retorna 201 y mensaje de éxito")
    public void crearDestino_DeberiaRetornarMensajeExito() throws Exception {

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

        when(destinoService.save(any(DestinoDTO.class))).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & ASSERT
        // ------------------------------------------------------------
        
        mockMvc.perform(post("/api/v1/destinos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isCreated())                             
                .andExpect(content().string("Destino creado con exito!"));
}



    @Test // POST NOT_FOUND
// SOLUCIONAR: El DTO no llama a  ningun otro microservicio, lo que no hace posible
//             poder obtener un "NOT_FOUND" correctamente. (Como se solicita en el controller)

    @DisplayName("POST /api/v1/destinos -> Retorna 500 si falla la creación del Destino")
    public void crearUsuario_CuandoFalla_DeberiaRetornar500() throws Exception {
        
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

        // Configuración clave: Usamos doThrow para simular que el servicio "explota" al intentar guardar
        // any(UsuarioDTO.class) significa que no importa qué le pasemos, siempre fallará en este test.
        org.mockito.Mockito.doThrow(new RuntimeException("Error simulado en la base de datos"))
                .when(destinoService).save(any(DestinoDTO.class));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(post("/api/v1/destinos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                // Esperamos que el servidor responda con un 500 Internal Server Error
                .andExpect(status().isInternalServerError());
    }

    // ====================================================================
    // PUT /api/v1/destinos/{id}
    // ====================================================================

    @Test // UPDATE OK
    @DisplayName("PUT /api/v1/destinos/{id} -> Retorna 200 y el objeto actualizado si el ID existe")
    public void actualizarDestino_CuandoExiste_DeberiaRetornar200() throws Exception {

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

        var destinoAntiguo = new Destino();
        destinoAntiguo.setId(1);
        destinoAntiguo.setNombre("Saltos del Petrohue");
        destinoAntiguo.setPais("Chile");
        destinoAntiguo.setCiudad("Puerto Varas");
        destinoAntiguo.setDescripcion("Tour hacia Saltos del Petrohue");
        destinoAntiguo.setImagenUrl("imagen epicarda aqui");

        var destinoActualizado = new Destino();
        destinoActualizado.setId(1);
        destinoActualizado.setNombre("Saltos del Petrohue");
        destinoActualizado.setPais("Chile");
        destinoActualizado.setCiudad("Puerto Varas");
        destinoActualizado.setDescripcion("Tour hacia Saltos del Petrohue");
        destinoActualizado.setImagenUrl("imagen ultra epicarda aqui");

        // Configuracion del Mock
        when(destinoService.findById(1)).thenReturn(Optional.of(destinoActualizado));

        // update recibe el id y el DTO, retorna el Operador ya modificado
        when(destinoService.update(eq(1), any(DestinoDTO.class))).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(put("/api/v1/destinos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Destino actualizado con exito!"));
    }

    @Test // PUT FAILED
    @DisplayName("PUT /api/v1/destinos/{id} -> Retorna 404 si el ID no existe")
    public void actualizarDestino_CuandoNoExiste_DeberiaRetornar404() throws Exception {

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

        when(destinoService.findById(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(put("/api/v1/destinos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe destino con id '99'"));
    }

    // ====================================================================
    // DELETE /api/v1/destinos/{id}
    // ====================================================================

    @Test // DELETE BY ID OK 
    @DisplayName("DELETE /api/v1/destinos/{id} -> Retorna 200 si se elimina correctamente")
    public void eliminarDestino_CuandoExiste_DeberiaRetornarExito() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var destinoExistente = new Destino();
        destinoExistente.setId(1);

        when(destinoService.findById(1)).thenReturn(Optional.of(destinoExistente));
        when(destinoService.delete(1)).thenReturn(true);
        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/destinos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Destino eliminado exitosamente"));        
    }   

    @Test // DELETE ID NOT FOUND
    @DisplayName("DELETE /api/v1/destinos/{id} -> Retorna 404 si el Destino no existe")
    public void eliminarDestino_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(destinoService.delete(99)).thenReturn(false);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/destinos/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe destino con id '99'"));
    }
}
