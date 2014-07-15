package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.ProblemType;
import hn.join.fieldwork.persistence.ProblemTypeMapper;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.SystemEventBus.DataUpdateRequest;
import hn.join.fieldwork.service.SystemEventBus.DataUpdateRequest.DataType;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 问题类别
 * @author aisino_lzw
 *
 */
@Service
public class ProblemTypeService {

	@Autowired
	private ProblemTypeMapper problemTypeMapper;
	
	@Autowired
	private SystemEventBus systemEventBus;

	
	/**
	 * 新建问题类别
	 * @param problemTypeId 
	 * @param problemTypeName
	 * @param problemCategoryId
	 */
	@Transactional(rollbackFor=Exception.class)
	public void newProblemType(Integer problemTypeId,String problemTypeName,Integer problemCategoryId){
		ProblemType _problemType=new ProblemType();
		_problemType.setId(problemTypeId);
		_problemType.setName(problemTypeName);
		problemTypeMapper.insertProblemType(_problemType, problemCategoryId);
		systemEventBus.postDataUpdate(new DataUpdateRequest(DataType.problem_type,new Date()));
	}
	
	/**
	 * 删除问题类别
	 * @param problemTypeId
	 */
	@Transactional(rollbackFor=Exception.class)
	public void removeProblemType(Integer problemTypeId){
		problemTypeMapper.deleteProblemType(problemTypeId);
		systemEventBus.postDataUpdate(new DataUpdateRequest(DataType.problem_type,new Date()));
	}
	
	/**
	 * 条件查询问题类别
	 * @param problemTypeName
	 * @param page
	 * @param rows
	 * @return
	 */
	public SQLQueryResult<ProblemType> findBy(String problemTypeName,Integer page,Integer rows){
		SQLQueryResult<ProblemType> queryResult=null;
		long _count=problemTypeMapper.countBy(problemTypeName);
		if(_count!=0){
			Integer offset=(page-1)*rows;
			List<ProblemType> _result=problemTypeMapper.findBy(problemTypeName, offset, rows);
			queryResult=new SQLQueryResult(_count,_result);
		}else{
			queryResult= SQLQueryResult.EMPTY_RESULT;
		}
		return queryResult;
	}
}
