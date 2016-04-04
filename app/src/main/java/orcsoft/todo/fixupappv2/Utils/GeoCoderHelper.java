package orcsoft.todo.fixupappv2.Utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import orcsoft.todo.fixupappv2.Exceptions.NetException;

@EBean
public class GeoCoderHelper {
    @RootContext
    Context context;

    private static final String baseAddress = "https://geocode-maps.yandex.ru/1.x/?geocode=";
    public static final String TAG = GeoCoderHelper.class.toString();

    public LatLng getGeoPoint(String address) throws IOException, NetException, ParserConfigurationException, SAXException {
        OkHttpClient okHttpClient = NetHelper_.getInstance_(context).getOkHttpClient();

        Request request = new Request.Builder()
                .url(baseAddress + address)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String responseBody = response.body().string();
        if (200 != response.code()) {
            throw new NetException(response.code(), "ошибка при получении координат");
        }

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        mySaxparser saxp = new mySaxparser();

        parser.parse(
                new InputSource(new StringReader(responseBody)),
                saxp);

        String parsedRsult = saxp.getValue();

        Pattern pattern = Pattern.compile("(\\d+.\\d+)\\s(\\d+.\\d+)");
        Matcher matchsr = pattern.matcher(parsedRsult);
        if (matchsr.matches()) {
            double longitude = Double.parseDouble(matchsr.group(1));
            double latitude = Double.parseDouble(matchsr.group(2));
            LatLng result = new LatLng(latitude, longitude);
            Log.d(TAG, result.longitude + "  " + result.latitude + "  " + address);
            return result;
        }

        return null;
    }

    private class mySaxparser extends DefaultHandler {
        private boolean found = false;
        private boolean thisElement;
        private String value;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("pos") && !found) {
                thisElement = true;
            } else {
                thisElement = false;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            thisElement = false;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (thisElement) {
                value = new String(ch, start, length);
                found = true;
            }
        }

        public String getValue() {
            return value;
        }
    }
}