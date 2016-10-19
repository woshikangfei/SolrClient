package com.fei.base.util;

/**
 * JDBC数据操作工具类
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlJdbcUtil {
	
	//定义连接数据库所需要的字段  
    private static String driveClassName = "oracle.jdbc.driver.OracleDriver";  
    private static String url = "jdbc:oracle:thin:@192.168.1.9:1521:ORCL";  
    private static String username = "SPIDER";  
    private static String password = "SPIDER";  
    //通过配置文件为以上字段赋值  
    static{  
        try {  
            //加载驱动类  
            Class.forName(driveClassName); 
        } catch (Exception e) {  
            e.printStackTrace();
        }  
    }  
    //
    

	/**
	 * 
	 * @Title getConnection
	 * @Description 这里用一句话描述这个方法的作用
	 * @return 设定文件
	 * @return 返回类型 Connection
	 * @throws 抛出的异常类型
	 */
	public static Connection getConnection() {
		Connection conn = null;
		// 采用普通jdbc链接
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 采用数据池链接
		/*
		 * Context ctx = null; DataSource ds = null; try { ctx = new
		 * InitialContext(); ds = (DataSource)
		 * ctx.lookup("java:/comp/env/lespool"); conn = ds.getConnection(); }
		 * catch (Exception e) { e.printStackTrace(); }
		 */
		return conn;
	}

	/**
	 * @param test
	 * @param rs
	 * @param conn
	 * @param closeConnection
	 * @return 返回类型HashMap
	 */
	public static ResultSet query(String sql, Connection conn) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 
	 * 执行SQL查询语句（select）返回List<HashMap>结果集 @
	 * */
	@SuppressWarnings("unchecked")
	public static List<HashMap> queryList(String sql, Connection conn,
			boolean closeConnection) {
		return rsToList(query(sql, conn), conn, closeConnection);
	}

	/**
	 * 执行SQL查询语句（select）返回一个HashMap结果
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public static HashMap find(String sql, Connection conn,
			boolean closeConnection) {
		return rsToALine(query(sql, conn), conn, closeConnection);
	}

	/**
	 * 返回结果集中的第一个记录的HashMap形式结果
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public static HashMap find(ResultSet rs, Connection conn,
			boolean closeConnection) {
		return rsToALine(rs, conn, closeConnection);
	}

	/**
	 * 执行没有结果集的SQL语句(insert update 和 delete)
	 * 
	 * */
	public static int execSQL(String sql, Connection conn,
			boolean closeConnection) {
		int affectNum = 0;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			affectNum = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (closeConnection) {
				closeAll(null, ps, conn);
			}
		}
		return affectNum;
	}
	
	/**
	 * 执行没有结果集的SQL语句(insert update 和 delete)
	 * 
	 * */
	public static int execSQL(String sql, Connection conn,Object[] val
			) {
		int affectNum = 0;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			for(int i=0;i<val.length;i++){
				ps.setString((i+1), val[i].toString());
			}
			
			affectNum = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return affectNum;
	}

	/**
	 * 将结果集转化为List<HashMap>
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public static List<HashMap> rsToList(ResultSet rs, Connection conn,
			boolean closeConnection) {
		ArrayList ret = new ArrayList();
		ArrayList rsColNames = new ArrayList();
		if (rs != null) {
			try {
				while (rs.next()) {
					rsColNames = getRsColumns(rs, conn);
					// System.out.println(rs.getInt("replyId"));
					Map line = new HashMap();
					for (int i = 0; i < rsColNames.size(); i++) {
						line.put(rsColNames.get(i).toString(),
								rs.getObject(rsColNames.get(i).toString()));
					}
					ret.add(line);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (closeConnection) {
					closeAll(rs, null, conn);
				}
			}
			// System.out.println(ret);
			return ret;
		}
		return null;
	}

	/**
	 * 返回结果集ResultSet中第一行的HashMap形式数据
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public static HashMap rsToALine(ResultSet rs, Connection conn,
			boolean closeConnection) {
		ArrayList rsColNames = new ArrayList();
		if (rs != null) {
			try {
				while (rs.next()) {
					rsColNames = getRsColumns(rs, conn);
					HashMap line = new HashMap();
					for (int i = 0; i < rsColNames.size(); i++) {
						line.put(rsColNames.get(i).toString(),
								rs.getObject(rsColNames.get(i).toString()));
					}
					closeAll(rs, null, null);
					return line;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (closeConnection) {
					closeAll(rs, null, conn);
				}
			}
		}
		return null;
	}

	/**
	 * 取得结果集ResultSet中表的字段信息
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public static ArrayList getRsColumns(ResultSet rs, Connection conn) {
		ArrayList ret = new ArrayList();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int k;
			k = rsmd.getColumnCount();
			for (int i = 1; i <= k; i++) {
				ret.add(rsmd.getColumnName(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static void closeConnection(Connection conn){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 释放资源
	 * 
	 * */
	public static void closeAll(ResultSet rs, PreparedStatement ps,
			Connection conn) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (ps != null) {
				ps.close();
				ps = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Connection conn = MysqlJdbcUtil.getConnection();
		/*String sql = "insert into SOLR_COMMITINDEX "
				+ "values ('ccc','Thread-8',13,32,23,20140721,7)";*/
		String sql = "insert into SOLR_ERRORINDEX values ('"+UUIDGenerator.getUUID()+"','aaa','bbb',20141212020203,20141212020203)";
		MysqlJdbcUtil.execSQL(sql, conn, true);
	}
}