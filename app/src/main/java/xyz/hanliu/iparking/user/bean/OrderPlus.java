package xyz.hanliu.iparking.user.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class OrderPlus {
    private int id;
    private String area;
    private String positionDetail;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date startTime;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date endTime;

    private float price;

    private String tenantMobile;
    /*
     * FastJSON的注解，方便Date类型转JSON字符串出错
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date payTime;
    private int status;

    /**
     * getter方法
     */
    public int getId() {
        return id;
    }

    public String getArea() {
        return area;
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

    public String getTenantMobile() {
        return tenantMobile;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public int getStatus() {
        return status;
    }

    public float getPrice() {
        return price;
    }

    /**
     * setter方法
     */
    public void setId(int id) {
        this.id = id;
    }

    public void setArea(String area) {
        this.area = area;
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

    public void setTenantMobile(String tenantMobile) {
        this.tenantMobile = tenantMobile;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
