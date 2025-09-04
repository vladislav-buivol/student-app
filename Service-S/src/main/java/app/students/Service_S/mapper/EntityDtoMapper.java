package app.students.Service_S.mapper;

public interface EntityDtoMapper<E, D> {
    D entityToDto(E entity);

    E dtoToEntity(D dto);

    void updateDtoFromEntity(D dto, E entity);

    void updateEntityFromDto(E entity, D dto);
}
