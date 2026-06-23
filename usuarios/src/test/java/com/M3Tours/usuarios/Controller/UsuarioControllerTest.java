// Definición del paquete donde reside la clase de prueba
package com.M3Tours.usuarios.Controller;

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
import com.M3Tours.usuarios.DTO.UsuarioDTO;
import com.M3Tours.usuarios.Model.Usuario;
import com.M3Tours.usuarios.Service.UsuarioService;

/**
 * Pruebas unitarias de la capa Web (Slice Test) para UsuarioController.
 * Utiliza anotaciones de Spring Boot 3.4+ y ventajas sintácticas de JDK 21.
 */
// Inicializa el entorno web de Spring de forma aislada
@WebMvcTest(UsuarioController.class)
// Desactiva los filtros de seguridad (JWT) para no recibir error 401/403 en las pruebas
@AutoConfigureMockMvc(addFilters = false) 
public class UsuarioControllerTest {

    // Inyecta la herramienta MockMvc para peticiones HTTP simuladas
    @Autowired
    private MockMvc mockMvc;

    // Crea un simulacro (mock) del servicio usando la nueva anotación de Spring Boot
    @MockitoBean
    private UsuarioService usuarioService;

    // ====================================================================
    // GET /api/v1/usuarios
    // ====================================================================

