package com.df.lib_config;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;

@Keep
public class DeskConfig {
    @Expose
    private String deskNumber = "-1";

    @Expose
    private String deskLine;

    @Expose
    private String deskColumn;

    @Expose
    private String deviceId;

    @Expose
    private String location;

    @Expose
    private String luaUrl;


    @Expose(serialize = false, deserialize = false)
    private static DeskConfig instance;

    public static DeskConfig getInstance() {
        if (instance == null) {
            instance = new DeskConfig();
        }
        return instance;
    }

    private DeskConfig() {
    }

    void update(DeskConfig config) {
        instance = config;
    }

    public String getDeskNumber() {
        return deskNumber;
    }

    public void setDeskNumber(String deskNumber) {
        this.deskNumber = deskNumber;
    }

    public String getDeskLine() {
        return deskLine;
    }

    public void setDeskLine(String deskLine) {
        this.deskLine = deskLine;
    }

    public String getDeskColumn() {
        return deskColumn;
    }

    public void setDeskColumn(String deskColumn) {
        this.deskColumn = deskColumn;
    }


    public void setXY(String row, String column) {
        this.deskLine = row;
        this.deskColumn = column;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLuaUrl() {
        return luaUrl;
    }

    public void setLuaUrl(String luaUrl) {
        this.luaUrl = luaUrl;
    }

    @Override
    public String toString() {
        return "DeskConfig{" +
                "deskNumber='" + deskNumber + '\'' +
                ", deskLine='" + deskLine + '\'' +
                ", deskColumn='" + deskColumn + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", location='" + location + '\'' +
                ", luaUrl='" + luaUrl + '\'' +
                '}';
    }
}