package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.ProblemCategory;
import hn.join.fieldwork.persistence.ProblemCategoryMapper;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.SystemEventBus.DataUpdateRequest;
import hn.join.fieldwork.service.SystemEventBus.DataUpdateRequest.DataType;
import hn.join.fieldwork.utils.FileUtil;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SystemConstants;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 问题分类
 * @author aisino_lzw
 *
 */
@Service
public class ProblemCategoryService {

	
	@Autowired
	private ProblemCategoryMapper problemCategoryMapper;
	
	@Autowired
	private SystemEventBus systemEventBus;
	
	
	/**
	 * 新建问题分类
	 * @param problemCategoryId 问题类别id
	 * @param problemCategoryName 问题类别名称
	 * @param productId 产品id
	 * @param productName 产品名称
	 */
	@Transactional(rollbackFor=Exception.class)
	public void newProblemCategory(Integer problemCategoryId,String problemCategoryName,Integer productId,String productName){
		ProblemCategory _problemCategory=new ProblemCategory();
		_problemCategory.setId(problemCategoryId);
		_problemCategory.setName(problemCategoryName);
		_problemCategory.setProductId(productId);
		_problemCategory.setProductName(productName);
		problemCategoryMapper.insertProblemCategory(_problemCategory);		
		systemEventBus.postDataUpdate(new DataUpdateRequest(DataType.problem_category,new Date()));
	}
	
	
	/**
	 * 删除问题分类
	 * @param problemCategoryId
	 */
	@Transactional(rollbackFor=Exception.class)
	public void removeProblemCategory(Integer problemCategoryId){
		problemCategoryMapper.deleteProblemCategory(problemCategoryId);
		systemEventBus.postDataUpdate(new DataUpdateRequest(DataType.problem_category,new Date()));
	}
	
	
	/**
	 * 条件查询问题分类 
	 * @param problemCategoryName  问题类别名称
	 * @param page 指定页
	 * @param rows 指定行数
	 * @return
	 */
	public SQLQueryResult<ProblemCategory> findBy(String problemCategoryName,Integer page,Integer rows){
		SQLQueryResult<ProblemCategory> queryResult=null;
		long _count=problemCategoryMapper.countBy(problemCategoryName);
		if(_count!=0){
			Integer offset=0;
			if(page!=null && rows!=null)
				offset=(page-1)*rows;
			List<ProblemCategory> _result=problemCategoryMapper.findBy(problemCategoryName, offset, rows);
			queryResult=new SQLQueryResult<ProblemCategory>(_count,_result);
		}else{
			queryResult= SQLQueryResult.EMPTY_RESULT;
		}
		return queryResult;
	}
	
	
}
