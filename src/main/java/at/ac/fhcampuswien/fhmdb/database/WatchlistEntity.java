package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class WatchlistEntity {
    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField(unique = true) // No Duplicates
    public String apiId;

    public WatchlistEntity() {}
    public WatchlistEntity(String apiId) {
        this.apiId = apiId;
    }
}
