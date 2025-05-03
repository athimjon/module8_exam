package org.example.repo;

import org.example.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    @Query(value = "select s.* from status s where s.position_number> :posNumber and s.is_active=true order by position_number asc", nativeQuery = true)
    List<Status> getStatusRight(Integer posNumber);

    @Query(value = "select s.* from status s where s.position_number< :posNumber and s.is_active=true order by position_number desc", nativeQuery = true)
    List<Status> getStatusLeft(Integer posNumber);

    List<Status> findByIsActiveTrue();

    List<Status> findAllByOrderByPositionNumberAsc();

    List<Status> findByIsActiveTrueOrderByPositionNumberAsc();

}