package com.M3Tours.categoria.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.M3Tours.categoria.DTO.CategoriaDTO;
import com.M3Tours.categoria.Model.Categoria;
import com.M3Tours.categoria.Repository.CategoriaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public List<Categoria> findAll(){
        return repository.findAll();
    }

    public Optional<Categoria> findById(Integer id){
        return repository.findById(id);
    }

    public boolean save(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setEstado(dto.getEstado());
        repository.save(categoria);
        return true;
    }

    public boolean update(Integer id, CategoriaDTO dto) {
        if(repository.findById(id).isEmpty()){
            return false;
        }
        Categoria categoria = repository.findById(id).get();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setEstado(dto.getEstado());
        repository.save(categoria);
        return true;
    }

    public boolean delete(Integer id) {
        if(repository.existsById(id)){
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}