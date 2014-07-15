package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.SyncDataTransferObject;

import java.util.List;
/**
 * 数据异步发送序列表DAO
 * 操作表：tbl_sync_dto
 * @author chenjinlong
 *
 */
public interface SyncDataTransferObjectMapper {
	/**
	 * 新增需要发送数据
	 * @param syncDto
	 */
	public void insertSyncDataTransferObject(SyncDataTransferObject syncDto);
	/**
	 * 查询发送数据
	 * @return
	 */
	public List<SyncDataTransferObject> findDataTransferObjectForSync();
	/**
	 * 更新正在发送的数据
	 * @param idList
	 */
	public void updateOnSync(List<Integer> idList);
	/**
	 * 修改发送成功数据标识
	 * @param syncId
	 */
	public void updateOnSucc(Integer syncId);
	/**
	 * 删除过期数据
	 */
	public void deleteStaleData();

}
