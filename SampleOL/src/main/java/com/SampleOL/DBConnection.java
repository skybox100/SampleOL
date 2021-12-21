package com.SampleOL;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.Date;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import java.io.*;


public class DBConnection {
	
	String url = "jdbc:sqlserver://110.10.130.51;databaseName=SmartDB";
	String url2 = "jdbc:sqlserver://110.10.130.51;databaseName=SmartPushDB";

	String user = "sa";
	String password = "todkagh123!";
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
	
	Connection con = null;

	Statement stmt = null;

	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;


	ResultSet rs = null;
	ResultSet rs2 = null;


	String sqlLastLocation = "select * from dbo.Locations where Timestamp in (select max(Timestamp) from dbo.Locations)";

	String sqlEquipSelect = "select * from dbo.TotalEquip where Regiment=?";
		
	static {
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			System.out.println("Driver Okay");
		} catch(Exception e) {
			e.printStackTrace();
		}
	
	}
		
	public Connection getConn() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}	
	
	public Connection getConn2() throws SQLException {
		return DriverManager.getConnection(url2, user, password);
	}	
		
	
	public  String loginCheck(String user_id, String user_pw) {
		String sql = "select * from dbo.PersonnelManagement where ServiceNumber = '" + user_id +"'";
	
		String id="false";
		try {
			
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) // 결과값을 하나씩 가져와서 저장하기 위한 while문
		    {
				id = rs.getString("ServiceNumber"); //DB에 있는 ID가져옴	
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}		
			return id;
	}
	
    public String sendPostJson(String sendUrl, String jsonValue) throws Exception {
        //JSON 데이터 받을 URL 객체 생성
        URL url = new URL (sendUrl);
        //HttpURLConnection 객체를 생성해 openConnection 메소드로 url 연결
        HttpURLConnection con2 = (HttpURLConnection) url.openConnection();
        //전송 방식 (POST)
        con2.setRequestMethod("POST");
        //application/json 형식으로 전송, Request body를 JSON으로 던져줌.
        con2.setRequestProperty("Content-Type", "application/json; utf-8");
        //Response data를 JSON으로 받도록 설정
        con2.setRequestProperty("Accept", "application/json");
        //Output Stream을 POST 데이터로 전송
        con2.setDoOutput(true);
        //json data
        String jsonInputString = jsonValue;
        System.out.println(jsonInputString);
        //JSON 보내는 Output stream
        try(OutputStream os = con2.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
 
        //Response data 받는 부분
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con2.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
        
            }
            System.out.println(response.toString());
            return response.toString();
        }
    }
   
    public String phone(String src) {
        if (src == null) {
          return "";
        }
        if (src.length() == 8) {
          return src.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (src.length() == 12) {
          return src.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }
        return src.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
      }
    

    
    public static String searchDateConvert(String date_s, String format) {
    	String newstring = "";
    	try {
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
			Date date = dt.parse(date_s); 
			newstring = new SimpleDateFormat(format).format(date);
			return newstring;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return newstring;
    }
    
	
    
	public ArrayList<Food> getFoodList(String reg, String sh,String fd,String od,String sc) {
		
		String sql = "";
		Food food = null;
		ArrayList<Food> foods = new ArrayList<Food>();
	//	JSONArray jsonLocations = new JSONArray();

		try {
			con = getConn();				
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			String order="	order by Regiment,storehouse, foodCode,expirationDate; ";
			if(od.equals("식재료명"))order="	order by foodName; ";
			else if(od.equals("입고일자"))order="	order by storeDate desc; ";
			else if(od.equals("유통기한"))order="	order by expirationDate desc; ";	
			else if(od.equals("재고번호"))order="	order by foodCode; ";	
			

			if(sc != "" && reg.equals("소속:전체") && fd.equals("식재료명:전체")) {
				sql = "select Regiment, c.CodeName as RegimentName,storehouse,a.CodeName as storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSourceName,foodSource,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	left outer join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	left outer join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	left outer join dbo.Code as c on f.Regiment = c.CodeID "
						+ " where c.CodeName like '%"+sc+"%' or a.CodeName like '%"+sc+"%' or  foodName like '%"+sc+"%' or foodCode like '%"+sc+"%'"
						+ order;

			}else if(sc != "" && fd.equals("식재료명:전체") && sh.equals("식당명:전체")) {
				sql = "select Regiment, c.CodeName as RegimentName,storehouse,a.CodeName as storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSourceName,foodSource,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	left outer join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	left outer join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	left outer join dbo.Code as c on f.Regiment = c.CodeID "
						+ " where f.Regiment = '"+reg+"' and "
						+ " ( c.CodeName like '%"+sc+"%' or a.CodeName like '%"+sc+"%' or  foodName like '%"+sc+"%' or foodCode like '%"+sc+"%')"
						+ order;

			}else if(sc != "" && fd.equals("식재료명:전체")) {
				sql = "select Regiment, c.CodeName as RegimentName,storehouse,a.CodeName as storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSourceName,foodSource,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	left outer join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	left outer join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	left outer join dbo.Code as c on f.Regiment = c.CodeID "
						+ " where (f.Regiment = '"+reg+"' and f.storehouse = '"+sh+"') and"
						+ " ( c.CodeName like '%"+sc+"%' or a.CodeName like '%"+sc+"%' or  foodName like '%"+sc+"%' or foodCode like '%"+sc+"%')"
						+ order;

			}else if(sc != "" && reg.equals("소속:전체")) {
				sql = "select Regiment, c.CodeName as RegimentName,storehouse,a.CodeName as storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSourceName,foodSource,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	left outer join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	left outer join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	left outer join dbo.Code as c on f.Regiment = c.CodeID "
						+ " where f.foodName = '"+fd+"' and "
						+ " ( c.CodeName like '%"+sc+"%' or a.CodeName like '%"+sc+"%' or  foodName like '%"+sc+"%' or foodCode like '%"+sc+"%')"
						+ order;

			}else if(sc != "" && sh.equals("식당명:전체")) {
				sql = "select Regiment, c.CodeName as RegimentName,storehouse,a.CodeName as storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSourceName,foodSource,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	left outer join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	left outer join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	left outer join dbo.Code as c on f.Regiment = c.CodeID "
						+ " where (f.Regiment = '"+reg+"' and f.foodName = '"+fd+"') and"
						+ " ( c.CodeName like '%"+sc+"%' or a.CodeName like '%"+sc+"%' or  foodName like '%"+sc+"%' or foodCode like '%"+sc+"%')"
						+ order;

			}else if(sc != "" ) {
				sql = "select Regiment, c.CodeName as RegimentName,storehouse,a.CodeName as storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSourceName,foodSource,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	left outer join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	left outer join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	left outer join dbo.Code as c on f.Regiment = c.CodeID "
						+ " where (f.Regiment = '"+reg+"' and f.foodName = '"+fd+"' and f.storehouse = '"+sh+"') and"
						+ " ( c.CodeName like '%"+sc+"%' or a.CodeName like '%"+sc+"%' or  foodName like '%"+sc+"%' or foodCode like '%"+sc+"%')"
						+ order;

			}else if(reg.equals("소속:전체") && sh.equals("식당명:전체") && fd.equals("식재료명:전체")) {
				sql = "select Regiment, c.CodeName as RegimentName,storehouse,a.CodeName as storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSourceName,foodSource,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	left outer join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	left outer join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	left outer join dbo.Code as c on f.Regiment = c.CodeID "
						+ order;

			}else if(reg.equals("소속:전체") && fd.equals("식재료명:전체")) {
				sql = "select Regiment, c.CodeName as RegimentName,storehouse,a.CodeName as storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSourceName,foodSource,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	left outer join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	left outer join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	left outer join dbo.Code as c on f.Regiment = c.CodeID "
						+ " where f.storehouse = '"+sh+"'"
						+ order;
			}else if(sh.equals("식당명:전체") && fd.equals("식재료명:전체")) {
				sql = "select Regiment, c.CodeName as RegimentName,storehouse,a.CodeName as storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSourceName,foodSource,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	left outer join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	left outer join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	left outer join dbo.Code as c on f.Regiment = c.CodeID "
						+ " where f.Regiment = '"+reg+"'"
						+ order;
			}else if(sh.equals("식당명:전체") && reg.equals("소속:전체")) {
				sql = "select Regiment, c.CodeName as RegimentName,storehouse,a.CodeName as storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSourceName,foodSource,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	left outer join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	left outer join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	left outer join dbo.Code as c on f.Regiment = c.CodeID "
						+ " where f.foodName = '"+fd+"'"
						+ order;
			}else if(reg.equals("소속:전체")) {
				sql = "select Regiment, c.CodeName as RegimentName,storehouse,a.CodeName as storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSourceName,foodSource,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	left outer join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	left outer join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	left outer join dbo.Code as c on f.Regiment = c.CodeID "
						+ " where f.storehouse = '"+sh+"' and f.foodName = '"+fd+"'"
						+ order;
			}else if(sh.equals("식당명:전체")) {
				sql = "select Regiment, c.CodeName as RegimentName,storehouse,a.CodeName as storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSourceName,foodSource,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	left outer join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	left outer join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	left outer join dbo.Code as c on f.Regiment = c.CodeID "
						+ " where f.Regiment = '"+reg+"' and f.foodName = '"+fd+"'"
						+ order;
			}else if(fd.equals("식재료명:전체")) {
				sql = "select Regiment, c.CodeName as RegimentName,storehouse,a.CodeName as storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSourceName,foodSource,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	left outer join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	left outer join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	left outer join dbo.Code as c on f.Regiment = c.CodeID "
						+ " where f.Regiment = '"+reg+"' and f.storehouse = '"+sh+"'"
						+ order;
			}else {
				sql = "select Regiment, c.CodeName as RegimentName,storehouse,a.CodeName as storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSourceName,foodSource,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	left outer join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	left outer join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	left outer join dbo.Code as c on f.Regiment = c.CodeID "
						+ " where f.Regiment = '"+reg+"' and f.storehouse = '"+sh+"' and f.foodName = '"+fd+"'"
						+ order;
			}
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String RegimentName = rs.getString("RegimentName") == null ? "":rs.getString("RegimentName");
				String storehouse = rs.getString("storehouse");
				String storehouseName = rs.getString("storehouseName");
				String foodCode = rs.getString("foodCode");
				String expirationDate = searchDateConvert(rs.getString("expirationDate"),"yyyy-MM-dd");
				String foodName = rs.getString("foodName");
				String storeDate = searchDateConvert(rs.getString("storeDate"),"yyyy-MM-dd");
				int currentQuantity = rs.getInt("currentQuantity");
				String unit = rs.getString("unit");
				String foodSource = rs.getString("foodSource");
				String foodSourceName = rs.getString("foodSourceName");
				long qRcodeIdx = rs.getLong("qRcodeIdx");
				String remark = rs.getString("remark");

				food= new Food(Regiment,RegimentName,storehouse,storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,foodSource,foodSourceName,qRcodeIdx,remark);
				//System.out.println(RegimentName+","+Regiment+","+storehouseName+","+storehouse+","+foodCode+","+expirationDate+","+foodName+","+storeDate+","+currentQuantity+","+unit+","+foodSource+","+foodSourceName+","+qRcodeIdx+","+remark);
				foods.add(food);
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}		
		return foods;

	}
	
	public ArrayList<Food> getFoodinfo(String reg, String sh,String fd,String ed) {
		
		String sql = "";
		Food food = null;
		ArrayList<Food> foods = new ArrayList<Food>();
	//	JSONArray jsonLocations = new JSONArray();

		try {
			con = getConn();				
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			

				sql = "select Regiment, c.CodeName as RegimentName,storehouse,a.CodeName as storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSourceName,foodSource,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	left outer join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	left outer join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	left outer join dbo.Code as c on f.Regiment = c.CodeID "
						+ " where f.Regiment = ? and f.storehouse = ? and f.foodCode = ? and f.expirationDate = ?";
	
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reg);
			pstmt.setString(2, sh);
			pstmt.setString(3, fd);
			pstmt.setString(4, ed);

			rs = pstmt.executeQuery();
		
			while(rs.next()) {
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String RegimentName = rs.getString("RegimentName") == null ? "":rs.getString("RegimentName");
				String storehouse = rs.getString("storehouse");
				String storehouseName = rs.getString("storehouseName");
				String foodCode = rs.getString("foodCode");
				String expirationDate = searchDateConvert(rs.getString("expirationDate"),"yyyy-MM-dd");
				String foodName = rs.getString("foodName");
				String storeDate = searchDateConvert(rs.getString("storeDate"),"yyyy-MM-dd");
				int currentQuantity = rs.getInt("currentQuantity");
				String unit = rs.getString("unit");
				String foodSource = rs.getString("foodSource");
				String foodSourceName = rs.getString("foodSourceName");
				long qRcodeIdx = rs.getLong("qRcodeIdx");
				String remark = rs.getString("remark");

				food= new Food(Regiment,RegimentName,storehouse,storehouseName,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,foodSource,foodSourceName,qRcodeIdx,remark);
				//System.out.println(RegimentName+","+Regiment+","+storehouseName+","+storehouse+","+foodCode+","+expirationDate+","+foodName+","+storeDate+","+currentQuantity+","+unit+","+foodSource+","+foodSourceName+","+qRcodeIdx+","+remark);
				foods.add(food);
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(pstmt != null) pstmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}		
		return foods;

	}
	
    public String MGRSString (String Lat, String Long)
	{ 
    	try {
    		if (Double.parseDouble(Long) < -80) return "Too far South" ; if (Double.parseDouble(Lat) > 84) return "Too far North" ;
    		int c = (int) (1 + Math.floor ((Double.parseDouble(Long)+180)/6));
    		double e = c*6 - 183 ;
    		double k = Double.parseDouble(Lat)*Math.PI/180;
    		double l = Double.parseDouble(Long)*Math.PI/180;
    		double m = e*Math.PI/180;
    		double n = Math.cos (k);
    		double o = 0.006739496819936062*Math.pow (n,2);
    		double p = 40680631590769L/(6356752.314*Math.sqrt(1 + o));
    		double q = Math.tan (k);
    		double r = q*q;
    		double s = (r*r*r) - Math.pow (q,6);
    		double t = l - m;
    		double u = 1.0 - r + o;
    		double v = 5.0 - r + 9*o + 4.0*(o*o);
    		double w = 5.0 - 18.0*r + (r*r) + 14.0*o - 58.0*r*o;
    		double x = 61.0 - 58.0*r + (r*r) + 270.0*o - 330.0*r*o;
    		double y = 61.0 - 479.0*r + 179.0*(r*r) - (r*r*r);
    		double z = 1385.0 - 3111.0*r + 543.0*(r*r) - (r*r*r);
    		double aa = p*n*t + (p/6.0*Math.pow (n,3)*u*Math.pow (t,3)) + (p/120.0*Math.pow (n,5)*w*Math.pow (t,5)) + (p/5040.0*Math.pow (n,7)*y*Math.pow (t,7));
    		double ab = 6367449.14570093*(k - (0.00251882794504*Math.sin (2*k)) + (0.00000264354112*Math.sin (4*k)) - (0.00000000345262*Math.sin (6*k)) + (0.000000000004892*Math.sin (8*k))) + (q/2.0*p*Math.pow (n,2)*Math.pow (t,2)) + (q/24.0*p*Math.pow (n,4)*v*Math.pow (t,4)) + (q/720.0*p*Math.pow (n,6)*x*Math.pow (t,6)) + (q/40320.0*p*Math.pow (n,8)*z*Math.pow (t,8));
    		aa = aa*0.9996 + 500000.0;
    		ab = ab*0.9996; if (ab < 0.0) ab += 10000000.0;
    		char ad = "CDEFGHJKLMNPQRSTUVWXX".charAt ((int) Math.floor (Double.parseDouble(Lat)/8 + 10));
    		double ae = Math.floor (aa/100000);
    		String[] afs ={"ABCDEFGH","JKLMNPQR","STUVWXYZ"};
    		char af = afs[(c-1)%3].charAt((int)ae-1);
    		double ag = Math.floor (ab/100000)%20;
    		String[] ahs ={"ABCDEFGHJKLMNPQRSTUV","FGHJKLMNPQRSTUVABCDE"};
    		char ah = ahs[(c-1)%2].charAt((int)ag);
    		aa = Math.floor (aa%100000);  		
    		ab = Math.floor (ab%100000);
    		return Integer.toString(c) + ad + " " + af + ah + " "  + (int)aa + " " + (int)ab;
    	}catch (Exception e) {
    		return "";
    	}
	
	}
	
	public ArrayList<PersonnelManagement> getPersonnelManagementList(String reg, String rc) {
		
		String sql = "";
		PersonnelManagement personnelmanagement = null;
		ArrayList<PersonnelManagement> personnelmanagements = new ArrayList<PersonnelManagement>();
	//	JSONArray jsonLocations = new JSONArray();

		try {
			con = getConn();				
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			if(reg.equals("소속:전체") && rc.equals("세부소속:전체")) {
				sql="SELECT p.ServiceNumber"
						+ "      ,f.CodeName as MissionType"
						+ "      ,e.CodeName as Rank"
						+ "      ,p.Name"
						+ "      ,c.CodeName as Regiment"
						+ "      ,d.CodeName as regimCompany"
						+ "      ,p.MOS"
						+ "      ,p.Duty"
						+ "      ,p.HelpCare"
						+ "      ,p.BirthDate"
						+ "      ,p.JoinDate"
						+ "      ,p.PromotionDate"
						+ "      ,p.MovingDate"
						+ "      ,p.RetireDate"
						+ "      ,p.MobileNumber"
						+ "      ,p.MyPhoneNumber"
						+ "      ,p.ParentsNumber"
						+ "      ,p.Remark"
						+ "      ,Picture"
						+ "      ,p.Password"
						+ "      ,p.RegimPlatoon"
						+ "      ,p.RegimSquad"
						+ "      ,g.CodeName as LeaderType"
						+ "      ,p.BloodType"
						+ "      ,p.Goout"
						+ "      ,p.Reserve01"
						+ "      ,p.Reserve02"
						+ "      ,p.Reserve03"
						+ "      ,p.Reserve04"
						+ "  FROM dbo.PersonnelManagement p"
						+ "  left outer join dbo.Code as c on p.Regiment = c.CodeID "
						+ "  left outer join dbo.Code as d on p.regimCompany = d.CodeID "
						+ "  left outer join dbo.Code as e on p.Rank = e.CodeID "
						+ "  left outer join dbo.Code as f on p.MissionType = f.CodeID"
						+ "  left outer join dbo.Code as g on p.LeaderType = g.CodeID;";


			}else if(reg.equals("소속:전체")) {
				sql="SELECT p.ServiceNumber"
						+ "      ,f.CodeName as MissionType"
						+ "      ,e.CodeName as Rank"
						+ "      ,p.Name"
						+ "      ,c.CodeName as Regiment"
						+ "      ,d.CodeName as regimCompany"
						+ "      ,p.MOS"
						+ "      ,p.Duty"
						+ "      ,p.HelpCare"
						+ "      ,p.BirthDate"
						+ "      ,p.JoinDate"
						+ "      ,p.PromotionDate"
						+ "      ,p.MovingDate"
						+ "      ,p.RetireDate"
						+ "      ,p.MobileNumber"
						+ "      ,p.MyPhoneNumber"
						+ "      ,p.ParentsNumber"
						+ "      ,p.Remark"
						+ "      ,Picture"
						+ "      ,p.Password"
						+ "      ,p.RegimPlatoon"
						+ "      ,p.RegimSquad"
						+ "      ,g.CodeName as LeaderType"
						+ "      ,p.BloodType"
						+ "      ,p.Goout"
						+ "      ,p.Reserve01"
						+ "      ,p.Reserve02"
						+ "      ,p.Reserve03"
						+ "      ,p.Reserve04"
						+ "  FROM dbo.PersonnelManagement p"
						+ "  left outer join dbo.Code as c on p.Regiment = c.CodeID "
						+ "  left outer join dbo.Code as d on p.regimCompany = d.CodeID "
						+ "  left outer join dbo.Code as e on p.Rank = e.CodeID "
						+ "  left outer join dbo.Code as f on p.MissionType = f.CodeID"
						+ "  left outer join dbo.Code as g on p.LeaderType = g.CodeID"
						+ " where p.regimCompany = '"+rc+"'";
			}else if(rc.equals("세부소속:전체")) {
				sql="SELECT p.ServiceNumber"
						+ "      ,f.CodeName as MissionType"
						+ "      ,e.CodeName as Rank"
						+ "      ,p.Name"
						+ "      ,c.CodeName as Regiment"
						+ "      ,d.CodeName as regimCompany"
						+ "      ,p.MOS"
						+ "      ,p.Duty"
						+ "      ,p.HelpCare"
						+ "      ,p.BirthDate"
						+ "      ,p.JoinDate"
						+ "      ,p.PromotionDate"
						+ "      ,p.MovingDate"
						+ "      ,p.RetireDate"
						+ "      ,p.MobileNumber"
						+ "      ,p.MyPhoneNumber"
						+ "      ,p.ParentsNumber"
						+ "      ,p.Remark"
						+ "      ,Picture"
						+ "      ,p.Password"
						+ "      ,p.RegimPlatoon"
						+ "      ,p.RegimSquad"
						+ "      ,g.CodeName as LeaderType"
						+ "      ,p.BloodType"
						+ "      ,p.Goout"
						+ "      ,p.Reserve01"
						+ "      ,p.Reserve02"
						+ "      ,p.Reserve03"
						+ "      ,p.Reserve04"
						+ "  FROM dbo.PersonnelManagement p"
						+ "  left outer join dbo.Code as c on p.Regiment = c.CodeID "
						+ "  left outer join dbo.Code as d on p.regimCompany = d.CodeID "
						+ "  left outer join dbo.Code as e on p.Rank = e.CodeID "
						+ "  left outer join dbo.Code as f on p.MissionType = f.CodeID"
						+ "  left outer join dbo.Code as g on p.LeaderType = g.CodeID"
						+ " where p.Regiment = '"+reg+"'";
			}else {
				sql="SELECT p.ServiceNumber"
						+ "      ,f.CodeName as MissionType"
						+ "      ,e.CodeName as Rank"
						+ "      ,p.Name"
						+ "      ,c.CodeName as Regiment"
						+ "      ,d.CodeName as regimCompany"
						+ "      ,p.MOS"
						+ "      ,p.Duty"
						+ "      ,p.HelpCare"
						+ "      ,p.BirthDate"
						+ "      ,p.JoinDate"
						+ "      ,p.PromotionDate"
						+ "      ,p.MovingDate"
						+ "      ,p.RetireDate"
						+ "      ,p.MobileNumber"
						+ "      ,p.MyPhoneNumber"
						+ "      ,p.ParentsNumber"
						+ "      ,p.Remark"
						+ "      ,Picture"
						+ "      ,p.Password"
						+ "      ,p.RegimPlatoon"
						+ "      ,p.RegimSquad"
						+ "      ,g.CodeName as LeaderType"
						+ "      ,p.BloodType"
						+ "      ,p.Goout"
						+ "      ,p.Reserve01"
						+ "      ,p.Reserve02"
						+ "      ,p.Reserve03"
						+ "      ,p.Reserve04"
						+ "  FROM dbo.PersonnelManagement p"
						+ "  left outer join dbo.Code as c on p.Regiment = c.CodeID "
						+ "  left outer join dbo.Code as d on p.regimCompany = d.CodeID "
						+ "  left outer join dbo.Code as e on p.Rank = e.CodeID "
						+ "  left outer join dbo.Code as f on p.MissionType = f.CodeID"
						+ "  left outer join dbo.Code as g on p.LeaderType = g.CodeID"
						+ " where p.Regiment = '"+reg+"' and p.regimCompany = '"+rc+"'";
			}
			
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String ServiceNumber = rs.getString("ServiceNumber");
				String MissionType = rs.getString("MissionType");
				String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
				String Name = rs.getString("Name");
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
				String MOS = rs.getString("MOS");
				String Duty = rs.getString("Duty");
				String HelpCare = rs.getString("HelpCare");
				String BirthDate = searchDateConvert(rs.getString("BirthDate"),"yyyy-MM-dd");
				String JoinDate = searchDateConvert(rs.getString("JoinDate"),"yyyy-MM-dd");
				String PromotionDate = searchDateConvert(rs.getString("PromotionDate"),"yyyy-MM-dd");
				String MovingDate = searchDateConvert(rs.getString("MovingDate"),"yyyy-MM-dd");
				String RetireDate = searchDateConvert(rs.getString("RetireDate"),"yyyy-MM-dd");
				String MobileNumber = phone(rs.getString("MobileNumber"));
				String MyPhoneNumber = phone(rs.getString("MyPhoneNumber"));
				String ParentsNumber = phone(rs.getString("ParentsNumber"));
				String Remark = rs.getString("Remark");
				String Password = rs.getString("Password");
				String RegimPlatoon = rs.getString("RegimPlatoon");
				String RegimSquad = rs.getString("RegimSquad");
				String LeaderType = rs.getString("LeaderType");
				String BloodType = rs.getString("BloodType");
				String Goout = rs.getString("Goout");
				String Reserve01 = rs.getString("Reserve01");
				String Reserve02 = rs.getString("Reserve02");
				String Reserve03 = rs.getString("Reserve03");
				String Reserve04 = rs.getString("Reserve04");
				String Picture = OutputPicture(rs.getBinaryStream("Picture"));

				personnelmanagement= new PersonnelManagement(ServiceNumber,MissionType,Rank,Name,Regiment,regimCompany,MOS,Duty,HelpCare,BirthDate,JoinDate,PromotionDate,MovingDate,RetireDate,MobileNumber,MyPhoneNumber,ParentsNumber,Remark,Picture,Password,RegimPlatoon,RegimSquad,LeaderType,BloodType,Goout,Reserve01,Reserve02,Reserve03,Reserve04);
				personnelmanagements.add(personnelmanagement);
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}		
		return personnelmanagements;

	}

	
	public ArrayList<Beacons> getBeaconsList(String reg, String rc,String eq) {
		
		String sql = "";
		ArrayList<Beacons> beacons = new ArrayList<Beacons>();
		Beacons beacon = null;
		String search="";
		
		
		try {

			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			
			if(eq != "" && reg.equals("소속:전체") && rc.equals("세부소속:전체")) {
				sql="SELECT Uuid "
						+ "      ,Latitude "
						+ "      ,Longitude "
						+ "      ,d.CodeName as EquipType "
						+ "	  ,EquipType as EquipTypeCode "
						+ "      ,EquipId "
						+ "      ,ModelName "
						+ "      ,Manufacturer "
						+ "      ,a.CodeName as Regiment "
						+ "	  ,Regiment as RegimentCode "
						+ "      ,c.CodeName as regimCompany "
						+ "	  ,regimCompany as regimCompanyCode "
						+ "      ,EquipLocation "
						+ "      ,RoomName "
						+ "      ,RoomNumber "
						+ "      ,b.Remark "
						+ "  FROM dbo.Beacons b "
						+ "  left outer join dbo.code as a on a.CodeID = b.Regiment and a.CodeType ='Regiment' "
						+ "  left outer join dbo.code as c on c.CodeID = b.regimCompany and c.CodeType ='regimCompany' "
						+ "  left outer join dbo.code as d on d.CodeID = b.EquipType and d.CodeType ='EquipType' "
						+ "  left outer join dbo.code as e on e.CodeID = b.EquipType and e.CodeType ='EquipType' "
						+ "  where RoomName like '%"+eq+"%' or EquipId like '%"+eq+"%' or RoomNumber like '%"+eq+"%' or EquipLocation like '%"+eq+"%' "
						+ "  order by b.EquipId,b.Uuid";
			}else if(eq != "" && reg.equals("소속:전체")) {
				sql="SELECT Uuid "
						+ "      ,Latitude "
						+ "      ,Longitude "
						+ "      ,d.CodeName as EquipType "
						+ "	  ,EquipType as EquipTypeCode "
						+ "      ,EquipId "
						+ "      ,ModelName "
						+ "      ,Manufacturer "
						+ "      ,a.CodeName as Regiment "
						+ "	  ,Regiment as RegimentCode "
						+ "      ,c.CodeName as regimCompany "
						+ "	  ,regimCompany as regimCompanyCode "
						+ "      ,EquipLocation "
						+ "      ,RoomName "
						+ "      ,RoomNumber "
						+ "      ,b.Remark "
						+ "  FROM dbo.Beacons b "
						+ "  left outer join dbo.code as a on a.CodeID = b.Regiment and a.CodeType ='Regiment' "
						+ "  left outer join dbo.code as c on c.CodeID = b.regimCompany and c.CodeType ='regimCompany' "
						+ "  left outer join dbo.code as d on d.CodeID = b.EquipType and d.CodeType ='EquipType' "
						+ "  left outer join dbo.code as e on e.CodeID = b.EquipType and e.CodeType ='EquipType' "
						+ " where b.regimCompany = '"+rc+"'"
						+ "  and (RoomName like '%"+eq+"%' or EquipId like '%"+eq+"%' or RoomNumber like '%"+eq+"%' or EquipLocation like '%"+eq+"%' )"

						+ "  order by b.EquipId,b.Uuid";

			}else if(eq != "" && rc.equals("세부소속:전체")) {
				sql="SELECT Uuid "
						+ "      ,Latitude "
						+ "      ,Longitude "
						+ "      ,d.CodeName as EquipType "
						+ "	  ,EquipType as EquipTypeCode "
						+ "      ,EquipId "
						+ "      ,ModelName "
						+ "      ,Manufacturer "
						+ "      ,a.CodeName as Regiment "
						+ "	  ,Regiment as RegimentCode "
						+ "      ,c.CodeName as regimCompany "
						+ "	  ,regimCompany as regimCompanyCode "
						+ "      ,EquipLocation "
						+ "      ,RoomName "
						+ "      ,RoomNumber "
						+ "      ,b.Remark "
						+ "  FROM dbo.Beacons b "
						+ "  left outer join dbo.code as a on a.CodeID = b.Regiment and a.CodeType ='Regiment' "
						+ "  left outer join dbo.code as c on c.CodeID = b.regimCompany and c.CodeType ='regimCompany' "
						+ "  left outer join dbo.code as d on d.CodeID = b.EquipType and d.CodeType ='EquipType' "
						+ "  left outer join dbo.code as e on e.CodeID = b.EquipType and e.CodeType ='EquipType' "
						+ " where b.Regiment = '"+reg+"'"
						+ "  and (RoomName like '%"+eq+"%' or EquipId like '%"+eq+"%' or RoomNumber like '%"+eq+"%' or EquipLocation like '%"+eq+"%' )"

						+ "  order by b.EquipId,b.Uuid";

			}else if(eq != ""){
				sql="SELECT Uuid "
						+ "      ,Latitude "
						+ "      ,Longitude "
						+ "      ,d.CodeName as EquipType "
						+ "	  ,EquipType as EquipTypeCode "
						+ "      ,EquipId "
						+ "      ,ModelName "
						+ "      ,Manufacturer "
						+ "      ,a.CodeName as Regiment "
						+ "	  ,Regiment as RegimentCode "
						+ "      ,c.CodeName as regimCompany "
						+ "	  ,regimCompany as regimCompanyCode "
						+ "      ,EquipLocation "
						+ "      ,RoomName "
						+ "      ,RoomNumber "
						+ "      ,b.Remark "
						+ "  FROM dbo.Beacons b "
						+ "  left outer join dbo.code as a on a.CodeID = b.Regiment and a.CodeType ='Regiment' "
						+ "  left outer join dbo.code as c on c.CodeID = b.regimCompany and c.CodeType ='regimCompany' "
						+ "  left outer join dbo.code as d on d.CodeID = b.EquipType and d.CodeType ='EquipType' "
						+ "  left outer join dbo.code as e on e.CodeID = b.EquipType and e.CodeType ='EquipType' "
						+ " where (b.Regiment = '"+reg+"' and b.regimCompany = '"+rc+"')"
						+ "  and (RoomName like '%"+eq+"%' or EquipId like '%"+eq+"%' or RoomNumber like '%"+eq+"%' or EquipLocation like '%"+eq+"%' )"

						+ "  order by b.EquipId,b.Uuid";
			}else if(reg.equals("소속:전체") && rc.equals("세부소속:전체")) {
				sql="SELECT Uuid "
						+ "      ,Latitude "
						+ "      ,Longitude "
						+ "      ,d.CodeName as EquipType "
						+ "	  ,EquipType as EquipTypeCode "
						+ "      ,EquipId "
						+ "      ,ModelName "
						+ "      ,Manufacturer "
						+ "      ,a.CodeName as Regiment "
						+ "	  ,Regiment as RegimentCode "
						+ "      ,c.CodeName as regimCompany "
						+ "	  ,regimCompany as regimCompanyCode "
						+ "      ,EquipLocation "
						+ "      ,RoomName "
						+ "      ,RoomNumber "
						+ "      ,b.Remark "
						+ "  FROM dbo.Beacons b "
						+ "  left outer join dbo.code as a on a.CodeID = b.Regiment and a.CodeType ='Regiment' "
						+ "  left outer join dbo.code as c on c.CodeID = b.regimCompany and c.CodeType ='regimCompany' "
						+ "  left outer join dbo.code as d on d.CodeID = b.EquipType and d.CodeType ='EquipType' "
						+ "  left outer join dbo.code as e on e.CodeID = b.EquipType and e.CodeType ='EquipType' "
						+ "  order by b.EquipId,b.Uuid";
			}else if(reg.equals("소속:전체")) {
				sql="SELECT Uuid "
						+ "      ,Latitude "
						+ "      ,Longitude "
						+ "      ,d.CodeName as EquipType "
						+ "	  ,EquipType as EquipTypeCode "
						+ "      ,EquipId "
						+ "      ,ModelName "
						+ "      ,Manufacturer "
						+ "      ,a.CodeName as Regiment "
						+ "	  ,Regiment as RegimentCode "
						+ "      ,c.CodeName as regimCompany "
						+ "	  ,regimCompany as regimCompanyCode "
						+ "      ,EquipLocation "
						+ "      ,RoomName "
						+ "      ,RoomNumber "
						+ "      ,b.Remark "
						+ "  FROM dbo.Beacons b "
						+ "  left outer join dbo.code as a on a.CodeID = b.Regiment and a.CodeType ='Regiment' "
						+ "  left outer join dbo.code as c on c.CodeID = b.regimCompany and c.CodeType ='regimCompany' "
						+ "  left outer join dbo.code as d on d.CodeID = b.EquipType and d.CodeType ='EquipType' "
						+ "  left outer join dbo.code as e on e.CodeID = b.EquipType and e.CodeType ='EquipType' "
						+ " where b.regimCompany = '"+rc+"'"
						+ "  order by b.EquipId,b.Uuid";

			}else if(rc.equals("세부소속:전체")) {
				sql="SELECT Uuid "
						+ "      ,Latitude "
						+ "      ,Longitude "
						+ "      ,d.CodeName as EquipType "
						+ "	  ,EquipType as EquipTypeCode "
						+ "      ,EquipId "
						+ "      ,ModelName "
						+ "      ,Manufacturer "
						+ "      ,a.CodeName as Regiment "
						+ "	  ,Regiment as RegimentCode "
						+ "      ,c.CodeName as regimCompany "
						+ "	  ,regimCompany as regimCompanyCode "
						+ "      ,EquipLocation "
						+ "      ,RoomName "
						+ "      ,RoomNumber "
						+ "      ,b.Remark "
						+ "  FROM dbo.Beacons b "
						+ "  left outer join dbo.code as a on a.CodeID = b.Regiment and a.CodeType ='Regiment' "
						+ "  left outer join dbo.code as c on c.CodeID = b.regimCompany and c.CodeType ='regimCompany' "
						+ "  left outer join dbo.code as d on d.CodeID = b.EquipType and d.CodeType ='EquipType' "
						+ "  left outer join dbo.code as e on e.CodeID = b.EquipType and e.CodeType ='EquipType' "
						+ " where b.Regiment = '"+reg+"'"
						+ "  order by b.EquipId,b.Uuid";

			}else {
				sql="SELECT Uuid "
						+ "      ,Latitude "
						+ "      ,Longitude "
						+ "      ,d.CodeName as EquipType "
						+ "	  ,EquipType as EquipTypeCode "
						+ "      ,EquipId "
						+ "      ,ModelName "
						+ "      ,Manufacturer "
						+ "      ,a.CodeName as Regiment "
						+ "	  ,Regiment as RegimentCode "
						+ "      ,c.CodeName as regimCompany "
						+ "	  ,regimCompany as regimCompanyCode "
						+ "      ,EquipLocation "
						+ "      ,RoomName "
						+ "      ,RoomNumber "
						+ "      ,b.Remark "
						+ "  FROM dbo.Beacons b "
						+ "  left outer join dbo.code as a on a.CodeID = b.Regiment and a.CodeType ='Regiment' "
						+ "  left outer join dbo.code as c on c.CodeID = b.regimCompany and c.CodeType ='regimCompany' "
						+ "  left outer join dbo.code as d on d.CodeID = b.EquipType and d.CodeType ='EquipType' "
						+ "  left outer join dbo.code as e on e.CodeID = b.EquipType and e.CodeType ='EquipType' "
						+ " where b.Regiment = '"+reg+"' and b.regimCompany = '"+rc+"'"
						+ "  order by b.EquipId,b.Uuid";

			}
				
				con = getConn();				
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);	

				
				while(rs.next()) {
					String Uuid = rs.getString("Uuid") ;
					String Latitude = rs.getString("Latitude");
					String Longitude = rs.getString("Longitude");
					String EquipType = rs.getString("EquipType")  == null ? "" : rs.getString("EquipType");
					String EquipTypeCode = rs.getString("EquipTypeCode")  == null ? "" : rs.getString("EquipTypeCode");
					String EquipId = rs.getString("EquipId")  == null ? "" : rs.getString("EquipId");
					String ModelName = rs.getString("ModelName")  == null ? "" : rs.getString("ModelName");
					String Manufacturer = rs.getString("Manufacturer")  == null ? "" : rs.getString("Manufacturer");
					String Regiment = rs.getString("Regiment")  == null ? "" : rs.getString("Regiment");
					String RegimentCode = rs.getString("RegimentCode")  == null ? "" : rs.getString("RegimentCode");
					String regimCompany = rs.getString("regimCompany")  == null ? "" : rs.getString("regimCompany");
					String regimCompanyCode = rs.getString("regimCompanyCode")  == null ? "" : rs.getString("regimCompanyCode");
					String EquipLocation = rs.getString("EquipLocation")  == null ? "" : rs.getString("EquipLocation");
					String RoomName = rs.getString("RoomName")  == null ? "" : rs.getString("RoomName");
					String RoomNumber = rs.getString("RoomNumber")  == null ? "" : rs.getString("RoomNumber");
					String Remark = rs.getString("Remark")  == null ? "" : rs.getString("Remark");
					String Mgrs = MGRSString(Latitude,Longitude);
					
					beacon= new Beacons(Uuid,Latitude,Longitude,EquipType,EquipTypeCode,EquipId,ModelName,Manufacturer,Regiment,RegimentCode,regimCompany,regimCompanyCode,EquipLocation,RoomName,RoomNumber,Remark,Mgrs);
					beacons.add(beacon);
					
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}		
		return beacons;

	}
	
	public ArrayList<Beacons> getBeaconById(String beaconId) {
		
		String sql="SELECT Uuid "
				+ "      ,Latitude "
				+ "      ,Longitude "
				+ "      ,d.CodeName as EquipType "
				+ "	  ,EquipType as EquipTypeCode "
				+ "      ,EquipId "
				+ "      ,ModelName "
				+ "      ,Manufacturer "
				+ "      ,a.CodeName as Regiment "
				+ "	  ,Regiment as RegimentCode "
				+ "      ,c.CodeName as regimCompany "
				+ "	  ,regimCompany as regimCompanyCode "
				+ "      ,EquipLocation "
				+ "      ,RoomName "
				+ "      ,RoomNumber "
				+ "      ,b.Remark "
				+ "  FROM dbo.Beacons b  "
				+ "  left outer join dbo.code as a on a.CodeID = b.Regiment and a.CodeType ='Regiment' "
				+ "  left outer join dbo.code as c on c.CodeID = b.regimCompany and c.CodeType ='regimCompany' "
				+ "  left outer join dbo.code as d on d.CodeID = b.EquipType and d.CodeType ='EquipType' "
				+ "  left outer join dbo.code as e on e.CodeID = b.EquipType and e.CodeType ='EquipType' "
				+ "  where b.Uuid = ? ";
		ArrayList<Beacons> beaconLocations = new ArrayList<Beacons>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, beaconId);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {				
				String Uuid = rs.getString("Uuid") ;
				String Latitude = rs.getString("Latitude");
				String Longitude = rs.getString("Longitude");
				String EquipType = rs.getString("EquipType")  == null ? "" : rs.getString("EquipType");
				String EquipTypeCode = rs.getString("EquipTypeCode")  == null ? "" : rs.getString("EquipTypeCode");
				String EquipId = rs.getString("EquipId")  == null ? "" : rs.getString("EquipId");
				String ModelName = rs.getString("ModelName")  == null ? "" : rs.getString("ModelName");
				String Manufacturer = rs.getString("Manufacturer")  == null ? "" : rs.getString("Manufacturer");
				String Regiment = rs.getString("Regiment")  == null ? "" : rs.getString("Regiment");
				String RegimentCode = rs.getString("RegimentCode")  == null ? "" : rs.getString("RegimentCode");
				String regimCompany = rs.getString("regimCompany")  == null ? "" : rs.getString("regimCompany");
				String regimCompanyCode = rs.getString("regimCompanyCode")  == null ? "" : rs.getString("regimCompanyCode");
				String EquipLocation = rs.getString("EquipLocation")  == null ? "" : rs.getString("EquipLocation");
				String RoomName = rs.getString("RoomName")  == null ? "" : rs.getString("RoomName");
				String RoomNumber = rs.getString("RoomNumber")  == null ? "" : rs.getString("RoomNumber");
				String Remark = rs.getString("Remark")  == null ? "" : rs.getString("Remark");
				String Mgrs = MGRSString(Latitude,Longitude);
				
				Beacons b= new Beacons(Uuid,Latitude,Longitude,EquipType,EquipTypeCode,EquipId,ModelName,Manufacturer,Regiment,RegimentCode,regimCompany,regimCompanyCode,EquipLocation,RoomName,RoomNumber,Remark,Mgrs);
							
				//System.out.println(equipId);
				

				//System.out.println(equipLocationObject.toString());
				
				beaconLocations.add(b);
				System.out.println(b.toString());
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(pstmt != null) pstmt.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		
			//for(EquipLocation e:equipLocations) {
			//	System.out.println(e.toString());
			//}
		
		return beaconLocations;
		
	}
	
	public Image loadImage(String file_name) {
		
		InputStream is = null;
		
		
		try {
			is = new FileInputStream(file_name);
			BufferedImage img= ImageIO.read(is);
			return img;
		}catch(Exception e) {
			throw new RuntimeException(e);
		}finally {
			if(is !=null)
				try {
					is.close();
				}catch (IOException e) {
				}
		}
		
	}

public ArrayList<PersonnelManagement> getPersonnelMemberInfo(String sn) {
		
			String sql="SELECT p.ServiceNumber "
					+ "      ,f.CodeName as MissionTypeName "
					+ "      ,p.MissionType "
					+ "      ,p.Rank "
					+ "      ,e.CodeName as RankName "
					+ "      ,p.Name "
					+ "      ,p.Regiment "
					+ "      ,c.CodeName as RegimentName "
					+ "      ,p.regimCompany "
					+ "      ,d.CodeName as regimCompanyName "
					+ "      ,p.MOS "
					+ "      ,p.Duty "
					+ "      ,p.HelpCare "
					+ "      ,p.BirthDate "
					+ "      ,p.JoinDate "
					+ "      ,p.PromotionDate "
					+ "      ,p.MovingDate "
					+ "      ,p.RetireDate "
					+ "      ,p.MobileNumber "
					+ "      ,p.MyPhoneNumber "
					+ "      ,p.ParentsNumber "
					+ "      ,p.Remark "
					+ "      ,Picture "
					+ "      ,p.Password "
					+ "      ,p.RegimPlatoon "
					+ "      ,p.RegimSquad "
					+ "      ,LeaderType "
					+ "      ,g.CodeName as LeaderTypeName "
					+ "      ,p.BloodType "
					+ "      ,p.Goout "
					+ "      ,p.Reserve01 "
					+ "      ,p.Reserve02 "
					+ "      ,p.Reserve03 "
					+ "      ,p.Reserve04 "
					+ "  FROM dbo.PersonnelManagement p "
					+ "  left outer join dbo.Code as c on p.Regiment = c.CodeID "
					+ "  left outer join dbo.Code as d on p.regimCompany = d.CodeID "
					+ "  left outer join dbo.Code as e on p.Rank = e.CodeID "
					+ "  left outer join dbo.Code as f on p.MissionType = f.CodeID "
					+ "  left outer join dbo.Code as g on p.LeaderType = g.CodeID "
					+ "  where p.ServiceNumber = ? ";

		PersonnelManagement personnelmanagement = null;
		ArrayList<PersonnelManagement> personnelmanagements = new ArrayList<PersonnelManagement>();
	//	JSONArray jsonLocations = new JSONArray();

		try {
			con = getConn();				
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");

			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, sn);

			
			rs = pstmt.executeQuery();
			

			
			while(rs.next()) {
				String ServiceNumber = rs.getString("ServiceNumber");
				String MissionType = rs.getString("MissionType");
				String MissionTypeName = rs.getString("MissionTypeName");
				String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
				String RankName = rs.getString("RankName") == null ? "" : rs.getString("RankName");
				String Name = rs.getString("Name");
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String RegimentName = rs.getString("RegimentName") == null ? "":rs.getString("RegimentName");
				String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
				String regimCompanyName = rs.getString("regimCompanyName") == null ? "" : rs.getString("regimCompanyName");
				String MOS = rs.getString("MOS");
				String Duty = rs.getString("Duty");
				String HelpCare = rs.getString("HelpCare");
				String BirthDate = searchDateConvert(rs.getString("BirthDate"),"yyyy-MM-dd");
				String JoinDate = searchDateConvert(rs.getString("JoinDate"),"yyyy-MM-dd");
				String PromotionDate = searchDateConvert(rs.getString("PromotionDate"),"yyyy-MM-dd");
				String MovingDate = searchDateConvert(rs.getString("MovingDate"),"yyyy-MM-dd");
				String RetireDate = searchDateConvert(rs.getString("RetireDate"),"yyyy-MM-dd");
				String MobileNumber = rs.getString("MobileNumber") == null ? "" : rs.getString("MobileNumber");
				String MyPhoneNumber = rs.getString("MyPhoneNumber") == null ? "" : rs.getString("MyPhoneNumber");
				String ParentsNumber = rs.getString("ParentsNumber") == null ? "" : rs.getString("ParentsNumber");
				String Remark = rs.getString("Remark");
				String Password = rs.getString("Password");
				String RegimPlatoon = rs.getString("RegimPlatoon");
				String RegimSquad = rs.getString("RegimSquad");
				String LeaderType = rs.getString("LeaderType");
				String LeaderTypeName = rs.getString("LeaderTypeName");

				String BloodType = rs.getString("BloodType");
				String Goout = rs.getString("Goout");
				String Reserve01 = rs.getString("Reserve01");
				String Reserve02 = rs.getString("Reserve02");
				String Reserve03 = rs.getString("Reserve03");
				String Reserve04 = rs.getString("Reserve04");
				String Picture = OutputPicture(rs.getBinaryStream("Picture"));


				personnelmanagement= new PersonnelManagement(ServiceNumber,MissionType,MissionTypeName,Rank,RankName,Name,Regiment,RegimentName,regimCompany,regimCompanyName,MOS,Duty,HelpCare,BirthDate,JoinDate,PromotionDate,MovingDate,RetireDate,MobileNumber,MyPhoneNumber,ParentsNumber,Remark,Picture,Password,RegimPlatoon,RegimSquad,LeaderType,BloodType,Goout,Reserve01,Reserve02,Reserve03,Reserve04,LeaderTypeName);
				personnelmanagements.add(personnelmanagement);
				
				System.out.println(personnelmanagement.toString());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}		
		return personnelmanagements;

	}

	public String getRegName(String sn) {
	
	String sql="SELECT p.regimCompany "
			+ "      ,d.CodeName as regimCompanyName "
			+ "  FROM dbo.PersonnelManagement p "
			+ "  left outer join dbo.Code as c on p.Regiment = c.CodeID "
			+ "  where p.ServiceNumber = ? ";

	String reg = null;
//	JSONArray jsonLocations = new JSONArray();

	try {
		con = getConn();				
		System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");

		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, sn);
		rs = pstmt.executeQuery();
	while(rs.next()) {
		String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
		String RegimentName = rs.getString("RegimentName") == null ? "":rs.getString("RegimentName");
		reg=RegimentName;
	}
		
	} catch (SQLException e) {
	// TODO Auto-generated catch block
		e.printStackTrace();
	
	
	} finally {
		try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
		try { if(rs != null) rs.close(); } catch(SQLException e) {}
		try { if(con != null) con.close(); } catch(SQLException e) {}
	}		
	return reg;
	
	}

	
	public String getRegId(String sn) {
		
	String sql="SELECT p.Regiment "
			+ "      ,c.CodeName as RegimentName "
			+ "  FROM dbo.PersonnelManagement p "
			+ "  left outer join dbo.Code as c on p.Regiment = c.CodeID "
			+ "  where p.ServiceNumber = ? ";

	String reg = null;
//	JSONArray jsonLocations = new JSONArray();

	try {
		con = getConn();				
		System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");

		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, sn);
		rs = pstmt.executeQuery();
	while(rs.next()) {
		String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
		String RegimentName = rs.getString("RegimentName") == null ? "":rs.getString("RegimentName");
		reg=Regiment;
	}
		
	} catch (SQLException e) {
	// TODO Auto-generated catch block
		e.printStackTrace();
	
	
	} finally {
		try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
		try { if(rs != null) rs.close(); } catch(SQLException e) {}
		try { if(con != null) con.close(); } catch(SQLException e) {}
	}		
	return reg;
	
	}
	
	public String getRegCompayName(String sn) {
		
	String sql="SELECT p.regimCompany "
			+ "      ,d.CodeName as regimCompanyName "
			+ "  FROM dbo.PersonnelManagement p "
			+ "  left outer join dbo.Code as d on p.regimCompany = d.CodeID "
			+ "  where p.ServiceNumber = ? ";

	String rc = null;
//	JSONArray jsonLocations = new JSONArray();

	try {
		con = getConn();				
		System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");

		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, sn);
		rs = pstmt.executeQuery();
	while(rs.next()) {
		String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
		String regimCompanyName = rs.getString("regimCompanyName") == null ? "" : rs.getString("regimCompanyName");
		rc=regimCompanyName;
	}
		
	} catch (SQLException e) {
	// TODO Auto-generated catch block
		e.printStackTrace();
	
	
	} finally {
		try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
		try { if(rs != null) rs.close(); } catch(SQLException e) {}
		try { if(con != null) con.close(); } catch(SQLException e) {}
	}		
	return rc;
	
	}
	public String getRegCompayID(String sn) {
		
	String sql="SELECT p.regimCompany "
			+ "      ,d.CodeName as regimCompanyName "
			+ "  FROM dbo.PersonnelManagement p "
			+ "  left outer join dbo.Code as d on p.regimCompany = d.CodeID "
			+ "  where p.ServiceNumber = ? ";

	String rc = null;
//	JSONArray jsonLocations = new JSONArray();

	try {
		con = getConn();				
		System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");

		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, sn);
		rs = pstmt.executeQuery();
	while(rs.next()) {
		String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
		String regimCompanyName = rs.getString("regimCompanyName") == null ? "" : rs.getString("regimCompanyName");
		rc=regimCompany;
	}
		
	} catch (SQLException e) {
	// TODO Auto-generated catch block
		e.printStackTrace();
	
	
	} finally {
		try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
		try { if(rs != null) rs.close(); } catch(SQLException e) {}
		try { if(con != null) con.close(); } catch(SQLException e) {}
	}		
	return rc;
	
	}
	
	public String getMobileNumber(String sn) {
		String sql = "select * from dbo.PersonnelManagement"
				+ " where ServiceNumber = ?";
		
		String phone = null;

		
		try {
			con = getConn();				
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, sn);
			rs = pstmt.executeQuery();
		while(rs.next()) {
			phone = rs.getString("MobileNumber");
		}
			
		} catch (SQLException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		
		
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}		
		return phone;
	}
	
	public String OutputPicture(InputStream in)
{
		String b64="";
     try
     {


		     BufferedImage bimg = ImageIO.read(in);
		       in.close();
		       ByteArrayOutputStream baos = new ByteArrayOutputStream();
		       ImageIO.write( bimg, "jpg", baos );

		       baos.flush();
		       byte[] imageInByteArray = baos.toByteArray();

		       baos.close();
		       b64 = javax.xml.bind.DatatypeConverter.printBase64Binary(imageInByteArray);

       }
     catch(Exception e)
     {
    	 b64="";
    	 System.out.println("해당 컬럼은 이미지 데이터가 없습니다");
     }
     return b64;


}

    
public ArrayList<MobileEquip> getMobileList(String reg, String rc,String ec) {
		
		String sql = "";
		MobileEquip mobile = null;
		ArrayList<MobileEquip> mobileList = new ArrayList<MobileEquip>();
	//	JSONArray jsonLocations = new JSONArray();
		try {
			con = getConn();				
			if(reg.equals("소속:전체") && rc.equals("세부소속:전체") && ec.equals("장비타입:전체")) {
				sql = "select m.MobileNumber, b.CodeName as Regiment,p.Regiment as RegimentCode, MobileType,p.Name,m.ServiceNumber, m.JoinDate, a.CodeName as regimCompany,p.regimCompany as regimCompanyCode,c.CodeName as Rank, p.Rank as RankCode,ModelnAME,ManufacturerName,m.Remark  "
						+ "from dbo.MobileManagement m "
						+ "	inner join dbo.PersonnelManagement p ON p.MobileNumber = m.MobileNumber "
						+ "	left outer Join dbo.Code a ON p.regimCompany = a.CodeID "
						+ "	inner Join dbo.Code b ON p.Regiment = b.CodeID  "
						+ "	left outer Join dbo.Code c ON p.Rank = c.CodeID  "
					+ "	order by p.Regiment , m.MobileNumber ; ";

			}else if(reg.equals("소속:전체") && rc.equals("세부소속:전체")) {
				sql = "select m.MobileNumber, b.CodeName as Regiment,p.Regiment as RegimentCode, MobileType,p.Name,m.ServiceNumber, m.JoinDate, a.CodeName as regimCompany,p.regimCompany as regimCompanyCode,c.CodeName as Rank, p.Rank as RankCode,ModelnAME,ManufacturerName,m.Remark  "
						+ "from dbo.MobileManagement m "
						+ "	inner join dbo.PersonnelManagement p ON p.MobileNumber = m.MobileNumber "
						+ "	left outer Join dbo.Code a ON p.regimCompany = a.CodeID "
						+ "	inner Join dbo.Code b ON p.Regiment = b.CodeID  "
						+ "	left outer Join dbo.Code c ON p.Rank = c.CodeID  "
						+ " where m.MobileType ='"+ec+"'"
						+ "	order by p.Regiment, m.MobileNumber ; ";
					

			}else if(rc.equals("세부소속:전체") && ec.equals("장비타입:전체")) {
				sql = "select m.MobileNumber, b.CodeName as Regiment,p.Regiment as RegimentCode, MobileType,p.Name,m.ServiceNumber, m.JoinDate, a.CodeName as regimCompany,p.regimCompany as regimCompanyCode,c.CodeName as Rank, p.Rank as RankCode,ModelnAME,ManufacturerName,m.Remark  "
						+ "from dbo.MobileManagement m "
						+ "	inner join dbo.PersonnelManagement p ON p.MobileNumber = m.MobileNumber "
						+ "	left outer Join dbo.Code a ON p.regimCompany = a.CodeID "
						+ "	inner Join dbo.Code b ON p.Regiment = b.CodeID  "
						+ "	left outer Join dbo.Code c ON p.Rank = c.CodeID  "
						+ " where p.Regiment = '"+reg+"'"
						+ "	order by p.Regiment  , m.MobileNumber ; ";
					

			}else if(reg.equals("소속:전체")) {
				sql = "select m.MobileNumber, b.CodeName as Regiment,p.Regiment as RegimentCode, MobileType,p.Name,m.ServiceNumber, m.JoinDate, a.CodeName as regimCompany,p.regimCompany as regimCompanyCode,c.CodeName as Rank, p.Rank as RankCode,ModelnAME,ManufacturerName,m.Remark  "
						+ "from dbo.MobileManagement m "
						+ "	inner join dbo.PersonnelManagement p ON p.MobileNumber = m.MobileNumber "
						+ "	left outer Join dbo.Code a ON p.regimCompany = a.CodeID "
						+ "	inner Join dbo.Code b ON p.Regiment = b.CodeID  "
						+ "	left outer Join dbo.Code c ON p.Rank = c.CodeID  "
						+ " where p.regimCompany = '"+rc+"' and m.MobileType ='"+ec+"'"
						+ "	order by p.Regiment  , m.MobileNumber ; ";
			}else if(rc.equals("세부소속:전체")) {
				sql = "select m.MobileNumber, b.CodeName as Regiment,p.Regiment as RegimentCode, MobileType,p.Name,m.ServiceNumber, m.JoinDate, a.CodeName as regimCompany,p.regimCompany as regimCompanyCode,c.CodeName as Rank, p.Rank as RankCode,ModelnAME,ManufacturerName,m.Remark  "
						+ "from dbo.MobileManagement m "
						+ "	inner join dbo.PersonnelManagement p ON p.MobileNumber = m.MobileNumber "
						+ "	left outer Join dbo.Code a ON p.regimCompany = a.CodeID "
						+ "	inner Join dbo.Code b ON p.Regiment = b.CodeID  "
						+ "	left outer Join dbo.Code c ON p.Rank = c.CodeID  "
						+ " where p.Regiment = '"+reg+"' and m.MobileType ='"+ec+"'"
						+ "	order by p.Regiment , m.MobileNumber ; ";
			}else if(ec.equals("장비타입:전체")) {
				sql = "select m.MobileNumber, b.CodeName as Regiment,p.Regiment as RegimentCode, MobileType,p.Name,m.ServiceNumber, m.JoinDate, a.CodeName as regimCompany,p.regimCompany as regimCompanyCode,c.CodeName as Rank, p.Rank as RankCode,ModelnAME,ManufacturerName,m.Remark  "
						+ "from dbo.MobileManagement m "
						+ "	inner join dbo.PersonnelManagement p ON p.MobileNumber = m.MobileNumber "
						+ "	left outer Join dbo.Code a ON p.regimCompany = a.CodeID "
						+ "	inner Join dbo.Code b ON p.Regiment = b.CodeID  "
						+ "	left outer Join dbo.Code c ON p.Rank = c.CodeID  "
						+ " where p.Regiment = '"+reg+"' and p.regimCompany = '"+rc+"'"
						+ "	order by p.Regiment , m.MobileNumber ; ";
			}else {
				sql = "select m.MobileNumber, b.CodeName as Regiment,p.Regiment as RegimentCode, MobileType,p.Name,m.ServiceNumber, m.JoinDate, a.CodeName as regimCompany,p.regimCompany as regimCompanyCode,c.CodeName as Rank, p.Rank as RankCode,ModelnAME,ManufacturerName,m.Remark  "
						+ "from dbo.MobileManagement m "
						+ "	inner join dbo.PersonnelManagement p ON p.MobileNumber = m.MobileNumber "
						+ "	left outer Join dbo.Code a ON p.regimCompany = a.CodeID "
						+ "	inner Join dbo.Code b ON p.Regiment = b.CodeID  "
						+ "	left outer Join dbo.Code c ON p.Rank = c.CodeID  "
						+ " where p.Regiment = '"+reg+"' and p.regimCompany = '"+rc+"' and m.MobileType ='"+ec+"'"
						+ "	order by p.Regiment  , m.MobileNumber ; ";
			}
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String MobileNumber = rs.getString("MobileNumber") ;
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String MobileType = rs.getString("MobileType") == null ? "":rs.getString("MobileType");
				String Name = rs.getString("Name")== null ? "":rs.getString("Name");
				String ServiceNumber = rs.getString("ServiceNumber") == null ? "":rs.getString("ServiceNumber");
				String JoinDate = rs.getString("JoinDate") == null ? "":searchDateConvert(rs.getString("JoinDate"),"yyyy-MM-dd");
				String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
				String Rank = rs.getString("Rank") == null ? "":rs.getString("Rank");
				String ModelnAME = rs.getString("ModelnAME") == null ? "":rs.getString("ModelnAME");
				String ManufacturerName = rs.getString("ManufacturerName") == null ? "":rs.getString("ManufacturerName");
				String Remark = rs.getString("Remark") == null ? "":rs.getString("Remark");



				//mobile= new MobileEquip(MobileNumber,Regiment,Rank,Name,ServiceNumber,MobileType,JoinDate);
				mobile= new MobileEquip(MobileNumber,Regiment,regimCompany,Name,ServiceNumber,MobileType,JoinDate,ModelnAME,ManufacturerName,Remark,Rank);

				mobileList.add(mobile);
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}		
		return mobileList;

	}


