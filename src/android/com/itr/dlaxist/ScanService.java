package com.itr.dlaxist;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.DecodeException;
import com.datalogic.decode.DecodeResult;
import com.datalogic.decode.ReadListener;
import com.datalogic.device.ErrorManager;

import java.util.ArrayList;

public class ScanService extends CordovaPlugin {

    protected ScanCallback<BarcodeScan> scanCallback;

    private final String LOGTAG = getClass().getName();

    BarcodeManager decoder = null;
    ReadListener listener = null;

    // Boolean to explain whether the scanning is in progress or not at any
    // specific point of time
    boolean isScanning = false;

    @Override
    public boolean execute(String action, JSONArray data, final CallbackContext callbackContext) throws JSONException {

        if ("register".equals(action)) {
            scanCallback = new ScanCallback<BarcodeScan>() {
                @Override
                public void execute(BarcodeScan scan) {
                    Log.i(LOGTAG, "Scan result [" + scan.LabelType + "-" + scan.Barcode + "].");

                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("type", scan.LabelType);
                        obj.put("barcode", scan.Barcode);
                        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, obj);
                        pluginResult.setKeepCallback(true);
                        callbackContext.sendPluginResult(pluginResult);
                    } catch(JSONException e){
                        Log.e(LOGTAG, "Error building json object", e);

                    }
                }
            };
        }
        else if ("start".equals(action)){
            // If the decoder instance is null, create it.
            if (decoder == null) { // Remember an onPause call will set it to null.
                decoder = new BarcodeManager();
            }

            try {

                // Create an anonymous class.
                listener = new ReadListener() {

                    // Implement the callback method.
                    @Override
                    public void onRead(DecodeResult decodeResult) {
                        if (scanCallback != null){
                            scanCallback.execute(new BarcodeScan("UPC", decodeResult.getText()));
                        }
                    }

                };

                decoder.addReadListener(listener);

            } catch (DecodeException e) {
                Log.e(LOGTAG, "Error while trying to bind a listener to BarcodeManager", e);
            }

        }
        else if ("trigger".equals(action)){
            if (scanCallback != null){
                scanCallback.execute(new BarcodeScan("UPCA", "000000000010"));
            }
        }

        return true;
    }
}
