import java.sql.*;
import java.io.*;
import java.util.*;

public class HBMGenerator {

	private static String db = null;
	private static String user = null;
	private static String pass = null;
	private static String url = null;
	private static Connection con = null;
	
	private static Map<String,String> attrs = new HashMap<String,String>();
	
	static{
		Scanner input = new Scanner(System.in);
		System.out.println("Database:");
		db = input.next();
		System.out.println("Username:");
		user = input.next();
		System.out.println("Password:");
		pass = input.next();
		url = "jdbc:mysql://127.0.0.1/"+db;
		input.close();
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	protected static Connection getConnection()
	{
		if(con != null)
			return con;
		try{
			con = DriverManager.getConnection(url, user, pass);
		} catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return con;
	}
	
	protected static void closeConnection()
	{
		try{
			if(con!=null)
				con.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
//	TABLE_CAT String => 表类别（可为 null）
//	TABLE_SCHEM String => 表模式（可为 null）
//	TABLE_NAME String => 表名称
//	TABLE_TYPE String => 表类型。
//	REMARKS String => 表的解释性注释
//	TYPE_CAT String => 类型的类别（可为 null）
//	TYPE_SCHEM String => 类型模式（可为 null）
//	TYPE_NAME String => 类型名称（可为 null）
//	SELF_REFERENCING_COL_NAME String => 有类型表的指定 "identifier" 列的名称（可为 null）
//	REF_GENERATION String
	public static void showMetaData()
	{
		try{
			DatabaseMetaData meta = con.getMetaData();
			ResultSet rs = meta.getTables(con.getCatalog(), "root", null, new String[]{"TABLE"});
			while(rs.next())
			{
				System.out.println(rs.getString("TABLE_NAME"));
				ResultSet rs1=meta.getColumns(null, null, rs.getString("TABLE_NAME"), "%");  
				ResultSetMetaData rsm = rs1.getMetaData();
				while(rs1.next())
				{
					for(int i=0;i<rsm.getColumnCount();i++)
						System.out.print(rs1.getString(i+1)+" ");
					System.out.println();
				}
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static String caption(String old)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(old.substring(0,1).toUpperCase());
		sb.append(old.substring(1,old.length()));
		return sb.toString();
	}
	
	public static void perform()
	{
		String filename = null;
		try{
			DatabaseMetaData meta = con.getMetaData();
			ResultSet rs = meta.getTables(con.getCatalog(), user, null, new String[]{"TABLE"});
			while(rs.next())
			{
				filename = rs.getString("TABLE_NAME");
				System.out.println("Generating "+filename);
				
				ResultSet rs1 = meta.getColumns(null, null, rs.getString("TABLE_NAME"), "%");  
				while(rs1.next())
				{
					String type = null;
					switch(rs1.getString(6))
					{
					case "FLOAT":
						type = "java.lang.Float";
						break;
					case "TINYINT":
					case "INT":
						type = "java.lang.Integer";
						break;
					default:
						type = "java.lang.String";
						break;
					}
					System.out.println(rs1.getString(4)+": "+rs1.getString(6));
					attrs.put(rs1.getString(4), type);
				}
				
//				PrintWriter pw = new PrintWriter(new File(caption(filename)+".hbm.xml"));
//				pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?> ");
//				pw.println("<!DOCTYPE hibernate-mapping PUBLIC");
//				pw.println("\"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"");
//				pw.println("\"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd\">");
//				pw.println("<hibernate-mapping>");
//				
//				pw.println("\t<class name=\""+caption(filename)+"\" table=\""+filename+"\">");
				ResultSet rs3 =  meta.getPrimaryKeys(null, null, rs.getString("TABLE_NAME"));
				while(rs3.next())
				{
					System.out.println(rs3.getString(4));
				}
				
				Iterator<String> iter = attrs.keySet().iterator();
				while(iter.hasNext())
				{
					String key = (String)iter.next();
					String val = (String)attrs.get(key);
				}
//				pw.println("\t</class>");
//				pw.println("</hibernate-mapping>");
//				pw.flush();
//				pw.close();
				attrs.clear();
				
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		getConnection();
		showMetaData();
		perform();
		closeConnection();	
	}
}
