package com.M3Tours.operadores.Controller;

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
import com.M3Tours.operadores.DTO.OperadorDTO;
import com.M3Tours.operadores.Model.Operador;
import com.M3Tours.operadores.Service.OperadorService;

/**
 * Pruebas unitarias de la capa Web (Slice Test) para UsuarioController.
 * Utiliza anotaciones de Spring Boot 3.4+ y ventajas sintácticas de JDK 21.
 */
// Inicializa el entorno web de Spring de forma aislada
@WebMvcTest(OperadorController.class)
// Desactiva los filtros de seguridad (JWT) para no recibir error 401/403 en las pruebas
@AutoConfigureMockMvc(addFilters = false) 
public class OperadorControllerTest {

    // Inyecta la herramienta MockMvc para peticiones HTTP simuladas
    @Autowired
    private MockMvc mockMvc;

    // Crea un simulacro (mock) del servicio usando la nueva anotación de Spring Boot
    @MockitoBean
    private OperadorService operadorService;

    // ====================================================================
    // GET /api/v1/operadores
    // ====================================================================

    @Test // GET ALL OK
    @DisplayName("GET /api/v1/operadores -> Retorna 200 y una lista con todos los Operadores")
    public void buscarAll_CuandoNoEstaVacio_DeberiaRetornarAll() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var operadorFalso = new Operador();
        operadorFalso.setId(1);
        operadorFalso.setEmpresaId(1);
        operadorFalso.setUsuarioId(1);
        operadorFalso.setNombre("OPJuan");
        operadorFalso.setApellido("Hernandez");
        operadorFalso.setRut("12345678-9");
        operadorFalso.setEmail("opjh@gmail.test");
        operadorFalso.setTelefono("+56912345678");

        var operadorFalso2 = new Operador();
        operadorFalso2.setId(2);
        operadorFalso2.setEmpresaId(2);
        operadorFalso2.setUsuarioId(2);
        operadorFalso2.setNombre("OPJuansito");
        operadorFalso2.setApellido("Hernandesito");
        operadorFalso2.setRut("12345678-1");
        operadorFalso2.setEmail("opjh2@gmail.test");
        operadorFalso2.setTelefono("+56912345671");

        List<Operador> listaFalsa = List.of(operadorFalso, operadorFalso2);

