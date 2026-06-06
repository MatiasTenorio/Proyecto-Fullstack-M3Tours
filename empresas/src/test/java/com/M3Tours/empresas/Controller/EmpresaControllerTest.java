package com.M3Tours.empresas.Controller;

// Importaciones estáticas de Mockito para simular (mockear) comportamientos de los componentes de servicio
import static org.mockito.ArgumentMatchers.any; // Permite aceptar cualquier instancia de una clase como argumento en el mock
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;         // Define el comportamiento esperado cuando se llama a un método del mock

// Importaciones estáticas de Spring Test para construir las peticiones HTTP ficticias
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;  // Simula una petición HTTP GET
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post; // Simula una petición HTTP POST

// Importaciones estáticas de Spring Test para validar las respuestas del servidor simulado
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; // Permite inspeccionar y evaluar el cuerpo JSON de la respuesta
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;   // Permite verificar el código de estado HTTP (200, 404, etc.)

// Importación de la clase utilitaria de Java para manejar valores que pueden o no ser nulos

import java.util.Optional;

// Importaciones de JUnit 5 para estructurar y documentar los casos de prueba

import org.junit.jupiter.api.DisplayName; // Permite asignar un nombre legible y descriptivo a cada test para los reportes
import org.junit.jupiter.api.Test;        // Indica que el método es un caso de prueba ejecutable

// Importaciones de Spring Framework para la inyección de dependencias y configuración del contexto de pruebas
import org.springframework.beans.factory.annotation.Autowired; // Inyecta automáticamente los beans configurados por Spring
import org.springframework.context.annotation.Import;         // Importa componentes adicionales necesarios que no levanta WebMvcTest por defecto
import org.springframework.http.MediaType;                    // Provee constantes para definir el Content-Type (ej. application/json)
import org.springframework.test.context.bean.override.mockito.MockitoBean; // Reemplazo oficial en Spring Boot 4 para registrar mocks en el contexto
import org.springframework.test.web.servlet.MockMvc;         // Punto de entrada principal para ejecutar pruebas del lado del servidor de Spring MVC


import static org.mockito.ArgumentMatchers.any; // Asegura esta importación
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

// Importaciones de las clases específicas del dominio del negocio
import com.M3Tours.empresas.Model.Empresa;
import com.M3Tours.empresas.Service.EmpresaService;
/**
 * Pruebas unitarias de la capa Web (Slice Test) para TipoUsuarioController.
 * Utiliza la anotación calificada de Spring Boot 4 y aprovecha las ventajas sintácticas de JDK 21.
 */
// @WebMvcTest: Inicializa el entorno web de Spring de forma aislada, cargando únicamente los componentes de la capa MVC para este controlador
@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(EmpresaController.class)
// @Import: Fuerza la carga del Assembler dentro del contexto del test para que los enlaces HATEOAS (_links) se procesen correctamente
public class EmpresaControllerTest {

    // Inyecta la herramienta MockMvc configurada por @WebMvcTest para realizar peticiones HTTP simuladas sin levantar un servidor real
    @Autowired
    private MockMvc mockMvc;

    // @MockitoBean: Crea un simulacro (mock) de la capa de servicio y lo introduce en el contenedor de Spring, sustituyendo al bean real
    @MockitoBean
    private EmpresaService empresaService;

    // Declara este método como una prueba unitaria ejecutable
    // @Test: Anotación de JUnit 5 que le indica al motor de pruebas que este método es un caso de prueba ejecutable.
    @Test
    // @DisplayName: Define el título descriptivo personalizado que se mostrará en los reportes visuales de VS Code o Maven.
    @DisplayName("GET /api/v2/tipo-usuario/{id} -> Retorna 200 y JSON si el ID existe")
    // Declaración del método público. 'throws Exception' es obligatorio porque MockMvc puede lanzar excepciones al simular peticiones HTTP.
    public void buscarPorId_CuandoExiste_DeberiaRetornarTipoUsuario() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE (Fase de preparación de las condiciones del test)
        // ------------------------------------------------------------
        
        // 'var': Inferencia de tipos de JDK 10+. Crea una nueva instancia de la entidad TipoUsuario de forma limpia.
        var tipoFalso = new Empresa();
        
        // Asigna el ID numérico (1) al objeto simulado que usaremos para la prueba.
        tipoFalso.setId(1); 
        
        // Establece el nombre "TOURSUR" en el atributo del objeto simulado.
        tipoFalso.setNombreEmpresa("TOURSUR");

        // 'when': Instrucción de Mockito. Configura el comportamiento del mock (simulacro) de la capa de servicio.
        // 'anyInt()': Matcher de Mockito que intercepta de forma segura cualquier argumento de tipo primitivo 'int'.
        // 'thenReturn': Indica que cuando el controlador llame a ese método, el servicio responderá con un Optional que envuelve a nuestro 'tipoFalso'.
        when(empresaService.findById(anyInt())).thenReturn(Optional.of(tipoFalso));

        // ------------------------------------------------------------
        // 2. ACT (Ejecución de la acción) & 3. ASSERT (Validación del resultado)
        // ------------------------------------------------------------
        
