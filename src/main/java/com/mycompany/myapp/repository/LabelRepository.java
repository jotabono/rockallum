package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Label entity.
 */
public interface LabelRepository extends JpaRepository<Label,Long> {

    @Query("select label from Label label where label.user.login = ?#{principal.username}")
    List<Label> findByUserIsCurrentUser();

}
