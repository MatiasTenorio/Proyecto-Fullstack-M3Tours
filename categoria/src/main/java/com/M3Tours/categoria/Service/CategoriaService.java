package com.M3Tours.categoria.Service;

import com.M3Tours.categoria.DTO.CategoriaDTO;
import com.M3Tours.categoria.Model.Categoria;
import com.M3Tours.categoria.Repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodos() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    public Categoria crear(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setEstado(dto.getEstado());
        return categoriaRepository.save(categoria);
    }

    public Optional<Categoria> actualizar(Integer id, CategoriaDTO dto) {
        return categoriaRepository.findById(id).map(categoria -> {
            categoria.setNombre(dto.getNombre());
            categoria.setDescripcion(dto.getDescripcion());
            categoria.setEstado(dto.getEstado());
            return categoriaRepository.save(categoria);
        });
    }

    public boolean eliminar(Integer id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}