public ArrayList<MobileEquip> getMobileInfo(String pn) {
	
	String sql = "";
	MobileEquip mobile = null;
	ArrayList<MobileEquip> mobileList = new ArrayList<MobileEquip>();
//	JSONArray jsonLocations = new JSONArray();
	try {
		con = getConn();				
		sql = "select m.MobileNumber, b.CodeName as Regiment,p.Regiment as RegimentCode, MobileType,p.Name,m.ServiceNumber, m.JoinDate, a.CodeName as regimCompany,p.regimCompany as regimCompanyCode,c.CodeName as Rank, p.Rank as RankCode,ModelnAME,ManufacturerName,m.Remark  "
				+ "from dbo.MobileManagement m "
				+ "	inner join dbo.PersonnelManagement p ON p.MobileNumber = m.MobileNumber "
				+ "	left outer Join dbo.Code a ON p.regimCompany = a.CodeID "
				+ "	inner Join dbo.Code b ON p.Regiment = b.CodeID  "
				+ "	left outer Join dbo.Code c ON p.Rank = c.CodeID  "
				+ " where m.MobileNumber ='"+ pn+"'"
				+ "	order by p.Regiment , m.MobileNumber ; ";

		
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			String MobileNumber = rs.getString("MobileNumber");
			String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
			String MobileType = rs.getString("MobileType");
			String Name = rs.getString("Name");
			String ServiceNumber = rs.getString("ServiceNumber");
			String JoinDate = rs.getString("JoinDate");
			String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
			String regimCompanyCode = rs.getString("regimCompanyCode")  == null ? "":rs.getString("regimCompanyCode");
			String RegimentCode = rs.getString("RegimentCode");
			String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
			String RankCode = rs.getString("RankCode");
			String ModelnAME = rs.getString("ModelnAME");
			String ManufacturerName = rs.getString("ManufacturerName");
			String Remark = rs.getString("Remark");



			mobile= new MobileEquip(MobileNumber,Regiment,RegimentCode,regimCompany,regimCompanyCode,Name,ServiceNumber,MobileType,JoinDate,ModelnAME,ManufacturerName,Remark,Rank,RankCode);
			//mobile= new MobileEquip(MobileNumber,Regiment,RegimentCode,regimCompany,regimCompanyCode,Name,ServiceNumber,MobileType,JoinDate,ModelnAME,ManufacturerName,Remark);

			mobileList.add(mobile);
			
		}

	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
		try { if(rs != null) rs.close(); } catch(SQLException e) {}
		try { if(con != null) con.close(); } catch(SQLException e) {}
	}		
	return mobileList;

}
	
	public ArrayList<Location> getLocations() {
	
		String sql = "select top (50) * from dbo.Locations order by InputTime desc";
		Location location = null;
		ArrayList<Location> locations = new ArrayList<Location>();
	//	JSONArray jsonLocations = new JSONArray();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));
				
				location = new Location(userKey, latitude, longitude, timestamp);
			//	System.out.println(location.toString());
				
				locations.add(location);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}		
		return locations;
	}

	
	public ArrayList<Circle> getCircle(String rc) {
		
		String sql = "select c.latitude,c.longitude,c.r,a.CodeName as Regiment  from dbo.Geofence c"
					+ " left outer join dbo.Code a ON c.Regiment=a.CodeID ";
	
		if(rc != "전체")
			sql = "select c.latitude,c.longitude,c.r,a.CodeName as Regiment from dbo.Geofence c "
					+ " left outer join dbo.Code a ON c.Regiment=a.CodeID "
					+ " where Regiment='"+rc+"'";
		
		Circle circle = null;
		ArrayList<Circle> Circles = new ArrayList<Circle>();
	//	JSONArray jsonLocations = new JSONArray();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String latitude = rs.getString("latitude");
				String longitude = rs.getString("longitude");
				String r = rs.getString("r");
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				
				circle = new Circle(latitude, longitude, r,Regiment);
			//	System.out.println(location.toString());
				
				Circles.add(circle);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}		
		return Circles;
	}
	

	
	public ArrayList<Location> getLocations(String reg, String rc, String device) {
		
		String sql = "select top (50) * from dbo.Locations order by InputTime desc";
		Location location = null;
		ArrayList<Location> locations = new ArrayList<Location>();
	//	JSONArray jsonLocations = new JSONArray();
		
		if(reg.equals("전체") && device.equals("전체")) {
			sql = "select top (50) p.*,l.* "
					+ "from dbo.Locations as l "
					+ "inner join dbo.PersonnelManagement as p "
					+ "on l.UserKey = p.MobileNumber "
					+ "order by l.InputTime desc ";
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				
				while(rs.next()) {
					
					String serviceNumber = rs.getString("serviceNumber");
					String name = rs.getString("name");
					String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
					String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
					String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
					String isDevice = rs.getString("isDevice");
					//if(isDevice.equals("wb"))isDevice="W-B";
					//if(isDevice.equals("wg"))isDevice="W-G";
					String duty = rs.getString("duty");
					String userKey = rs.getString("UserKey");
					String latitude = rs.getString("Latitude");
					String longitude = rs.getString("longitude");
					String timestamp = format.format(rs.getTimestamp("Timestamp"));
					
					location = new Location(serviceNumber, userKey, name, Rank, Regiment,
						 regimCompany, isDevice, duty, latitude, longitude, timestamp);
				//	System.out.println(location.toString());
					
					locations.add(location);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
				try { if(rs != null) rs.close(); } catch(SQLException e) {}
				try { if(con != null) con.close(); } catch(SQLException e) {}
			}
		} else if(device.equals("전체")) {
			
			if(rc.equals("전체")) {
				sql = "select top (50) p.*,l.* "
						+ "from dbo.Locations as l "
						+ "inner join dbo.PersonnelManagement as p "
						+ "on l.UserKey = p.MobileNumber "
						+ "where p.Regiment = ? "
						+ "order by l.InputTime desc ";
				
				try {
					con = getConn();
					System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
					
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, reg);
					rs = pstmt.executeQuery();
					
					while(rs.next()) {
						
						String serviceNumber = rs.getString("serviceNumber");
						String name = rs.getString("name");
						String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
						String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
						String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
						String isDevice = rs.getString("isDevice");
						//if(isDevice.equals("wb"))isDevice="W-B";
						//if(isDevice.equals("wg"))isDevice="W-G";
						String duty = rs.getString("duty");
						String userKey = rs.getString("UserKey");
						String latitude = rs.getString("Latitude");
						String longitude = rs.getString("longitude");
						String timestamp = format.format(rs.getTimestamp("Timestamp"));
						
						location = new Location(serviceNumber, userKey, name, Rank, Regiment,
							 regimCompany, isDevice, duty, latitude, longitude, timestamp);
					//	System.out.println(location.toString());
						
						locations.add(location);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
					try { if(rs != null) rs.close(); } catch(SQLException e) {}
					try { if(con != null) con.close(); } catch(SQLException e) {}
				}
				
			} else {
				sql = "select top (50) p.*,l.* "
						+ "from dbo.Locations as l "
						+ "inner join dbo.PersonnelManagement as p "
						+ "on l.UserKey = p.MobileNumber "
						+ "where p.regimCompany = ? "
						+ "order by l.InputTime desc ";
				
				try {
					con = getConn();
					System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
					
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, rc);
					rs = pstmt.executeQuery();
					
					while(rs.next()) {
						String serviceNumber = rs.getString("serviceNumber");
						String name = rs.getString("name");
						String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
						String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
						String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
						String isDevice = rs.getString("isDevice");
						//if(isDevice.equals("wb"))isDevice="W-B";
						//if(isDevice.equals("wg"))isDevice="W-G";
						String duty = rs.getString("duty");
						String userKey = rs.getString("UserKey");
						String latitude = rs.getString("Latitude");
						String longitude = rs.getString("longitude");
						String timestamp = format.format(rs.getTimestamp("Timestamp"));
						
						location = new Location(serviceNumber, userKey, name, Rank, Regiment,
							 regimCompany, isDevice, duty, latitude, longitude, timestamp);
						
						locations.add(location);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
					try { if(rs != null) rs.close(); } catch(SQLException e) {}
					try { if(con != null) con.close(); } catch(SQLException e) {}
				}
				
			}

		} else if(reg.equals("전체")) {
			sql = "select top (50) p.*,l.* "
					+ "from dbo.Locations as l "
					+ "inner join dbo.PersonnelManagement as p "
					+ "on l.UserKey = p.MobileNumber "
					+ "where l.isDevice = ? "
					+ "order by l.InputTime desc ";
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, device);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					String serviceNumber = rs.getString("serviceNumber");
					String name = rs.getString("name");
					String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
					String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
					String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
					String isDevice = rs.getString("isDevice");
					//if(isDevice.equals("wb"))isDevice="W-B";
					//if(isDevice.equals("wg"))isDevice="W-G";
					String duty = rs.getString("duty");
					String userKey = rs.getString("UserKey");
					String latitude = rs.getString("Latitude");
					String longitude = rs.getString("longitude");
					String timestamp = format.format(rs.getTimestamp("Timestamp"));
					
					location = new Location(serviceNumber, userKey, name, Rank, Regiment,
						 regimCompany, isDevice, duty, latitude, longitude, timestamp);
					
					locations.add(location);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
				try { if(rs != null) rs.close(); } catch(SQLException e) {}
				try { if(con != null) con.close(); } catch(SQLException e) {}
			}
			
		} else {
			if(rc.equals("전체")) {
				sql = "select top (50) p.*,l.* "
						+ "from dbo.Locations as l "
						+ "inner join dbo.PersonnelManagement as p "
						+ "on l.UserKey = p.MobileNumber "
						+ "where l.isDevice = ? and p.Regiment = ? "
						+ "order by l.InputTime desc ";
				
				try {
					con = getConn();
					System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
					
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, device);
					pstmt.setString(2, reg);
					rs = pstmt.executeQuery();
					
					while(rs.next()) {
						String serviceNumber = rs.getString("serviceNumber");
						String name = rs.getString("name");
						String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
						String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
						String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
						String isDevice = rs.getString("isDevice");
						//if(isDevice.equals("wb"))isDevice="W-B";
						//if(isDevice.equals("wg"))isDevice="W-G";
						String duty = rs.getString("duty");
						String userKey = rs.getString("UserKey");
						String latitude = rs.getString("Latitude");
						String longitude = rs.getString("longitude");
						String timestamp = format.format(rs.getTimestamp("Timestamp"));
						
						location = new Location(serviceNumber, userKey, name, Rank, Regiment,
							 regimCompany, isDevice, duty, latitude, longitude, timestamp);
						
						locations.add(location);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
					try { if(rs != null) rs.close(); } catch(SQLException e) {}
					try { if(con != null) con.close(); } catch(SQLException e) {}
				}
			} else {
				sql = "select top (50) p.*,l.* "
						+ "from dbo.Locations as l "
						+ "inner join dbo.PersonnelManagement as p "
						+ "on l.UserKey = p.MobileNumber "
						+ "where l.isDevice = ? and p.Regiment = ? and p.regimCompany = ? "
						+ "order by l.InputTime desc ";
				
				try {
					con = getConn();
					System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
					
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, device);
					pstmt.setString(2, reg);
					pstmt.setString(3, rc);
					rs = pstmt.executeQuery();
					
					while(rs.next()) {
						String serviceNumber = rs.getString("serviceNumber");
						String name = rs.getString("name");
						String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
						String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
						String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
						String isDevice = rs.getString("isDevice");
						//if(isDevice.equals("wb"))isDevice="W-B";
						//if(isDevice.equals("wg"))isDevice="W-G";
						String duty = rs.getString("duty");
						String userKey = rs.getString("UserKey");
						String latitude = rs.getString("Latitude");
						String longitude = rs.getString("longitude");
						String timestamp = format.format(rs.getTimestamp("Timestamp"));
						
						location = new Location(serviceNumber, userKey, name, Rank, Regiment,
							 regimCompany, isDevice, duty, latitude, longitude, timestamp);
						
						locations.add(location);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
					try { if(rs != null) rs.close(); } catch(SQLException e) {}
					try { if(con != null) con.close(); } catch(SQLException e) {}
				}
			}
			
			
		}
		
		return locations;
		
	}
	
	public ArrayList<Location> getMobileStatus(String reg, String rc, String device) {
		
		String sql = "select top (50) * from dbo.MobileStatus order by InputTime desc";
		Location location = null;
		ArrayList<Location> locations = new ArrayList<Location>();
	//	JSONArray jsonLocations = new JSONArray();
		
		if(reg.equals("전체") && device.equals("전체")) {
			sql = "select top (50) p.*,l.* "
					+ "from dbo.MobileStatus as l "
					+ "inner join dbo.PersonnelManagement as p "
					+ "on l.UserKey = p.MobileNumber "
					+ "order by l.InputTime desc ";
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				
				while(rs.next()) {
					
					String serviceNumber = rs.getString("serviceNumber");
					String name = rs.getString("name");
					String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
					String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
					String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
					String isDevice = rs.getString("isDevice");
					//if(isDevice.equals("wb"))isDevice="W-B";
					//if(isDevice.equals("wg"))isDevice="W-G";
					String duty = rs.getString("duty");
					String userKey = rs.getString("UserKey");
					String latitude = rs.getString("Latitude");
					String longitude = rs.getString("longitude");
					String timestamp = format.format(rs.getTimestamp("Timestamp"));
					
					location = new Location(serviceNumber, userKey, name, Rank, Regiment,
						 regimCompany, isDevice, duty, latitude, longitude, timestamp);
				//	System.out.println(location.toString());
					
					locations.add(location);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
				try { if(rs != null) rs.close(); } catch(SQLException e) {}
				try { if(con != null) con.close(); } catch(SQLException e) {}
			}
		} else if(device.equals("전체")) {
			
			if(rc.equals("전체")) {
				sql = "select top (50) p.*,l.* "
						+ "from dbo.MobileStatus as l "
						+ "inner join dbo.PersonnelManagement as p "
						+ "on l.UserKey = p.MobileNumber "
						+ "where p.Regiment = ? "
						+ "order by l.InputTime desc ";
				
				try {
					con = getConn();
					System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
					
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, reg);
					rs = pstmt.executeQuery();
					
					while(rs.next()) {
						
						String serviceNumber = rs.getString("serviceNumber");
						String name = rs.getString("name");
						String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
						String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
						String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
						String isDevice = rs.getString("isDevice");
						//if(isDevice.equals("wb"))isDevice="W-B";
						//if(isDevice.equals("wg"))isDevice="W-G";
						String duty = rs.getString("duty");
						String userKey = rs.getString("UserKey");
						String latitude = rs.getString("Latitude");
						String longitude = rs.getString("longitude");
						String timestamp = format.format(rs.getTimestamp("Timestamp"));
						
						location = new Location(serviceNumber, userKey, name, Rank, Regiment,
							 regimCompany, isDevice, duty, latitude, longitude, timestamp);
					//	System.out.println(location.toString());
						
						locations.add(location);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
					try { if(rs != null) rs.close(); } catch(SQLException e) {}
					try { if(con != null) con.close(); } catch(SQLException e) {}
				}
				
			} else {
				sql = "select top (50) p.*,l.* "
						+ "from dbo.MobileStatus as l "
						+ "inner join dbo.PersonnelManagement as p "
						+ "on l.UserKey = p.MobileNumber "
						+ "where p.regimCompany = ? "
						+ "order by l.InputTime desc ";
				
				try {
					con = getConn();
					System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
					
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, rc);
					rs = pstmt.executeQuery();
					
					while(rs.next()) {
						String serviceNumber = rs.getString("serviceNumber");
						String name = rs.getString("name");
						String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
						String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
						String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
						String isDevice = rs.getString("isDevice");
						//if(isDevice.equals("wb"))isDevice="W-B";
						//if(isDevice.equals("wg"))isDevice="W-G";
						String duty = rs.getString("duty");
						String userKey = rs.getString("UserKey");
						String latitude = rs.getString("Latitude");
						String longitude = rs.getString("longitude");
						String timestamp = format.format(rs.getTimestamp("Timestamp"));
						
						location = new Location(serviceNumber, userKey, name, Rank, Regiment,
							 regimCompany, isDevice, duty, latitude, longitude, timestamp);
						
						locations.add(location);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
					try { if(rs != null) rs.close(); } catch(SQLException e) {}
					try { if(con != null) con.close(); } catch(SQLException e) {}
				}
				
			}

		} else if(reg.equals("전체")) {
			sql = "select top (50) p.*,l.* "
					+ "from dbo.MobileStatus as l "
					+ "inner join dbo.PersonnelManagement as p "
					+ "on l.UserKey = p.MobileNumber "
					+ "where l.isDevice = ? "
					+ "order by l.InputTime desc ";
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, device);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					String serviceNumber = rs.getString("serviceNumber");
					String name = rs.getString("name");
					String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
					String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
					String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
					String isDevice = rs.getString("isDevice");
					//if(isDevice.equals("wb"))isDevice="W-B";
					//if(isDevice.equals("wg"))isDevice="W-G";
					String duty = rs.getString("duty");
					String userKey = rs.getString("UserKey");
					String latitude = rs.getString("Latitude");
					String longitude = rs.getString("longitude");
					String timestamp = format.format(rs.getTimestamp("Timestamp"));
					
					location = new Location(serviceNumber, userKey, name, Rank, Regiment,
						 regimCompany, isDevice, duty, latitude, longitude, timestamp);
					
					locations.add(location);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
				try { if(rs != null) rs.close(); } catch(SQLException e) {}
				try { if(con != null) con.close(); } catch(SQLException e) {}
			}
			
		} else {
			if(rc.equals("전체")) {
				sql = "select top (50) p.*,l.* "
						+ "from dbo.MobileStatus as l "
						+ "inner join dbo.PersonnelManagement as p "
						+ "on l.UserKey = p.MobileNumber "
						+ "where l.isDevice = ? and p.Regiment = ? "
						+ "order by l.InputTime desc ";
				
				try {
					con = getConn();
					System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
					
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, device);
					pstmt.setString(2, reg);
					rs = pstmt.executeQuery();
					
					while(rs.next()) {
						String serviceNumber = rs.getString("serviceNumber");
						String name = rs.getString("name");
						String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
						String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
						String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
						String isDevice = rs.getString("isDevice");
						//if(isDevice.equals("wb"))isDevice="W-B";
						//if(isDevice.equals("wg"))isDevice="W-G";
						String duty = rs.getString("duty");
						String userKey = rs.getString("UserKey");
						String latitude = rs.getString("Latitude");
						String longitude = rs.getString("longitude");
						String timestamp = format.format(rs.getTimestamp("Timestamp"));
						
						location = new Location(serviceNumber, userKey, name, Rank, Regiment,
							 regimCompany, isDevice, duty, latitude, longitude, timestamp);
						
						locations.add(location);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
					try { if(rs != null) rs.close(); } catch(SQLException e) {}
					try { if(con != null) con.close(); } catch(SQLException e) {}
				}
			} else {
				sql = "select top (50) p.*,l.* "
						+ "from dbo.MobileStatus as l "
						+ "inner join dbo.PersonnelManagement as p "
						+ "on l.UserKey = p.MobileNumber "
						+ "where l.isDevice = ? and p.Regiment = ? and p.regimCompany = ? "
						+ "order by l.InputTime desc ";
				
				try {
					con = getConn();
					System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
					
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, device);
					pstmt.setString(2, reg);
					pstmt.setString(3, rc);
					rs = pstmt.executeQuery();
					
					while(rs.next()) {
						String serviceNumber = rs.getString("serviceNumber");
						String name = rs.getString("name");
						String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
						String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
						String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
						String isDevice = rs.getString("isDevice");
						//if(isDevice.equals("wb"))isDevice="W-B";
						//if(isDevice.equals("wg"))isDevice="W-G";
						String duty = rs.getString("duty");
						String userKey = rs.getString("UserKey");
						String latitude = rs.getString("Latitude");
						String longitude = rs.getString("longitude");
						String timestamp = format.format(rs.getTimestamp("Timestamp"));
						
						location = new Location(serviceNumber, userKey, name, Rank, Regiment,
							 regimCompany, isDevice, duty, latitude, longitude, timestamp);
						
						locations.add(location);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
					try { if(rs != null) rs.close(); } catch(SQLException e) {}
					try { if(con != null) con.close(); } catch(SQLException e) {}
				}
			}
			
			
		}
		
		return locations;
		
	}
	
