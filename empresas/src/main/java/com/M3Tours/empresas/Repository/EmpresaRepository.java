package com.M3Tours.empresas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.M3Tours.empresas.Model.Empresa;



@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer>{
    Empresa findByNombreEmpresa(String nombreEmpresa);

    @Query(value="Select e from empresa e nombre_empresa= :nombre", nativeQuery = true)
    Empresa findByRutEmpresa(String rutEmpresa);
}