        // 'mockMvc.perform': Punto de entrada para disparar la petición HTTP ficticia hacia el entorno aislado del controlador.
        // 'get(...)': Construye una petición simulada utilizando el verbo HTTP GET apuntando al endpoint con la variable de ruta '1'.
        mockMvc.perform(get("/api/v1/empresas/1")
                
                // '.contentType(...)': Configura la cabecera (Header) de la petición indicando que viaja/espera formato JSON standard.
                .contentType(MediaType.APPLICATION_JSON))
                
                // '.andExpect(status().isOk())': Primera aserción. Valida rigurosamente que el controlador responda con un estado HTTP 200 OK.
                .andExpect(status().isOk()) 
                
                // '.andDo(print())': Herramienta de diagnóstico. Vuelca en la consola de VS Code todo el detalle del Request y del Response (Headers, Body, etc.).
                .andDo(print())
                
                // 'jsonPath("$.id")': Evalúa el cuerpo JSON de la respuesta mediante JSONPath. Verifica que el atributo 'id' en la raíz sea exactamente 1.
                .andExpect(jsonPath("$.id").value(1)) 
                
                // 'jsonPath("$.nombre")': Evalúa el JSON de salida y comprueba que el campo 'nombre' contenga la cadena exacta "FONASA".
                .andExpect(jsonPath("$.nombre").value("FONASA")) 
                
                // 'jsonPath(...)exists()': Valida la correcta intervención de Spring HATEOAS comprobando que exista la ruta del enlace dinámico autogenerado.
                .andExpect(jsonPath("$._links.self.href").exists()); 
    }
    // Declara este método como otra prueba unitaria ejecutable
    @Test
    // Asigna el título descriptivo para el caso de flujo alterno o de error (recurso no encontrado)
    @DisplayName("GET /api/v2/tipo-usuario/{id} -> Retorna 404 y mensaje si el ID no existe")
    public void buscarPorId_CuandoNoExiste_DeberiaRetornar404YMensaje() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE (Preparar el escenario para el caso vacío)
        // ------------------------------------------------------------
        // Configura el Mock: "Cuando el servicio busque el ID 99, simulamos que no se encontró en la BD retornando un Optional vacío"
        when(tipoUsuarioService.buscarPorId(99)).thenReturn(Optional.empty());

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecutar la petición y evaluar el error)
        // ------------------------------------------------------------
        // Ejecuta una petición simulada GET buscando el ID inexistente (99)
        mockMvc.perform(get("/api/v2/tipo-usuario/99")
                // Indica el tipo de contenido esperado de la petición
                .contentType(MediaType.APPLICATION_JSON))
                // Valida que el controlador responda adecuadamente con un código de estado HTTP 404 Not Found
                .andExpect(status().isNotFound()) 
                // Valida que el cuerpo completo de la respuesta coincida exactamente con la cadena de texto de error esperada
                .andExpect(jsonPath("$").value("No existe el tipo de usuario con ID: 99")); 
    }

    // Declara este método como una prueba unitaria ejecutable enfocada en la creación de registros
    @Test
    // Define el título del flujo de inserción de datos
    @DisplayName("POST /api/v2/tipo-usuario -> Retorna 200 y el objeto creado")
    public void crearTipoUsuario_DeberiaRetornarObjetoCreado() throws Exception {
        
        // ------------------------------------------------------------
        // 1. ARRANGE (Preparar datos y comportamientos para la inserción)
        // ------------------------------------------------------------
        // Instancia un nuevo objeto TipoUsuario que simulará ser la entidad ya guardada y procesada por la base de datos
        var tipoGuardado = new TipoUsuario();
        // Asigna el ID autogenerado que teóricamente otorgaría la base de datos tras la inserción
        tipoGuardado.setId(2);
        // Asigna el nombre correspondiente al registro guardado
        tipoGuardado.setNombre("ISAPRE");

        // Configura el Mock: "Cuando el método Crear del servicio reciba cualquier objeto de tipo TipoUsuario, debe retornar nuestro objeto persistido ficticio"
        when(tipoUsuarioService.Crear(any(TipoUsuario.class))).thenReturn(tipoGuardado);

        // Uso de Text Blocks (JDK 15+): Define el cuerpo JSON del request en múltiples líneas sin necesidad de escapar comillas dobles ni concatenar
        String jsonRequestBody = """
                {
                    "nombre": "ISAPRE"
                }
                """;

        // ------------------------------------------------------------
        // 2. ACT & 3. ASSERT (Ejecutar el envío de datos y verificar la respuesta)
        // ------------------------------------------------------------
        // Ejecuta una petición simulada utilizando el método HTTP POST hacia el endpoint de creación
        mockMvc.perform(post("/api/v2/tipo-usuario")
                // Configura la cabecera indicando que los datos adjuntos viajan en formato JSON
                .contentType(MediaType.APPLICATION_JSON)
                // Inserta el JSON definido en el Text Block dentro del cuerpo de la petición HTTP
                .content(jsonRequestBody)) 
                // Valida que el controlador procese la solicitud exitosamente y responda con un código de estado 200 OK
                .andExpect(status().isOk()) 
                // Verifica mediante JSONPath que la respuesta devuelva el JSON del objeto con su nuevo ID asignado (2)
                .andExpect(jsonPath("$.id").value(2))
                // Verifica que el nombre del objeto en el JSON de respuesta mantenga el valor correcto ("ISAPRE")
                .andExpect(jsonPath("$.nombre").value("ISAPRE"));
    }
}
