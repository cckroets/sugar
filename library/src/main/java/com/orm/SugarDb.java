package com.orm;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.orm.helper.ManifestHelper;
import com.orm.util.SugarCursorFactory;

import static com.orm.util.ContextUtil.getContext;
import static com.orm.helper.ManifestHelper.getDatabaseVersion;
import static com.orm.helper.ManifestHelper.getDbName;
import static com.orm.SugarContext.getDbConfiguration;

public class SugarDb extends SQLiteOpenHelper {
    private static final String LOG_TAG = "Sugar";

    private final SchemaGenerator schemaGenerator;
    private final SugarDbCallback dbCallback;
    private SQLiteDatabase sqLiteDatabase;
    private int openedConnections = 0;

    //Prevent instantiation
    private SugarDb(SugarDbCallback callback) {
        super(getContext(), getDbName(), new SugarCursorFactory(ManifestHelper.isDebugEnabled()), getDatabaseVersion());
        dbCallback = callback;
        schemaGenerator = SchemaGenerator.getInstance();
    }

    public static SugarDb getInstance() {
        return new SugarDb(null);
    }

    public static SugarDb getInstance(SugarDbCallback callback) {
        return new SugarDb(callback);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        schemaGenerator.createDatabase(sqLiteDatabase);
        if (dbCallback != null) {
            dbCallback.onCreate(sqLiteDatabase);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        final SugarDbConfiguration configuration = getDbConfiguration();

        if (null != configuration) {
            db.setLocale(configuration.getDatabaseLocale());
            db.setMaximumSize(configuration.getMaxSize());
            db.setPageSize(configuration.getPageSize());
        }

        super.onConfigure(db);
        if (dbCallback != null) {
            dbCallback.onConfigure(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        schemaGenerator.doUpgrade(sqLiteDatabase, oldVersion, newVersion);
        if (dbCallback != null) {
            dbCallback.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
        }
    }

    public synchronized SQLiteDatabase getDB() {
        if (this.sqLiteDatabase == null) {
            this.sqLiteDatabase = getWritableDatabase();
        }

        return this.sqLiteDatabase;
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        Log.d(LOG_TAG, "getReadableDatabase");
        openedConnections++;
        return super.getReadableDatabase();
    }

    @Override
    public synchronized void close() {
        Log.d(LOG_TAG, "getReadableDatabase");
        openedConnections--;
        if(openedConnections == 0) {
            Log.d(LOG_TAG, "closing");
            super.close();
        }
    }
}
