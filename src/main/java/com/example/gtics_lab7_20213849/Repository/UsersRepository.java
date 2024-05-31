package com.example.gtics_lab7_20213849.Repository;

import com.example.gtics_lab7_20213849.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {
    @Query(nativeQuery = true,value = "select * from users where authorizedResource=?1 and active=1")
    List<Users> listarPorRecurso(int resource);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update users set active=0")
    void apagarUsuarioPorId();
}
