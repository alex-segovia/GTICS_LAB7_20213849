package com.example.gtics_lab7_20213849.Repository;

import com.example.gtics_lab7_20213849.Entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource,Integer> {
    public Resource findByName(String name);
}
