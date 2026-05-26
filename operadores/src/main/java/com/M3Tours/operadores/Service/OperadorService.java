package com.M3Tours.operadores.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.M3Tours.operadores.DTO.OperadorDTO;
import com.M3Tours.operadores.Model.Operador;
import com.M3Tours.operadores.Repository.OperadorRepository;

@Service
public class OperadorService {

    @Autowired
    private OperadorRepository operadorRepository;

    public List<Operador> listarTodos() {
        return operadorRepository.findAll();
    }

    public Optional<Operador> buscarPorId(Integer id) {
        return operadorRepository.findById(id);
    }

    public Operador crear(OperadorDTO dto) {
        Operador operador = new Operador();
        operador.setEmpresaId(dto.getEmpresaId());
        operador.setNombre(dto.getNombre());
        operador.setApellido(dto.getApellido());
        operador.setRut(dto.getRut());
        operador.setEmail(dto.getEmail());
        operador.setTelefono(dto.getTelefono());
        return operadorRepository.save(operador);
    }

    public Optional<Operador> actualizar(Integer id, OperadorDTO dto) {
        return operadorRepository.findById(id).map(operador -> {
            operador.setEmpresaId(dto.getEmpresaId());
            operador.setNombre(dto.getNombre());
            operador.setApellido(dto.getApellido());
            operador.setRut(dto.getRut());
            operador.setEmail(dto.getEmail());
            operador.setTelefono(dto.getTelefono());
            return operadorRepository.save(operador);
        });
    }

    public boolean eliminar(Integer id) {
        if (operadorRepository.existsById(id)) {
            operadorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}