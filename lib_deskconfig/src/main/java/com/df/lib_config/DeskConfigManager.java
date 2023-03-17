package com.df.lib_config;


import android.os.Environment;
import android.text.TextUtils;
import android.util.Pair;

import com.df.lib_config.util.Tools;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DeskConfigManager {
    private static final String TAG = "ConfigManager";
    private final static String configFileName = "DeskConfig.conf";
    private final static String mappingFileName = "DeskNumberMapping.conf";
    public String deskConfigPath;
    public String deskNumberMappingDataPath;
    public final DeskNumberMappingData mappingData = new DeskNumberMappingData();

    private static DeskConfigManager instance;

    public static DeskConfigManager getInstance() {
        if (instance == null) {
            instance = new DeskConfigManager();
        }
        return instance;
    }

    private DeskConfigManager() {
        String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        deskConfigPath = rootDir + configFileName;
        deskNumberMappingDataPath = rootDir + mappingFileName;
    }

    //为了兼容老版本
    private void copyOldConfigFileToNewDir() {
//        String oldRootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
//        String newRootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/config/";
//
//        File newDir = new File(newRootDir);
//        if (!newDir.exists()) {
//            newDir.mkdirs();
//        }
//
//        File oldConfigFile = new File(oldRootDir + configFileName);
//        if (oldConfigFile.exists()) {
//            File newConfigFile = new File(newRootDir + configFileName);
//            if (!newConfigFile.exists() || newConfigFile.lastModified() < oldConfigFile.lastModified()) {
//                boolean ret = copyFileUsingStream(oldConfigFile, newConfigFile);
//                if (ret) {
//                    oldConfigFile.delete();
//                }
//            }
//        }
//
//        File oldMappingFile = new File(oldRootDir + mappingFileName);
//        if (oldMappingFile.exists()) {
//            File newMappingFile = new File(newRootDir + mappingFileName);
//            if (!newMappingFile.exists() || newMappingFile.lastModified() < oldMappingFile.lastModified()) {
//                boolean ret = copyFileUsingStream(oldMappingFile, newMappingFile);
//                if (ret) {
//                    oldConfigFile.delete();
//                }
//            }
//        }
//        DESK_CONFIG_PATH = newRootDir + configFileName;
//        DESK_NUMBER_MAPPING_DATA_PATH = newRootDir + mappingFileName;
    }


    //检测桌号合法性
    public static boolean isDeskNumberRight(String deskNumber) {
        return !TextUtils.isEmpty(deskNumber) && !"-1".equals(deskNumber);
    }

    public void updateData() {
        //copyOldConfigFileToNewDir();
        loadLocationConfig();
        loadLocalDeskNumberMappingData();
    }

    private void loadLocationConfig() {
        DeskConfig config = loadDeskConfig();
        if (isDeskNumberRight(config.getDeskNumber())) {
            String mappingNumber = findDeskNumberFormMappingFile(config.getDeskLine(), config.getDeskColumn());
            if (mappingNumber != null) {
                config.setDeskNumber(mappingNumber);
            }
        }
        DeskConfig.getInstance().update(config);
    }

    public static Pair<String, String> tryParseDeskNumberToXY(String deskNumber) {
        if (TextUtils.isEmpty(deskNumber)) {
            return null;
        }
        if (deskNumber.contains("-")) {
            String[] data = deskNumber.split("-");
            if (data.length == 2) {
                return new Pair<>(data[0], data[1]);
            }
        }
        return null;
    }


    private DeskConfig loadDeskConfig() {
        return Tools.loadDeskData(DeskConfig.class, deskConfigPath);
    }

    private void saveDeskConfig() {
        Tools.saveDataToDesk(DeskConfig.getInstance(), deskConfigPath);
    }

    private void loadLocalDeskNumberMappingData() {
        Map<String, String> data = new HashMap<>();
        data = Tools.loadDeskData(data.getClass(), deskNumberMappingDataPath);
        mappingData.setMappingData(data);
    }

    /**
     * 从映射文件中查找对应的桌号
     *
     * @param deskLine
     * @param deskColumn
     * @return
     */
    public String findDeskNumberFormMappingFile(String deskLine, String deskColumn) {
        if (mappingData.getMappingData() == null) {
            loadLocalDeskNumberMappingData();
        }
        return mappingData.findDeskNumber(deskLine, deskColumn);
    }

    public void setLuaUrl(String luaUrl) {
        DeskConfig.getInstance().setLuaUrl(luaUrl);
        saveDeskConfig();
    }

    public void setDeviceId(String deviceId) {
        DeskConfig.getInstance().setDeviceId(deviceId);
        saveDeskConfig();
    }


    public void setDeskNumber(String deskNumber) {
        if (!isDeskNumberRight(deskNumber)) {
            return;
        }
        if (deskNumber.contains("-")) {
            Pair<String, String> rc = tryParseDeskNumberToXY(deskNumber);
            if (rc != null) {
                updateXY(rc.first, rc.second);
            }
        } else {
            String XAndY = mappingData.findDeskLineAndColumn(deskNumber);
            Pair<String, String> rc = tryParseDeskNumberToXY(XAndY);
            if (rc == null) {
                rc = new Pair<>("", "");
            }
            DeskConfig.getInstance().setXY(rc.first, rc.second);
            DeskConfig.getInstance().setDeskNumber(deskNumber);
            saveDeskConfig();
        }
    }

    public void updateXY(String row, String column) {
        String deskNumber = findDeskNumberFormMappingFile(row, column);
        if (deskNumber != null) {
            DeskConfig.getInstance().setDeskNumber(deskNumber);
        }
        DeskConfig.getInstance().setXY(row, column);
        saveDeskConfig();
    }


    @Override
    public String toString() {
        return "ConfigManager{" + "deskConfigPath='" + deskConfigPath + '\'' + ", deskNumberMappingDataPath='" + deskNumberMappingDataPath + '\'' + '}';
    }
}
