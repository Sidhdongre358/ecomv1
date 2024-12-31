package com.sdongre.favourite_service.service;


import com.sdongre.favourite_service.model.dto.FavouriteDto;
import com.sdongre.favourite_service.model.entity.id.FavouriteId;

import java.util.List;

public interface FavouriteService {
    List<FavouriteDto> findAll();

    FavouriteDto findById(final FavouriteId favouriteId);

    FavouriteDto save(final FavouriteDto favouriteDto);

    FavouriteDto update(final FavouriteDto favouriteDto);

    void deleteById(final FavouriteId favouriteId);
}
