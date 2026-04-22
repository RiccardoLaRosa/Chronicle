package it.aulab.chronicle.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.aulab.chronicle.models.CareerRequest;

@Repository
public interface CareerRequestRepository extends JpaRepository<CareerRequest, Long> {
    
    List<CareerRequest> findByIsCheckedFalse();

    boolean existsByUserId(Long userId);

    boolean existsByRoleId(Long roleId);

    boolean existsByUserIdAndIsChecked(Long userId, boolean isChecked);
}
