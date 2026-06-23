// Definición del paquete donde reside la clase de prueba
package com.M3Tours.empresas.Controller;

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
import com.M3Tours.empresas.DTO.EmpresaDTO;
import com.M3Tours.empresas.Model.Empresa;
import com.M3Tours.empresas.Service.EmpresaService;

/**
 * Pruebas unitarias de la capa Web (Slice Test) para UsuarioController.
 * Utiliza anotaciones de Spring Boot 3.4+ y ventajas sintácticas de JDK 21.
 */
// Inicializa el entorno web de Spring de forma aislada
@WebMvcTest(EmpresaController.class)
// Desactiva los filtros de seguridad (JWT) para no recibir error 401/403 en las pruebas
@AutoConfigureMockMvc(addFilters = false) 
public class EmpresaControllerTest {

    // Inyecta la herramienta MockMvc para peticiones HTTP simuladas
    @Autowired
    private MockMvc mockMvc;

    // Crea un simulacro (mock) del servicio usando la nueva anotación de Spring Boot
    @MockitoBean
    private EmpresaService empresaService;

    // ====================================================================
    // GET /api/v1/empresas
    // ====================================================================

    @Test // GET ALL OK
    @DisplayName("GET /api/v1/usuarios -> Retorna 200 y una lista con todos los Usuarios")
    public void buscarAll_CuandoNoEstaVacio_DeberiaRetornarAll() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var empresaFalsa = new Empresa();
        empresaFalsa.setId(1);
        empresaFalsa.setNombreEmpresa("PuertoTour");
        empresaFalsa.setRutEmpresa("12345678-9");
        empresaFalsa.setNumeroEmpresa("+56223456783");
        empresaFalsa.setRazonSocial("PUERTO TOUR Servicios Turisticos SpA");

        var empresaFalsa2 = new Empresa();
        empresaFalsa2.setId(2);
        empresaFalsa2.setNombreEmpresa("PuertoToursito");
        empresaFalsa2.setRutEmpresa("12345678-0");
        empresaFalsa2.setNumeroEmpresa("+56223456784");
        empresaFalsa2.setRazonSocial("PUERTO TOURSITO Servicios Turisticos SpA");

        List<Empresa> listaFalsa = List.of(empresaFalsa, empresaFalsa2);