public long TimeTick() {
	long TICKS_AT_EPOCH = 621355968000000000L; 
	long tick = (System.currentTimeMillis() + TimeZone.getDefault().getRawOffset()) * 10000 + TICKS_AT_EPOCH;
	SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss.SSS");
	return tick;
}
	
public ArrayList<Location> getMobileStatus(String reg, String rc) {
		
		String sql = "select top (50) * from dbo.MobileStatus order by InputTime desc";
		Location location = null;
		ArrayList<Location> locations = new ArrayList<Location>();
	//	JSONArray jsonLocations = new JSONArray();
		long TICKS_AT_EPOCH = 621355968000000000L; 
		long tick = (System.currentTimeMillis() + TimeZone.getDefault().getRawOffset()) * 10000 + TICKS_AT_EPOCH;
		
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss.SSS");

		System.out.println(tick );
		System.out.println( format1.format (System.currentTimeMillis()));

		if(reg.equals("전체") && rc.equals("전체")) {
			sql = "select p.MobileNumber,p.ServiceNumber, p.Name,p.Regiment, c.CodeName as RegimentName, p.regimCompany,d.CodeName as regimCompanyName,p.Rank, e.CodeName as RankName, p.Duty, l.*,  "
					+ "  MissionType,"	
					//+ " (case when (l.HeartRate > 1 and (l.isDevice = 'wb' or l.isDevice ='wg')) then  concat('Y',l.BatteryPercent) ELSE concat('N',l.BatteryPercent) END) AS etc, "
					+ "l.BatteryPercent  AS etc,"
					+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.EquipLocation from beacons b where l.Uuid = b.Uuid) else '' end) as 'EquipLocation', "
					+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomName from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomName' , "
					+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomNumber from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomNumber'  "
					+ "from dbo.MobileStatus as l "
					+ "inner join dbo.PersonnelManagement as p on l.UserKey = p.MobileNumber "
					+ "left outer join dbo.Code as c on p.Regiment = c.CodeID "
					+ "left outer join dbo.Code as d on p.regimCompany = d.CodeID "
					+ "left outer join dbo.Code as e on p.Rank = e.CodeID "
					+ "order by p.Regiment, p.regimCompany, p.Rank, p.Name ";
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				
				while(rs.next()) {
					String serviceNumber = rs.getString("serviceNumber");
					String name = rs.getString("name");
					String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
					String RankName = rs.getString("RankName") == null ? "" : rs.getString("RankName");
					String RegimentName = rs.getString("RegimentName") == null ? "":rs.getString("RegimentName");
					String regimCompanyName = rs.getString("regimCompanyName") == null ? "" : rs.getString("regimCompanyName");
					String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
					String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
					String isDevice = rs.getString("isDevice");
					//if(isDevice.equals("wb"))isDevice="W-B";
					//if(isDevice.equals("wg"))isDevice="W-G";

					String duty = rs.getString("duty");
					String userKey = rs.getString("UserKey");
					String latitude = rs.getString("Latitude");
					String longitude = rs.getString("longitude");
					String mgrs = MGRSString(latitude, longitude);

					String timestamp = format.format(rs.getTimestamp("Timestamp"));				
					String mobileNumber = rs.getString("MobileNumber");
					String EventId = Long.toString(tick);
					String EventDateTime = format1.format (System.currentTimeMillis());
					String MissionType = rs.getString("MissionType");
					String EquipID = serviceNumber;
					String EventType = "EVT-14";
					String ObjectType = "OBT-02";
					String EventRemark = "geoF-On 이탈 이벤트 발생\n"+RegimentName+"\n"+RankName+" "+name+"("+serviceNumber+")\n"+phone(mobileNumber);
					String Status = "EVS-01";
					String ActionStartDate = format2.format (System.currentTimeMillis());
					String ActionEndDate = ""; //rs.getString("ActionEndDate");
					String Actioncontents = ""; //rs.getString("Actioncontents");
					String ResultContents = ""; //rs.getString("ResultContents");
					String IsSendOK = "N";
					String GroupCode="XX";
					String EquipLocation = rs.getString("EquipLocation");
					String RoomName = rs.getString("RoomName");
					String RoomNumber = rs.getString("RoomNumber");
					String etc = rs.getString("etc");
					tick= tick+1;
					location = new Location(serviceNumber, userKey, name, Rank,RankName, Regiment,RegimentName,
							regimCompany,regimCompanyName, isDevice, duty, latitude, longitude, timestamp,EventId,EventDateTime,MissionType,EquipID
							 ,EventType,ObjectType,EventRemark,Status,ActionStartDate,ActionEndDate,Actioncontents,ResultContents,GroupCode,IsSendOK,EquipLocation,RoomName,mobileNumber,etc,RoomNumber,mgrs);
					//location = new Location(serviceNumber, userKey, name, Rank, Regiment,
					//	 regimCompany, isDevice, duty, latitude, longitude, timestamp,);
					
					locations.add(location);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
				try { if(rs != null) rs.close(); } catch(SQLException e) {}
				try { if(con != null) con.close(); } catch(SQLException e) {}
			}
		} else if(rc.equals("전체")) {
			
			sql = "select p.MobileNumber,p.ServiceNumber, p.Name,p.Regiment, c.CodeName as RegimentName, p.regimCompany,d.CodeName as regimCompanyName,p.Rank, e.CodeName as RankName, p.Duty, l.*,  "
					+ "  MissionType,"	
					//+ " (case when (l.HeartRate > 1 and (l.isDevice = 'wb' or l.isDevice ='wg')) then  concat('Y',l.BatteryPercent) ELSE concat('N',l.BatteryPercent) END) AS etc, "
					+ "l.BatteryPercent  AS etc,"
					+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.EquipLocation from beacons b where l.Uuid = b.Uuid) else '' end) as 'EquipLocation', "
					+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomName from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomName' , "
					+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomNumber from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomNumber'  "
					+ "from dbo.MobileStatus as l "
					+ "inner join dbo.PersonnelManagement as p on l.UserKey = p.MobileNumber "
					+ "left outer join dbo.Code as c on p.Regiment = c.CodeID "
					+ "left outer join dbo.Code as d on p.regimCompany = d.CodeID "
					+ "left outer join dbo.Code as e on p.Rank = e.CodeID "
					+ "where p.Regiment = ? "
					+ "order by p.Regiment, p.regimCompany, p.Rank, p.Name ";
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, reg);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					String serviceNumber = rs.getString("serviceNumber");
					String name = rs.getString("name");
					String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
					String RankName = rs.getString("RankName") == null ? "" : rs.getString("RankName");
					String RegimentName = rs.getString("RegimentName") == null ? "":rs.getString("RegimentName");
					String regimCompanyName = rs.getString("regimCompanyName") == null ? "" : rs.getString("regimCompanyName");
					String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
					String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
					String isDevice = rs.getString("isDevice");
					//if(isDevice.equals("wb"))isDevice="W-B";
					//if(isDevice.equals("wg"))isDevice="W-G";

					String duty = rs.getString("duty");
					String userKey = rs.getString("UserKey");
					String latitude = rs.getString("Latitude");
					String longitude = rs.getString("longitude");
					String timestamp = format.format(rs.getTimestamp("Timestamp"));				
					String mobileNumber = rs.getString("MobileNumber");
					String EventId = Long.toString(tick);
					String EventDateTime = format1.format (System.currentTimeMillis());
					String MissionType = rs.getString("MissionType");
					String EquipID = serviceNumber;
					String EventType = "EVT-14";
					String ObjectType = "OBT-02";
					String EventRemark = "geoF-On 이탈 이벤트 발생\n"+RegimentName+"\n"+RankName+" "+name+"("+serviceNumber+")\n"+phone(mobileNumber);
					String Status = "EVS-01";
					String ActionStartDate = format2.format (System.currentTimeMillis());
					String ActionEndDate = ""; //rs.getString("ActionEndDate");
					String Actioncontents = ""; //rs.getString("Actioncontents");
					String ResultContents = ""; //rs.getString("ResultContents");
					String IsSendOK = "N";
					String GroupCode="XX";
					String EquipLocation = rs.getString("EquipLocation");
					String RoomName = rs.getString("RoomName");
					String RoomNumber = rs.getString("RoomNumber");
					String etc = rs.getString("etc");
					tick= tick+1;
					String mgrs = MGRSString(latitude, longitude);
					location = new Location(serviceNumber, userKey, name, Rank,RankName, Regiment,RegimentName,
							regimCompany,regimCompanyName, isDevice, duty, latitude, longitude, timestamp,EventId,EventDateTime,MissionType,EquipID
							 ,EventType,ObjectType,EventRemark,Status,ActionStartDate,ActionEndDate,Actioncontents,ResultContents,GroupCode,IsSendOK,EquipLocation,RoomName,mobileNumber,etc,RoomNumber,mgrs);
					locations.add(location);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
				try { if(rs != null) rs.close(); } catch(SQLException e) {}
				try { if(con != null) con.close(); } catch(SQLException e) {}
			}

		} else {
			sql = "select p.MobileNumber,p.ServiceNumber, p.Name,p.Regiment, c.CodeName as RegimentName, p.regimCompany,d.CodeName as regimCompanyName,p.Rank, e.CodeName as RankName, p.Duty, l.*,  "
					+ "  MissionType,"	
					//+ " (case when (l.HeartRate > 1 and (l.isDevice = 'wb' or l.isDevice ='wg')) then  concat('Y',l.BatteryPercent) ELSE concat('N',l.BatteryPercent) END) AS etc, "
					+ "l.BatteryPercent  AS etc,"
					+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.EquipLocation from beacons b where l.Uuid = b.Uuid) else '' end) as 'EquipLocation', "
					+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomName from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomName' , "
					+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomNumber from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomNumber'  "
					+ "from dbo.MobileStatus as l "
					+ "inner join dbo.PersonnelManagement as p on l.UserKey = p.MobileNumber "
					+ "left outer join dbo.Code as c on p.Regiment = c.CodeID "
					+ "left outer join dbo.Code as d on p.regimCompany = d.CodeID "
					+ "left outer join dbo.Code as e on p.Rank = e.CodeID "
					+ "where p.Regiment = ? and p.regimCompany = ? "
					+ "order by p.Regiment, p.regimCompany, p.Rank, p.Name ";
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, reg);
				pstmt.setString(2, rc);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					String serviceNumber = rs.getString("serviceNumber");
					String name = rs.getString("name");
					String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
					String RankName = rs.getString("RankName") == null ? "" : rs.getString("RankName");
					String RegimentName = rs.getString("RegimentName") == null ? "":rs.getString("RegimentName");
					String regimCompanyName = rs.getString("regimCompanyName") == null ? "" : rs.getString("regimCompanyName");
					String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
					String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
					String isDevice = rs.getString("isDevice");
					//if(isDevice.equals("wb"))isDevice="W-B";
					//if(isDevice.equals("wg"))isDevice="W-G";

					String duty = rs.getString("duty");
					String userKey = rs.getString("UserKey");
					String latitude = rs.getString("Latitude");
					String longitude = rs.getString("longitude");
					String mgrs = MGRSString(latitude, longitude);
					String timestamp = format.format(rs.getTimestamp("Timestamp"));				
					String mobileNumber = rs.getString("MobileNumber");
					String EventId = Long.toString(tick);
					String EventDateTime = format1.format (System.currentTimeMillis());
					String MissionType = rs.getString("MissionType");
					String EquipID = serviceNumber;
					String EventType = "EVT-14";
					String ObjectType = "OBT-02";
					String EventRemark = "geoF-On 이탈 이벤트 발생\n"+RegimentName+"\n"+RankName+" "+name+"("+serviceNumber+")\n"+phone(mobileNumber);
					String Status = "EVS-01";
					String ActionStartDate = format2.format (System.currentTimeMillis());
					String ActionEndDate = ""; //rs.getString("ActionEndDate");
					String Actioncontents = ""; //rs.getString("Actioncontents");
					String ResultContents = ""; //rs.getString("ResultContents");
					String IsSendOK = "N";
					String GroupCode="XX";
					String EquipLocation = rs.getString("EquipLocation");
					String RoomName = rs.getString("RoomName");
					String RoomNumber = rs.getString("RoomNumber");
					String etc = rs.getString("etc");
					tick= tick+1;
					location = new Location(serviceNumber, userKey, name, Rank,RankName, Regiment,RegimentName,
							regimCompany,regimCompanyName, isDevice, duty, latitude, longitude, timestamp,EventId,EventDateTime,MissionType,EquipID
							 ,EventType,ObjectType,EventRemark,Status,ActionStartDate,ActionEndDate,Actioncontents,ResultContents,GroupCode,IsSendOK,EquipLocation,RoomName,mobileNumber,etc,RoomNumber,mgrs);
					
					//location = new Location(serviceNumber, userKey, name, Rank, Regiment,
					//	 regimCompany, isDevice, duty, latitude, longitude, timestamp);
					
					locations.add(location);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
				try { if(rs != null) rs.close(); } catch(SQLException e) {}
				try { if(con != null) con.close(); } catch(SQLException e) {}
			}			
		}
		return locations;
		
	}

	public ArrayList<Location> getLocationsByUser(String phoneNum) {

		String sql = "SELECT TOP(50) p.ServiceNumber, p.MobileNumber,p.Name,c.CodeName as Regiment, d.CodeName as regimCompany,e.CodeName as Rank, p.Duty,l.Userkey, l.Latitude, l.Longitude, l.Timestamp, l.isDevice, "
				+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.EquipLocation from beacons b where l.Uuid = b.Uuid) else '' end) as 'EquipLocation',  "
				+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomName from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomName',  "
				+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomNumber from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomNumber'  "

				+ " FROM dbo.PersonnelManagement p "
				+ " INNER JOIN Locations AS l  "
				+ " ON p.MobileNumber = l.UserKey  "
				+ " left outer join dbo.Code as c on p.Regiment = c.CodeID "
				+ " left outer join dbo.Code as d on p.regimCompany = d.CodeID "
				+ " left outer join dbo.Code as e on p.Rank = e.CodeID "
				+ " WHERE p.MobileNumber=?  "
				+ " ORDER BY l.Timestamp DESC";
		Location location = null;
		ArrayList<Location> locations = new ArrayList<Location>();

		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, phoneNum);
			rs = pstmt.executeQuery();

			while(rs.next()) {


				String serviceNumber = rs.getString("serviceNumber");
				String name = rs.getString("name");
				String Rank = rs.getString("Rank") ;
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
				String isDevice = rs.getString("isDevice");
				//if(isDevice.equals("wb"))isDevice="W-B";
				//if(isDevice.equals("wg"))isDevice="W-G";
				String duty = rs.getString("duty");
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));
				String EquipLocation = rs.getString("EquipLocation");
				String RoomName = rs.getString("RoomName");
				String RoomNumber = rs.getString("RoomNumber");
				String mobileNumber = rs.getString("MobileNumber");

				String Mgrs = MGRSString(latitude, longitude);				

				if(longitude == null || latitude == null) {
					
				} else {
					location = new Location( serviceNumber,  userKey,  name,  Rank,  Regiment,
							 regimCompany,  isDevice,  duty,  latitude,  longitude,  timestamp,
							 mobileNumber,  EquipLocation,  RoomName,RoomNumber,Mgrs) ;
					locations.add(location);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}


		//for(Location l:locations) {
		//	System.out.println(l.toString());
		//}

		return locations;

	}

	public ArrayList<Location> getLocationsByService(String serviceNum) {
		
		String sql = "SELECT top(50) p.ServiceNumber, p.MobileNumber, p.Name,c.CodeName as Regiment, d.CodeName as regimCompany, e.CodeName as Rank, p.Duty, l.Latitude, l.Longitude, l.Timestamp ,l.Userkey, l.isDevice,"
				+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.EquipLocation from beacons b where l.Uuid = b.Uuid) else '' end) as 'EquipLocation',  "
				+ "(case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomName from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomName', "
				+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomNumber from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomNumber'  "
	
				+ "FROM PersonnelManagement AS p "
				+ "INNER JOIN Locations AS l "
				+ "ON p.MobileNumber = l.UserKey "
				+ " left outer join dbo.Code as c on p.Regiment = c.CodeID "
				+ " left outer join dbo.Code as d on p.regimCompany = d.CodeID "
				+ " left outer join dbo.Code as e on p.Rank = e.CodeID "
				+ "WHERE p.ServiceNumber=? "
				+ "ORDER BY l.Timestamp DESC";
		Location location = null;
		ArrayList<Location> locations = new ArrayList<Location>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, serviceNum);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String serviceNumber = rs.getString("serviceNumber");
				String name = rs.getString("name");
				String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
				String isDevice = rs.getString("isDevice");
				//if(isDevice.equals("wb"))isDevice="W-B";
				//if(isDevice.equals("wg"))isDevice="W-G";
				String duty = rs.getString("duty");
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));
				String EquipLocation = rs.getString("EquipLocation");
				String RoomName = rs.getString("RoomName");
				String RoomNumber = rs.getString("RoomNumber");
				String mobileNumber = rs.getString("MobileNumber");
				String Mgrs = MGRSString(latitude, longitude);				

				if(longitude == null || latitude == null) {
			
				} else {
					location = new Location( serviceNumber,  userKey,  name,  Rank,  Regiment,
							 regimCompany,  isDevice,  duty,  latitude,  longitude,  timestamp,
							 mobileNumber,  EquipLocation,  RoomName,RoomNumber,Mgrs) ;
					locations.add(location);
				}
			//	System.out.println(location.toString());

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return locations;
		
	}
	
		
	public Location getLastLocation(String serviceNum) {
		
		Location location = null;
		//String sql = "select * from dbo.Locations where Timestamp in (select max(Timestamp) from dbo.Locations where UserKey=?)";
		String sql = "SELECT p.ServiceNumber, p.MobileNumber, p.Name,c.CodeName as Regiment, d.CodeName as regimCompany, e.CodeName as Rank, p.Duty, l.Latitude, l.Longitude, l.Timestamp ,l.Userkey, l.isDevice,"
				+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.EquipLocation from beacons b where l.Uuid = b.Uuid) else '' end) as 'EquipLocation',  "
				+ "(case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomName from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomName', "
				+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomNumber from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomNumber'  "

				+ "FROM dbo.PersonnelManagement p "
				+ "INNER JOIN dbo.MobileStatus l ON p.MobileNumber = l.UserKey "
				+ " left outer join dbo.Code as c on p.Regiment = c.CodeID "
				+ " left outer join dbo.Code as d on p.regimCompany = d.CodeID "
				+ " left outer join dbo.Code as e on p.Rank = e.CodeID "
				+ "WHERE p.ServiceNumber=? ";
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, serviceNum);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String serviceNumber = rs.getString("serviceNumber");
				String name = rs.getString("name");
				String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
				String isDevice = rs.getString("isDevice");
				//if(isDevice.equals("wb"))isDevice="W-B";
				//if(isDevice.equals("wg"))isDevice="W-G";
				String duty = rs.getString("duty");
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));
				String EquipLocation = rs.getString("EquipLocation");
				String RoomName = rs.getString("RoomName");
				String RoomNumber = rs.getString("RoomNumber");
				String mobileNumber = rs.getString("MobileNumber");
				String Mgrs = MGRSString(latitude, longitude);				

				if(longitude == null || latitude == null) {
			
				} else {
					location = new Location( serviceNumber,  userKey,  name,  Rank,  Regiment,
							 regimCompany,  isDevice,  duty,  latitude,  longitude,  timestamp,
							 mobileNumber,  EquipLocation,  RoomName,RoomNumber,Mgrs) ;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return location;
	}
	
	public Location getLastLocationByUser(String phoneNum) {

		Location location = null;
		//String sql = "select * from dbo.Locations where Timestamp in (select max(Timestamp) from dbo.Locations where UserKey=?)";
		String sql = "SELECT p.ServiceNumber, p.MobileNumber, p.Name,c.CodeName as Regiment, d.CodeName as regimCompany, e.CodeName as Rank, p.Duty, l.Latitude, l.Longitude, l.Timestamp ,l.Userkey, l.isDevice,"
				+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.EquipLocation from beacons b where l.Uuid = b.Uuid) else '' end) as 'EquipLocation',  "
				+ "(case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomName from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomName', "
				+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomNumber from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomNumber'  "
				+ "FROM dbo.PersonnelManagement p "
				+ "INNER JOIN dbo.MobileStatus l ON p.MobileNumber = l.UserKey "
				+ " left outer join dbo.Code as c on p.Regiment = c.CodeID "
				+ " left outer join dbo.Code as d on p.regimCompany = d.CodeID "
				+ " left outer join dbo.Code as e on p.Rank = e.CodeID "
				+ "WHERE p.MobileNumber=? ";

		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, phoneNum);
			rs = pstmt.executeQuery();

			while(rs.next()) {
				String serviceNumber = rs.getString("serviceNumber");
				String name = rs.getString("name");
				String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
				String isDevice = rs.getString("isDevice");
				//if(isDevice.equals("wb"))isDevice="W-B";
				//if(isDevice.equals("wg"))isDevice="W-G";
				String duty = rs.getString("duty");
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));
				String EquipLocation = rs.getString("EquipLocation");
				String RoomName = rs.getString("RoomName");
				String RoomNumber = rs.getString("RoomNumber");
				String mobileNumber = rs.getString("MobileNumber");
				String Mgrs = MGRSString(latitude, longitude);				

								
				location = new Location( serviceNumber,  userKey,  name,  Rank,  Regiment,
						 regimCompany,  isDevice,  duty,  latitude,  longitude,  timestamp,
						 mobileNumber,  EquipLocation,  RoomName,RoomNumber,Mgrs) ;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}

		//for(Location l:locations) {
		//	System.out.println(l.toString());
		//}

		return location;
	}

	public ArrayList<EquipLocation> getEquipLocations() {
		
		String sql="select equipId, b.CodeName as Regiment, c.CodeName as equipType,equipLocation,longitude,latitude"
				+ " FROM dbo.TotalEquip a"
				+ " INNER JOIN dbo.Code b ON b.CodeID = a.Regiment"
				+ " INNER JOIN dbo.Code c ON c.CodeID = a.equipType"
				+ " where Regiment ='RG-283'";
		
		EquipLocation equipLocationObject = null;
		ArrayList<EquipLocation> equipLocations = new ArrayList<EquipLocation>();
	//	JSONArray jsonLocations = new JSONArray();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				String equipId = rs.getString("EquipId");
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String equipType = rs.getString("EquipType");
				String equipLocation = rs.getString("EquipLocation");
				String longitude = rs.getString("Longitude");
				String latitude = rs.getString("Latitude");
				String Mgrs = MGRSString(latitude, longitude);				
				//System.out.println(equipId);
				
				equipLocationObject = new EquipLocation(equipId, Regiment, equipType, equipLocation, longitude, latitude,Mgrs);
				System.out.println(equipLocationObject.toString());
				
				equipLocations.add(equipLocationObject);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		
			//for(EquipLocation e:equipLocations) {
			//	System.out.println(e.toString());
			//}
		
		return equipLocations;
		
	}
	
	public ArrayList<EquipLocation> getEquipLocations(String reg, String et) {
		
		String sql = "select * from dbo.TotalEquip where Regiment = ? and EquipType = ?";		
		EquipLocation equipLocationObject = null;
		ArrayList<EquipLocation> equipLocations = new ArrayList<EquipLocation>();
		
		if(reg.equals("전체") && et.equals("전체")) {
			
			sql = "select equipId, b.CodeName as Regiment, c.CodeName as equipType,equipLocation,longitude,latitude"
					+ " FROM dbo.TotalEquip a"
					+ " INNER JOIN dbo.Code b ON b.CodeID = a.Regiment"
					+ " INNER JOIN dbo.Code c ON c.CodeID = a.equipType"
					+ " order by a.Regiment,equipType,equipId";
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				
				while(rs.next()) {
					
					String equipId = rs.getString("EquipId");
					String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
					String equipType = rs.getString("EquipType");
					String equipLocation = rs.getString("EquipLocation");
					String longitude = rs.getString("Longitude");
					String latitude = rs.getString("Latitude");
					String Mgrs = MGRSString(latitude, longitude);

					equipLocationObject = new EquipLocation(equipId, Regiment, equipType, equipLocation, longitude, latitude,Mgrs);
					System.out.println(equipLocationObject.toString());
					
					equipLocations.add(equipLocationObject);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
				try { if(rs != null) rs.close(); } catch(SQLException e) {}
				try { if(con != null) con.close(); } catch(SQLException e) {}
			}
			
		} else if(et.equals("전체")) {
			
			sql = "select equipId, b.CodeName as Regiment, c.CodeName as equipType,equipLocation,longitude,latitude"
					+ " FROM dbo.TotalEquip a"
					+ " INNER JOIN dbo.Code b ON b.CodeID = a.Regiment"
					+ " INNER JOIN dbo.Code c ON c.CodeID = a.EquipType"
					+ " where a.Regiment = ?"
					+ " order by a.Regiment,equipType,equipId";
					
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, reg);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					
					String equipId = rs.getString("EquipId");
					String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
					String equipType = rs.getString("EquipType");
					String equipLocation = rs.getString("EquipLocation");
					String longitude = rs.getString("Longitude");
					String latitude = rs.getString("Latitude");
					String Mgrs = MGRSString(latitude, longitude);

					equipLocationObject = new EquipLocation(equipId, Regiment, equipType, equipLocation, longitude, latitude,Mgrs);
					System.out.println(equipLocationObject.toString());
					
					equipLocations.add(equipLocationObject);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
				try { if(rs != null) rs.close(); } catch(SQLException e) {}
				try { if(con != null) con.close(); } catch(SQLException e) {}
			}
			
		} else {
			
			sql = "select equipId, b.CodeName as Regiment, c.CodeName as equipType,equipLocation,longitude,latitude"
					+ " FROM dbo.TotalEquip a"
					+ " INNER JOIN dbo.Code b ON b.CodeID = a.Regiment"
					+ " INNER JOIN dbo.Code c ON c.CodeID = a.equipType"
					+ " where a.Regiment = ? and a.EquipType = ?"
					+ " order by a.Regiment,equipType,equipId";
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, reg);
				pstmt.setString(2, et);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					
					String equipId = rs.getString("EquipId");
					String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
					String equipType = rs.getString("EquipType");
					String equipLocation = rs.getString("EquipLocation");
					String longitude = rs.getString("Longitude");
					String latitude = rs.getString("Latitude");
					String Mgrs = MGRSString(latitude, longitude);

					equipLocationObject = new EquipLocation(equipId, Regiment, equipType, equipLocation, longitude, latitude,Mgrs);
					System.out.println(equipLocationObject.toString());
					
					equipLocations.add(equipLocationObject);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
				try { if(rs != null) rs.close(); } catch(SQLException e) {}
				try { if(con != null) con.close(); } catch(SQLException e) {}
			}
			
		}

		return equipLocations;
		
	} 
	
	public ArrayList<EquipLocation> getEquipById(String equipId) {
		
		String sql="select equipId, b.CodeName as Regiment, c.CodeName as equipType,equipLocation,longitude,latitude"
				+ " FROM dbo.TotalEquip a"
				+ " INNER JOIN dbo.Code b ON b.CodeID = a.Regiment"
				+ " INNER JOIN dbo.Code c ON c.CodeID = a.equipType"
				+ " where EquipId=?";
		ArrayList<EquipLocation> equipLocations = new ArrayList<EquipLocation>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, equipId);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {				
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String equipType = rs.getString("EquipType");
				String equipLocation = rs.getString("EquipLocation");
				String longitude = rs.getString("Longitude");
				String latitude = rs.getString("Latitude");
				String Mgrs = MGRSString(latitude, longitude);
				
				//System.out.println(equipId);
				
				EquipLocation e = new EquipLocation(equipId, Regiment, equipType, equipLocation, longitude, latitude,Mgrs);
				//System.out.println(equipLocationObject.toString());
				
				equipLocations.add(e);
				System.out.println(e.toString());
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(pstmt != null) pstmt.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		
			//for(EquipLocation e:equipLocations) {
			//	System.out.println(e.toString());
			//}
		
		return equipLocations;
		
	}
	
	
