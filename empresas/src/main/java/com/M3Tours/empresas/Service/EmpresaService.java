package com.M3Tours.empresas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.M3Tours.empresas.DTO.EmpresaDTO;
import com.M3Tours.empresas.Model.Empresa;
import com.M3Tours.empresas.Repository.EmpresaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmpresaService {
    @Autowired
    private EmpresaRepository repository;

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

    public Empresa save(EmpresaDTO DTO){
        Empresa empresa = new Empresa();
        empresa.setNombreEmpresa(DTO.getNombreEmpresa());
        empresa.setRutEmpresa(DTO.getRutEmpresa());
        empresa.setNumeroEmpresa(DTO.getNumeroEmpresa());
        empresa.setRazonSocial(DTO.getRazonSocial());

        Empresa empresaSave = repository.save(empresa);
        return empresaSave;
    }

    public Empresa guardar(Empresa empresa){
        return repository.save(empresa);
    }

    public Boolean delete(Integer id){ 
        repository.delete(repository.findById(id).get());
        return true;
    }
}
