package com.orm.helper;

import android.database.sqlite.SQLiteDatabase;
import timber.log.Timber;

import static com.orm.SugarContext.getSugarContext;

public final class SugarTransactionHelper {
    //Prevent instantiation..
    private SugarTransactionHelper() { }

    public static void doInTransaction(Callback callback) {
        final SQLiteDatabase database = getSugarContext().getSugarDb().getDB();
        database.beginTransaction();

        try {
            Timber.d("Callback executing within transaction");

            callback.manipulateInTransaction();
            database.setTransactionSuccessful();

            Timber.d("Callback successfully executed within transaction");
        } catch (Throwable e) {
            Timber.d("Could execute callback within transaction", e);
        } finally {
            database.endTransaction();
        }
    }

    public interface Callback {
        void manipulateInTransaction();
    }
}
