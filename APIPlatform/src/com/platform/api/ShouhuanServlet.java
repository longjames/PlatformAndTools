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
import org.hibernate.Transaction;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.platform.model.*;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class ShouhuanServlet
 */
@WebServlet("/shouhuan")
public class ShouhuanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShouhuanServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("id");
		System.out.println("Shouhuan_id: "+id);
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
			SQLQuery query = s.createSQLQuery("select * from shouhuan where shouhuan_id="+id).addEntity(Shouhuan.class);
			Shouhuan hand = (Shouhuan) query.uniqueResult();
			System.out.println(hand.getBirthday());
			
			data.put("code","100");
			data.put("msg", "��ȡ���ݳɹ�");
			data.put("data", JSONObject.fromObject(hand).toString());
			
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
		String id = request.getParameter("shouhuan_id");
		String birth = request.getParameter("birthday");
		String url = request.getParameter("headiconurl");
		Integer sex = 0;
		Integer tmprgt = 0;
		Integer ver = 0;
		String nick = request.getParameter("nickname");
		String name = request.getParameter("name");
		String reg = request.getParameter("registrationdate");
		
		try{
			sex = Integer.valueOf(request.getParameter("sex"));
			tmprgt = Integer.valueOf(request.getParameter("temporaryright"));
			ver = Integer.valueOf(request.getParameter("currentversion"));
		}catch(NumberFormatException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("Shouhuan: "+id+" "+birth+" "+url+" "+sex+" "+tmprgt+" "+ver+" "+nick+" "+name+" "+reg);
		response.setContentType("text/x-json");
		
		PrintWriter out = response.getWriter();
		Map<String, String> data = new HashMap<String, String>();
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		
		try{
			
			Shouhuan shouhuan = new Shouhuan();
			shouhuan.setShouhuan_id(id);
			shouhuan.setBirthday(birth);
			shouhuan.setHeadiconurl(url);
			shouhuan.setSex(sex);
			shouhuan.setName(name);
			shouhuan.setNickname(nick);
			shouhuan.setTemporaryright(tmprgt);
			shouhuan.setRegistrationdate(reg);
			shouhuan.setCurrentversion(ver);
			
			s.save(shouhuan);
			t.commit();
			
			data.put("code","100");
			data.put("msg", "�������ݳɹ�");
			data.put("data", "");
			
			out.println(JSONObject.fromObject(data).toString());
		}catch(Exception e)
		{
			t.rollback();
			data.put("code","200");
			data.put("msg", "��������ʧ��");
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