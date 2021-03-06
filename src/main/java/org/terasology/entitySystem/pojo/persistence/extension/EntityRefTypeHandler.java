package org.terasology.entitySystem.pojo.persistence.extension;

import com.google.common.collect.Lists;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.terasology.entitySystem.EntityRef;
import org.terasology.entitySystem.PersistableEntityManager;
import org.terasology.entitySystem.pojo.persistence.TypeHandler;
import org.terasology.protobuf.EntityData;

import java.util.List;

/**
 * @author Immortius <immortius@gmail.com>
 */
public class EntityRefTypeHandler implements TypeHandler<EntityRef> {
    private PersistableEntityManager entityManager;

    private TIntSet validIds = new TIntHashSet();

    public EntityRefTypeHandler(PersistableEntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityData.Value serialize(EntityRef value) {
        if (value.exists()) {
            return EntityData.Value.newBuilder().addInteger((value).getId()).build();
        }
        return null;
    }

    public EntityRef deserialize(EntityData.Value value) {
        if (value.getIntegerCount() > 0 && validIds.contains(value.getInteger(0))) {
            return entityManager.createEntityRefWithId(value.getInteger(0));
        }
        return EntityRef.NULL;
    }

    public EntityRef copy(EntityRef value) {
        return value;
    }

    public EntityData.Value serialize(Iterable<EntityRef> value) {
        EntityData.Value.Builder result = EntityData.Value.newBuilder();
        for (EntityRef ref : value) {
            if (!ref.exists()) {
                result.addInteger(0);
            }
            else {
                result.addInteger((ref).getId());
            }
        }
        return result.build();
    }

    public List<EntityRef> deserializeList(EntityData.Value value) {
        List<EntityRef> result = Lists.newArrayListWithCapacity(value.getIntegerCount());
        for (Integer item : value.getIntegerList()) {
            if (validIds.contains(item)) {
                result.add(entityManager.createEntityRefWithId(item));
            } else {
                result.add(EntityRef.NULL);
            }
        }
        return result;
    }

    public PersistableEntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(PersistableEntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public TIntSet getValidIds() {
        return validIds;
    }
}
