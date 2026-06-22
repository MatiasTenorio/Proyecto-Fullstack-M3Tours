package com.M3Tours.operadores.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.M3Tours.operadores.DTO.EmpresaDTO;
import com.M3Tours.operadores.DTO.OperadorDTO;
import com.M3Tours.operadores.DTO.UsuarioDTO;
import com.M3Tours.operadores.Model.Operador;
import com.M3Tours.operadores.Repository.OperadorRepository;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class OperadorService {
    @Autowired
    private OperadorRepository repository;

    @Autowired
    @Qualifier("WebClientUsuarios")
    private WebClient webClientUsuarios;

    @Autowired
    @Qualifier("WebClientEmpresas")
    private WebClient webClientEmpresas;

    public List<Operador> findAll() {
        return repository.findAll();
    }

    public Optional<Operador> findById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Operador> findByNombre(String nombre) {
        return repository.findByNombre(nombre);
    }

    public Optional<Operador> findByRut(String rut) {
        return repository.findByRut(rut);
    }

    public Optional<Operador> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Boolean save(OperadorDTO operadorDTO) {
        UsuarioDTO usuario = webClientUsuarios.get()
                .uri("/usuarios/{id}", operadorDTO.getUsuarioId()) 
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> 
                    Mono.error(new RuntimeException("Usuario no encontrado")))
                .bodyToMono(UsuarioDTO.class) 
                .block();
        if (usuario == null) {
            return false;
        }
        EmpresaDTO empresa = webClientEmpresas.get()
                .uri("/pagos/{id}", operadorDTO.getEmpresaId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> 
                    Mono.error(new RuntimeException("Empresa no encontrada")))
                .bodyToMono(EmpresaDTO.class) 
                .block();
        if (empresa == null) {
            return false;
        }

        Operador operador = new Operador();
        operador.setEmpresaId(operadorDTO.getEmpresaId());
        operador.setNombre(usuario.getNombre());
        operador.setApellido(usuario.getApellido());
        operador.setRut(usuario.getRut());
        operador.setEmail(usuario.getEmail());
        operador.setTelefono(operadorDTO.getTelefono());
        repository.save(operador);
        return true;
    }

    public boolean update(Integer id, OperadorDTO dto) {
    if(repository.findById(id).isEmpty()){
        return false;
    }
    Operador operador = repository.findById(id).get();
    operador.setEmpresaId(dto.getEmpresaId());
    operador.setUsuarioId(dto.getUsuarioId());
    operador.setTelefono(dto.getTelefono());
    repository.save(operador);
    return true;
}

    public boolean delete(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}