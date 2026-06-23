package com.M3Tours.categoria.Controller;

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
import com.M3Tours.categoria.DTO.CategoriaDTO;
import com.M3Tours.categoria.Model.Categoria;
import com.M3Tours.categoria.Service.CategoriaService;

/**
 * Pruebas unitarias de la capa Web (Slice Test) para UsuarioController.
 * Utiliza anotaciones de Spring Boot 3.4+ y ventajas sintácticas de JDK 21.
 */
// Inicializa el entorno web de Spring de forma aislada
@WebMvcTest(CategoriaController.class)
// Desactiva los filtros de seguridad (JWT) para no recibir error 401/403 en las pruebas
@AutoConfigureMockMvc(addFilters = false) 
public class CategoriaControllerTest {

    // Inyecta la herramienta MockMvc para peticiones HTTP simuladas
    @Autowired
    private MockMvc mockMvc;

    // Crea un simulacro (mock) del servicio usando la nueva anotación de Spring Boot
    @MockitoBean
    private CategoriaService categoriaService;

    // ====================================================================
    // GET /api/v1/categorias
    // ====================================================================

    @Test // GET ALL OK
    @DisplayName("GET /api/v1/categorias -> Retorna 200 y una lista con todas las categorias")
    public void buscarAll_CuandoNoEstaVacio_DeberiaRetornarAll() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var categoriaFalsa = new Categoria();
        categoriaFalsa.setId(1);
        categoriaFalsa.setNombre("Facil");
        categoriaFalsa.setDescripcion("Recorrido liviano, apto para todos");
        categoriaFalsa.setEstado("Activa");

        var categoriaFalsa2 = new Categoria();
        categoriaFalsa2.setId(2);
        categoriaFalsa2.setNombre("Media");
        categoriaFalsa2.setDescripcion("Recorrido mas pasado, ¡A ponerse en forma!");
        categoriaFalsa2.setEstado("Activa");

        List<Categoria> listaFalsa = List.of(categoriaFalsa, categoriaFalsa2);

        // Confirgurar Mock
        when(categoriaService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/categorias")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test // GET ALL EMPTY
    @DisplayName("GET /api/v1/categorias -> Retorna 404 si no hay registros")
    public void buscarAll_CuandoNoHayDatos_DeberiaRetornarListaVacia() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        List<Categoria> listaFalsa = List.of();

        when(categoriaService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/categorias")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No hay categorias en este momento"));
    }

    // ====================================================================
    // GET /api/v1/categorias/{id}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/categorias/{id} -> Retorna 200 y JSON si el ID existe")
    public void buscarPorId_CuandoExiste_DeberiaRetornarCategoria() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var categoriaFalsa = new Categoria();
        categoriaFalsa.setId(1);

        // Configura el mock
        when(categoriaService.findById(anyInt())).thenReturn(Optional.of(categoriaFalsa));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/categorias/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/categorias/{id} -> Retorna 404 y mensaje si el ID no existe")
    public void buscarPorId_CuandoNoExiste_DeberiaRetornar404YMensaje() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(categoriaService.findById(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/categorias/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe categoria con id '99'"));
    }

    // ====================================================================
    // POST /api/v1/categorias
    // ====================================================================

    @Test // POST OK
    @DisplayName("POST /api/v1/categorias -> Retorna 201 y mensaje de éxito")
    public void crearCategoria_DeberiaRetornarMensajeExito() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
            {
                "nombre": "Historia",
                "descripcion": "Tours de sectores historicos",
                "estado": "Activo"
            }
            """;

        when(categoriaService.save(any(CategoriaDTO.class))).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & ASSERT
        // ------------------------------------------------------------
        
        mockMvc.perform(post("/api/v1/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isCreated())                             
                .andExpect(content().string("Categoria creada con exito!"));
}



    @Test // POST NOT_FOUND
// SOLUCIONAR: El DTO no llama a  ningun otro microservicio, lo que no hace posible
//             poder obtener un "NOT_FOUND" correctamente. (Como se solicita en el controller)

    @DisplayName("POST /api/v1/categorias-> Retorna 500 si falla la creación de la categoria")
    public void crearUsuario_CuandoFalla_DeberiaRetornar500() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
            {
                "nombre": "Historia",
                "descripcion": "Tours de sectores historicos",
                "estado": "Activo"
            }
                """;

        // Configuración clave: Usamos doThrow para simular que el servicio "explota" al intentar guardar
        // any(UsuarioDTO.class) significa que no importa qué le pasemos, siempre fallará en este test.
        org.mockito.Mockito.doThrow(new RuntimeException("Error simulado en la base de datos"))
                .when(categoriaService).save(any(CategoriaDTO.class));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(post("/api/v1/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                // Esperamos que el servidor responda con un 500 Internal Server Error
                .andExpect(status().isInternalServerError());
    }

    // ====================================================================
    // PUT /api/v1/categorias/{id}
    // ====================================================================

    @Test // UPDATE OK
    @DisplayName("PUT /api/v1/categorias/{id} -> Retorna 200 y el objeto actualizado si el ID existe")
    public void actualizarCategoria_CuandoExiste_DeberiaRetornar200() throws Exception {

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

        var categoriaAntigua = new Categoria();
        categoriaAntigua.setId(1);
        categoriaAntigua.setNombre("Facil");
        categoriaAntigua.setDescripcion("Recorrido liviano, apto para todos");
        categoriaAntigua.setEstado("Activa");

        var categoriaActualizada = new Categoria();
        categoriaActualizada.setId(1);
        categoriaActualizada.setNombre("Facil");
        categoriaActualizada.setDescripcion("Recorrido liviano, apto para todos");
        categoriaActualizada.setEstado("Inactiva");

        // Configuracion del Mock
        when(categoriaService.findById(1)).thenReturn(Optional.of(categoriaActualizada));

        // update recibe el id y el DTO, retorna el Operador ya modificado
        when(categoriaService.update(eq(1), any(CategoriaDTO.class))).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(put("/api/v1/categorias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Categoria actualizada con exito!"));
    }

    @Test // PUT FAILED
    @DisplayName("PUT /api/v1/categorias/{id} -> Retorna 404 si el ID no existe")
    public void actualizarCategoria_CuandoNoExiste_DeberiaRetornar404() throws Exception {

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

        when(categoriaService.findById(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(put("/api/v1/categorias/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe categoria con id '99'"));
    }

    // ====================================================================
    // DELETE /api/v1/categorias/{id}
    // ====================================================================

    @Test // DELETE BY ID OK 
    @DisplayName("DELETE /api/v1/categorias/{id} -> Retorna 200 si se elimina correctamente")
    public void eliminarCategoria_CuandoExiste_DeberiaRetornarExito() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var categoriaExistente = new Categoria();
        categoriaExistente.setId(1);

        when(categoriaService.findById(1)).thenReturn(Optional.of(categoriaExistente));
        when(categoriaService.delete(1)).thenReturn(true);
        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Categoria eliminada exitosamente"));        
    }   

    @Test // DELETE ID NOT FOUND
    @DisplayName("DELETE /api/v1/categorias/{id} -> Retorna 404 si la Categoria no existe")
    public void eliminarCategoria_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(categoriaService.delete(99)).thenReturn(false);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/categorias/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe categoria con id '99'"));
    }
}
