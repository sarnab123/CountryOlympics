package com.olympics.olympicsandroid.networkLayer.cache.database.iFace;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sarnab.poddar on 7/16/16.
 */
public class DBOperationsHelper {
    private static final String TAG = DBOperationsHelper.class.getSimpleName();

    /**
     * @return Writable instance of database.
     */
    protected SQLiteDatabase getWritableDb() {
        return DBHelper.getInstance().getWritableDatabase();
    }

    /**
     * @return Readable instance of database.
     */
    protected SQLiteDatabase getReadableDb() {
        return DBHelper.getInstance().getReadableDatabase();
    }

    /**
     * Close database connection & cursor.
     *
     * @param db database instance to be closed.
     * @param cursor cursor instance to be closed
     */
    protected void dispose(SQLiteDatabase db,
                           Cursor cursor) {
        // cursor.close();
        // db.close();
    }

    /**
     * Close cursor.
     *
     * @param cursor cursor instance to be closed
     */
    protected void dispose(Cursor cursor) {
        cursor.close();
    }

    /**
     * Close database connection.
     *
     * @param db database instance to be closed.
     */
    protected void dispose(SQLiteDatabase db) {
        db.close();
    }

    protected long performInsert(final String tableName,
                                 final Object vo,
                                 final SQLiteDatabase db) {
        try {
            ContentValues values = VOProcessor.getInstance().getContentValues(tableName, vo);

            return db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }catch(Exception ex)
        {
            return -1;
        }
    }

    protected long insertReminderToDB(final String tableName,
                                 final Object vo,
                                 final SQLiteDatabase db) {
        try {
            ContentValues values = VOProcessor.getInstance().getContentValues(tableName, vo);
            return db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }catch(Exception ex)
        {
            return -1;
        }
    }


    protected Cursor performQuery(DBQueries query,
                                  String[] selectionArgs,
                                  final SQLiteDatabase db) throws Exception {
        return performQuery(query.getQuery(), selectionArgs, db);
    }

    protected Cursor performQuery(String query,
                                  String[] selectionArgs,
                                  final SQLiteDatabase db) throws Exception {
        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (!(cursor != null && cursor.moveToFirst())) {
            throw new Exception("No rows found");
        }
        return cursor;

    }

    /**
     * Delete all the rows of the table.
     *
     * @param table table name.
     */
    public void deleteAll(String table) {
        SQLiteDatabase db = getWritableDb();
        db.delete(table, null, null);
        dispose(db);
    }

    public Object get(final String tableName,
                      final String whereColumnName,
                      final String whereColumnValue,
                      final Class<?> voClass) throws Exception {
        Object vo = null;
        SQLiteDatabase db = getReadableDb();
        Cursor cursor = db.query(tableName, null, whereColumnName + "=?", new String[] {
                whereColumnValue
        }, null, null, null);
        if (!(cursor != null && cursor.moveToFirst())) {
            throw new Exception("No rows obtained for: table: " + tableName
                    + ", whereColumnName: "
                    + whereColumnName
                    + ", whereColumnValue: "
                    + whereColumnValue);
        }

        vo = VOProcessor.getInstance().getVO(tableName, cursor);

        dispose(db, cursor);
        return vo;
    }

    public long insert(String tableName,
                       Object vo) {
        SQLiteDatabase db = getWritableDb();
        long returnVal = performInsert(tableName, vo, db);

        dispose(db);
        return returnVal;
    }

//    public long insertAll(String tableName,
//                          List<Object> list) {
//        SQLiteDatabase db = getWritableDb();
//        long returnVal = 0;
//        if (list != null) {
//            for (Object vo : list) {
//                returnVal = performInsert(tableName, vo, db);
//                if (returnVal <= -1) {
//                    break;
//                }
//            }
//        }
//
//        dispose(db);
//        return returnVal;
//    }

    public int update(String tableName,
                      final String whereColumnName,
                      final String whereColumnValue,
                      Object vo) {
        SQLiteDatabase db = getWritableDb();
        ContentValues values = VOProcessor.getInstance().getContentValues(tableName, vo);
        int returnVal = db.update(tableName, values, whereColumnName + "=?", new String[] {
                whereColumnValue
        });

        dispose(db);
        return returnVal;
    }

    public void delete(String tableName,
                       final String whereColumnName,
                       final String whereColumnValue) {
        SQLiteDatabase db = getWritableDb();
        db.delete(tableName, whereColumnName + "= ?", new String[] {
                whereColumnValue
        });
        dispose(db);
    }

    /**
     * Fetches count of the rows for table specified
     *
     * @param tableName
     * @return total count
     */
    public int count(String tableName) {
        SQLiteDatabase db = getReadableDb();
        Cursor cursor = null;
        int count = 0;
        try {
            String query = DBQueries.COUNT.getQuery().replaceAll("[/?]", tableName);
            cursor = performQuery(query, new String[] {}, db);
            count = cursor.getInt(0);
        } catch (Exception e) {
            // No handling required
        } finally {
            if (cursor != null) {
                dispose(cursor);
            }
        }
        return count;
    }

    /**
     * Check if table exists
     *
     * @param db sqlite db
     * @param tableName table name
     * @return true if table exists else false
     */
    private boolean isTableExists(SQLiteDatabase db,
                                  String tableName) {
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }
        Cursor cursor =
                db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?",
                        new String[] {
                                "table", tableName
                        });
        if (!cursor.moveToFirst()) {
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
}

