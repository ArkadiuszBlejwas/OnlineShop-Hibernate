package com.capgemini.store.persistence.mapper;

public interface Mapper<Entity, Dto> {

    Dto mapToDTO(Entity entity);

    Entity mapToEntity(Dto dto);

}
