package com.example.gtics_lab7_20213849.Repository;

import com.example.gtics_lab7_20213849.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {
}
