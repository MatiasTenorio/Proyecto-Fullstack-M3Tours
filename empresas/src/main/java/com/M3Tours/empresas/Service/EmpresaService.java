package com.M3Tours.empresas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.M3Tours.empresas.Model.Empresa;
import com.M3Tours.empresas.Repository.EmpresaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmpresaService {
    @Autowired
    private EmpresaRepository repository;

    @Autowired
    @Qualifier("WebClientConfig")
    private WebClient webClient;

    public List<Empresa> findAll(){
        return repository.findAll();
    }

    public Empresa findById(Integer id){
        return repository.findById(id).get();
    }

    public Empresa findByNombre(String nombre){
        return repository.findByNombreEmpresa(nombre);
    }

    public Empresa findByRut(String rutEmpresa){
        return repository.findByRutEmpresa(rutEmpresa);
    }

    // VVVVVVV EN PROGRESO VVVVVVV
    public Empresa save(Empresa e){
        return repository.save(e);
    }

    public Boolean delete(Empresa e){ 
        repository.delete(e);
        return true;
    }
}
