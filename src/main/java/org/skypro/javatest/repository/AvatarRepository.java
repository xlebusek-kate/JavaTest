package org.skypro.javatest.repository;

import org.skypro.javatest.obj.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar , Long> {

    Optional<Avatar> findByStudentId(Long id);


}
