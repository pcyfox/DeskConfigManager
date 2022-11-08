package com.df.lib_config;

import android.text.TextUtils;
import android.util.Log;

import java.util.Map;
import java.util.Set;

public class DeskNumberMappingData {
    private static final String TAG = "DeskNumberMappingData";
    private Map<String, String> mappingData;

    public Map<String, String> getMappingData() {
        return mappingData;
    }

    public void setMappingData(Map<String, String> mappingData) {
        Log.d(TAG, "setMappingData() called with: mappingData = [" + mappingData + "]");
        this.mappingData = mappingData;
    }

    public String findDeskLineAndColumn(String deskNumber) {
        if (mappingData == null || mappingData.isEmpty() || TextUtils.isEmpty(deskNumber)) {
            return null;
        }
        Set<Map.Entry<String, String>> entrySet = mappingData.entrySet();
        for (Map.Entry<String, String> e : entrySet) {
            if (deskNumber.equals(e.getValue())) {
                return e.getKey();
            }
        }
        return null;
    }


    public String findDeskNumber(String deskLine, String deskColumn) {
        String defNumber = deskLine + "-" + deskColumn;
        if (mappingData != null) {
            String deskNumber = mappingData.get(defNumber);
            if (!TextUtils.isEmpty(deskNumber)) {
                defNumber = deskNumber;
            }
        }
        return defNumber;
    }

    @Override
    public String toString() {
        return "DeskNumberMappingData{" +
                "mappingData=" + mappingData +
                '}';
    }
}
