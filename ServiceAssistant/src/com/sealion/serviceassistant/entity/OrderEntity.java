package com.sealion.serviceassistant.entity;

/**
 * ����ʵ����
 */
public class OrderEntity {
    private int id;

    private long workCardId;    //�ɹ���ID int��(����)
    private String serviceDate;    //��������(��ʽ:yyyyMMddHHmmss) (����)
    private int workOrderType;    //�ɹ����,int�� 1��ֱ���ɹ���2���ط��ɹ�(����)
    private long customerId;    //�ͻ�ID,int��(����)
    private String linkPerson;    //�ͻ���ϵ�� ,varchar(64)(����)
    private String customerName;    //�ͻ���˾����(����)
    private String taxCode;    //˰��(����)
    private int departmentId;    //��������ID,int��(����)
    private String departmentName;    //������������,varchar(64)(����)
    private int revenueId;        //����˰��־�ID,int��(����)
    private String revenueName;    //����˰��־�����,varchar(64)(����)
    private String customerAddr;    //���ŵ�ַ,varchar(256)(����)
    private String customerMobile;    //�ͻ��ֻ� varchar(64) (����)
    private String customerTel;    //�ͻ���ϵ�绰 varchar(64) (����)
    private String customerCounty;    //�ͻ�����������,varchar(32)(����)
    private String customerLatitude;    //�ͻ���ַ����γ��,varchar(9)(����)
    private String customerLongitude;    //�ͻ���ַ���ھ���,varchar(9)(����)
    private int fieldWorkerId;    //������ԱID,int��(����)
    private String fieldWorkerName; //������Ա����,varchar(64)(����)
    private String workOrderDescription; //�ɹ�˵��,varchar(1000)(����)
    private String expectArriveTime; //������ԱԤ�Ƶ���ʱ��(��ʽ:yyyyMMddHHmmss)(����)
    private String productName; //������Ʒ����,int��(����)

    private int chargeType; //�Ƿ��շ�(1:��,2:��) int(����)
    private String chargeMoney; //�շѽ��,varchar(10)(����)
    private int urgency; //�����Ľ����̶�(1:һ��,2:����,3:�ǳ�����),4���������int(����)
    private String city; //�ͻ����ڳ���(�������ƣ����糤ɳ�У���̶�У�����������) varchar(32)(����)

    private int order_sign; //����״̬��ǣ�0��δ�Ķ���1:�Ѿ��Ķ���2:��������
    // 3��������ɣ�4����������ͨ����5����������δͨ����
    // 6�������ֳ���7:�绰���  10��������ɣ�
    private int upload_sign; //0��δ�ϴ�����1�������ϴ����
    private int finish_type; //�����������  Ĭ��Ϊ0�� 1���ֳ���� ��2���绰�����3������
    private int is_task; //�Ƿ�ӵ������Ҫ�ϴ� 0:û�У�1����
    private String receiveOrderTime; //��������ʱ��
    private String orderToken; //���ݺ�
    private String visit_type; //�ط�����
    private String product_status; //��Ʒ״̬
    private String handle_result; //������
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
