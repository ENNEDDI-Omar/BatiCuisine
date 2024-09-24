package startup.repository.interfaces;

import startup.domain.entities.Component;
import startup.domain.enums.ComponentType;

import java.util.List;

public interface ComponentInterface extends CrudInterface<Component>
{
    List<Component> findByComponentType(ComponentType type);

    List<Component> findByProjectId(Long projectId);


}
