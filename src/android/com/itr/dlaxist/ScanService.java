package com.itr.dlaxist;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScanService extends CordovaPlugin {

    protected ScanCallback<BarcodeScan> scanCallback;

    // Boolean to explain whether the scanning is in progress or not at any
    // specific point of time
    boolean isScanning = false;

    @Override
    public boolean execute(String action, JSONArray data, final CallbackContext callbackContext) throws JSONException {

        if ("register".equals(action)) {
            scanCallback = new ScanCallback<BarcodeScan>() {
                @Override
                public void execute(BarcodeScan scan) {
                    Log.i(TAG, "Scan result [" + scan.LabelType + "-" + scan.Barcode + "].");

                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("type", scan.LabelType);
                        obj.put("barcode", scan.Barcode);
                        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, obj);
                        pluginResult.setKeepCallback(true);
                        callbackContext.sendPluginResult(pluginResult);
                    } catch(JSONException e){
                        Log.e(TAG, "Error building json object", e);

                    }
                }
            };
        }
        else if ("start".equals(action)){
            EMDKResults results = EMDKManager.getEMDKManager(this.cordova.getActivity().getApplicationContext(), this);
            if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
                return false;
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
