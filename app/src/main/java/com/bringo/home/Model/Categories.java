package com.bringo.home.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Categories {

//    @SerializedName("categories")
//    @Expose
//    private Data data;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("m_name")
    @Expose
    private String m_name;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("desc")
    @Expose
    private String desc;


    @SerializedName("service")
    @Expose
    private String service;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("created_at")
    @Expose
    private String created_at;

//    public Data getData() {
//        return data;
//    }
//
//    public void setData(Data data) {
//        this.data = data;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

//    public Categories(String id, String m_name, String status, String desc, String service, String thumbnail, String created_at, String updated_at) {
//       // this.data = data;
//        this.id = id;
//        this.m_name = m_name;
//        this.status = status;
//        this.desc = desc;
//        this.service = service;
//        this.thumbnail = thumbnail;
//        this.created_at = created_at;
//        this.updated_at = updated_at;
//    }
}
