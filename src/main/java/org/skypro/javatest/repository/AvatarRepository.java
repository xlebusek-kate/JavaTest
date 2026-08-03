package org.skypro.javatest.repository;

import org.skypro.javatest.obj.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar , Long> {
    Optional<Avatar> findByStudentId(Long id);
}
