package com.platform.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

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
 * Servlet implementation class CrossfenceServlet
 */
@WebServlet("/CrossfenceServlet")
public class CrossfenceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrossfenceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("fence_id");
		Date time = null;
		SimpleDateFormat time_fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			time = time_fmt.parse(request.getParameter("time"));
		}catch(Exception e)
		{
			System.out.println("Crossfence: time format error!");
			e.printStackTrace();
			time = new Date();
		}
		
		System.out.println("Fence_id: "+id);
		response.setContentType("text/x-json");
		
		PrintWriter out = response.getWriter();
		Map<String, String> data = new HashMap<String, String>();
		
		if(id==null || id.equals(""))
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
			SQLQuery query = s.createSQLQuery("select * from cross_fence where fence_id=? and time=?");
			query.addEntity(Cross_fence.class);
			query.setParameter(0, id);
			query.setParameter(1, time_fmt.format(time));
			Cross_fence info = (Cross_fence) query.uniqueResult();
			
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
		Integer id = 0;
		Integer inout = 0;
		Date time = null;
		SimpleDateFormat time_fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try{
			id = Integer.valueOf(request.getParameter("fence_id"));
		}catch(NumberFormatException e)
		{
			e.printStackTrace();
		}
		
		try{
			time = time_fmt.parse(request.getParameter("time"));
		}catch(Exception e)
		{
			System.out.println("Cross_fence: time format error");
			e.printStackTrace();
			time = new Date();
		}
		
		System.out.println("CrossFence: "+id+" "+inout+" "+time);
		response.setContentType("text/x-json");
		
		PrintWriter out = response.getWriter();
		Map<String, String> data = new HashMap<String, String>();
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		
		try{
			
			Cross_fence  cf = new Cross_fence();
			cf.setTime(time);
			cf.setIn_Out(inout);
			cf.setFence_id(id);
			
			s.save(cf);
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
