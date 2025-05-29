package com.example.demo.Repository;

import com.example.demo.Entity.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetailRepository extends JpaRepository<Detail,Long> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM Detail d WHERE " +
            "(:name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
                    "(:idGost IS NULL OR d.id_gost = :idGost) AND " +
                    "(:code IS NULL OR d.code = :code) AND " +
            "(:material IS NULL OR LOWER(d.material) LIKE LOWER(CONCAT('%', :material, '%')))  AND " +
            "(:priority IS NULL OR LOWER(d.priority) LIKE LOWER(CONCAT('%', :priority, '%')))  AND " +
            "(d.weight >= :minWeight OR :minWeight IS NULL) AND " +
            "(d.weight <= :maxWeight OR :maxWeight IS NULL) ")
    List<Detail> findByParam(@Param("name") String name,
                             @Param("idGost") String idGost,
                             @Param("code") String code,
                             @Param("material") String material,
                             @Param("priority") String priority,
                             @Param("minWeight") Double minWeight,
                             @Param("maxWeight") Double maxWeight
    );
}

