package com.sealion.serviceassistant.entity;

/**
 * 电话解决实例
 */
public class PhoneComplishEntity
{
	private int id;
	private String order_num; //工单号
	private String q_describe; //问题描述
	private String q_solve; //问题解决
	private String solve_time; //解决时间
	private int is_create_new_order; //是否生成新工单，0:否，1：是
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getOrder_num()
	{
		return order_num;
	}
	public void setOrder_num(String order_num)
	{
		this.order_num = order_num;
	}
	public String getQ_describe()
	{
		return q_describe;
	}
	public void setQ_describe(String q_describe)
	{
		this.q_describe = q_describe;
	}
	public String getQ_solve()
	{
		return q_solve;
	}
	public void setQ_solve(String q_solve)
	{
		this.q_solve = q_solve;
	}
	public String getSolve_time()
	{
		return solve_time;
	}
	public void setSolve_time(String solve_time)
	{
		this.solve_time = solve_time;
	}
	public int getIs_create_new_order()
	{
		return is_create_new_order;
	}
	public void setIs_create_new_order(int is_create_new_order)
	{
		this.is_create_new_order = is_create_new_order;
	}
	
	
}