        // Confirgurar Mock
        when(operadorService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/operadores")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test // GET ALL EMPTY
    @DisplayName("GET /api/v1/operadores -> Retorna 200 y una lista vacía si no hay registros")
    public void buscarAll_CuandoNoHayDatos_DeberiaRetornarListaVacia() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        List<Operador> listaFalsa = List.of();

        when(operadorService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/operadores")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No hay operadores en este momento"));
    }

    // ====================================================================
    // GET /api/v1/operadores/{id}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/operadores/{id} -> Retorna 200 y JSON si el ID existe")
    public void buscarPorId_CuandoExiste_DeberiaRetornarOperador() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var operadorFalso = new Operador();
        operadorFalso.setId(1);
        // operadorFalso.setEmpresaId(1);
        // operadorFalso.setUsuarioId(1);
        // operadorFalso.setNombre("OPJuan");
        // operadorFalso.setApellido("Hernandez");
        // operadorFalso.setRut("12345678-9");
        // operadorFalso.setEmail("opjh@gmail.test");
        // operadorFalso.setTelefono("+56912345678");

        // Configura el mock
        when(operadorService.findById(anyInt())).thenReturn(Optional.of(operadorFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/operadores/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/operadores/{id} -> Retorna 404 y mensaje si el ID no existe")
    public void buscarPorId_CuandoNoExiste_DeberiaRetornar404YMensaje() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(operadorService.findById(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/operadores/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe operador con id '99'")); 
    }

    // ====================================================================
    // GET /api/v1/operadores/nombre/{nombre}
    // ====================================================================

    @Test // GET NOMBRE OK
    @DisplayName("GET /api/v1/operadores/nombre/{nombre} -> Retorna 200 y JSON si el Nombre existe")
    public void buscarPorNombre_CuandoExiste_DeberiaRetornarNombre() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE (Fase de preparación)
        // ------------------------------------------------------------
        var operadorFalso = new Operador();
        operadorFalso.setNombre("OPJuan");

        // Configura el mock
        when(operadorService.findByNombre(anyString())).thenReturn(Optional.of(operadorFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/operadores/nombre/OPJuan")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre").value("OPJuan"));

    }

    @Test // GET NOMBRE NOT FOUND
    @DisplayName("GET /api/v1/operadores/nombre/{nombre}} -> Retorna 404 si el nombre no existe")
    public void buscarPorNombre_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(operadorService.findByNombre(anyString())).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/operadores/nombre/OPJuan")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe operador con nombre 'OPJuan'")); 
    }

    // ====================================================================
    // GET /api/v1/usuarios/rut/{rut}
    // ====================================================================

    @Test // GET RUT OK
    @DisplayName("GET /api/v1/operadores/rut/{rut} -> Retorna 200 y JSON si el RUT existe")
    public void buscarPorRut_CuandoExiste_DeberiaRetornarRut() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE (Fase de preparación)
        // ------------------------------------------------------------
        var operadorFalso = new Operador();
        operadorFalso.setRut("12345678-9");

        // Configura el mock
        when(operadorService.findByRut(anyString())).thenReturn(Optional.of(operadorFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/operadores/rut/12345678-9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }   

    @Test // GET BY RUT NOT FOUND
    @DisplayName("GET /api/v1/operadores/rut/{rut}} -> Retorna 404 si el RUT no existe")
    public void buscarPorRut_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(operadorService.findByRut(anyString())).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/operadores/rut/12345678-9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe operador con rut '12345678-9'")); 
    }

    // ====================================================================
    // GET /api/v1/usuarios/email/{email}
    // ====================================================================

    @Test // GET RUT OK
    @DisplayName("GET /api/v1/operadores/email/{email} -> Retorna 200 y JSON si el Email existe")
    public void buscarPorEmail_CuandoExiste_DeberiaRetornarEmail() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE (Fase de preparación)
        // ------------------------------------------------------------
        var operadorFalso = new Operador();
        operadorFalso.setEmail("opjh@gmail.test");

        // Configura el mock
        when(operadorService.findByEmail(anyString())).thenReturn(Optional.of(operadorFalso));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/operadores/email/opjh@gmail.test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.email").value("opjh@gmail.test"));
    }   

    @Test // GET BY EMAIL NOT FOUND
    @DisplayName("GET /api/v1/operadores/email/{email}} -> Retorna 404 si el Email no existe")
    public void buscarPorEmail_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(operadorService.findByEmail(anyString())).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/operadores/email/opjh@gmail.test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ====================================================================
    // POST /api/v1/operadores/agregar-operador
    // ====================================================================

    @Test
    @DisplayName("POST /api/v1/operadores/agregar-operador -> Retorna 201 y mensaje de éxito")
    public void crearOperador_DeberiaRetornarMensajeExito() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
            {
                "empresaId": 1,
                "usuarioId": 1,
                "telefono": "+56912345678"
            }
            """;

        when(operadorService.save(any(OperadorDTO.class))).thenReturn(true);

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/operadores/agregar-operador")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isCreated())                             
                .andExpect(content().string("Operador creado con exito!"));
}

    @Test // POST NOT FOUND
    @DisplayName("POST /api/v1/operadores/agregar-operador -> Retorna 404 si el operador no existe")
    public void crearOperador_CuandoEmpresaOUsuarioNoExiste_DeberiaRetornar404() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
                {
                    "empresaId": 99,
                    "usuarioId": 99
                }
                """;

        when(operadorService.save(any(OperadorDTO.class))).thenReturn(false);

        // ------------------------------------------------------------
        // 2. ACT & ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(post("/api/v1/operadores/agregar-operador")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isNotFound())                                   
                .andExpect(content().string("Empresa o Usuario no encontrado"));
    }

    // ====================================================================
    // PUT /api/v1/operadores/{id}
    // ====================================================================

    @Test // UPDATE OK
    @DisplayName("PUT /api/v1/operadores/{id} -> Retorna 200 y el objeto actualizado si el ID existe")
    public void actualizarOperador_CuandoExiste_DeberiaRetornar200() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
                {
                    "empresaId": 1,
                    "usuarioId": 1,
                    "telefono": "+56999999999"
                }
                """;

        var operadorAntiguo = new Operador();
        operadorAntiguo.setId(1);
        operadorAntiguo.setNombre("OPJuan");
        operadorAntiguo.setApellido("Hernandez");
        operadorAntiguo.setEmpresaId(1);

        var operadorActualizado = new Operador();
        operadorActualizado.setId(1);
        operadorActualizado.setNombre("OPJuan");      
        operadorActualizado.setApellido("Hernandez");
        operadorActualizado.setEmpresaId(2);      
        operadorActualizado.setTelefono("+56999999999");

        // Configuracion del Mock
        when(operadorService.findById(1)).thenReturn(Optional.of(operadorAntiguo));

        // update recibe el id y el DTO, retorna el Operador ya modificado
        when(operadorService.update(eq(1), any(OperadorDTO.class))).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(put("/api/v1/operadores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test // PUT FAILED
    @DisplayName("PUT /api/v1/operadores/{id} -> Retorna 404 si el ID no existe")
    public void actualizarOperador_CuandoNoExiste_DeberiaRetornar404() throws Exception {

        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
                {
                    "empresaId": 1,
                    "usuarioId": 1
                }
                """;

        when(operadorService.findById(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(put("/api/v1/operadores/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe operador con id '99'"));
    }

    // ====================================================================
    // DELETE /api/v1/operadores/{id}
    // ====================================================================

    @Test // DELETE BY ID OK
    @DisplayName("DELETE /api/v1/operadores/{id} -> Retorna 200 si se elimina correctamente")
    public void eliminarOperador_CuandoExiste_DeberiaRetornarExito() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var operadorFalso = new Operador();
        operadorFalso.setId(1);

        when(operadorService.findById(1)).thenReturn(Optional.of(operadorFalso)); // ← faltaba esto
        when(operadorService.delete(1)).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/operadores/1"))
                .andExpect(status().isOk());
    }

    @Test // DELETE ID NOT FOUND
    @DisplayName("DELETE /api/v1/usuarios/{id} -> Retorna 404 si el usuario no existe")
    public void eliminarUsuario_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(operadorService.delete(99)).thenReturn(false);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/operadores/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No existe operador con id '99'"));
    }

}   
