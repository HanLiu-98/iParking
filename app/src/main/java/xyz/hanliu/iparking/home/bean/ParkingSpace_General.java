package xyz.hanliu.iparking.home.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class ParkingSpace_General {

    private int id;
    private String positionDetail;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date startTime;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date endTime;
    private float price;
    private String imagePath;


    /*
     *getter方法
     */

    public int getId() {
        return id;
    }

    public String getPositionDetail() {
        return positionDetail;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public float getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }
    /*
     *setter方法
     */

    public void setId(int id) {
        this.id = id;
    }

    public void setPositionDetail(String positionDetail) {
        this.positionDetail = positionDetail;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /*
     *toString方法
     */

    @Override
    public String toString() {
        return "ParkingSpace_General{" +
                "id=" + id +
                ", positionDetail='" + positionDetail + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", price=" + price +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
