package xmltransfer;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import service.DbService;

import java.util.List;
import java.util.Map;

/*
 * 派工单XML数据生成与解析
 */
public class WorkCardXmlUtil {
    public static void CreateWorkCardOrderXml(String rootPath, String logsPath, String filePath, int totalCount) throws Exception {
        String sql = "SELECT t2.* from (SELECT t1.*,ROWNUM rn from ( " +
                "SELECT SMD.DISPATCH_ID workCardId,SMD.DIS_DATE serviceDate,SMD.SERVICE_TYPE_ID workOrderType," +
                "'' visitType,SMD.CUSTOMER_ID customerId,SMD.CONTACT linkPerson,SMD.CUSTOMER_NAME customerName,SMD.CUSTOMER_TAX_ID taxCode," +
                "SMD.DISPATCH_DEPARTMENT departmentId,ORG.ORG_NAME departmentName,CUS.TAX_BUREAU_ID revenueId,TAX.TAX_AUTHORITY_NAME revenueName,SMD.OFFICE_SPACE customerAddr," +
                "SMD.MOBILE customerMobile,SMD.TEL customerTel,ORG.BUSINESS_ADDRESS customerCounty,'' customerLatitude,'' customerLongitude," +
                "SMD.PROCESSER_ID fieldWorkerId,EMP. NAME fieldWorkerName,SMD.DISPATCH_NOTE workOrderDescription,SMD.RESERVATION expectArriveTime,'' route," +
                "'' productStatus,'' handleResult,'' returnProductId,2 chargeType,0 chargeMoney,URGENCY urgency,ORG.BUSINESS_ADDRESS city,SMD.UPDATE_ID updateId " +
                "FROM SM_DISPATCH SMD LEFT JOIN SYS_EMPLOYEE EMP ON SMD.PROCESSER_ID = EMP.EMPLOYEE_ID LEFT JOIN SYS_ORG ORG ON SMD.DISPATCH_DEPARTMENT = ORG.ORG_ID " +
                " LEFT JOIN CUS_ORG_CUSTOMER CUS ON SMD.CUSTOMER_ID = CUS.ORG_CUSTOMER_ID LEFT JOIN SYS_TAX_AUTHORITY TAX ON TAX.TAX_AUTHORITY_ID = CUS.TAX_BUREAU_ID " + 
                "WHERE SMD.DISPATCH_ID IN (SELECT DISPATCH_ID FROM SM_TEL_RECORD_QUESTION) AND SMD.DISPATCH_ID IN (SELECT DISPATCH_ID FROM WQ_UNDISPATCH_ORDER)" +
                ") t1 WHERE ROWNUM<51)t2 WHERE t2.rn>0";
        String questionSql = "SELECT QST.QUESTION_ID questionId,T.PARENT_CATEGORY_ID questionTypPid,T.CATEGORY_ID questionTypId,QST.QUESTION_CONTENT questionDesc,QST.QUESTION_ANSWER solution,QST.PRODUCT_ID productId " +
                "FROM SM_TEL_RECORD_QUESTION QST LEFT JOIN TEL_KNOWLEDGE_CATEGORY T ON QST.QUESTION_TYPE_ID = T.CATEGORY_ID WHERE QST.DISPATCH_ID = ?";
        System.out.println("sql:" + sql);
        //DbService dbSer = new DbService();
        List<Map<String, String>> listData = DbService.getResultList(sql, null, 0);

        for (Map<String, String> mapTemp : listData) {
            String dispatchId = mapTemp.get("workCardId");

            List<Map<String, String>> qustionListData = DbService.getResultList(questionSql, dispatchId, 1);

            // 创建XML文档对象
            Document document = DocumentHelper.createDocument();
            // 创建跟节点-workOrder
            Element workOrderElement = document.addElement("workOrder");
            workOrderElement.addElement("workCardId").addText(mapTemp.get("workCardId"));
            workOrderElement.addElement("serviceDate").addText(mapTemp.get("serviceDate"));
            workOrderElement.addElement("workOrderType").addText(mapTemp.get("workOrderType"));
            workOrderElement.addElement("productStatus").addText(mapTemp.get("productStatus") == null ? "" : mapTemp.get("productStatus"));
            workOrderElement.addElement("handleResult").addText(mapTemp.get("handleResult") == null ? "" : mapTemp.get("handleResult"));
            workOrderElement.addElement("visitType").addText(mapTemp.get("visitType") == null ? "" : mapTemp.get("visitType"));

            //在根节点workOrder下添加customer节点
            Element customerElement = workOrderElement.addElement("customer");
            customerElement.addElement("customerId").addText(mapTemp.get("customerId"));
            customerElement.addElement("linkPerson").addText(mapTemp.get("linkPerson") == null ? "" : mapTemp.get("linkPerson"));
            customerElement.addElement("customerName").addText(mapTemp.get("customerName"));
            customerElement.addElement("taxCode").addText(mapTemp.get("taxCode"));
            customerElement.addElement("departmentId").addText(mapTemp.get("departmentId"));
            customerElement.addElement("departmentName").addText(mapTemp.get("departmentName"));
            customerElement.addElement("revenueId").addText(mapTemp.get("revenueId") == null ? "" : mapTemp.get("revenueId"));
            customerElement.addElement("revenueName").addText(mapTemp.get("revenueName") == null ? "" : mapTemp.get("revenueName"));
            customerElement.addElement("customerAddr").addText(mapTemp.get("customerAddr"));
            customerElement.addElement("customerMobile").addText(mapTemp.get("customerMobile") == null ? "" : mapTemp.get("customerMobile"));
            customerElement.addElement("customerTel").addText(mapTemp.get("customerTel") == null ? "" : mapTemp.get("customerTel"));
            customerElement.addElement("customerCounty").addText(mapTemp.get("customerCounty") == null ? "" : mapTemp.get("customerCounty"));
            customerElement.addElement("customerLatitude").addText(mapTemp.get("customerLatitude") == null ? "" : mapTemp.get("customerLatitude"));
            customerElement.addElement("customerLongitude").addText(mapTemp.get("customerLongitude") == null ? "" : mapTemp.get("customerLongitude"));
            workOrderElement.addElement("fieldWorkerId").addText(mapTemp.get("fieldWorkerId"));//log4j
            workOrderElement.addElement("fieldWorkerName").addText(mapTemp.get("fieldWorkerName"));//log4j
            workOrderElement.addElement("workOrderDescription").addText(mapTemp.get("workOrderDescription") == null ? "" : mapTemp.get("workOrderDescription"));
            workOrderElement.addElement("expectArriveTime").addText(mapTemp.get("expectArriveTime"));
            workOrderElement.addElement("route").addText(mapTemp.get("route") == null ? "" : mapTemp.get("route"));

            workOrderElement.addElement("returnProductId").addText(mapTemp.get("returnProductId") == null ? "" : mapTemp.get("returnProductId"));
            workOrderElement.addElement("chargeType").addText(mapTemp.get("chargeType") == null ? "" : mapTemp.get("chargeType"));
            workOrderElement.addElement("chargeMoney").addText(mapTemp.get("chargeMoney"));
            workOrderElement.addElement("urgency").addText(mapTemp.get("urgency")==null ? "1" : mapTemp.get("urgency"));
            workOrderElement.addElement("city").addText(mapTemp.get("city").substring(0, 8));
            //添加改派状态
            workOrderElement.addElement("updateId").addText(mapTemp.get("updateId")==null ? "0" :mapTemp.get("updateId"));
            //在根节点workOrder下添加problemList节点
            Element problemListElement = workOrderElement.addElement("problemList");
            for (Map<String, String> aQustionListData : qustionListData) {
                Map map2 = (Map) aQustionListData;
                //在节点problemList下添加problem节点
                Element problemElement = problemListElement.addElement("problem");
                problemElement.addElement("questionId").addText(map2.get("questionId").toString());
                problemElement.addElement("questionTypId").addText(map2.get("questionTypId") == null ? "" : map2.get("questionTypId").toString());
                problemElement.addElement("questionTypPid").addText(map2.get("questionTypPid") == null ? "" : map2.get("questionTypPid").toString());
                problemElement.addElement("questionDesc").addText(map2.get("questionDesc") == null ? "" : map2.get("questionDesc").toString());
                problemElement.addElement("solution").addText(map2.get("solution") == null ? "" : map2.get("solution").toString());
                problemElement.addElement("product_id").addText(map2.get("productId") == null ? "" : map2.get("productId").toString());
            }
            // 返回
            System.out.println(document.asXML());
            //发送
            SendXMLUtil.sendXML("http://222.240.218.90:20000/fieldwork/interface/work-order", document.asXML(), dispatchId, mapTemp.get("fieldWorkerName"), rootPath, logsPath, filePath, totalCount);
           // SendXMLUtil.sendXML("http://192.168.7.203:8080/fieldwork/interface/work-order", document.asXML(), mapTemp.get("fieldWorkerId"), mapTemp.get("fieldWorkerName"), rootPath, logsPath, filePath, totalCount);
        }

    }


}
