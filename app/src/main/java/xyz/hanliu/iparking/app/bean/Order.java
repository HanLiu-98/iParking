package xyz.hanliu.iparking.app.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @author HanLiu
 * @create 2020-03-18-17:47
 * @blogip 47.110.70.206
 */
public class Order {
    /*取消id字段，通过 空闲车位编号&&租户手机号 就已经可以唯一标识一个订单*/
    //private String id;
    private int spaceId;
    private String tenantMobile;
    /*
     * FastJSON的注解，方便Date类型转JSON字符串出错
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date payTime;
    private int status;

    /*
     *getter方法
     */

    public int getSpaceId() {
        return spaceId;
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
    /*
     *setter方法
     */

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
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
    /*
     *toString方法
     */

    @Override
    public String toString() {
        return "Order{" +
                "spaceId=" + spaceId +
                ", tenantMobile='" + tenantMobile + '\'' +
                ", createTime=" + createTime +
                ", payTime=" + payTime +
                ", status=" + status +
                '}';
    }
}
