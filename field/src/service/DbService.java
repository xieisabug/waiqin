package service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.pool.DruidPooledPreparedStatement;
import com.alibaba.druid.pool.DruidPooledResultSet;

import DbUtil.DruidDataSourceUtil;

public class DbService {
    static DruidDataSourceUtil druidDataSourceUtil = new DruidDataSourceUtil();
    private static DruidDataSource druidDataSource = null;
    private static final Logger logger = Logger.getLogger(DbService.class);

    static {
        try {
            druidDataSource = druidDataSourceUtil.getDataSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, String>> getResultList(String strSql, String strDispatchMark, int iflag) {

        List<Map<String, String>> resultListMap = null;

        DruidPooledConnection dbConn = null;

        DruidPooledResultSet rs = null;

        DruidPooledPreparedStatement ps = null;
        try {
            dbConn = druidDataSourceUtil.getDruidConnection(druidDataSource);
            ps = (DruidPooledPreparedStatement) dbConn.prepareStatement(strSql);

            if (strDispatchMark != null) {
                ps.setString(1, strDispatchMark);
            }

            rs = (DruidPooledResultSet) ps.executeQuery();

            if (iflag == 0) {
                resultListMap = new ArrayList<Map<String, String>>();
                dataToListMap(resultListMap, rs);

            }

            if (iflag == 1) {
                resultListMap = new ArrayList<Map<String, String>>();
                dataToQuestListMap(resultListMap, rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (dbConn != null) {
                try {
                    dbConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return resultListMap;
    }

    private static void dataToListMap(List<Map<String, String>> resultListMap, DruidPooledResultSet rs) throws Exception {

        Map<String, String> mapTemp;
        while (rs.next()) {
            mapTemp = new HashMap<String, String>();
            mapTemp.put("workCardId", rs.getString("workCardId"));
            mapTemp.put("serviceDate", rs.getString("serviceDate"));
            mapTemp.put("workOrderType", rs.getString("workOrderType"));
            mapTemp.put("productStatus", rs.getString("productStatus"));
            mapTemp.put("handleResult", rs.getString("handleResult"));
            mapTemp.put("visitType", rs.getString("visitType"));
            mapTemp.put("customerId", rs.getString("customerId"));
            mapTemp.put("linkPerson", rs.getString("linkPerson"));
            mapTemp.put("workCardId", rs.getString("workCardId"));
            mapTemp.put("customerName", rs.getString("customerName"));
            mapTemp.put("taxCode", rs.getString("taxCode"));
            mapTemp.put("departmentId", rs.getString("departmentId"));
            mapTemp.put("departmentName", rs.getString("departmentName"));
            mapTemp.put("revenueId", rs.getString("revenueId"));
            mapTemp.put("revenueName", rs.getString("revenueName"));
            mapTemp.put("customerAddr", rs.getString("customerAddr"));
            mapTemp.put("customerMobile", rs.getString("customerMobile"));
            mapTemp.put("customerTel", rs.getString("customerTel"));
            mapTemp.put("customerCounty", rs.getString("customerCounty"));
            mapTemp.put("customerLatitude", rs.getString("customerLatitude"));
            mapTemp.put("customerLongitude", rs.getString("customerLongitude"));
            mapTemp.put("fieldWorkerId", rs.getString("fieldWorkerId"));
            mapTemp.put("fieldWorkerName", rs.getString("fieldWorkerName"));
            mapTemp.put("workOrderDescription", rs.getString("workOrderDescription"));
            mapTemp.put("expectArriveTime", rs.getString("expectArriveTime"));
            mapTemp.put("route", rs.getString("route"));
            mapTemp.put("returnProductId", rs.getString("returnProductId"));
            mapTemp.put("chargeType", rs.getString("chargeType"));
            mapTemp.put("chargeMoney", rs.getString("chargeMoney"));
            mapTemp.put("urgency", rs.getString("urgency"));
            mapTemp.put("city", rs.getString("city"));
            mapTemp.put("updateId", rs.getString("updateId"));
            resultListMap.add(mapTemp);
        }
    }

    private static void dataToQuestListMap(List<Map<String, String>> resultListMap, DruidPooledResultSet rs) throws Exception {

        Map<String, String> mapTemp;

        while (rs.next()) {
            mapTemp = new HashMap<String, String>();
            mapTemp.put("questionId", rs.getString("questionId"));
            mapTemp.put("questionTypId", rs.getString("questionTypId"));
            mapTemp.put("questionTypPid", rs.getString("questionTypPid"));
            mapTemp.put("questionDesc", rs.getString("questionDesc"));
            mapTemp.put("solution", rs.getString("solution"));
            mapTemp.put("productId", rs.getString("productId"));
            resultListMap.add(mapTemp);

        }
    }

    public static int updateSm_tel_record_question(String sql, String dispath_mark) throws Exception {
        DruidPooledConnection dbConn = druidDataSourceUtil.getDruidConnection(druidDataSource);

        DruidPooledPreparedStatement ps = (DruidPooledPreparedStatement) dbConn.prepareStatement(sql);

        ps.setString(1, dispath_mark);

        int result = ps.executeUpdate();
        logger.debug("updateSm_tel_record_question result:" + result);
        if (ps != null) {
            ps.close();
        }

        if (dbConn != null) {

            dbConn.close();
        }

        return result;
    }

    public static int updateSm_dispatch(String sql, String date) {
        int result = 0;
        DruidPooledConnection dbConn;
        try {
            dbConn = druidDataSourceUtil.getDruidConnection(druidDataSource);
            DruidPooledPreparedStatement ps = (DruidPooledPreparedStatement) dbConn.prepareStatement(sql);
            ps.setString(1, date);
            result = ps.executeUpdate();

            if (ps != null) {
                ps.close();
            }

            if (dbConn != null) {

                dbConn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 改派数据库操作方法
     *
     * @param sql
     * @param changeReason
     * @param date
     * @return
     * @authror cjl
     * @version 2014年6月16日 下午5:06:09
     */
    public static int updateChangeDispatch(String sql, String changeReason, String date) {
        int result = 0;
        DruidPooledConnection dbConn;
        try {
            dbConn = druidDataSourceUtil.getDruidConnection(druidDataSource);
            DruidPooledPreparedStatement ps = (DruidPooledPreparedStatement) dbConn.prepareStatement(sql);
            ps.setString(1, changeReason);
            ps.setString(2, date);

            result = ps.executeUpdate();

            if (ps != null) {
                ps.close();
            }

            if (dbConn != null) {

                dbConn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 处理派工单临时表WQ_UNDISPATH_ORDER
     *
     * @author Administrator
     * @version 2014-06-16 17:16
     */
    public static int deleteTempOrder(String dispatchId) {
        int result = 0;
        String sql = "delete FROM WQ_UNDISPATCH_ORDER where DISPATCH_ID=?";
        DruidPooledConnection dbConn;
        try {
            dbConn = druidDataSourceUtil.getDruidConnection(druidDataSource);
            DruidPooledPreparedStatement ps = (DruidPooledPreparedStatement) dbConn.prepareStatement(sql);
            ps.setString(1, dispatchId);
            result = ps.executeUpdate();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据税号查询出客户的ID
     *
     * @param taxCode 税号
     * @return
     * @version 2014年6月12日 上午9:40:47
     */
    public static String queryCustomerIdByTaxcode(String taxCode) {
        int result = 0;
        ResultSet resultSet;
        DruidPooledConnection dbConn;
        String customerId = "";
        Map<String, String> idMap = new HashMap<String, String>();
        try {
            dbConn = druidDataSourceUtil.getDruidConnection(druidDataSource);
            String sql = "SELECT org_customer_id from CUS_ORG_CUSTOMER where  CUST_TAX_CODE=? and IS_VALID=1";
            DruidPooledPreparedStatement ps = (DruidPooledPreparedStatement) dbConn.prepareStatement(sql);
            ps.setString(1, taxCode);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("org_customer_id");
                idMap.put("CUSTOMER_ID", id);
            }
            if (idMap.size() > 0) {
                customerId = idMap.get("CUSTOMER_ID").toString();
                System.out.println("根据税号查出客户ID:-->" + customerId);
            }
            if (ps != null) {
                ps.close();
            }

            if (dbConn != null) {

                dbConn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerId;
    }

    /**
     * 修改客户基本信息
     *
     * @author chenjl
     * @version 2014-06-12 10:27
     */
    public static int udpateCustomerInfo(Map<String, String> custMap) {
        int result = 0;
        DruidPooledConnection dbConn;
        try {
            dbConn = druidDataSourceUtil.getDruidConnection(druidDataSource);
            String sql = "update CUS_ADDR set CONTACT=?,ADDR=?,TEL=?,MOBILE=? where ORG_CUSTOMER_ID=? and IS_DEFAULT='1'";
            DruidPooledPreparedStatement ps = (DruidPooledPreparedStatement) dbConn.prepareStatement(sql);
            System.out.println("custMap is ---->:" + custMap.toString());
            ps.setString(1, custMap.get("contract").trim().toString());
            ps.setString(2, custMap.get("addr").trim().toString());
            ps.setString(3, custMap.get("tel").trim().toString());
            ps.setString(4, custMap.get("mobile").trim().toString());
            ps.setString(5, custMap.get("ORG_CUSTOMER_ID").trim().toString());
            result = ps.executeUpdate();

            if (ps != null) {
                ps.close();
            }

            if (dbConn != null) {
                dbConn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 通过dispatchId查询PV_VISIT_BILL_DETAIL表中的相关数据
     * @param dispatchId 查询用的dispatchId
     * @return 查询到的结果集
     */
    public static Map<String,String> queryVisitBillByDispatchId(String dispatchId) {
        ResultSet resultSet;
        DruidPooledConnection dbConn = null;
        Map<String, String> idMap = new HashMap<String, String>();
        DruidPooledPreparedStatement ps = null;
        try {
            dbConn = druidDataSourceUtil.getDruidConnection(druidDataSource);
            String sql = "SELECT PRODUCT_ID,VISIT_ID from PV_VISIT_BILL where DISPATCH_ID=?";
            System.out.println("DbService queryVisitBillDetailByDispatchId sql-> " + sql);
            ps = (DruidPooledPreparedStatement) dbConn.prepareStatement(sql);
            ps.setLong(1, Long.parseLong(dispatchId));
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String productId = resultSet.getString("PRODUCT_ID");
                String visitId = resultSet.getString("VISIT_ID");
                idMap.put("PRODUCT_ID", productId);
                idMap.put("VISIT_ID", visitId);
            }
            System.out.println("DbService queryVisitBillDetailByDispatchId idMap-> " + idMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (dbConn != null) {
                try {
                    dbConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return idMap;
    }

    /**
     * 执行插入的sql
     * @param sql 执行的sql语句
     * @return 影响条数
     */
    public static int excuteInsert(String sql) {
        int result = 0;
        DruidPooledConnection dbConn;
        try {
            dbConn = druidDataSourceUtil.getDruidConnection(druidDataSource);
            DruidPooledPreparedStatement ps = (DruidPooledPreparedStatement) dbConn.prepareStatement(sql);
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
