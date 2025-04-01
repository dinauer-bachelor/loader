package loader.persistance;

import com.arcadedb.database.RID;

public class Entity
{
    private RID rid;

    public RID getRid()
    {
        return rid;
    }

    public Entity setRid(RID rid)
    {
        this.rid = rid;
        return this;
    }
}
