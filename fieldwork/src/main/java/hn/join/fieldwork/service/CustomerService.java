package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.Customer;
import hn.join.fieldwork.domain.SyncDataTransferObject;
import hn.join.fieldwork.domain.SyncDataTransferObject.DtoType;
import hn.join.fieldwork.persistence.CustomerMapper;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.utils.XmlUtil;
import hn.join.fieldwork.web.dto.CustInfoDto;
import hn.join.fieldwork.web.dto.CustomerDto;
import hn.join.fieldwork.web.dto.OnTerminalReceiveFeedbackDto;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 客户管理服务类
 * @author aisino_lzw
 *
 */
@Service
public class CustomerService {

	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private SystemEventBus systemEventBus;
	
	/**
	 * 根据客户id获取客户信息
	 * @param customerId  客户id
	 * @return 客户信息
	 */
	public Customer getById(Long customerId){
		return customerMapper.getById(customerId);
	}

	/**
	 * 根据区域编码统计客户数
	 * @param areaCode  区域编号
	 * @param customerName
	 * @return 客户数量
	 */
	public long countByAreaCode(String areaCode,String customerName) {
		return customerMapper.countByAreaCode(areaCode);
	}

	/**
	 * 根据区域编码加载客户列表
	 * @param areaCode 区域编号
	 * @param idStart 开始客户编号
	 * @param limit  客户数量
	 * @return 客户列表
	 */
	public List<Customer> loadByAreaCode(String areaCode, Long idStart,
			Integer limit) {
		return customerMapper.loadByAreaCode(areaCode, idStart, limit);
	}

	/**
	 * 多条件统计统计客户数量
	 * @param areaCode  区域编号
	 * @param customerAddress 客户地址
	 * @param customerName 客户名称
	 * @param tels 联系电话
	 * @return 客户数量
	 */
	public long countBy(String areaCode, String customerAddress,
			String customerName, String tels) {
		return customerMapper.countBy(areaCode, customerAddress, customerName,
				tels);
	}

	/**
	 * 条件查询指定客户
	 * @param areaCode 区域编号
	 * @param customerAddress 客户地址
	 * @param customerName 客户名称
	 * @param tels 联系电话
	 * @param offset 指定行
	 * @param limit  行数量
	 * @return 客户列表
	 */
	public List<Customer> findBy(String areaCode, String customerAddress,
			String customerName, String tels, Integer offset, Integer limit) {
		return customerMapper.findBy(areaCode, customerAddress, customerName,
				tels, offset, limit);
	}
	
	/**
	 * 添加客户
	 * @param customer  客户信息
	 */
	@Transactional(rollbackFor=Exception.class)
	public void newCustomer(Customer customer){
		customerMapper.insertCustomer(customer);
	}
	
	/**
	 * 批量更新客户状态
	 * @param customerIdList 客户
	 * @param customerTag 客户状态
	 */
	public void markCustomer(List<Integer> customerIdList,Short customerTag){
		customerMapper.updateCustomerTag(customerIdList, customerTag);
	}
	
	/**
	 * 根据经纬度查找客户
	 * @param latitude 纬度
	 * @param longitude 经度
	 * @param page 指定页
	 * @param rows 指定行
	 * @return 客户列表
	 */
	public SQLQueryResult<Customer> findByCoordinate(float latitude,float longitude,Integer page, Integer rows){
		SQLQueryResult<Customer> queryResult = null;

		long _count = customerMapper.countByCoordinate(latitude, longitude);
		if (_count != 0) {
			Integer offset = null;
			if (page != null && rows != null)
				offset = (page - 1) * rows;
			List<Customer> _result = customerMapper.findByCoordinate(latitude,
					longitude,  offset, rows);
			queryResult = new SQLQueryResult<Customer>(_count, _result);
		} else {
			queryResult = SQLQueryResult.EMPTY_RESULT;
		}
		return queryResult;
	}
	/**
	 * 发送客户基本信息更新同步消息
	 * @version 2014年6月11日 下午7:40:19
	 * @param cust_name
	 * @param cust_tax_code
	 * @param addr
	 * @param contract
	 * @param tel
	 * @param mobile
	 * @throws Exception 
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateCustomerInfo(String cust_name,String cust_tax_code,String addr,String contract,String tel,String mobile) throws Exception {
		SyncDataTransferObject syncObject = createCustomerInfoFeedbackDto(cust_name, cust_tax_code, addr, contract, tel, mobile);
		systemEventBus.postSyncData(syncObject);
	}
	/**
	 * 创建同步对象
	 * 
	 * @param workOrderNo
	 * @param confirmTime
	 * @return
	 * @throws Exception
	 */
	private SyncDataTransferObject createCustomerInfoFeedbackDto(
			String customerName,String tax_code,String customerAddr,String linkPerson,String customerTel,String customerMobile) throws Exception {
		CustInfoDto dtoObject = new CustInfoDto(customerName, tax_code, customerAddr, linkPerson, customerTel, customerMobile);
		String dtoXml = XmlUtil.toXml(dtoObject);
		SyncDataTransferObject syncObject = new SyncDataTransferObject(
				DtoType.cust_info, dtoXml);
		return syncObject;
	}

	

}