public boolean getTotalPrivilegeCheck(String sn) {
		
		String sql="select * from dbo.TotalPrivilege where ServiceNumber = ?;";
		ArrayList<EquipLocation> equipLocations = new ArrayList<EquipLocation>();
		boolean flag=false;
		String ServiceNumber="";
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, sn);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				
				ServiceNumber= rs.getString("ServiceNumber");
					
				
			}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try { if(rs != null) rs.close(); } catch(SQLException e) {}
				try { if(pstmt != null) pstmt.close(); } catch(SQLException e) {}
				try { if(con != null) con.close(); } catch(SQLException e) {}
			}
		
		
			if("전체" != sn && sn.equals(ServiceNumber)) {;
				flag=true;
			}
		
		return flag;
}
	
	//같은 EquipType & Location 장비 검색
	public ArrayList<EquipLocation> getSameTypeEquips(EquipLocation equip) {
		
		ArrayList<EquipLocation> equipLocations = new ArrayList<EquipLocation>();
		String sql="select equipId, b.CodeName as Regiment, c.CodeName as equipType,equipLocation,longitude,latitude"
				+ " FROM dbo.TotalEquip a"
				+ " INNER JOIN dbo.Code b ON b.CodeID = a.Regiment"
				+ " INNER JOIN dbo.Code c ON c.CodeID = a.equipType"
				+ " where EquipType=? and Regiment=?";
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, equip.getEquipType());
			pstmt.setString(2, equip.getRegiment());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				String equipId = rs.getString("EquipId");
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String equipType = rs.getString("EquipType");
				String equipLocation = rs.getString("EquipLocation");
				String longitude = rs.getString("Longitude");
				String latitude = rs.getString("Latitude");
				String Mgrs = MGRSString(latitude, longitude);
		
				//System.out.println(equipId);
				
				EquipLocation e = new EquipLocation(equipId, Regiment, equipType, equipLocation, longitude, latitude,Mgrs);
				//System.out.println(equipLocationObject.toString());
				
				equipLocations.add(e);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(pstmt != null) pstmt.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		
			//for(EquipLocation e:equipLocations) {
			//	System.out.println(e.toString());
			//}
		
		return equipLocations;
		
	}
	

	public ArrayList<String> getEquipIdList(){
		
		String sql = "select EquipId from dbo.TotalEquip where Regiment='RG-280' or Regiment ='RG-283'";
		ArrayList<String> equipIdList = new ArrayList<String>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String equipId = rs.getString("EquipId");
				
				equipIdList.add(equipId);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		return equipIdList;		
		
	}
	

	
	public String getCodeName(String codeID){
			
			String sql = "select CodeName from dbo.Code where CodeID=?";
			String codeName = "";
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, codeID);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					codeName = rs.getString("CodeName");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
				try { if(rs != null) rs.close(); } catch(SQLException e) {}
				try { if(con != null) con.close(); } catch(SQLException e) {}
			}
		
			return codeName;		
			
		}
	
	public String getCodeID(String codeName){
		
		String sql = "select CodeID from dbo.Code where CodeName=?";
		String codeID = "";
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, codeName);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				codeID = rs.getString("codeID");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		return codeID;		
		
	}
	
	public String getCodeID(String codeType, String codeName){
			
			String sql = "select CodeID from dbo.Code where CodeType=? and CodeName=?";
			String codeID = "";
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, codeType);
				pstmt.setString(2, codeName);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					codeID = rs.getString("codeID");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
				try { if(rs != null) rs.close(); } catch(SQLException e) {}
				try { if(con != null) con.close(); } catch(SQLException e) {}
			}
		
			return codeID;		
			
		}
		
	public String getCodeName(String codeType, String codeId){
		
		String sql = "select CodeName from dbo.Code where CodeType=? and CodeId=?";
		String RankName = "";
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, codeType);
			pstmt.setString(2, codeId);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				RankName = rs.getString("CodeName");
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		return RankName;		
		
	}

	public String getName(String phoneNum) {
		
		String sql = "select Name from dbo.PersonnelManagement where MobileNumber=?";
		
		String name = "";
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, phoneNum);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				name = rs.getString("Name");
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
		return name;
		
	}
	
	/*
	public ArrayList<Location> getMobileStatus(){
		
		String sql = "select * from dbo.MobileStatus order by InputTime desc";
		Location location = null;
		ArrayList<Location> locations = new ArrayList<Location>();
	//	JSONArray jsonLocations = new JSONArray();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));
				
				location = new Location(userKey, latitude, longitude, timestamp);
			//	System.out.println(location.toString());
				
				locations.add(location);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return locations;
		
	}
	*/
	
	
	
	public ArrayList<Location> getMobileStatus(){
		
		Location location = null;
		long TICKS_AT_EPOCH = 621355968000000000L; 
		long tick = (System.currentTimeMillis() + TimeZone.getDefault().getRawOffset()) * 10000 + TICKS_AT_EPOCH;
		
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss.SSS");

		System.out.println(tick );
		System.out.println( format1.format (System.currentTimeMillis()));

		
		String sql = "select p.MobileNumber,p.ServiceNumber, p.Name,p.Regiment, c.CodeName as RegimentName, p.regimCompany,d.CodeName as regimCompanyName,p.Rank, e.CodeName as RankName, p.Duty, l.*,  "
				+ "  MissionType,"	
				+ " (case when (l.HeartRate > 1 and (l.isDevice = 'wb' or l.isDevice ='wg')) then  concat('Y',l.BatteryPercent) ELSE concat('N',l.BatteryPercent) END) AS etc, "
				+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.EquipLocation from beacons b where l.Uuid = b.Uuid) else '' end) as 'EquipLocation', "
				+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomName from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomName' , "
				+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomNumber from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomNumber'  "
				+ "from dbo.MobileStatus as l "
				+ "inner join dbo.PersonnelManagement as p on l.UserKey = p.MobileNumber "
				+ "left outer join dbo.Code as c on p.Regiment = c.CodeID "
				+ "left outer join dbo.Code as d on p.regimCompany = d.CodeID "
				+ "left outer join dbo.Code as e on p.Rank = e.CodeID "
				+ "order by l.InputTime desc ";
		ArrayList<Location> locations = new ArrayList<Location>();
	//	JSONArray jsonLocations = new JSONArray();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String serviceNumber = rs.getString("serviceNumber");
				String name = rs.getString("name");
				String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
				String RankName = rs.getString("RankName") == null ? "" : rs.getString("RankName");
				String RegimentName = rs.getString("RegimentName") == null ? "":rs.getString("RegimentName");
				String regimCompanyName = rs.getString("regimCompanyName") == null ? "" : rs.getString("regimCompanyName");
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
				String isDevice = rs.getString("isDevice");
				//if(isDevice.equals("wb"))isDevice="W-B";
				//if(isDevice.equals("wg"))isDevice="W-G";
				String duty = rs.getString("duty");
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String mgrs = MGRSString(latitude, longitude);
				String timestamp = format.format(rs.getTimestamp("Timestamp"));				
				String mobileNumber = rs.getString("MobileNumber");
				String EventId = Long.toString(tick);
				String EventDateTime = format1.format (System.currentTimeMillis());
				String MissionType = rs.getString("MissionType");
				String EquipID = serviceNumber;
				String EventType = "EVT-14";
				String ObjectType = "OBT-02";
				String EventRemark = "geoF-On 이탈 이벤트 발생\n"+RegimentName+"\n"+RankName+" "+name+"("+serviceNumber+")\n"+phone(mobileNumber);
				String Status = "EVS-01";
				String ActionStartDate = format2.format (System.currentTimeMillis());
				String ActionEndDate = ""; //rs.getString("ActionEndDate");
				String Actioncontents = ""; //rs.getString("Actioncontents");
				String ResultContents = ""; //rs.getString("ResultContents");
				String IsSendOK = "N";
				String GroupCode="XX";
				String EquipLocation = rs.getString("EquipLocation");
				String RoomName = rs.getString("RoomName");
				String RoomNumber = rs.getString("RoomNumber");
				String etc = rs.getString("etc");
				tick= tick+1;
				location = new Location(serviceNumber, userKey, name, Rank,RankName, Regiment,RegimentName,
						regimCompany,regimCompanyName, isDevice, duty, latitude, longitude, timestamp,EventId,EventDateTime,MissionType,EquipID
						 ,EventType,ObjectType,EventRemark,Status,ActionStartDate,ActionEndDate,Actioncontents,ResultContents,GroupCode,IsSendOK,EquipLocation,RoomName,mobileNumber,etc,RoomNumber,mgrs);
				locations.add(location);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(rs2 != null) rs2.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}

		}
	
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return locations;
		
	}
	
	
	public ArrayList<Location> getMobileStatus(String pn){
		
		Location location = null;
		long TICKS_AT_EPOCH = 621355968000000000L; 
		long tick = (System.currentTimeMillis() + TimeZone.getDefault().getRawOffset()) * 10000 + TICKS_AT_EPOCH;
		
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss.SSS");

		System.out.println(tick );
		System.out.println( format1.format (System.currentTimeMillis()));

		
		String sql = "select p.MobileNumber,p.ServiceNumber, p.Name,p.Regiment, c.CodeName as RegimentName, p.regimCompany,d.CodeName as regimCompanyName,p.Rank, e.CodeName as RankName, p.Duty, l.*,  "
				+ "  MissionType,"	
				+ " (case when (l.HeartRate > 1 and (l.isDevice = 'wb' or l.isDevice ='wg')) then  concat('Y',l.BatteryPercent) ELSE concat('N',l.BatteryPercent) END) AS etc, "
				+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.EquipLocation from beacons b where l.Uuid = b.Uuid) else '' end) as 'EquipLocation', "
				+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomName from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomName' , "
				+ " (case when l.IsDevice = 'W-B' or l.IsDevice ='P-B' then (select b.RoomNumber from beacons b where l.Uuid = b.Uuid) else '' end) as 'RoomNumber'  "	
				+ "from dbo.MobileStatus as l "
				+ "inner join dbo.PersonnelManagement as p on l.UserKey = p.MobileNumber "
				+ "left outer join dbo.Code as c on p.Regiment = c.CodeID "
				+ "left outer join dbo.Code as d on p.regimCompany = d.CodeID "
				+ "left outer join dbo.Code as e on p.Rank = e.CodeID "
				+ "where l.UserKey = '"+pn+"'"
				+ "order by l.InputTime desc ";
		ArrayList<Location> locations = new ArrayList<Location>();
	//	JSONArray jsonLocations = new JSONArray();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String serviceNumber = rs.getString("serviceNumber");
				String name = rs.getString("name");
				String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
				String RankName = rs.getString("RankName") == null ? "" : rs.getString("RankName");
				String RegimentName = rs.getString("RegimentName") == null ? "":rs.getString("RegimentName");
				String regimCompanyName = rs.getString("regimCompanyName") == null ? "" : rs.getString("regimCompanyName");
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
				String isDevice = rs.getString("isDevice");
				//if(isDevice.equals("wb"))isDevice="W-B";
				//if(isDevice.equals("wg"))isDevice="W-G";

				String duty = rs.getString("duty");
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));				
				String mobileNumber = rs.getString("MobileNumber");
				String EventId = Long.toString(tick);
				String EventDateTime = format1.format (System.currentTimeMillis());
				String MissionType = rs.getString("MissionType");
				String EquipID = serviceNumber;
				String EventType = "EVT-14";
				String ObjectType = "OBT-02";
				String EventRemark = "geoF-On 이탈 이벤트 발생\n"+RegimentName+"\n"+RankName+" "+name+"("+serviceNumber+")\n"+phone(mobileNumber);
				String Status = "EVS-01";
				String ActionStartDate = format2.format (System.currentTimeMillis());
				String ActionEndDate = ""; //rs.getString("ActionEndDate");
				String Actioncontents = ""; //rs.getString("Actioncontents");
				String ResultContents = ""; //rs.getString("ResultContents");
				String IsSendOK = "N";
				String GroupCode="XX";
				String EquipLocation = rs.getString("EquipLocation");
				String RoomName = rs.getString("RoomName");
				String RoomNumber = rs.getString("RoomNumber");
				String etc = rs.getString("etc");
				tick= tick+1;
				String Mgrs = MGRSString(latitude,longitude);

				location = new Location(serviceNumber, userKey, name, Rank,RankName, Regiment,RegimentName,
						regimCompany,regimCompanyName, isDevice, duty, latitude, longitude, timestamp,EventId,EventDateTime,MissionType,EquipID
						 ,EventType,ObjectType,EventRemark,Status,ActionStartDate,ActionEndDate,Actioncontents,ResultContents,GroupCode,IsSendOK,EquipLocation,RoomName,mobileNumber,etc,RoomNumber,Mgrs);
				//location = new Location(serviceNumber, userKey, name, Rank, Regiment,
				//	 regimCompany, isDevice, duty, latitude, longitude, timestamp);
				
				locations.add(location);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(rs2 != null) rs2.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}

		}
	
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return locations;
		
	}
	
	public ArrayList<Location> getMobileStatusByName(String n){
		
		String sql = "select TOP(50) l.*, p.ServiceNumber, p.Name, c.CodeName as Regiment, d.CodeName as regimCompany, e.CodeName as Rank, p.Duty "
				+ "from dbo.MobileStatus as l "
				+ "inner join dbo.PersonnelManagement as p on l.UserKey = p.MobileNumber "
				+ "left outer join dbo.Code as c on p.Regiment = c.CodeID "
				+ "left outer join dbo.Code as d on p.regimCompany = d.CodeID "
				+ "left outer join dbo.Code as e on p.Rank = e.CodeID "
				+ "where p.Name = ? "
				+ "order by l.InputTime desc ";
		Location location = null;
		ArrayList<Location> locations = new ArrayList<Location>();
	//	JSONArray jsonLocations = new JSONArray();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, n);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String serviceNumber = rs.getString("serviceNumber");
				String name = rs.getString("name");
				String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
				String isDevice = rs.getString("isDevice");
				//if(isDevice.equals("wb"))isDevice="W-B";
				//if(isDevice.equals("wg"))isDevice="W-G";
				String duty = rs.getString("duty");
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));
				
				location = new Location(serviceNumber, userKey, name, Rank, Regiment,
					 regimCompany, isDevice, duty, latitude, longitude, timestamp);
				
				locations.add(location);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return locations;
		
	}
	
	public ArrayList<Location> getMobileStatusByService(String s){
		
		String sql = "select TOP(50) l.*, p.ServiceNumber, p.Name, c.CodeName as Regiment, d.CodeName as regimCompany, e.CodeName as Rank, p.Duty "
				+ "from dbo.MobileStatus as l "
				+ "inner join dbo.PersonnelManagement as p on l.UserKey = p.MobileNumber "
				+ "left outer join dbo.Code as c on p.Regiment = c.CodeID "
				+ "left outer join dbo.Code as d on p.regimCompany = d.CodeID "
				+ "left outer join dbo.Code as e on p.Rank = e.CodeID "
				+ "where p.ServiceNumber = ? "
				+ "order by l.InputTime desc ";
		Location location = null;
		ArrayList<Location> locations = new ArrayList<Location>();
	//	JSONArray jsonLocations = new JSONArray();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, s);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String serviceNumber = rs.getString("serviceNumber");
				String name = rs.getString("name");
				String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
				String isDevice = rs.getString("isDevice");
				//if(isDevice.equals("wb"))isDevice="W-B";
				//if(isDevice.equals("wg"))isDevice="W-G";
				String duty = rs.getString("duty");
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));
				
				location = new Location(serviceNumber, userKey, name, Rank, Regiment,
					 regimCompany, isDevice, duty, latitude, longitude, timestamp);
				
				locations.add(location);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return locations;
		
	}
	
	public ArrayList<Location> getMobileStatusByMobile(String m){
		
		String sql = "select TOP(50) l.*, p.ServiceNumber, p.Name, c.CodeName as Regiment, d.CodeName as regimCompany, e.CodeName as Rank, p.Duty "
				+ "from dbo.MobileStatus as l "
				+ "inner join dbo.PersonnelManagement as p on l.UserKey = p.MobileNumber "
				+ "left outer join dbo.Code as c on p.Regiment = c.CodeID "
				+ "left outer join dbo.Code as d on p.regimCompany = d.CodeID "
				+ "left outer join dbo.Code as e on p.Rank = e.CodeID "
				+ "where p.MobileNumber = ? "
				+ "order by l.InputTime desc ";
		Location location = null;
		ArrayList<Location> locations = new ArrayList<Location>();
	//	JSONArray jsonLocations = new JSONArray();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, m);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String serviceNumber = rs.getString("serviceNumber");
				String name = rs.getString("name");
				String Rank = rs.getString("Rank") == null ? "" : rs.getString("Rank");
				String Regiment = rs.getString("Regiment") == null ? "":rs.getString("Regiment");
				String regimCompany = rs.getString("regimCompany") == null ? "":rs.getString("regimCompany");
				String isDevice = rs.getString("isDevice");
				//if(isDevice.equals("wb"))isDevice="W-B";
				//if(isDevice.equals("wg"))isDevice="W-G";
				String duty = rs.getString("duty");
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));
				
				location = new Location(serviceNumber, userKey, name, Rank, Regiment,
					 regimCompany, isDevice, duty, latitude, longitude, timestamp);
				
				locations.add(location);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return locations;
		
	}
	
	public ArrayList<String> getCodeNameList(String codeType){
		String sql = "select CodeName from dbo.Code where CodeType=?";
		ArrayList<String> codeNameList = new ArrayList<String>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, codeType);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String cn = rs.getString("CodeName");
				
				codeNameList.add(cn);
				 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		return codeNameList;
	}
	
	
	public String getCodeRemark(String codeType,String codeId){
		String sql = "select Remark from dbo.Code where CodeType=? and CodeId=?";
		String codeRemark="";
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, codeType);
			pstmt.setString(2, codeId);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				codeRemark = rs.getString("Remark");
				
				 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		return codeRemark;
	}
	
	public ArrayList<String> getCodeRemarkList(String codeType){
		String sql = "select DISTINCT Remark from dbo.Code where CodeType=?";
		ArrayList<String> codeRemarkList = new ArrayList<String>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, codeType);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String rm = rs.getString("Remark");
				
				codeRemarkList.add(rm);
				 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		return codeRemarkList;
	}
	
	public ArrayList<String> getCodeNameList(String codeType, String groupCode){
		String sql = "select CodeName from dbo.Code where CodeType=? and GroupCode=?";
		ArrayList<String> codeNameList = new ArrayList<String>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, codeType);
			pstmt.setString(2, groupCode);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String cn = rs.getString("CodeName");
				
				codeNameList.add(cn);
				 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		return codeNameList;
	}	
	
	
	public ArrayList<String> getCodeIDList(String codeType, String groupCode){
		String sql = "select CodeID from dbo.Code where CodeType=? and GroupCode=?";
		ArrayList<String> codeIDList = new ArrayList<String>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, codeType);
			pstmt.setString(2, groupCode);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String cn = rs.getString("CodeID");
				
				codeIDList.add(cn);
				 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		return codeIDList;
	}	
	
	public ArrayList<String> getFoodIDList(){
		String sql = "select CodeID from dbo.Code where CodeType='FoodCode' order by CodeName";
		ArrayList<String> codeIDList = new ArrayList<String>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String cn = rs.getString("CodeID");
				
				codeIDList.add(cn);
				 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		return codeIDList;
	}	
	
	public ArrayList<String> getDutyReg(){
		
		String sql = "SELECT DISTINCT b.Duty "
				+ "FROM dbo.PersonnelManagement AS b";
		ArrayList<String> dutys = new ArrayList<String>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String reg = rs.getString("Duty").trim();
				
				dutys.add(reg);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
		return dutys;		
		
	}
	
	public ArrayList<String> getMobileStatusReg(){
			
			String sql = "SELECT DISTINCT c.CodeName, c.CodeID "
					+ "FROM dbo.MobileStatus AS a "
					+ "INNER JOIN dbo.PersonnelManagement AS b ON a.UserKey = b.MobileNumber "
					+ "INNER JOIN dbo.Code AS c ON b.Regiment = c.CodeID and c.CodeType= 'Regiment' "
					+ "ORDER BY c.CodeID";
			ArrayList<String> Regiments = new ArrayList<String>();
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				
				while(rs.next()) {
					String reg = rs.getString("CodeName");
					
					Regiments.add(reg);
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
				try { if(rs != null) rs.close(); } catch(SQLException e) {}
				try { if(con != null) con.close(); } catch(SQLException e) {}
			}
			
			return Regiments;		
			
		}
	
	
	public ArrayList<String> getMobileManagementReg(){
		
		String sql = "SELECT DISTINCT c.CodeName, c.CodeID  "
				+ " FROM dbo.MobileManagement AS a  "
				+ " INNER JOIN dbo.PersonnelManagement AS b ON a.MobileNumber = b.MobileNumber  "
				+ " INNER JOIN dbo.Code AS c ON b.Regiment = c.CodeID and c.CodeType= 'Regiment'  "
				+ " ORDER BY c.CodeID";
		ArrayList<String> Regiments = new ArrayList<String>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String reg = rs.getString("CodeName");
				
				Regiments.add(reg);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
		return Regiments;		
		
	}
	
	public ArrayList<String> getBeaconReg(){
		
		String sql = "SELECT DISTINCT c.CodeName, c.CodeID  "
				+ " FROM dbo.Beacons AS a  "
				+ " INNER JOIN dbo.Code AS c ON a.Regiment = c.CodeID and c.CodeType= 'Regiment'  "
				+ " ORDER BY c.CodeID";
		ArrayList<String> Regiments = new ArrayList<String>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String reg = rs.getString("CodeName");
				
				Regiments.add(reg);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
		return Regiments;		
		
	}
	
	
	public ArrayList<String> getPersonnelReg(){
		
		String sql = "SELECT DISTINCT c.CodeName, c.CodeID "
				+ "FROM dbo.PersonnelManagement AS b "
				+ "INNER JOIN dbo.Code AS c ON b.Regiment = c.CodeID and c.CodeType= 'Regiment' "
				+ "ORDER BY c.CodeID";
		ArrayList<String> Regiments = new ArrayList<String>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String reg = rs.getString("CodeName");
				
				Regiments.add(reg);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
		return Regiments;		
		
	}
	
	public ArrayList<String> getFoodReg(){
		
		String sql = "SELECT DISTINCT c.CodeName, c.CodeID "
				+ "FROM dbo.FoodInventory AS b "
				+ "INNER JOIN dbo.Code AS c ON b.Regiment = c.CodeID and c.CodeType= 'Regiment' "
				+ "ORDER BY c.CodeID";
		ArrayList<String> Regiments = new ArrayList<String>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String reg = rs.getString("CodeName");
				
				Regiments.add(reg);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
		return Regiments;		
		
	}
	
	
	
	public ArrayList<String> getRankReg(){
		
		String sql = "SELECT DISTINCT c.CodeName, c.CodeID "
				+ "FROM dbo.MobileStatus AS a "
				+ "INNER JOIN dbo.PersonnelManagement AS b ON a.UserKey = b.MobileNumber "
				+ "INNER JOIN dbo.Code AS c ON b.Rank = c.CodeID "
				+ "ORDER BY c.CodeID";
		ArrayList<String> Regiments = new ArrayList<String>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String reg = rs.getString("CodeName");
				
				Regiments.add(reg);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
		return Regiments;		
		
	}
	public ArrayList<String> getMobileStatusRc(String reg){
		
		String sql = "SELECT DISTINCT d.CodeName, d.CodeID "
				+ "FROM dbo.MobileStatus AS a  "
				+ "INNER JOIN dbo.PersonnelManagement AS b ON a.UserKey = b.MobileNumber "
				+ "INNER JOIN dbo.Code AS c ON b.Regiment = c.CodeID "
				+ "INNER JOIN dbo.Code AS d ON b.regimCompany = d.CodeID "
				+ "WHERE b.Regiment = ? "
				+ "ORDER BY d.CodeID";
		ArrayList<String> rcs = new ArrayList<String>();
		rcs.add("전체");
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reg);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String regimCompany = rs.getString("CodeName").trim();
				
				rcs.add(regimCompany);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return rcs;		
		
	}
	
	public ArrayList<String> getMobileStatusRcID(String reg){
		
		String sql = "SELECT DISTINCT d.CodeName, d.CodeID "
				+ "FROM dbo.MobileStatus AS a  "
				+ "INNER JOIN dbo.PersonnelManagement AS b ON a.UserKey = b.MobileNumber "
				+ "INNER JOIN dbo.Code AS c ON b.Regiment = c.CodeID "
				+ "INNER JOIN dbo.Code AS d ON b.regimCompany = d.CodeID "
				+ "WHERE b.Regiment = ? "
				+ "ORDER BY d.CodeID";
		ArrayList<String> rcs = new ArrayList<String>();
		rcs.add("전체");
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reg);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String regimCompany = rs.getString("CodeID").trim();
				
				rcs.add(regimCompany);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return rcs;		
		
	}
	
	public ArrayList<String> getBeaconsRc(String reg){
		
		String sql = "SELECT DISTINCT d.CodeName, d.CodeID "
				+ "FROM dbo.Beacons AS a  "
				+ "INNER JOIN dbo.Code AS c ON a.Regiment = c.CodeID "
				+ "INNER JOIN dbo.Code AS d ON a.regimCompany = d.CodeID "
				+ "WHERE a.Regiment = ? "
				+ "ORDER BY d.CodeID";
		ArrayList<String> rcs = new ArrayList<String>();
		rcs.add("세부소속:전체");
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reg);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String regimCompany = rs.getString("CodeName").trim();
				
				rcs.add(regimCompany);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return rcs;		
		
	}
	
	public ArrayList<String> getBeaconsRcID(String reg){
		
		String sql = "SELECT DISTINCT d.CodeName, d.CodeID "
				+ "FROM dbo.Beacons AS a  "
				+ "INNER JOIN dbo.Code AS c ON a.Regiment = c.CodeID "
				+ "INNER JOIN dbo.Code AS d ON a.regimCompany = d.CodeID "
				+ "WHERE a.Regiment = ? "
				+ "ORDER BY d.CodeID";
		ArrayList<String> rcs = new ArrayList<String>();
		rcs.add("세부소속:전체");
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reg);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String regimCompany = rs.getString("CodeID").trim();
				
				rcs.add(regimCompany);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return rcs;		
		
	}
	
	public ArrayList<String> getMobileMangementRc(String reg){
		
		String sql = "SELECT DISTINCT d.CodeName, d.CodeID "
				+ "FROM dbo.MobileManagement AS a  "
				+ "INNER JOIN dbo.PersonnelManagement AS b ON a.MobileNumber = b.MobileNumber "
				+ "INNER JOIN dbo.Code AS c ON b.Regiment = c.CodeID "
				+ "INNER JOIN dbo.Code AS d ON b.regimCompany = d.CodeID "
				+ "WHERE c.CodeID = ? "
				+ "ORDER BY d.CodeID";
		ArrayList<String> rcs = new ArrayList<String>();
		rcs.add("세부소속:전체");
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reg);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String regimCompany = rs.getString("CodeName").trim();
				
				rcs.add(regimCompany);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return rcs;		
		
	}
	
	public ArrayList<String> getMobileMangementIDRc(String reg){
		
		String sql = "SELECT DISTINCT d.CodeName, d.CodeID "
				+ "FROM dbo.MobileManagement AS a  "
				+ "INNER JOIN dbo.PersonnelManagement AS b ON a.MobileNumber = b.MobileNumber "
				+ "INNER JOIN dbo.Code AS c ON b.Regiment = c.CodeID "
				+ "INNER JOIN dbo.Code AS d ON b.regimCompany = d.CodeID "
				+ "WHERE c.CodeID = ? "
				+ "ORDER BY d.CodeID";
		ArrayList<String> rcs = new ArrayList<String>();
		rcs.add("세부소속:전체");
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reg);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String regimCompany = rs.getString("CodeID").trim();
				
				rcs.add(regimCompany);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return rcs;		
		
	}
	
	public String PwRandom(){
		String sql="select master.dbo.pCrypto_enc('normal',dbo.ufn_GeneratePassword (9),'') as password";
		String pw="";
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			//pstmt.setString(1, pw);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				pw = rs.getString("password");	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		return pw;	
		
	}
	public ArrayList<String> getPersonnelRc(String reg){
		
		String sql = "SELECT DISTINCT d.CodeName, d.CodeID "
				+ "FROM dbo.PersonnelManagement AS b "
				+ "INNER JOIN dbo.Code AS c ON b.Regiment = c.CodeID "
				+ "INNER JOIN dbo.Code AS d ON b.regimCompany = d.CodeID "
				+ "WHERE c.CodeName = ? "
				+ "ORDER BY d.CodeID";
		ArrayList<String> rcs = new ArrayList<String>();
		rcs.add("세부소속:전체");
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reg);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String regimCompany = rs.getString("CodeName").trim();
				
				rcs.add(regimCompany);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return rcs;		
		
	}
	
	public ArrayList<String> getMobileStatusRcId(String reg){
		
		String sql = "SELECT DISTINCT d.CodeName, d.CodeID "
				+ "FROM dbo.MobileStatus AS a "
				+ "INNER JOIN dbo.PersonnelManagement AS b ON a.UserKey = b.MobileNumber "
				+ "INNER JOIN dbo.Code AS c ON b.Regiment = c.CodeID "
				+ "INNER JOIN dbo.Code AS d ON b.regimCompany = d.CodeID "
				+ "WHERE c.CodeName = ? "
				+ "ORDER BY d.CodeID";
		ArrayList<String> rcs = new ArrayList<String>();
		rcs.add("전체");
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reg);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String regimCompany = rs.getString("CodeID").trim();
				
				rcs.add(regimCompany);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return rcs;		
		
	}
	
	public ArrayList<String> getPersonnelRcId(String reg){
		
		String sql = "SELECT DISTINCT d.CodeName, d.CodeID "
				+ "FROM dbo.PersonnelManagement AS b "
				+ "INNER JOIN dbo.Code AS c ON b.Regiment = c.CodeID "
				+ "INNER JOIN dbo.Code AS d ON b.regimCompany = d.CodeID "
				+ "WHERE c.CodeName = ? "
				+ "ORDER BY d.CodeID";
		ArrayList<String> rcs = new ArrayList<String>();
		rcs.add("전체");
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reg);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String regimCompany = rs.getString("CodeID").trim();
				
				rcs.add(regimCompany);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return rcs;		
		
	}
	
	public ArrayList<String> getFoodStore(String reg){
		
		String sql = "SELECT DISTINCT c.CodeName, c.CodeID "
				+ "FROM dbo.FoodInventory AS a "
				+ "INNER JOIN dbo.Code AS b ON a.Regiment = b.CodeID "
				+ "INNER JOIN dbo.Code AS c ON a.Storehouse = c.CodeID "
				+ "WHERE b.CodeName = '"+reg
				+ "' ORDER BY c.CodeID";
		/*
		if(reg.equals("전체")) {
			sql = "SELECT DISTINCT c.CodeName, c.CodeID "
					+ "	FROM dbo.FoodInventory AS a "
					+ "	INNER JOIN dbo.Code AS b ON a.Regiment = b.CodeID "
					+ "	INNER JOIN dbo.Code AS c ON a.Storehouse = c.CodeID "
					+ "	ORDER BY c.CodeID;";
		}
		*/
		ArrayList<String> rcs = new ArrayList<String>();
		rcs.add("식당명:전체");
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String storehouse = rs.getString("CodeName");
				
				rcs.add(storehouse);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return rcs;		
		
	}
	
	public ArrayList<String> getFoodIndex(String reg,String sh){
		
		String sql = "";
		
		if(reg.equals("소속:전체") && sh.equals("식당명:전체")) {
			sql = "SELECT DISTINCT a.foodName "
					+ "FROM dbo.FoodInventory AS a "
					+ "INNER JOIN dbo.Code AS b ON a.Regiment = b.CodeID "
					+ "INNER JOIN dbo.Code AS c ON a.Storehouse = c.CodeID "
					+ " ORDER BY a.foodName";
		}else if(reg.equals("소속:전체")) {
			sql = "SELECT DISTINCT a.foodName "
					+ "FROM dbo.FoodInventory AS a "
					+ "INNER JOIN dbo.Code AS b ON a.Regiment = b.CodeID "
					+ "INNER JOIN dbo.Code AS c ON a.Storehouse = c.CodeID "
					+ "WHERE c.CodeName = '"+sh 
					+ "' ORDER BY a.foodName";
		}else if(sh.equals("식당명:전체")) {
			sql = "SELECT DISTINCT a.foodName "
					+ "FROM dbo.FoodInventory AS a "
					+ "INNER JOIN dbo.Code AS b ON a.Regiment = b.CodeID "
					+ "INNER JOIN dbo.Code AS c ON a.Storehouse = c.CodeID "
					+ "WHERE b.CodeName = '"+reg 
					+ "' ORDER BY a.foodName";
		}else {
			sql = "SELECT DISTINCT a.foodName "
					+ " FROM dbo.FoodInventory AS a "
					+ " INNER JOIN dbo.Code AS b ON a.Regiment = b.CodeID "
					+ " INNER JOIN dbo.Code AS c ON a.Storehouse = c.CodeID "
					+ " WHERE b.CodeName = '"+reg + "' and c.CodeName= '"+sh+"'"
					+ "	ORDER BY a.foodName;";
		
		}
		
		ArrayList<String> rcs = new ArrayList<String>();
		rcs.add("식재료명:전체");
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String foodName = rs.getString("foodName");
				
				rcs.add(foodName);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return rcs;		
		
	}
	
	public ArrayList<String> getTotalEquipReg(){
		
		String sql = "SELECT DISTINCT b.CodeName, b.CodeID "
				+ "FROM dbo.TotalEquip AS a "
				+ "INNER JOIN dbo.Code AS b ON a.Regiment = b.CodeID "
				+ "ORDER BY b.CodeID";
		ArrayList<String> Regiments = new ArrayList<String>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String reg = rs.getString("CodeName");
				
				Regiments.add(reg);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return Regiments;		
		
	}

	public ArrayList<String> getTotalEquipLocation(String reg){
		
		String sql = "SELECT DISTINCT EquipLocation "
				+ "FROM dbo.TotalEquip AS a "
				+ "INNER JOIN dbo.Code AS b ON a.Regiment = b.CodeID "
				+ "WHERE CodeName = ?";
		ArrayList<String> tel = new ArrayList<String>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reg);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String equipLocation = rs.getString("EquipLocation");
				
				tel.add(equipLocation);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return tel;		
		
	}
	
	public ArrayList<String> getTotalEquipType(String reg){
		
		String sql = "SELECT DISTINCT a.EquipType, b.CodeName "
				+ "FROM dbo.TotalEquip AS a "
				+ "INNER JOIN dbo.Code AS b ON a.EquipType = b.CodeID "
				+ "WHERE a.Regiment = ? "
				+ "ORDER BY a.EquipType";
		ArrayList<String> tet = new ArrayList<String>();
		tet.add("전체");
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reg);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String equipLocation = rs.getString("CodeName");
				tet.add(equipLocation);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return tet;
	}

	public ArrayList<String> getTotalEquipTypeID(String reg){
		
		String sql = "SELECT DISTINCT a.EquipType, b.CodeName "
				+ "FROM dbo.TotalEquip AS a "
				+ "INNER JOIN dbo.Code AS b ON a.EquipType = b.CodeID "
				+ "WHERE a.Regiment = ? "
				+ "ORDER BY a.EquipType";
		ArrayList<String> tet = new ArrayList<String>();
		tet.add("전체");
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, reg);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String equipLocation = rs.getString("EquipType");
				tet.add(equipLocation);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
			//for(Location l:locations) {
			//	System.out.println(l.toString());
			//}
		
		return tet;
	}
	
	
	public String getEmergencyGroupName(String MobileNumber){
		
		String sql = "SELECT GroupName "
				+ "FROM dbo.EmergencyGroup "
				+ "WHERE MobileNumber = ?";
		String EmergencyGroup = "";

		try {
			con = getConn2();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, MobileNumber);
			rs2 = pstmt.executeQuery();
			
			while(rs2.next()) {
				EmergencyGroup = rs2.getString("GroupName");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

		return EmergencyGroup;		
		
	}
	public static void main(String[] args) {
		
		DBConnection cd = new DBConnection();
		Location location = new Location();
		ArrayList<Location> locations = new ArrayList<Location>();
		ArrayList<EquipLocation> equipLocations = new ArrayList<EquipLocation>();
		
//		equipLocations = cd.getEquipLocations("전체", "전체");
//		
//		for(int i=0; i<equipLocations.size(); i++) {
//			System.out.println(equipLocations.get(i).toString());
//		}
		
		location = cd.getLastLocationByUser("01029215834");
		System.out.println(location.toString());
		
//		locations = cd.getMobileStatus2();
//		for(int i=0; i<locations.size(); i++) {
//		}
		
		
	}	

		
		
}


