package com.platform.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.platform.model.*;

/**
 * Servlet implementation class Historyrecord
 */
@WebServlet("/historyrecord")
public class HistoryrecordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HistoryrecordServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("shouhuan_id");
//		Date time = null;
//		SimpleDateFormat time_fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		try{
//			time = time_fmt.parse(request.getParameter("time"));
//		}catch(Exception e)
//		{
//			System.out.println("Crossfence: time format error!");
//			e.printStackTrace();
//			time = new Date();
//		}
//		
//		System.out.println("Shouhuanid: "+id+" time"+time);
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
	
		try{
			SQLQuery query = s.createSQLQuery("select * from historyrecord where shouhuan_id=?");
			query.addEntity(Historyrecord.class);
			query.setParameter(0, id);
			List list = query.list();
			JSONArray arr = new JSONArray();
			for(int i=0;i<list.size();i++)
				arr.add(list.get(i));
			data.put("code","100");
			data.put("msg", "��ȡ���ݳɹ�");
			data.put("data", arr.toString());
			
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
		String shid = request.getParameter("shouhuan_id");
		String fid = request.getParameter("from_id");
		String time = request.getParameter("time");
		Integer type = 0;
		String url = request.getParameter("record_url");
		
		try{
			type = Integer.valueOf(request.getParameter("type"));
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println("Historyrecord: "+shid+" "+fid+" "+time+" "+type+" "+url);
		response.setContentType("text/x-json");
		
		PrintWriter out = response.getWriter();
		Map<String, String> data = new HashMap<String, String>();
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		
		try{
			
			Historyrecord hr = new Historyrecord();
			hr.setFrom_id(fid);
			hr.setFrom_type(type);
			hr.setRecord_url(url);
			hr.setShouhuan_id(shid);
			hr.setTime(time);
			
			s.save(hr);
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
		String id = request.getParameter("shouhuan_id");
		System.out.println("Historyrecord(delete): "+id);
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
			SQLQuery query = s.createSQLQuery("delete from historyrecord where shouhuan_id=?");
			query.addEntity(Historyrecord.class);
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
