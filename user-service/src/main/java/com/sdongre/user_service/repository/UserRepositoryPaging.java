package com.sdongre.user_service.repository;


import com.sdongre.user_service.model.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserRepositoryPaging extends PagingAndSortingRepository<User, Long> {
    @Query("SELECT u FROM User u")
    List<User> findAll(Sort sort);
}