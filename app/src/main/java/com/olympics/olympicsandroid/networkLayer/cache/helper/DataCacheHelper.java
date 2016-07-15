package com.olympics.olympicsandroid.networkLayer.cache.helper;

import android.os.AsyncTask;
import android.util.ArraySet;
import android.util.Log;

import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.presentationModel.CountryEventUnitModel;
import com.olympics.olympicsandroid.networkLayer.cache.ICacheListener;
import com.olympics.olympicsandroid.utility.UtilityMethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Set;

/**
 * Created by sarnab.poddar on 7/13/16.
 */
public class DataCacheHelper {

    public static final byte CACHE_COUNTRY_MODEL = 0x00;

    private static DataCacheHelper ourInstance = new DataCacheHelper();

    Set<ICacheListener> cacheListeners = new ArraySet<>();

    public static DataCacheHelper getInstance() {
        return ourInstance;
    }

    public void saveDataModel(byte dataType, IResponseModel dataModel) {
        switch (dataType) {
            case CACHE_COUNTRY_MODEL:
                if (dataModel instanceof CountryEventUnitModel) {
                    CountryEventUnitModel countryEventUnitModel = (CountryEventUnitModel) dataModel;
                    new FileSaveCacheAsync(countryEventUnitModel.getCountryAlias()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, countryEventUnitModel);
                }
                break;
        }
    }

    public synchronized void getDataModel(byte dataType, String dataIdentifier, ICacheListener listener) {
        cacheListeners.add(listener);
        new FileRetrieveAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,dataIdentifier);
    }

    class FileRetrieveAsync extends AsyncTask<String, Void, IResponseModel> {

        @Override
        protected IResponseModel doInBackground(String... params) {

            IResponseModel responseModel = null;
            ObjectInputStream objectStream = null;
            if (UtilityMethods.doesExist(params[0])) {
                File file = null;
                try {
                    file = UtilityMethods.createFile(params[0]);
                    if (file != null && file.exists()) {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        if (fileInputStream != null && fileInputStream.available() > 0) {
                            objectStream = new ObjectInputStream(fileInputStream);
                            if (objectStream != null) {
                                responseModel = (IResponseModel) objectStream.readObject();
                            }
                        }
                    }

                } catch (Exception ex)
                {
                    Log.d("DataCacheHelper","Exceptin while reading ="+ex);
                } finally {
                    try {
                        if (objectStream != null) {
                            objectStream.close();
                        }
                    } catch (IOException e) {
                    }
                }
                return responseModel;
            }

            return null;
        }

        @Override
        protected void onPostExecute(IResponseModel responseModel) {

            super.onPostExecute(responseModel);
            for (ICacheListener cacheListener : cacheListeners) {
                cacheListener.datafromCache(responseModel);
            }
            cacheListeners.clear();
        }
    }


    class FileSaveCacheAsync extends AsyncTask<IResponseModel, Void, Void> {

        String countryAlias;

        public FileSaveCacheAsync(String countryAlias) {
            this.countryAlias = countryAlias;
        }

        @Override
        protected Void doInBackground(IResponseModel... params) {

            FileOutputStream fileOutputStream = null;
            ObjectOutputStream objectOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(UtilityMethods.createFile(countryAlias));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (fileOutputStream != null) {
                    objectOutputStream = new ObjectOutputStream(fileOutputStream);
                }
            } catch (StreamCorruptedException e)
            {
                Log.d("DataCacheHelper","Exceptin while writing ="+e);

            } catch (IOException e)
            {
                Log.d("DataCacheHelper","Exceptin while writing 111 ="+e);

            }
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.writeObject(params[0]);
                }
            } catch (IOException e)
            {
                Log.d("DataCacheHelper","Exceptin while writing 2222 ="+e);

            }
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.flush();
                    objectOutputStream.close();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
            }
            return null;
        }
    }

}
