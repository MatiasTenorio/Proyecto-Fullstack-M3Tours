package com.M3Tours.itinerarios.Service;

import com.M3Tours.itinerarios.DTO.ItinerarioDTO;
import com.M3Tours.itinerarios.Model.Itinerario;
import com.M3Tours.itinerarios.Repository.ItinerarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItinerarioService {

    @Autowired
    private ItinerarioRepository itinerarioRepository;

    public List<Itinerario> listarTodos() {
        return itinerarioRepository.findAll();
    }

    public Optional<Itinerario> buscarPorId(Integer id) {
        return itinerarioRepository.findById(id);
    }

    public Itinerario crear(ItinerarioDTO dto) {
        Itinerario itinerario = new Itinerario();
        itinerario.setTourId(dto.getTourId());
        itinerario.setDestinoId(dto.getDestinoId());
        itinerario.setDia(dto.getDia());
        itinerario.setDescripcion(dto.getDescripcion());
        itinerario.setHoraInicio(dto.getHoraInicio());
        itinerario.setHoraFin(dto.getHoraFin());
        itinerario.setLugar(dto.getLugar());
        return itinerarioRepository.save(itinerario);
    }

    public Optional<Itinerario> actualizar(Integer id, ItinerarioDTO dto) {
        return itinerarioRepository.findById(id).map(itinerario -> {
            itinerario.setTourId(dto.getTourId());
            itinerario.setDestinoId(dto.getDestinoId());
            itinerario.setDia(dto.getDia());
            itinerario.setDescripcion(dto.getDescripcion());
            itinerario.setHoraInicio(dto.getHoraInicio());
            itinerario.setHoraFin(dto.getHoraFin());
            itinerario.setLugar(dto.getLugar());
            return itinerarioRepository.save(itinerario);
        });
    }

    public boolean eliminar(Integer id) {
        if (itinerarioRepository.existsById(id)) {
            itinerarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}