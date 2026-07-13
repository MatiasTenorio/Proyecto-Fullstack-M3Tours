package com.M3Tours.destinos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.M3Tours.destinos.DTO.DestinoDTO;
import com.M3Tours.destinos.Model.Destino;
import com.M3Tours.destinos.Repository.DestinoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DestinoService {

    @Autowired
    private DestinoRepository destinoRepository;

    public List<Destino> findAll(){
        return destinoRepository.findAll();
    }

    public Optional<Destino> findById(Integer id){
        return destinoRepository.findById(id);
    }

    public boolean save(DestinoDTO dto) {
        Destino destino = new Destino();
        destino.setNombre(dto.getNombre());
        destino.setPais(dto.getPais());
        destino.setCiudad(dto.getCiudad());
        destino.setDescripcion(dto.getDescripcion());
        destino.setImagenUrl(dto.getImagenUrl());
        destinoRepository.save(destino);
        return true;
    }

    public boolean update(Integer id, DestinoDTO dto) {
        if(destinoRepository.findById(id).isEmpty()){
            return false;
        }
        Destino destino = destinoRepository.findById(id).get();
        destino.setNombre(dto.getNombre());
        destino.setPais(dto.getPais());
        destino.setCiudad(dto.getCiudad());
        destino.setDescripcion(dto.getDescripcion());
        destino.setImagenUrl(dto.getImagenUrl());
        destinoRepository.save(destino);
        return true;
    }

    public boolean delete(Integer id) {
        if(destinoRepository.existsById(id)){
            destinoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
