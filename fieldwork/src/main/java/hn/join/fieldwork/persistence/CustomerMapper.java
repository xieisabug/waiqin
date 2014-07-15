package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.Customer;

import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 客户DAO
 * 操作表：tbl_customer
 * @author chenjinlong
 *
 */
public interface CustomerMapper {
	/**
	 * 新增客户
	 * @param customer
	 */
	void insertCustomer(Customer customer);

	/*
	 * void insertCustomerJournal(@Param(value = "customerId") Long customerId,
	 * 
	 * @Param(value = "journal") CustomerJournal customerJournal,
	 * 
	 * @Param(value = "operatorId") Long operatorId);
	 */
	/**
	 * 更新客户信息
	 * @param customer
	 */
	void updateCustomer(Customer customer);
	/**
	 * 通过客户ID查找客户
	 * @param customerId
	 * @return
	 */
	Customer getById(Long customerId);

	/* List<CustomerJournal> findJournalByCustomerId(Long customerId); */
	/**
	 * 根据客户ID统计客户
	 * @param customerId
	 * @return
	 */
	Long countById(@Param(value = "customerId") Long customerId);
	/**
	 * 根据区域编码统计
	 * @param areaCode
	 * @return
	 */
	Long countByAreaCode(@Param(value = "areaCode") String areaCode);
	/**
	 * 根据区域编码加载客户列表
	 * @param areaCode
	 * @param idStart
	 * @param limit
	 * @return
	 */
	List<Customer> loadByAreaCode(@Param(value = "areaCode") String areaCode,
                                  @Param(value = "idStart") long idStart,
                                  @Param(value = "limit") int limit);
	/**
	 * 多条件统计统计
	 * @param areaCode
	 * @param customerAddress
	 * @param customerName
	 * @param tels
	 * @return
	 */
	long countBy(@Param(value = "areaCode") String areaCode,
                 @Param(value = "customerAddress") String customerAddress,
                 @Param(value = "customerName") String customerName,
                 @Param(value = "tels") String tels);
	/**
	 * 多条件客户查询
	 * @param areaCode
	 * @param customerAddress
	 * @param customerName
	 * @param tels
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Customer> findBy(@Param(value = "areaCode") String areaCode,
                          @Param(value = "customerAddress") String customerAddress,
                          @Param(value = "customerName") String customerName,
                          @Param(value = "tels") String tels,
                          @Param(value = "offset") Integer offset,
                          @Param(value = "limit") Integer limit);
	/**
	 * 批量更新客户标记
	 * @param customerIdList
	 * @param customerTag
	 */
	void updateCustomerTag(
            @Param(value = "customerIdList") List<Integer> customerIdList,
            @Param(value = "customerTag") Short customerTag);
	/**
	 * 根据经纬度统计客户
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	long countByCoordinate(@Param(value = "latitude") float latitude, @Param(value = "longitude") float longitude);
	/**
	 * 根据经纬度查找客户
	 * @param latitude
	 * @param longitude
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Customer> findByCoordinate(@Param(value = "latitude") float latitude, @Param(value = "longitude") float longitude, @Param(value = "offset") Integer offset,
                                    @Param(value = "limit") int limit);
}
