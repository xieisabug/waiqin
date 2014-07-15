package com.sealion.serviceassistant.entity;

/**
 * 工单实体类
 */
public class OrderEntity {
    private int id;

    private long workCardId;    //派工单ID int型(必填)
    private String serviceDate;    //服务日期(格式:yyyyMMddHHmmss) (必填)
    private int workOrderType;    //派工类别,int型 1：直接派工；2：回访派工(必填)
    private long customerId;    //客户ID,int型(必填)
    private String linkPerson;    //客户联系人 ,varchar(64)(必填)
    private String customerName;    //客户公司名称(必填)
    private String taxCode;    //税号(可填)
    private int departmentId;    //所属部门ID,int型(必填)
    private String departmentName;    //所属部门名称,varchar(64)(必填)
    private int revenueId;        //所属税务分局ID,int型(必填)
    private String revenueName;    //所属税务分局名称,varchar(64)(必填)
    private String customerAddr;    //上门地址,varchar(256)(必填)
    private String customerMobile;    //客户手机 varchar(64) (必填)
    private String customerTel;    //客户联系电话 varchar(64) (必填)
    private String customerCounty;    //客户所在区、县,varchar(32)(可填)
    private String customerLatitude;    //客户地址所在纬度,varchar(9)(可填)
    private String customerLongitude;    //客户地址所在经度,varchar(9)(可填)
    private int fieldWorkerId;    //服务人员ID,int型(必填)
    private String fieldWorkerName; //服务人员姓名,varchar(64)(必填)
    private String workOrderDescription; //派工说明,varchar(1000)(可填)
    private String expectArriveTime; //服务人员预计到达时间(格式:yyyyMMddHHmmss)(可填)
    private String productName; //所属产品名称,int型(必填)

    private int chargeType; //是否收费(1:是,2:否) int(必填)
    private String chargeMoney; //收费金额,varchar(10)(可填)
    private int urgency; //工单的紧急程度(1:一般,2:紧急,3:非常紧急),4：外呼工单int(必填)
    private String city; //客户所在城市(城市名称，比如长沙市，湘潭市，湘西自治州) varchar(32)(必填)

    private int order_sign; //工单状态标记（0：未阅读，1:已经阅读，2:接受任务，
    // 3：申请改派，4：改派审批通过，5：改派审批未通过，
    // 6：到达现场，7:电话解决  10：工单完成）
    private int upload_sign; //0：未上传任务，1：任务上传完成
    private int finish_type; //工单完成类型  默认为0， 1：现场解决 ，2：电话解决，3，改派
    private int is_task; //是否拥有任务要上传 0:没有，1：有
    private String receiveOrderTime; //工单接收时间
    private String orderToken; //单据号
    private String visit_type; //回访类型
    private String product_status; //产品状态
    private String handle_result; //处理结果
    private String optimal_path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getWorkCardId() {
        return workCardId;
    }

    public void setWorkCardId(long workCardId) {
        this.workCardId = workCardId;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public int getWorkOrderType() {
        return workOrderType;
    }

    public void setWorkOrderType(int workOrderType) {
        this.workOrderType = workOrderType;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getLinkPerson() {
        return linkPerson;
    }

    public void setLinkPerson(String linkPerson) {
        this.linkPerson = linkPerson;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getRevenueId() {
        return revenueId;
    }

    public void setRevenueId(int revenueId) {
        this.revenueId = revenueId;
    }

    public String getRevenueName() {
        return stringShow(revenueName);
    }

    public void setRevenueName(String revenueName) {
        this.revenueName = revenueName;
    }

    public String getCustomerAddr() {
        return customerAddr;
    }

    public void setCustomerAddr(String customerAddr) {
        this.customerAddr = customerAddr;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    public String getCustomerCounty() {
        return customerCounty;
    }

    public void setCustomerCounty(String customerCounty) {
        this.customerCounty = customerCounty;
    }

    public String getCustomerLatitude() {
        return customerLatitude;
    }

    public void setCustomerLatitude(String customerLatitude) {
        this.customerLatitude = customerLatitude;
    }

    public String getCustomerLongitude() {
        return customerLongitude;
    }

    public void setCustomerLongitude(String customerLongitude) {
        this.customerLongitude = customerLongitude;
    }

    public int getFieldWorkerId() {
        return fieldWorkerId;
    }

    public void setFieldWorkerId(int fieldWorkerId) {
        this.fieldWorkerId = fieldWorkerId;
    }

    public String getFieldWorkerName() {
        return fieldWorkerName;
    }

    public void setFieldWorkerName(String fieldWorkerName) {
        this.fieldWorkerName = fieldWorkerName;
    }

    public String getWorkOrderDescription() {
        return stringShow(workOrderDescription);
    }

    public void setWorkOrderDescription(String workOrderDescription) {
        this.workOrderDescription = workOrderDescription;
    }

    public String getExpectArriveTime() {
        return expectArriveTime;
    }

    public void setExpectArriveTime(String expectArriveTime) {
        this.expectArriveTime = expectArriveTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getChargeType() {
        return chargeType;
    }

    public void setChargeType(int chargeType) {
        this.chargeType = chargeType;
    }

    public String getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(String chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getOrder_sign() {
        return order_sign;
    }

    public void setOrder_sign(int order_sign) {
        this.order_sign = order_sign;
    }

    public int getUpload_sign() {
        return upload_sign;
    }

    public void setUpload_sign(int upload_sign) {
        this.upload_sign = upload_sign;
    }

    public int getFinish_type() {
        return finish_type;
    }

    public void setFinish_type(int finish_type) {
        this.finish_type = finish_type;
    }

    public int getIs_task() {
        return is_task;
    }

    public void setIs_task(int is_task) {
        this.is_task = is_task;
    }

    public String getReceiveOrderTime() {
        return receiveOrderTime;
    }

    public void setReceiveOrderTime(String receiveOrderTime) {
        this.receiveOrderTime = receiveOrderTime;
    }

    public String getOrderToken() {
        return orderToken;
    }

    public void setOrderToken(String orderToken) {
        this.orderToken = orderToken;
    }

    public String getVisit_type() {
        return visit_type;
    }

    public void setVisit_type(String visit_type) {
        this.visit_type = visit_type;
    }

    public String getProduct_status() {
        return product_status;
    }

    public void setProduct_status(String product_status) {
        this.product_status = product_status;
    }

    public String getHandle_result() {
        return handle_result;
    }

    public void setHandle_result(String handle_result) {
        this.handle_result = handle_result;
    }

    public String getOptimal_path() {
        return optimal_path;
    }

    public void setOptimal_path(String optimal_path) {
        this.optimal_path = optimal_path;
    }

    private String stringShow(String s) {
        if (s == null || s.equalsIgnoreCase("null"))
            return "";
        else
            return s;
    }
}
