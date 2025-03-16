package loader.persistance;

import com.arcadedb.database.RID;

public class Entity
{
    private String rid;

    public String getRid()
    {
        return rid;
    }

    public Entity setRid(String rid)
    {
        this.rid = rid;
        return this;
    }
}