        @Test // GET ALL OK
        @DisplayName("GET /api/v1/usuarios -> Retorna 200 y una lista con todos los Usuarios")
        public void buscarAll_CuandoNoEstaVacio_DeberiaRetornarAll() throws Exception {
            
            // ------------------------------------------------------------
            // 1. ARRANGE
            // ------------------------------------------------------------
        var usuarioFalso = new Usuario();
        usuarioFalso.setId(1);
        usuarioFalso.setUsuario("Juan");
        usuarioFalso.setApellidoUsuario("Hernandez");
        usuarioFalso.setEmailUsuario("jh@gmail.test");
        usuarioFalso.setNombreUsuario("JuanHPRO");
        usuarioFalso.setRutUsuario("12345678-9");
        usuarioFalso.setPswUsuario("juan1234");

        var usuarioFalso2 = new Usuario();
        usuarioFalso2.setId(1);
        usuarioFalso2.setUsuario("Juansito");
        usuarioFalso2.setApellidoUsuario("Hernandesito");
        usuarioFalso2.setEmailUsuario("jh2@gmail.test");
        usuarioFalso2.setNombreUsuario("JuanHPRO2");
        usuarioFalso2.setRutUsuario("12345678-0");
        usuarioFalso2.setPswUsuario("juansito1234");

        List<Usuario> listaFalsa = List.of(usuarioFalso, usuarioFalso2);

        // Configura el mock
        when(usuarioService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test // GET ALL EMPTY
    @DisplayName("GET /api/v1/usuarios -> Retorna 200 y una lista vacía si no hay registros")
    public void buscarAll_CuandoNoHayDatos_DeberiaRetornarListaVacia() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        List<Usuario> listaFalsa = List.of();

        when(usuarioService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verificamos que el tamaño del Array de respuesta sea exactamente 0
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ====================================================================
    // GET /api/v1/usuarios/{id}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/usuarios/{id} -> Retorna 200 y JSON si el ID existe")
    public void buscarPorId_CuandoExiste_DeberiaRetornarUsuario() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var usuarioFalso = new Usuario();
        usuarioFalso.setId(1);
        usuarioFalso.setUsuario("Juan");
        usuarioFalso.setApellidoUsuario("Hernandez");
        usuarioFalso.setEmailUsuario("jh@gmail.test");
        usuarioFalso.setNombreUsuario("JuanHPRO");
        usuarioFalso.setRutUsuario("12345678-9");
        usuarioFalso.setPswUsuario("juan1234");

        // Configura el mock
        when(usuarioService.findById(anyInt())).thenReturn(Optional.of(usuarioFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.usuario").value("Juan"))
                .andExpect(jsonPath("$.rutUsuario").value("12345678-9"));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/usuarios/{id} -> Retorna 404 y mensaje si el ID no existe")
    public void buscarPorId_CuandoNoExiste_DeberiaRetornar404YMensaje() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(usuarioService.findById(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuario con ID 99 no encontrado.")); 
    }

    // ====================================================================
    // POST /api/v1/usuarios/agregar-usuario
    // ====================================================================

    @Test // POST OK
    @DisplayName("POST /api/v1/usuarios/agregar-usuario -> Retorna 200 y mensaje de éxito")
    public void crearUsuario_DeberiaRetornarMensajeExito() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
                {
                    "usuario": "Pedro",
                    "rutUsuario": "98765432-1",
                    "emailUsaurio": "pedro@email.com",
                    "cualquiercosaxd": "si"
                }
                """;

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(post("/api/v1/usuarios/agregar-usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario añadido con éxito."));
    }

    @Test // POST FAILED
    @DisplayName("POST /api/v1/usuarios/agregar-usuario -> Retorna 500 si falla la creación del usuario")
    public void crearUsuario_CuandoFalla_DeberiaRetornar500() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
                {
                    "usuario": "Pedro",
                    "rutUsuario": "98765432-1",
                    "emailUsuario": "pedro@email.com"
                }
                """;

        // Configuración clave: Usamos doThrow para simular que el servicio "explota" al intentar guardar
        // any(UsuarioDTO.class) significa que no importa qué le pasemos, siempre fallará en este test.
        org.mockito.Mockito.doThrow(new RuntimeException("Error simulado en la base de datos"))
                .when(usuarioService).save(any(UsuarioDTO.class));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(post("/api/v1/usuarios/agregar-usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                // Esperamos que el servidor responda con un 500 Internal Server Error
                .andExpect(status().isInternalServerError());
    }

    // ====================================================================
    // DELETE /api/v1/usuarios/{id}
    // ====================================================================

    @Test // DELETE ID OK
    @DisplayName("DELETE /api/v1/usuarios/{id} -> Retorna 200 si se elimina correctamente")
    public void eliminarUsuario_CuandoExiste_DeberiaRetornarExito() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(usuarioService.delete(1)).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario eliminado con éxito"));
    }

    @Test // DELETE ID NOT FOUND
    @DisplayName("DELETE /api/v1/usuarios/{id} -> Retorna 404 si el usuario no existe")
    public void eliminarUsuario_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(usuarioService.delete(99)).thenReturn(false);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/usuarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuario con ID 99 no encontrado."));
    }

    // ====================================================================
    // GET /api/v1/usuarios/nombre/{nombre}
    // ====================================================================

    @Test // GET NOMBRE OK
    @DisplayName("GET /api/v1/usuarios/nombre/{nombre} -> Retorna 200 y JSON si el Nombre existe")
    public void buscarPorNombre_CuandoExiste_DeberiaRetornarNombre() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE (Fase de preparación)
        // ------------------------------------------------------------
        var usuarioFalso = new Usuario();
        usuarioFalso.setId(1);
        usuarioFalso.setUsuario("Juan");
        usuarioFalso.setApellidoUsuario("Hernandez");
        usuarioFalso.setEmailUsuario("jh@gmail.test");
        usuarioFalso.setNombreUsuario("JuanHPRO");
        usuarioFalso.setRutUsuario("12345678-9");
        usuarioFalso.setPswUsuario("juan1234");

        // Configura el mock
        when(usuarioService.findByNombre(anyString())).thenReturn(Optional.of(usuarioFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/usuarios/nombre/Juan")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.usuario").value("Juan"));

    }

    @Test // GET NOMBRE NOT FOUND
    @DisplayName("GET /api/v1/usuarios/nombre/{nombre}} -> Retorna 404 si el nombre no existe")
    public void buscarPorNombre_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(usuarioService.findByNombre(anyString())).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/usuarios/nombre/Juan")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuario con nombre 'Juan' no encontrado.")); 
    }

    // ====================================================================
    // GET /api/v1/usuarios/rut/{rut}
    // ====================================================================

    @Test // GET RUT OK
    @DisplayName("GET /api/v1/usuarios/rut/{rut} -> Retorna 200 y JSON si el RUT existe")
    public void buscarPorRut_CuandoExiste_DeberiaRetornarRut() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE (Fase de preparación)
        // ------------------------------------------------------------
        var usuarioFalso = new Usuario();
        usuarioFalso.setId(1);
        usuarioFalso.setUsuario("Juan");
        usuarioFalso.setApellidoUsuario("Hernandez");
        usuarioFalso.setEmailUsuario("jh@gmail.test");
        usuarioFalso.setNombreUsuario("JuanHPRO");
        usuarioFalso.setRutUsuario("12345678-9");
        usuarioFalso.setPswUsuario("juan1234");

        // Configura el mock
        when(usuarioService.findByRut(anyString())).thenReturn(Optional.of(usuarioFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/usuarios/rut/12345678-9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.rutUsuario").value("12345678-9"));
    }   

    @Test // GET BY RUT NOT FOUND
    @DisplayName("GET /api/v1/usuarios/rut/{rut}} -> Retorna 404 si el RUT no existe")
    public void buscarPorRut_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(usuarioService.findByRut(anyString())).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/usuarios/rut/12345678-9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuario con RUT '12345678-9' no encontrado.")); 
    }  
}