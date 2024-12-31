package com.sdongre.favourite_service.repository;


import com.sdongre.favourite_service.model.entity.Favourite;
import com.sdongre.favourite_service.model.entity.id.FavouriteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavouriteRepository extends JpaRepository<Favourite, FavouriteId> {

}
