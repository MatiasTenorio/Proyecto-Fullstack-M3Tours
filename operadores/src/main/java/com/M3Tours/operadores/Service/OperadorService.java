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
    private OperadorRepository operadorRepository;
    
    @Autowired
    @Qualifier("WebClientEmpresas")
    private WebClient webClientEmpresas;

    @Autowired
    @Qualifier("WebClientUsuarios")
    private WebClient webClientUsuarios;
    

    public List<Operador> findAll() {
        return operadorRepository.findAll();
    }

    public Optional<Operador> findById(Integer id) {
        return operadorRepository.findById(id);
    }

    public boolean save(OperadorDTO dto) {
        EmpresaDTO empresa = webClientEmpresas.get()
                .uri("/empresas/{id}", dto.getEmpresaId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                    Mono.error(new RuntimeException("Empresa no encontrada")))
                .bodyToMono(EmpresaDTO.class)
                .block();
        if (empresa == null) {
            return false;
        }

        UsuarioDTO usuario = webClientUsuarios.get()
                .uri("/usuarios/{id}", dto.getUsuarioId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                    Mono.error(new RuntimeException("Usuario no encontrado")))
                .bodyToMono(UsuarioDTO.class)
                .block();
        if (usuario == null) {
            return false;
        }

        Operador operador = new Operador();
        operador.setEmpresaId(empresa.getId());
        operador.setUsuarioId(usuario.getId());
        operador.setNombre(usuario.getNombre());
        operador.setApellido(usuario.getApellido());
        operador.setRut(usuario.getRut());
        operador.setEmail(usuario.getEmail());
        operador.setTelefono(dto.getTelefono());
        operadorRepository.save(operador);
        return true;
    }

    public boolean update(Integer id, OperadorDTO dto) {
    if(operadorRepository.findById(id).isEmpty()){
        return false;
    }
        Operador operador = operadorRepository.findById(id).get();
        operador.setEmpresaId(dto.getEmpresaId());
        operador.setUsuarioId(dto.getUsuarioId());
        operador.setTelefono(dto.getTelefono());
        operadorRepository.save(operador);
    return true;
    }

    public boolean delete(Integer id) {
        if(operadorRepository.existsById(id)){
            operadorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}