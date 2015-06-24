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
 * Servlet implementation class FenceServlet
 */
@WebServlet("/fence")
public class FenceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FenceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("fence_id");
		System.out.println("Fence: "+id);
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
		Integer id_i = Integer.valueOf(id);
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session s = sf.openSession();
	
		try{
			SQLQuery query = s.createSQLQuery("select * from fence where fence_id="+id_i).addEntity(Fence.class);
			Fence fec = (Fence) query.uniqueResult();
			
			data.put("code","100");
			data.put("msg", "��ȡ���ݳɹ�");
			data.put("data", JSONObject.fromObject(fec).toString());
			
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
		String fence = request.getParameter("fence");
		String fence_name = request.getParameter("fence_name");
		String sh_id = request.getParameter("shouhuan_id");
		String us_id = request.getParameter("user_id");
		
		Integer id = 0;
		Integer sign = 0;
		
		try{
			id = Integer.valueOf(request.getParameter("fence_id"));
			sign = Integer.valueOf(request.getParameter("sign"));
		}catch(NumberFormatException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("Fence: "+fence+" "+fence_name+" "+sh_id+" "+us_id+" "+id+" "+sign);
		response.setContentType("text/x-json");
		
		PrintWriter out = response.getWriter();
		Map<String, String> data = new HashMap<String, String>();
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		
		try{
			
			Fence fen = new Fence();
			
			fen.setFence(fence);
			fen.setFence_id(id);
			fen.setFence_name(fence_name);
			fen.setShouhuan_id(sh_id);
			fen.setUser_id(us_id);
			fen.setSign(sign);
			
			s.save(fen);
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

}
