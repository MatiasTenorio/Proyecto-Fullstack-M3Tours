package com.M3Tours.pago.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.M3Tours.pago.Model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer>{

    @Query(value="Select p from pago p orden_compra= :ordenCompra", nativeQuery = true)
    Optional<Pago> findByOrdenComrpa(Integer ordenComrpa);

    Optional<Pago> findByUsuarioId(Integer id);

    Optional<Pago> findByReservaId(Integer id);

}
