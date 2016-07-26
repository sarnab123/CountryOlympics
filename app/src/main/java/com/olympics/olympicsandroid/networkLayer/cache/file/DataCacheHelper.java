package com.olympics.olympicsandroid.networkLayer.cache.file;

import android.content.Intent;
import android.os.AsyncTask;

import com.olympics.olympicsandroid.OlympicsApplication;
import com.olympics.olympicsandroid.model.CountryModel;
import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.MedalTally;
import com.olympics.olympicsandroid.model.presentationModel.CountryEventUnitModel;
import com.olympics.olympicsandroid.networkLayer.cache.ICacheListener;
import com.olympics.olympicsandroid.networkLayer.cache.service.AppCacheService;
import com.olympics.olympicsandroid.networkLayer.controller.ScheduleController;
import com.olympics.olympicsandroid.utility.Logger;
import com.olympics.olympicsandroid.utility.UtilityMethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sarnab.poddar on 7/13/16.
 */
public class DataCacheHelper {

    public static final byte CACHE_COUNTRY_MODEL = 0x00;
    public static final byte CACHE_MEDALTALLY_MODEL = 0x01;
    public static final byte CACHE_COUNTRYSELECTION_MODEL = 0x02;

    public static final String CACHE_MEDALTALLY_KEY = "medaltally";
    public static final String COUNTRY_SELECTION_KEY = "countrySelection";

    public static String countryToCache = "USA;IND;CHN;BRA;FRG;FRA;GBR;ITA";


    private static DataCacheHelper ourInstance = new DataCacheHelper();

    Set<ICacheListener> cacheListeners = new HashSet<>();

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
            case CACHE_MEDALTALLY_MODEL:
                if (dataModel instanceof MedalTally) {
                    MedalTally medalTallyModel = (MedalTally) dataModel;
                    new FileSaveCacheAsync(CACHE_MEDALTALLY_KEY).executeOnExecutor
                            (AsyncTask
                            .THREAD_POOL_EXECUTOR, medalTallyModel);
                }
                break;
            case CACHE_COUNTRYSELECTION_MODEL:
                if (dataModel instanceof CountryModel) {
                    CountryModel medalTallyModel = (CountryModel) dataModel;
                    new FileSaveCacheAsync(COUNTRY_SELECTION_KEY).executeOnExecutor
                            (AsyncTask
                                    .THREAD_POOL_EXECUTOR, medalTallyModel);
                }
                break;
        }
    }

    public synchronized void getDataModel(byte dataType, String dataIdentifier, ICacheListener listener, boolean deleteCache) {
        if(deleteCache)
        {
            new FileDeleteAsync().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            Intent msgIntent = new Intent(OlympicsApplication.getAppContext(),AppCacheService.class);
            OlympicsApplication.getAppContext().startService(msgIntent);
        }
        else {
            cacheListeners.add(listener);
            new FileRetrieveAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dataIdentifier);
        }
    }


    class FileDeleteAsync extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            ScheduleController.getInstance().clearScheduleData();
            UtilityMethods.deleteCache(OlympicsApplication.getAppContext());
            return null;
        }
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
                    Logger.logs("DataCacheHelper","Exceptin while reading ="+ex);
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

            Logger.logs("DataCacheHelper", "Caching alias = =" + countryAlias);

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
                Logger.logs("DataCacheHelper","Exceptin while writing ="+e);

            } catch (IOException e)
            {
                Logger.logs("DataCacheHelper","Exceptin while writing 111 ="+e);

            }
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.writeObject(params[0]);
                }
            } catch (IOException e)
            {
                Logger.logs("DataCacheHelper","Exceptin while writing 2222 ="+e);

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
