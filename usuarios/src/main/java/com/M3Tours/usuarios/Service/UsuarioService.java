package com.M3Tours.usuarios.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.M3Tours.usuarios.DTO.UsuarioDTO;
import com.M3Tours.usuarios.Model.Usuario;
import com.M3Tours.usuarios.Repository.UsuarioRepository;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    public Usuario save(UsuarioDTO DTO) {
        Usuario usuario = new Usuario();
        usuario.setUsuario(DTO.getUsuario());
        usuario.setNombreUsuario(DTO.getNombreUsuario());
        usuario.setApellidoUsuario(DTO.getApellidoUsuario());
        usuario.setEmailUsuario(DTO.getEmailUsuario());
        usuario.setRutUsuario(DTO.getRutUsuario());
        usuario.setPswUsuario(DTO.getPswUsuario());
        usuario.setFechaRegistro(DTO.getFechaRegistro());

        Usuario usuarioSave = repository.save(usuario);
        return usuarioSave;
    }

    public Boolean delete(Integer id){
        if (repository.findById(id).isEmpty()){
            return false;
        }
        repository.delete(repository.findById(id).get());
        return true;
    }

    public List<Usuario> findAll(){
        return repository.findAll();
    }

    public Optional<Usuario> findById(Integer id){
        return repository.findById(id);
    }

    public Optional<Usuario> findByNombre(String nombre){
        return repository.findByNombre(nombre);
    }

    public Optional<Usuario> findByRut(String RUT){
        return repository.findByRut(RUT);
    }
}
