package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.Sequence;
/**
 * 序列操作DAO
 * 操作表：sequence
 * @author chenjinlong
 *
 */
public interface SequenceMapper {
	/**
	 * 新增序列	
	 * @param seq
	 */
	public void insertSequence(Sequence seq);
	/**
	 * 获取序列的下一个值	
	 * @param seqName
	 * @return
	 */
	public Integer getNextvalBySeq(String seqName);

}