        // Configura el mock
        when(empresaService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/empresas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(2));
    }


    @Test // GET ALL EMPTY
    @DisplayName("GET /api/v1/empresas -> Retorna 200 y una lista vacía si no hay registros")
    public void buscarAll_CuandoNoHayDatos_DeberiaRetornarListaVacia() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        List<Empresa> listaFalsa = List.of();

        when(empresaService.findAll()).thenReturn(listaFalsa);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/empresas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verificamos que el tamaño del Array de respuesta sea exactamente 0
                .andExpect(jsonPath("$.length()").value(0));
    }


    // ====================================================================
    // GET /api/v1/empresas/{id}
    // ====================================================================

    @Test // GET ID OK
    @DisplayName("GET /api/v1/empresas/{id} -> Retorna 200 y JSON si el ID existe")
    public void buscarPorId_CuandoExiste_DeberiaRetornarEmpresa() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        var empresaFalsa = new Empresa();
        empresaFalsa.setId(1);
        // empresaFalsa.setNombreEmpresa("PuertoTour");
        // empresaFalsa.setRutEmpresa("12345678-9");
        // empresaFalsa.setNumeroEmpresa("+56223456783");
        // empresaFalsa.setRazonSocial("PUERTO TOUR Servicios Turisticos SpA");

        // Configura el mock
        when(empresaService.findById(anyInt())).thenReturn(Optional.of(empresaFalsa));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/empresas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test // GET ID NOT FOUND
    @DisplayName("GET /api/v1/empresas/{id} -> Retorna 404 y mensaje si el ID no existe")
    public void buscarPorId_CuandoNoExiste_DeberiaRetornar404YMensaje() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(empresaService.findById(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/empresas/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())

                .andExpect(content().string("Empresa con ID '99' No encontrada.")); 
    }

    // ====================================================================
    // POST /api/v1/empresas/agregar-empresa
    // ====================================================================

    @Test // POST OK
    @DisplayName("POST /api/v1/empresas/agregar-empresa -> Retorna 200 y mensaje de éxito")
    public void crearEmpresa_DeberiaRetornarMensajeExito() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        String jsonRequestBody = """
                {
                    "nombre_empresa": "PuertoTour",
                    "rut_empresa": "12345678-9",
                    "numero_empresa": "+56223456783",
                    "razon_social": "PUERTO TOUR Servicios Turisticos SpA"
                }
                """;

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(post("/api/v1/empresas/agregar-empresa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Empresa añadida con exito."));
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
        // any(empresaDTO.class) significa que no importa qué le pasemos, siempre fallará en este test.
        org.mockito.Mockito.doThrow(new RuntimeException("Error simulado en la base de datos"))
                .when(empresaService).save(any(EmpresaDTO.class));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(post("/api/v1/empresas/agregar-empresa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                // Esperamos que el servidor responda con un 500 Internal Server Error
                .andExpect(status().isInternalServerError());
    }

    // ====================================================================
    // DELETE /api/v1/empresas/{id}
    // ====================================================================

    @Test // DELETE ID OK
    @DisplayName("DELETE /api/v1/empresas/{id} -> Retorna 200 si se elimina correctamente")
    public void eliminarEmpresa_CuandoExiste_DeberiaRetornarExito() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(empresaService.delete(1)).thenReturn(true);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/empresas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Empresa eliminada con exito"));
    }

    @Test // DELETE ID NOT FOUND
    @DisplayName("DELETE /api/v1/empresas/{id} -> Retorna 404 si la empresa no existe")
    public void eliminarEmpresa_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(empresaService.delete(99)).thenReturn(false);

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(delete("/api/v1/empresas/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Empresa con ID 99 No encontrada."));
    }

    // ====================================================================
    // GET /api/v1/empresas/nombre/{nombre}
    // ====================================================================

    @Test // GET NOMBRE OK
    @DisplayName("GET /api/v1/empresas/nombre/{nombre} -> Retorna 200 y JSON si la Empresa existe")
    public void buscarPorNombre_CuandoExiste_DeberiaRetornarNombre() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE (Fase de preparación)
        // ------------------------------------------------------------
        var empresaFalsa = new Empresa();
        empresaFalsa.setNombreEmpresa("PuertoTour");
        // empresaFalsa.setRutEmpresa("12345678-9");
        // empresaFalsa.setNumeroEmpresa("+56223456783");
        // empresaFalsa.setRazonSocial("PUERTO TOUR Servicios Turisticos SpA");

        // Configura el mock
        when(empresaService.findByNombre(anyString())).thenReturn(Optional.of(empresaFalsa));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/empresas/nombre/PuertoTour")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombreEmpresa").value("PuertoTour"));

    }

    @Test // GET BY NOMBRE NOT FOUND
    @DisplayName("GET /api/v1/empresas/nombre/{nombre}} -> Retorna 404 si la empresa no existe")
    public void buscarPorNombre_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(empresaService.findByNombre(anyString())).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/empresas/nombre/PuertoTour")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Empresa con nombre 'PuertoTour' No encontrada.")); 
    }

    // ====================================================================
    // GET /api/v1/empresas/rut/{rutEmpresa}
    // ====================================================================

    @Test // GET RUT OK
    @DisplayName("GET /api/v1/empresas/rut/{rutEmpresa} -> Retorna 200 y JSON si el RUT de Empresa existe")
    public void buscarPorRut_CuandoExiste_DeberiaRetornarRut() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE (Fase de preparación)
        // ------------------------------------------------------------
        var empresaFalsa = new Empresa();
        empresaFalsa.setRutEmpresa("12345678-9");

        // Configura el mock
        when(empresaService.findByRut(anyString())).thenReturn(Optional.of(empresaFalsa));

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecución y validación)
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/empresas/rut/12345678-9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.rutEmpresa").value("12345678-9"));
    }   

    @Test // GET BY RUT NOT FOUND
    @DisplayName("GET /api/v1/empresas/rut/{rut}} -> Retorna 404 si el RUT de Empresa no existe")
    public void buscarPorRut_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE
        // ------------------------------------------------------------
        when(empresaService.findByRut(anyString())).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT
        // ------------------------------------------------------------
        mockMvc.perform(get("/api/v1/empresas/rut/12345678-9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Empresa con RUT '12345678-9' No encontrada.")); 
    }  
}