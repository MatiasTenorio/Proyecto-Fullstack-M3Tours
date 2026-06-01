package com.M3Tours.destinos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.M3Tours.destinos.DTO.DestinoDTO;
import com.M3Tours.destinos.Model.Destino;
import com.M3Tours.destinos.Repository.DestinoRepository;

@Service
public class DestinoService {

    @Autowired
    private DestinoRepository destinoRepository;

    public List<Destino> listarTodos() {
        return destinoRepository.findAll();
    }

    public Optional<Destino> buscarPorId(Integer id) {
        return destinoRepository.findById(id);
    }

    public Destino crear(DestinoDTO dto) {
        Destino destino = new Destino();
        destino.setNombre(dto.getNombre());
        destino.setPais(dto.getPais());
        destino.setCiudad(dto.getCiudad());
        destino.setDescripcion(dto.getDescripcion());
        destino.setImagenUrl(dto.getImagenUrl());
        return destinoRepository.save(destino);
    }

    public Optional<Destino> actualizar(Integer id, DestinoDTO dto) {
        return destinoRepository.findById(id).map(destino -> {
            destino.setNombre(dto.getNombre());
            destino.setPais(dto.getPais());
            destino.setCiudad(dto.getCiudad());
            destino.setDescripcion(dto.getDescripcion());
            destino.setImagenUrl(dto.getImagenUrl());
            return destinoRepository.save(destino);
        });
    }

    public boolean eliminar(Integer id) {
        if (destinoRepository.existsById(id)) {
            destinoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}