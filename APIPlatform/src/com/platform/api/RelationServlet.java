package com.platform.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.platform.model.*;

/**
 * Servlet implementation class RelationServlet
 */
@WebServlet("/RelationServlet")
public class RelationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RelationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userid = request.getParameter("user_id");
		String shouhuanid = request.getParameter("shouhuan_id");
		
		System.out.println("User_id: "+userid+" Shouhuan_id:"+shouhuanid);
		response.setContentType("text/x-json");
		
		PrintWriter out = response.getWriter();
		Map<String, String> data = new HashMap<String, String>();
		
		if(userid==null || userid.equals(""))
		{
			data.put("code","200");
			data.put("msg", "获取数据失败");
			data.put("data", "");
			out.println(JSONObject.fromObject(data).toString());
			return;
		}
		
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session s = sf.openSession();
	
		try{
			SQLQuery query = s.createSQLQuery("select * from relation where user_id="+userid+" and shouhuan_id="+shouhuanid).addEntity(Relation.class);
			Relation info = (Relation) query.uniqueResult();
			
			data.put("code","100");
			data.put("msg", "获取数据成功");
			data.put("data", JSONObject.fromObject(info).toString());
			
			out.println(JSONObject.fromObject(data).toString());
		}catch(Exception e)
		{
			data.put("code","200");
			data.put("msg", "获取数据失败");
			data.put("data", "");
			e.printStackTrace();
			out.println(JSONObject.fromObject(data).toString());
		}finally
		{
			s.close();
			sf.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("user_id");
		String shid = request.getParameter("shouhuan_id");
		String relation = request.getParameter("relation");
		Integer power = 0;
		Integer administor = 0;
		
		try{
			power = Integer.valueOf(request.getParameter("power"));
			administor = Integer.valueOf(request.getParameter("administor"));
		}catch(NumberFormatException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("Relation: "+id+" "+shid+" "+relation+" "+power+" "+administor);
		response.setContentType("text/x-json");
		
		PrintWriter out = response.getWriter();
		Map<String, String> data = new HashMap<String, String>();
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		
		try{
			
			Relation rel = new Relation();
			rel.setAdministor(administor);
			rel.setPower(power);
			rel.setRelation(relation);
			rel.setShouhuan_id(shid);
			rel.setUser_id(id);
			
			s.save(rel);
			t.commit();
			
			data.put("code","100");
			data.put("msg", "添加数据成功");
			data.put("data", "");
			
			out.println(JSONObject.fromObject(data).toString());
		}catch(Exception e)
		{
			t.rollback();
			data.put("code","200");
			data.put("msg", "添加数据失败");
			data.put("data", "");
			e.printStackTrace();
			out.println(JSONObject.fromObject(data).toString());
		}finally
		{
			s.close();
			sf.close();
		}
	}

}
