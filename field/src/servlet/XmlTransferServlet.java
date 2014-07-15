package servlet;

import org.dom4j.*;

import quartz.QuartzManager;
import service.DbService;
import xmltransfer.TestJob;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class XmlTransferServlet extends HttpServlet {

    private static DbService dbService;
    private static final long serialVersionUID = 1L;

    public void init() throws ServletException {
        dbService = new DbService();
        TestJob quartzJob2 = new TestJob();
        try {
            QuartzManager.addJob("job_11", quartzJob2, "0 0/1 * * * ?");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("XmlTransferServlet visit");
        //DbSvr dbSvr = DbSvr.getDbService("local");
        if (null != request.getParameter("flag")
                && !request.getParameter("flag").equals("")) {

            // 接受中转Servlet的response值
            BufferedInputStream in = new BufferedInputStream(request
                    .getInputStream());

            StringBuilder responseBuffer = new StringBuilder();
            String readLine;

            // 处理响应流，必须与服务器响应流输出的编码一致
            BufferedReader responseReader = new BufferedReader(
                    new InputStreamReader(in, "utf-8"));

            while ((readLine = responseReader.readLine()) != null) {
                responseBuffer.append(readLine).append("\n");
            }

            //读取输入流的信息
            System.out.println("Http Response:" + responseBuffer.toString());

            responseReader.close();

            //a:阅读时间XML
            if (request.getParameter("flag").equals("a")) {
                boolean readTimeFlag = parseReadXml(responseBuffer.toString());
                System.out.println("readTimeFlag：" + readTimeFlag);
                if (readTimeFlag) {
                    //回执--成功
                    response.getWriter().write("1");
                } else {
                    //回执--失败
                    response.getWriter().write("2");
                }
            }
            //接收时间XML--保留
            else if (request.getParameter("flag").equals("b")) {
                boolean receiveTime = parseReceiveTimeXml(responseBuffer.toString());
                if (receiveTime) {
                    //回执--成功
                    response.getWriter().write("1");
                } else {
                    //回执--失败
                    response.getWriter().write("2");
                }
            }
            //申请改派XML
            else if (request.getParameter("flag").equals("c")) {
                boolean result = parseChangeXml(responseBuffer.toString());
                if (result) {
                    //回执--成功
                    response.getWriter().write("1");
                } else {
                    //回执--失败
                    response.getWriter().write("2");
                }
            }
            //联系客户时间XML
            else if (request.getParameter("flag").equals("d")) {
                boolean result = parseCallCustomerXml(responseBuffer.toString());
                if (result) {
                    //回执--成功
                    response.getWriter().write("1");
                } else {
                    //回执--失败
                    response.getWriter().write("2");
                }
            }
            //电话解决XML
            else if (request.getParameter("flag").equals("e")) {
                boolean result = parseTelSoluteXml(responseBuffer.toString());
                if (result) {
                    //回执--成功
                    response.getWriter().write("1");
                } else {
                    //回执--失败
                    response.getWriter().write("2");
                }
            }
            //到达现场XML--保留
            else if (request.getParameter("flag").equals("f")) {
                boolean arrivedTimeFlag = parseArrivedTimeXml(responseBuffer.toString());
                if (arrivedTimeFlag) {
                    //回执--成功
                    response.getWriter().write("1");
                } else {
                    //回执--失败
                    response.getWriter().write("2");
                }
            }
            //完成工单XML
            else if (request.getParameter("flag").equals("g")) {
                System.out.println("完成工单");
            }
            //服务回单XML
            else if (request.getParameter("flag").equals("h")) {
                boolean serviceOrderFlag = parseServiceOrderXml(responseBuffer.toString());

                if (serviceOrderFlag) {
                    //回执--成功
                    response.getWriter().write("1");
                } else {
                    //回执--失败
                    response.getWriter().write("2");
                }
            }
            //修改客户信息XML
            else if (request.getParameter("flag").equals("i")) {
                boolean updateCustFlag = parseCustInfoXml(responseBuffer.toString());
                if (updateCustFlag) {
                    //回执--成功
                    response.getWriter().write("1");
                } else {
                    //回执--成功
                    response.getWriter().write("2");
                }
            }
        }
    }


    /**
     * 派工单阅读时间XML解析并更新派工单阅读时间
     *
     * @param xmlContent xml正文
     * @return 读取是否成功
     */
    public boolean parseReadXml(String xmlContent) {
        String workCardId = "";

        String readTime = "";

        boolean updateFlag = false;
        try {
            Document document = DocumentHelper.parseText(xmlContent);
            // 获取根元素
            Element root = document.getRootElement();

            // 获取所有子元素，遍历
            for (Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element child = (Element) i.next();

                // 找到workCard_id标签
                if (child.getQName().getName().equals("workCard_id")) {
                    //得到XML派工单ID
                    workCardId = child.getTextTrim().trim();
                    System.out.println("workCardId:" + child.getTextTrim());
                }

                // 找到read_time标签
                if (child.getQName().getName().equals("read_time")) {
                    //得到XML阅读工单时间
                    readTime = child.getTextTrim().trim();
                    System.out.println("read_time:" + child.getTextTrim());
                }
            }

            //判断派工单ID和阅读时间
            if (!workCardId.trim().equals("") && !readTime.trim().equals("")) {
//				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//				Date date = null;
//				try {
//					date = (Date) sdf.parse(readTime);
//				} catch (ParseException e) {					
//					e.printStackTrace();
//				}
                //TODO:根据派工单ID更新派工单阅读时间
                //DbService dbService = new DbService();
                //自定义字符安MYDATWE1
                String sql = "UPDATE SM_DISPATCH SET USER_DEFINE01=? WHERE DISPATCH_ID=" + workCardId;
                int result = DbService.updateSm_dispatch(sql, readTime);
                if (result > 0) {
                    updateFlag = true;
                }
            }

            // 返回
            return updateFlag;
        } catch (DocumentException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * 派工单接收时间XML解析 并更新派工单接收时间----保留
     *
     * @param xmlContent
     * @return
     */
    public boolean parseReceiveTimeXml(String xmlContent) {
        boolean updateFlag = false;

        String dispatchId = "";

        String receiveTime = "";

        try {
            Document document = DocumentHelper.parseText(xmlContent);
            // 获取根元素
            Element root = document.getRootElement();

            // 获取所有子元素，遍历
            for (Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element child = (Element) i.next();

                // 找到workCard_id标签
                if (child.getQName().getName().equals("workCard_id")) {
                    //得到XML派工单ID
                    dispatchId = child.getTextTrim().trim();
                }
                // 找到receive_time标签
                if (child.getQName().getName().equals("receive_time")) {
                    //得到XML阅读工单时间
                    receiveTime = child.getTextTrim().trim();
                }
            }
            //判断派工单ID和阅读时间
            if (!dispatchId.trim().equals("") && !receiveTime.trim().equals("")) {
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = null;
                try {
                    date = sdf.parse(receiveTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //TODO:根据派工单ID更新派工单阅读时间

                //DbService dbService = new DbService();
                //自定义字符安MYDATWE1
                String sql = "UPDATE SM_DISPATCH SET USER_DEFINE02=?,USER_DEFINE20='1' WHERE DISPATCH_ID=" + dispatchId;
                int result = DbService.updateSm_dispatch(sql, receiveTime);
                if (result > 0) {
                    updateFlag = true;
                }
            }
            return updateFlag;
        } catch (DocumentException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * 到达客户现场XML解析 并更新派工单到达时间-----保留
     *
     * @param xmlContent
     * @return
     */
    public boolean parseArrivedTimeXml(String xmlContent) {
        boolean updateFlag = false;

        String dispatchId = "";

        String arrivedTime = "";

        try {
            Document document = DocumentHelper.parseText(xmlContent);
            // 获取根元素
            Element root = document.getRootElement();

            // 获取所有子元素，遍历
            for (Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element child = (Element) i.next();

                // 找到workCard_id标签
                if (child.getQName().getName().equals("workCard_id")) {
                    //得到XML派工单ID
                    dispatchId = child.getTextTrim().trim();

                }

                // 找到arrived_time标签
                if (child.getQName().getName().equals("arrived_time")) {
                    //得到XML阅读工单时间
                    arrivedTime = child.getTextTrim().trim();

                }
            }

            //判断派工单ID和阅读时间
            if (!dispatchId.trim().equals("") && !arrivedTime.trim().equals("")) {
                //根据派工单ID更新派工单阅读时间
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = null;
                try {
                    date = sdf.parse(arrivedTime);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //TODO:根据派工单ID更新派工单阅读时间

                //DbService dbService = new DbService();
                //自定义字符安MYDATWE1
                String sql = "UPDATE SM_DISPATCH SET USER_DEFINE03=? WHERE DISPATCH_ID=" + dispatchId;
                int result = DbService.updateSm_dispatch(sql, arrivedTime);
                if (result > 0) {
                    updateFlag = true;
                }

            }

            // 返回
            return updateFlag;
        } catch (DocumentException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * 派工单申请改派XML解析  并更新申请改派时间，改派原因
     *
     * @param xmlContent
     * @return
     */
    public boolean parseChangeXml(String xmlContent) {
        boolean updateFlag = false;

        String workCardId = "";

        String changeReason = "";

        String changeTime = "";

        try {
            Document document = DocumentHelper.parseText(xmlContent);
            // 获取根元素
            Element root = document.getRootElement();

            // 获取所有子元素，遍历
            for (Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element child = (Element) i.next();

                // 找到workCard_id标签
                if (child.getQName().getName().equals("workCard_id")) {
                    //得到XML派工单ID
                    workCardId = child.getTextTrim().trim();
                    System.out.println("workCardId:" + child.getTextTrim());
                }

                // 找到reason标签
                if (child.getQName().getName().equals("reason")) {
                    //得到XML阅读工单时间
                    changeReason = child.getTextTrim().trim();
                    System.out.println("reason:" + child.getTextTrim());
                }

                // 找到reason标签
                if (child.getQName().getName().equals("change_time")) {
                    //得到XML阅读工单时间
                    changeTime = child.getTextTrim().trim();
                    System.out.println("reason:" + child.getTextTrim());
                }
            }

            //判断派工单ID和阅读时间
            if (!workCardId.trim().equals("") && !changeReason.trim().equals("")) {
                //根据派工单ID更新派工单阅读时间
                //DbService dbService = new DbService();
                //自定义字符安USER_DEFIN01
                String sql = "UPDATE SM_DISPATCH SET DISPATCH_NOTE=?,USER_DEFINE04=?,USER_DEFINE19='1' WHERE DISPATCH_ID=" + workCardId;
                int result = DbService.updateChangeDispatch(sql, changeReason, changeTime);
                if (result > 0) {
                    updateFlag = true;
                }
            } else {
                return false;
            }

            // 返回
            return updateFlag;
        } catch (DocumentException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * 联系客户XML解析 并更新派工单联系客户时间
     *
     * @param xmlContent
     * @return
     */
    public boolean parseCallCustomerXml(String xmlContent) {
        boolean updateFlag = false;

        String workCardId = "";

        String callCustomerTime = "";

        try {
            Document document = DocumentHelper.parseText(xmlContent);
            // 获取根元素
            Element root = document.getRootElement();

            // 获取所有子元素，遍历
            for (Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element child = (Element) i.next();

                // 找到workCard_id标签
                if (child.getQName().getName().equals("workCard_id")) {
                    //得到XML派工单ID
                    workCardId = child.getTextTrim().trim();
                    System.out.println("workCardId:" + child.getTextTrim());
                }

                // 找到call_customer_time标签
                if (child.getQName().getName().equals("call_customer_time")) {
                    //得到XML阅读工单时间
                    callCustomerTime = child.getTextTrim().trim();
                    System.out.println("callCustomerTime:" + child.getTextTrim());
                }
            }

            //判断派工单ID和阅读时间
            if (workCardId != null && !workCardId.trim().equals("")
                    && callCustomerTime != null && !callCustomerTime.trim().equals("")) {
                //根据派工单ID更新派工单阅读时间
                //DbService dbService = new DbService();
                //自定义字符安USER_DEFIN01
                String sql = "UPDATE SM_DISPATCH SET USER_DEFINE05=? WHERE DISPATCH_ID=" + workCardId;
                int result = dbService.updateSm_dispatch(sql, callCustomerTime);
                if (result > 0) {
                    updateFlag = true;
                }
            } else {
                return updateFlag;
            }

            // 返回
            return updateFlag;
        } catch (DocumentException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * 电话解决XML解析  并更新派工单状态，热线电话单状态
     *
     * @param xmlContent
     * @return
     */
    public boolean parseTelSoluteXml(String xmlContent) {
        boolean updateFlag = false;

        String workCardId = "";

//        List srvQuestionInfoList = new ArrayList();

        try {
            Document document = DocumentHelper.parseText(xmlContent);
            // 获取根元素
            Element root = document.getRootElement();

            // 获取所有子元素，遍历
            for (Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element child = (Element) i.next();

                // 找到head标签
                if (child.getQName().getName().equals("head")) {
                    for (Iterator i1 = child.elementIterator(); i1.hasNext(); ) {
                        Element workCardIdChild = (Element) i1.next();
                        //得到workCardId
                        workCardId = workCardIdChild.getTextTrim();
                        System.out.println("workCardId:" + workCardId);
                    }
                }

                // 找到question_body标签
                if (child.getQName().getName().equals("question_body")) {
                    //往下遍历一层
                    for (Iterator i1 = child.elementIterator(); i1.hasNext(); ) {
                        Element questionChild = (Element) i1.next();

//						SrvQuestionInfo srvQuestionInfo =  new SrvQuestionInfo();

                        //找到problem标签
                        if (questionChild.getQName().getName().equals("problem")) {
                            String questionId = null;
                            String questionTypId = null;
                            String questionTypPId;
                            String questionDesc = null;
                            String solutionMethod = null;
                            //在往下遍历一层
                            for (Iterator i21 = questionChild.elementIterator(); i21.hasNext(); ) {
                                Element question = (Element) i21.next();
                                //找到 question_id标签
                                if (question.getQName().getName().equals("question_id")) {
                                    questionId = question.getTextTrim();
                                    //srvQuestionInfo.setId(new Integer(questionId));
                                    System.out.println("question_id:" + question.getTextTrim());
                                }
                                //找到 question_typ_id标签
                                else if (question.getQName().getName().equals("question_typ_id")) {
                                    questionTypId = question.getTextTrim();
                                    //srvQuestionInfo.setQuestionPTypId(new Integer(questionTypId));
                                    System.out.println("question_typ_id:" + question.getTextTrim());
                                }
                                //找到 question_typ_p_id标签
                                else if (question.getQName().getName().equals("question_typ_p_id")) {
                                    questionTypPId = question.getTextTrim();
                                    //srvQuestionInfo.setQuestionPTypId(new Integer(questionTypPId));
                                    System.out.println("question_typ_p_id:" + question.getTextTrim());
                                }
                                //找到question_desc 标签
                                else if (question.getQName().getName().equals("question_desc")) {
                                    questionDesc = question.getTextTrim();
                                    //srvQuestionInfo.setQuestionDesc(questionDesc);
                                    System.out.println("question_desc:" + question.getTextTrim());
                                }
                                //找到 solution_method标签
                                else if (question.getQName().getName().equals("solution_method")) {
                                    solutionMethod = question.getTextTrim();
                                    //srvQuestionInfo.setSolutionMethod(solutionMethod);
                                    System.out.println("solution_method:" + question.getTextTrim());
                                }
                            }
                            String sql = "UPDATE SM_TEL_RECORD_QUESTION t SET "
                                    + "t.MAINTAIN_QUESTION_CONTENT ='" + questionDesc + "'"
                                    + ",t.MAINTAIN_QUESTION_ANSWER='" + solutionMethod + "'"
                                    + ",t.QUESTION_TYPE_ID='" + questionTypId + "'"
                                    + " WHERE t.question_id=?";

                            //DbService dbSer = new DbService();
                            DbService.updateSm_tel_record_question(sql, questionId);
                            //更新SM_TEL_RECORD_QUESTION表设置问题已解决
                            String questionSql = "UPDATE SM_TEL_RECORD_QUESTION SET QUESTION_STATUS = '1' WHERE DISPATCH_ID = ?";
                            DbService.updateSm_tel_record_question(questionSql, workCardId);
                        }
                        //srvQuestionInfoList.add(srvQuestionInfo);
                    }
                }
            }

            //判断派工单ID和阅读时间
            if (workCardId != null && !workCardId.trim().equals("")) {
                String sql = "UPDATE SM_DISPATCH t set t.IS_TEL_SUCESS = '1' where t.DISPATCH_ID=?";
                try {
                    int count = DbService.updateSm_tel_record_question(sql, workCardId);
                    if (count > 0) {
                        updateFlag = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //根据派工单ID更新派工单阅读时间
                /*DbService dbService = new DbService();
                //自定义字符安USER_DEFIN01
				String sql = "UPDATE SM_DISPATCH SET USER_DEFIN01=? WHERE DISPATCH_ID=" + workCardId; 
				int result = dbService.updateSm_dispatch(sql, callCustomerTime);
				if(result > 0){
					updateFlag = true;
				}*/
                //updateFlag = workCardOrderManager.updateTelSolute(workCardId,srvQuestionInfoList);
            } else {
                return false;
            }
            //上门维护单
            String sql1 = "UPDATE SM_MAINTAIN SET "
                    + " IS_RESOLVE= '1'"   //设置问题已解决
                    + " WHERE DISPATCH_ID=?";
            System.out.println("维护sql:" + sql1);
            DbService.updateSm_tel_record_question(sql1, workCardId);

            String sql = "UPDATE SM_DISPATCH t SET "
                    + " t.DISPATCH_STATUS_ID=9 "
                    + " WHERE t.DISPATCH_ID=?";

            //DbService dbSer = new DbService();
            DbService.updateSm_tel_record_question(sql, workCardId);
            // 返回
            return updateFlag;
        } catch (DocumentException e) {
            e.printStackTrace();

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 服务回单XML解析，并保存更新数据库
     *
     * @param xmlContent
     * @return
     */
    public boolean parseServiceOrderXml(String xmlContent) {
        boolean updateFlag = false;
        String seviceDate1 = "";

        String ArriveDate = "";  //到达时间
        String ServiceEndDate = ""; //服务结束时间

        String workCardId = "";//单据ID
        Date seviceDate = null;//服务日期
        Date arriveDate = null;//到达日期
        Date serviceEndDate = null;//服务完成时间
        String OrderCode = "";//纸制单据号
        String satisfied = "";//是否满意
        String result = "";//处理结果
        String customerSignature = "";//客户签名
        String longitude = "";//定义客户地址经度
        String latitude = "";//定义客户地址维度
        String customerUpdatFlag = "";//定义是否更新客户信息标志
        String orderTyp = "";//定义单据类型
        String spendingProcess = "";//定义费用过程
        //客户联系电话
        String Tel = "";
        //客户联系人
        String LinkPerson = "";
        //客户上门地址
        String CustomerAddr = "";
        //客户手机
        String Mobile = "";
        //备注
        String Notes = "";
        //产品运行状态
        String ProductCondition = "";
        //处理结果
        String handleResult = "";
        //硬件型号
        String hardwareCode = "";
        //软件版本
        String softwareVersion = "";
        //使用环境
        String environment = "";
        //服务总体评价
        String customer_if_satisfied_service = "";
        //产品的总体评价
        String customer_if_satisfied_product = "";
        //回访类型
        String visitType = "1";


        String isCharge = "";
        String isChargeValue = "";

        SimpleDateFormat timeformatD = new SimpleDateFormat("yyyy-MM-dd");
        List spendingList = new ArrayList();//定义费用LIST
        try {
            Document document = DocumentHelper.parseText(xmlContent);
            Element root = document.getRootElement();// 获取根元素
            for (Iterator i = root.elementIterator(); i.hasNext(); )// 获取所有子元素，遍历
            {
                Element child = (Element) i.next();

                // 找到service_head标签
                if (child.getQName().getName().equals("service_head")) {
                    Attribute updateFlagAttr = child.attribute("customerUpdatFlag");
                    customerUpdatFlag = updateFlagAttr.getValue();
                    //获取回单类型“0”为上门维护单“1”为上门回访单
                    Attribute orderTypAttr = child.attribute("orderTyp");
                    orderTyp = orderTypAttr.getValue();//回单类型
                    for (Iterator i1 = child.elementIterator(); i1.hasNext(); ) {
                        Element serviceOrderChild = (Element) i1.next();

                        if (serviceOrderChild.getQName().getName().equals("workCard_id")) {
                            //得到workCardId
                            workCardId = serviceOrderChild.getTextTrim();
                        } else if (serviceOrderChild.getQName().getName().equals("service_date")) {
                            //服务日期
                            String servDate = serviceOrderChild.getTextTrim();
                            seviceDate1 = servDate;
                            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            try {
                                seviceDate = sdf.parse(servDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if (serviceOrderChild.getQName().getName().equals("order_code")) {
                            //纸制单据号
                            OrderCode = serviceOrderChild.getTextTrim();
                        } else if (serviceOrderChild.getQName().getName().equals("customer_addr")) {
                            //客户上门地址
                            CustomerAddr = serviceOrderChild.getTextTrim();

                        } else if (serviceOrderChild.getQName().getName().equals("longitude")) {
                            //客户地址经度
                            longitude = serviceOrderChild.getTextTrim();//不用更新

                        } else if (serviceOrderChild.getQName().getName().equals("latitude")) {
                            //客户地址维度
                            latitude = serviceOrderChild.getTextTrim();//不用更新

                        } else if (serviceOrderChild.getQName().getName().equals("link_person")) {
                            //客户联系人
                            LinkPerson = serviceOrderChild.getTextTrim();
                        } else if (serviceOrderChild.getQName().getName().equals("customer_telphone")) {
                            //客户联系电话
                            Tel = serviceOrderChild.getTextTrim();

                        } else if (serviceOrderChild.getQName().getName().equals("customer_mobile")) {
                            //客户手机
                            Mobile = serviceOrderChild.getTextTrim();
                        } else if (serviceOrderChild.getQName().getName().equals("arrive_date")) {
                            //到达现场时间
                            ArriveDate = serviceOrderChild.getTextTrim();
                            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            try {
                                arriveDate = sdf.parse(ArriveDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        } else if (serviceOrderChild.getQName().getName().equals("service_end_date")) {
                            //服务完成时间
                            ServiceEndDate = serviceOrderChild.getTextTrim();
                            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            try {
                                serviceEndDate = sdf.parse(ServiceEndDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if (serviceOrderChild.getQName().getName().equals("customer_signature")) {
                            //客户签名
                            customerSignature = serviceOrderChild.getTextTrim();
                        } else if (serviceOrderChild.getQName().getName().equals("customer_if_satisfied")) {
                            //客户是否满意
                            satisfied = serviceOrderChild.getTextTrim();
                            System.out.println("customer_if_satisfied:" + serviceOrderChild.getTextTrim().length());
                        } else if (serviceOrderChild.getQName().getName().equals("spending_process")) {
                            //费用过程
                            spendingProcess = serviceOrderChild.getTextTrim();

                        } else if (serviceOrderChild.getQName().getName().equals("flag")) {
                            //解决方式
                            result = serviceOrderChild.getTextTrim();

                        } else if (serviceOrderChild.getQName().getName().equals("notes")) {
                            //备注
                            Notes = serviceOrderChild.getTextTrim();

                        } else if (serviceOrderChild.getQName().getName().equals("productStatus")) {
                            //产品运行状态
                            ProductCondition = serviceOrderChild.getTextTrim();

                        } else if (serviceOrderChild.getQName().getName().equals("handleResult")) {
                            //处理结果
                            handleResult = serviceOrderChild.getTextTrim();
                        } else if (serviceOrderChild.getQName().getName().equals("hardwareCode")) {
                            //硬件型号
                            hardwareCode = serviceOrderChild.getTextTrim();
                        } else if (serviceOrderChild.getQName().getName().equals("softwareVersion")) {
                            //软件版本
                            softwareVersion = serviceOrderChild.getTextTrim();
                        } else if (serviceOrderChild.getQName().getName().equals("environment")) {
                            //使用环境
                            environment = serviceOrderChild.getTextTrim();
                        } else if (serviceOrderChild.getQName().getName().equals("isCharge")) {
                            //是否收费
                            isCharge = serviceOrderChild.getTextTrim();
                        } else if (serviceOrderChild.getQName().getName().equals("isChargeValue")) {
                            //收费金额
                            isChargeValue = serviceOrderChild.getTextTrim();
                        } else if (serviceOrderChild.getQName().getName().equals("customer_if_satisfied_service")) {
                            //服务评价
                            customer_if_satisfied_service = serviceOrderChild.getTextTrim();
                        } else if (serviceOrderChild.getQName().getName().equals("customer_if_satisfied_product")) {
                            //服务评价
                            customer_if_satisfied_product = serviceOrderChild.getTextTrim();
                        } else if (serviceOrderChild.getQName().getName().equals("visitType")) {
                            //服务评价
                            visitType = serviceOrderChild.getTextTrim();
                        }
                    }
                }

                //找到question_body标签
                else if (child.getQName().getName().equals("question_body")) {
                    String questionId = "";
                    String solutionMethod = "";
                    String questonDesc = "";
                    String questionType = "";
                    String productId = "";
                    for (Iterator i1 = child.elementIterator(); i1.hasNext(); ) {
                        Element questionChild = (Element) i1.next();

                        //找到problem标签
                        if (questionChild.getQName().getName().equals("problem")) {
                            System.out.println(questionChild.attributeCount());
                            for (Iterator i2 = questionChild.elementIterator(); i2.hasNext(); ) {
                                Element problemChild = (Element) i2.next();

                                if (problemChild.getQName().getName().equals("question_id")) {
                                    questionId = problemChild.getTextTrim();

                                } else if (problemChild.getQName().getName().equals("question_typ_id")) {
                                    questionType = problemChild.getTextTrim();

                                } else if (problemChild.getQName().getName().equals("question_typ_p_id")) {
                                    String questionTypp = problemChild.getTextTrim();

                                } else if (problemChild.getQName().getName().equals("question_desc")) {
                                    questonDesc = problemChild.getTextTrim();

                                } else if (problemChild.getQName().getName().equals("solution_method")) {
                                    solutionMethod = problemChild.getTextTrim();

                                } else if (problemChild.getQName().getName().equals("productId")) {
                                    productId = problemChild.getTextTrim();
                                }
                            }
                            String sql = "UPDATE SM_TEL_RECORD_QUESTION t SET "
                                    + "t.MAINTAIN_QUESTION_CONTENT ='" + questonDesc + "'"
                                    + ",t.MAINTAIN_QUESTION_ANSWER='" + solutionMethod + "'"
                                    + ("".equals(productId) || productId == null ? "" : ",t.PRODUCT_ID='" + productId + "'")
                                    + ",t.QUESTION_TYPE_ID='" + questionType + "'"
                                    + " WHERE t.question_id=?";

                            //DbService dbSer = new DbService();
                            int count = DbService.updateSm_tel_record_question(sql, questionId);

                            if (count > 0) {
                                updateFlag = true;
                            }
                        }
                    }
                }
                //找到spending_info_body标签
                else if (child.getQName().getName().equals("spending_info_body")) {
                    //往下遍历一层
                    for (Iterator i1 = child.elementIterator(); i1.hasNext(); ) {
                        Element spendingChild = (Element) i1.next();
                        String spendItem = "";
                        String spendName = "";
                        String spending = "";

                        //找到spending_info标签
                        if (spendingChild.getQName().getName().equals("spending_info")) {

                            //往下遍历一层
                            for (Iterator i2 = spendingChild.elementIterator(); i2.hasNext(); ) {
                                Element spendingInfoChild = (Element) i2.next();

                                //找到spending_item_id标签
                                if (spendingInfoChild.getQName().getName().equals("spending_item_id")) {
                                    spendItem = spendingInfoChild.getTextTrim();
                                } else if (spendingInfoChild.getQName().getName().equals("spending_name")) {
                                    spendName = spendingInfoChild.getTextTrim();
                                } else if (spendingInfoChild.getQName().getName().equals("spending")) {
                                    spending = spendingInfoChild.getTextTrim();
                                }
                            }
                            String tableName;
                            if (orderTyp.equals("1")) {
                                tableName = "PV_VISIT_BILL";
                            } else {
                                tableName = "SM_MAINTAIN";
                            }

                            if (spendItem.trim().equals("3")) {
                                String sql = "update " + tableName + " t set t.USER_DEFINE01='" + spending + "'" +
                                        " where t.DISPATCH_ID=?";
                                //DbService dbSer = new DbService();
                                int count = DbService.updateSm_tel_record_question(sql, workCardId);
                                if (count > 0) {
                                    updateFlag = true;
                                }
                            } else if (spendItem.trim().equals("1")) {
                                String sql = "update " + tableName + " t set t.USER_DEFINE02='" + spending + "'" +
                                        " where t.DISPATCH_ID=?";
                                //DbService dbSer = new DbService();
                                int count = DbService.updateSm_tel_record_question(sql, workCardId);
                                if (count > 0) {
                                    updateFlag = true;
                                }
                            } else if (spendItem.trim().equals("2")) {
                                String sql = "update " + tableName + " t set t.USER_DEFINE03='" + spending + "'" +
                                        " where t.DISPATCH_ID=?";
                                //DbService dbSer = new DbService();
                                int count = DbService.updateSm_tel_record_question(sql, workCardId);
                                if (count > 0) {
                                    updateFlag = true;
                                }
                            } else if (spendItem.trim().equals("7")) {
                                String sql = "update " + tableName + " t set t.USER_DEFINE04='" + spending + "'" +
                                        " where t.DISPATCH_ID=?";
                                //DbService dbSer = new DbService();
                                int count = DbService.updateSm_tel_record_question(sql, workCardId);
                                if (count > 0) {
                                    updateFlag = true;
                                }
                            } else if (spendItem.trim().equals("8")) {
                                String sql = "update " + tableName + " t set t.USER_DEFINE06='" + spending + "'" +
                                        " where t.DISPATCH_ID=?";
                                //DbService dbSer = new DbService();
                                int count = DbService.updateSm_tel_record_question(sql, workCardId);
                                if (count > 0) {
                                    updateFlag = true;
                                }
                            } else if (spendItem.trim().equals("27")) {
                                String sql = "update " + tableName + " t set t.USER_DEFINE05='" + spending + "'" +
                                        " where t.DISPATCH_ID=?";
                                //DbService dbSer = new DbService();
                                int count = DbService.updateSm_tel_record_question(sql, workCardId);
                                if (count > 0) {
                                    updateFlag = true;
                                }
                            }
                        }
                    }
                }
            }

            //TODO:更新到数据库中
            if (orderTyp.equals("1")) {
                //上门回访单
                String sql = "UPDATE PV_VISIT_BILL SET "
                        + "VISIT_START_DATE=to_date('" + seviceDate1 + "','yyyy-mm-dd HH24:mi:ss')"
                        + ",BUSINESS_BILL_ID='" + OrderCode + "'"
                        + ",START_HOUR=" + arriveDate.getHours()
                        + ",START_MINUTE=" + arriveDate.getMinutes()
                        + ",END_HOUR=" + serviceEndDate.getHours()
                        + ",END_MINUTE=" + serviceEndDate.getMinutes()
                        + ",PROCESS_RESULT='" + result + "'"
                        + ",TEL='" + Tel + "'"
                        + ",MARK='" + visitType + "'"
                        + ",TELEPHONE='" + Mobile + "'"
                        + ",CONTACT='" + LinkPerson + "'"
                        + ",OFFICE_SPACE='" + CustomerAddr + "'"
                        + ",USER_DEFINE07='" + spendingProcess + "'"
                        + ",SERVICE_EVALUATION='" + customer_if_satisfied_service + "'"
                        + ",PRODUCT_EVALUATION='" + customer_if_satisfied_product + "'"
                        + ",CHANNEL='" + customerSignature + "'"
                        + " WHERE DISPATCH_ID=?";
                System.out.println("回访sql:" + sql);
                //DbService dbSer = new DbService();
                int count = DbService.updateSm_tel_record_question(sql, workCardId);
                if (count > 0) {
                    updateFlag = true;
                }
                Map<String, String> pv = DbService.queryVisitBillByDispatchId(workCardId);
                sql = "insert into PV_VISIT_BILL_DETAIL(DETAIL_ID,VISIT_ID,PRODUCT_ID,PRODUCT_RUN_CIRCS,PROCESS_RESULT) "
                        + " values(1," + pv.get("VISIT_ID") + "," + pv.get("PRODUCT_ID") + ",'" + ProductCondition + "','" + handleResult + "')";
                System.out.println("插入问题sql:" + sql);
                count = DbService.excuteInsert(sql);
                if (count > 0) {
                    updateFlag = true;
                }
//				Crud crud = new Crud("PT_VISIT_BILL");
//				crud.define("VISIT_START_DATE;BUSINESS_BILL_ID;START_HOUR;START_MINUTE;END_HOUR;END_MINUTE;SERVICE_EVALUATION;PROCESS_RESULT",
//						new Object[]{seviceDate,OrderCode,arriveDate.getHours(),arriveDate.getMinutes(),serviceEndDate.getHours(),serviceEndDate.getMinutes(),satisfied,result});
//				crud.defineCondition("DISPATCH_ID", new Object[]{workCardId});
//				Eso eso= crud.getUpdateEso();
//				DbSvr.getDbService("local").execute(eso);
            } else if (orderTyp.equals("0")) {
                //上门维护单
                String sql = "UPDATE SM_MAINTAIN SET "
                        + "ARRIVE_DATE= to_date('" + ArriveDate + "','yyyy-mm-dd HH24:mi:ss')"                       //服务日期
                        + ",FINISH_DATE= to_date('" + ServiceEndDate + "','yyyy-mm-dd HH24:mi:ss')"                       //服务日期
                        + ",PAPER_NUM='" + OrderCode + "'"                          //纸制单据号
                        + ",ARRIVE_DATE_HOUR=" + arriveDate.getHours()
                        + ",ARRIVE_DATE_MINUTE=" + arriveDate.getMinutes()
                        + ",FINISH_DATE_HOUR=" + serviceEndDate.getHours()
                        + ",FINISH_DATE_MINUTE=" + serviceEndDate.getMinutes()
                        + ",IS_SATISFACTION='" + satisfied + "'"
                        + ",TELEPHONE='" + Tel + "'"
                        + ",CONTACT='" + LinkPerson + "'"
                        + ",OFFICE_SPACE='" + CustomerAddr + "'"
                        + ",MOBILE='" + Mobile + "'"
                        + ",SERIAL_NUMBER='" + hardwareCode + "'"
                        + ",SOFTWARE_VERSION='" + softwareVersion + "'"
                        + ",ENVIRONMENT='" + environment + "'"

                        + ",USER_DEFINE07='" + spendingProcess + "'"   //费用过程
                        + ",ADDVICE='" + Notes + "'"   //备注
                        + ",IS_CHARGE='" + isCharge + "'"   //是否收费
                        + ",CHARGE_SUM='" + isChargeValue + "'"   //收费金额
                        + ",CLIENT_SIGN='" + customerSignature + "'"   //客户签名
                        + ",IS_RESOLVE= '1'"   //设置问题已解决

                        + " WHERE DISPATCH_ID=?";
                System.out.println("维护sql:" + sql);
                //DbService dbSer = new DbService();

                //更新SM_TEL_RECORD_QUESTION表设置问题已解决
                String questionSql = "UPDATE SM_TEL_RECORD_QUESTION SET QUESTION_STATUS = '1' WHERE DISPATCH_ID = ?";
                String dispatchSql = "update sm_dispatch set arrive_date = to_date('" + ArriveDate + "','yyyy-mm-dd HH24:mi:ss') ,done_date=to_date('" + ServiceEndDate + "','yyyy-mm-dd HH24:mi:ss') where dispatch_id=?";
                int count = DbService.updateSm_tel_record_question(sql, workCardId);

                if (count > 0) {
                	DbService.updateSm_tel_record_question(dispatchSql, workCardId);
                    if (DbService.updateSm_tel_record_question(questionSql, workCardId) > 0) {
                        updateFlag = true;
                    }

                }
            }
            String sql = "UPDATE SM_DISPATCH t SET "
                    + " t.DISPATCH_STATUS_ID=9 "
                    + " WHERE t.DISPATCH_ID=?";

            //DbService dbSer = new DbService();
            int count = DbService.updateSm_tel_record_question(sql, workCardId);

        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
        return updateFlag;
    }


    /**
     * 解析修改客户基本信息XML,修改客户基本信息
     */
    public boolean parseCustInfoXml(String xmlContent) {
        String cust_name = "";//客户名称
        String cust_tax_code = "";//客户税号
        String addr = "";//客户地址
        String contract = "";//客户联系人
        String tel = "";//客户电话
        String mobile = "";//客户手机号码
        boolean updateFlag = false;//更新标志
        Map<String, String> custMap = new HashMap<String, String>();
        try {
            Document document = DocumentHelper.parseText(xmlContent);
            //获取根元素
            Element root = document.getRootElement();
            // 获取所有子元素，遍历
            for (Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element child = (Element) i.next();
                //获得客户名称
                if (child.getQName().getName().equals("customerName")) {
                    cust_name = child.getTextTrim().trim();
                }
                //获得客户税号
                if (child.getQName().getName().equals("taxCode")) {
                    cust_tax_code = child.getTextTrim().trim();
                }
                //获得客户地址
                if (child.getQName().getName().equals("customerAddr")) {
                    addr = child.getTextTrim().trim();
                }
                //获得客户联系人
                if (child.getQName().getName().equals("linkPerson")) {
                    contract = child.getTextTrim().trim();
                    System.out.println("contract is --->:" + contract);
                }
                //获得客户电话
                if (child.getQName().getName().equals("customerTel")) {
                    tel = child.getTextTrim().trim();
                }
                //获得客户移动电话
                if (child.getQName().getName().equals("customerMobile")) {
                    mobile = child.getTextTrim().trim();
                }
                System.out.println("cust_tax_code is --->:" + cust_tax_code);
                String ORG_CUSTOMER_ID = dbService.queryCustomerIdByTaxcode(cust_tax_code);
                System.out.println("ORG_CUSTOMER_ID is --->:" + ORG_CUSTOMER_ID);
                custMap.put("contract", contract);
                custMap.put("addr", addr);
                custMap.put("tel", tel);
                custMap.put("mobile", mobile);
                custMap.put("ORG_CUSTOMER_ID", ORG_CUSTOMER_ID);
                int result = dbService.udpateCustomerInfo(custMap);
                if (result == 1) {
                    updateFlag = true;
                }

            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return false;
    }
}
