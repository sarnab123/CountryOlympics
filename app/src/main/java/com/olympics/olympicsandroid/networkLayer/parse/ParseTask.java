package com.olympics.olympicsandroid.networkLayer.parse;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.olympics.olympicsandroid.model.ErrorModel;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
public class ParseTask<T>
{

    // data to be parsed
    private String dataToParse;

    // dataModel to refer to.
    private Class<T> modelClass;

    private IParseListener parseListener;

    private byte parseTechique;

    //Types of Data - JSON or XML
    public static byte JSON_DATA = 0x00;
    public static byte XML_DATA = 0x01;


    public ParseTask(Class<T> modelClass,String dataToParse, IParseListener parseListener , byte parseTechique)
    {
        this.modelClass = modelClass;
        this.dataToParse = dataToParse;
        this.parseListener = parseListener;
        this.parseTechique = parseTechique;
    }

    public void startParsing()
    {
        new ParseAsyncTask(this.parseTechique,this.dataToParse).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,modelClass);
    }

    private class ParseAsyncTask extends AsyncTask<Class<T>,Void,T>
    {

        byte parseTechnique;
        String dataToParse;

        public ParseAsyncTask(byte parseTechnique,String dataToParse)
        {
            this.parseTechnique = parseTechnique;
            this.dataToParse = dataToParse;
        }

        @Override
        protected T doInBackground(Class<T>... params) {

            if(this.parseTechnique == JSON_DATA)
            {
                Gson gson = new Gson();
                return gson.fromJson(dataToParse, params[0]);
            }
            else if(this.parseTechnique == XML_DATA)
            {
                Serializer serializer = new Persister();
                try {
                    return serializer.read(params[0], dataToParse);
                } catch (Exception e) {
                     e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(T responseModel) {
            super.onPostExecute(responseModel);
            if(parseListener != null && responseModel != null)
            {
                parseListener.onParseSuccess(responseModel);
            }
            else if(parseListener != null)
            {
                ErrorModel errorModel = new ErrorModel();
                errorModel.setErrorCode("502");
                errorModel.setErrorMessage("Parse failure");
                parseListener.onParseFailure(errorModel);
            }
        }
    }


}
