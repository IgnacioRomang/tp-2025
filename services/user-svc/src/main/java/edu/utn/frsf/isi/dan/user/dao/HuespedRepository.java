package edu.utn.frsf.isi.dan.user.dao;

import edu.utn.frsf.isi.dan.user.model.Huesped;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface HuespedRepository extends JpaRepository<Huesped, Long> {
}