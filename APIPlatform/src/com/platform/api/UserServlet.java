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
 * Servlet implementation class UserServlet
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userid = request.getParameter("user_id");
		System.out.println("User: "+userid);
		response.setContentType("text/x-json");
		
		PrintWriter out = response.getWriter();
		Map<String, String> data = new HashMap<String, String>();
		
		if(userid==null || userid.equals(""))
		{
			data.put("code","200");
			data.put("msg", "��ȡ����ʧ��");
			data.put("data", "");
			out.println(JSONObject.fromObject(data).toString());
			return;
		}
		
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session s = sf.openSession();
	
		try{
			SQLQuery query = s.createSQLQuery("select * from user where user_id="+userid).addEntity(User.class);
			User info = (User) query.uniqueResult();
			System.out.println(info.getUser_id()+" "+info.getPassword());
			
			data.put("code","100");
			data.put("msg", "��ȡ���ݳɹ�");
			data.put("data", JSONObject.fromObject(info).toString());
			
			out.println(JSONObject.fromObject(data).toString());
		}catch(Exception e)
		{
			data.put("code","200");
			data.put("msg", "��ȡ����ʧ��");
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
		String pass = request.getParameter("password");
		
		System.out.println("User: "+id+" "+pass);
		response.setContentType("text/x-json");
		
		PrintWriter out = response.getWriter();
		Map<String, String> data = new HashMap<String, String>();
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		
		try{
			
			User info = new User();
			info.setUser_id(id);
			info.setPassword(pass);
			
			s.save(info);
			t.commit();
			
			data.put("code","100");
			data.put("msg", "������ݳɹ�");
			data.put("data", "");
			
			out.println(JSONObject.fromObject(data).toString());
		}catch(Exception e)
		{
			t.rollback();
			data.put("code","200");
			data.put("msg", "�������ʧ��");
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
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("user_id");
		System.out.println("User(delete): "+id);
		response.setContentType("text/x-json");
		
		PrintWriter out = response.getWriter();
		Map<String, String> data = new HashMap<String, String>();
		
		if(id==null || id.equals(""))
		{
			data.put("code","200");
			data.put("msg", "��ȡ����ʧ��");
			data.put("data", "");
			out.println(JSONObject.fromObject(data).toString());
			return;
		}
		
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
	
		try{
			SQLQuery query = s.createSQLQuery("delete from user where user_id=?");
			query.addEntity(User.class);
			query.setParameter(0, id);
			query.executeUpdate();
			t.commit();
			
			data.put("code","100");
			data.put("msg", "��ȡ���ݳɹ�");
			data.put("data", "");
			
			out.println(JSONObject.fromObject(data).toString());
		}catch(Exception e)
		{
			data.put("code","200");
			data.put("msg", "��ȡ����ʧ��");
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
