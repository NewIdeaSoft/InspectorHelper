package com.newideasoft.qualityinspectorhelper;

import java.util.Arrays;

/**
 * Created by NewIdeaSoft on 2016/3/28.
 */
public class FileInfo {
    private String productId;
    private String stationId;
    private String [][] filesName;

    public String getProductId() {
        return productId;
    }

    public String[][] getFilesName() {
        return filesName;
    }

    public String getStationId() {

        return stationId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public void setFilesName(String[][] filesName) {
        this.filesName = filesName;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "productId='" + productId + '\'' +
                ", stationId='" + stationId + '\'' +
                ", filesName=" + Arrays.toString(filesName) +
                '}';
    }

    public FileInfo(String stationId, String productId, String[][] filesName) {
        this.stationId = stationId;
        this.productId = productId;
        this.filesName = filesName;
    }

    public FileInfo() {
    }
}
