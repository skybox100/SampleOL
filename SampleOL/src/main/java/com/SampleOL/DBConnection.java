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
    
	
    
	public ArrayList<Food> getFoodList(String reg, String sh) {
		
		String sql = "";
		Food food = null;
		ArrayList<Food> foods = new ArrayList<Food>();
	//	JSONArray jsonLocations = new JSONArray();

		try {
			con = getConn();				
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			if(reg.equals("전체") && sh.equals("전체")) {
				sql = "select regiment as regimentCode, c.CodeName as regiment,storehouse as storehouseCode,a.CodeName as storehouse,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSource,foodSource as foodSourceCode,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	inner join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	inner join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	inner join dbo.Code as c on f.regiment = c.CodeID "
						+ "	order by regimentCode desc,storehouseCode desc, qRcodeIdx desc; ";


			}else if(reg.equals("전체")) {
				sql = "select regiment as regimentCode, c.CodeName as regiment,storehouse as storehouseCode,a.CodeName as storehouse,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSource,foodSource as foodSourceCode,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	inner join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	inner join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	inner join dbo.Code as c on f.regiment = c.CodeID "
						+ " where f.storehouse = '"+sh+"'"
						+ "	order by regimentCode desc,storehouseCode desc, qRcodeIdx desc; ";
			}else if(sh.equals("전체")) {
				sql = "select regiment as regimentCode, c.CodeName as regiment,storehouse as storehouseCode,a.CodeName as storehouse,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSource,"
						+ "Source as foodSourceCode,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	inner join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	inner join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	inner join dbo.Code as c on f.regiment = c.CodeID "
						+ " where f.regiment = '"+reg+"'"
						+ "	order by regimentCode desc,storehouseCode desc, qRcodeIdx desc; ";
			}else {
				sql = "select regiment as regimentCode, c.CodeName as regiment,storehouse as storehouseCode,a.CodeName as storehouse,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,b.CodeName as foodSource,foodSource as foodSourceCode,qRcodeIdx,f.remark "
						+ " from dbo.FoodInventory f "
						+ "	inner join dbo.Code as a on f.storehouse = a.CodeID "
						+ "	inner join dbo.Code as b on f.foodSource = b.CodeID "
						+ "	inner join dbo.Code as c on f.regiment = c.CodeID "
						+ " where f.regiment = '"+reg+"' and f.storehouse = '"+sh+"'"
						+ "	order by regimentCode desc,storehouseCode desc, qRcodeIdx desc; ";
			}
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String regimentCode = rs.getString("regimentCode");
				String regiment = rs.getString("regiment");
				String storehouse = rs.getString("storehouse");
				String storehouseCode = rs.getString("storehouseCode");
				String foodCode = rs.getString("foodCode");
				String expirationDate = searchDateConvert(rs.getString("expirationDate"),"yyyy-MM-dd");
				String foodName = rs.getString("foodName");
				String storeDate = searchDateConvert(rs.getString("storeDate"),"yyyy-MM-dd");
				String currentQuantity = rs.getString("currentQuantity");
				String unit = rs.getString("unit");
				String foodSourceCode = rs.getString("foodSourceCode");
				String foodSource = rs.getString("foodSource");
				String qRcodeIdx = rs.getString("qRcodeIdx");
				String remark = rs.getString("remark");

				food= new Food(regimentCode,regiment,storehouse,storehouseCode,foodCode,expirationDate,foodName,storeDate,currentQuantity,unit,foodSourceCode,foodSource,qRcodeIdx,remark);
				System.out.println(regimentCode+","+regiment+","+storehouse+","+storehouseCode+","+foodCode+","+expirationDate+","+foodName+","+storeDate+","+currentQuantity+","+unit+","+foodSourceCode+","+foodSource+","+qRcodeIdx+","+remark);
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
	
	public ArrayList<PersonnelManagement> getPersonnelManagementList(String reg, String rc) {
		
		String sql = "";
		PersonnelManagement personnelmanagement = null;
		ArrayList<PersonnelManagement> personnelmanagements = new ArrayList<PersonnelManagement>();
	//	JSONArray jsonLocations = new JSONArray();

		try {
			con = getConn();				
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			if(reg.equals("전체") && rc.equals("전체")) {
				sql="SELECT p.ServiceNumber"
						+ "      ,f.CodeName as MissionType"
						+ "      ,e.CodeName as Rank"
						+ "      ,p.Name"
						+ "      ,c.CodeName as Regiment"
						+ "      ,d.CodeName as RegimCompany"
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
						+ "  inner join dbo.MobileStatus as l on l.UserKey = p.MobileNumber "
						+ "  inner join dbo.Code as c on p.Regiment = c.CodeID "
						+ "  inner join dbo.Code as d on p.RegimCompany = d.CodeID "
						+ "  inner join dbo.Code as e on p.rank = e.CodeID "
						+ "  inner join dbo.Code as f on p.MissionType = f.CodeID"
						+ "  inner join dbo.Code as g on p.LeaderType = g.CodeID;";


			}else if(reg.equals("전체")) {
				sql="SELECT p.ServiceNumber"
						+ "      ,f.CodeName as MissionType"
						+ "      ,e.CodeName as Rank"
						+ "      ,p.Name"
						+ "      ,c.CodeName as Regiment"
						+ "      ,d.CodeName as RegimCompany"
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
						+ "  inner join dbo.MobileStatus as l on l.UserKey = p.MobileNumber "
						+ "  inner join dbo.Code as c on p.Regiment = c.CodeID "
						+ "  inner join dbo.Code as d on p.RegimCompany = d.CodeID "
						+ "  inner join dbo.Code as e on p.rank = e.CodeID "
						+ "  inner join dbo.Code as f on p.MissionType = f.CodeID"
						+ "  inner join dbo.Code as g on p.LeaderType = g.CodeID"
						+ " where p.RegimCompany = '"+rc+"'";
			}else if(rc.equals("전체")) {
				sql="SELECT p.ServiceNumber"
						+ "      ,f.CodeName as MissionType"
						+ "      ,e.CodeName as Rank"
						+ "      ,p.Name"
						+ "      ,c.CodeName as Regiment"
						+ "      ,d.CodeName as RegimCompany"
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
						+ "  inner join dbo.MobileStatus as l on l.UserKey = p.MobileNumber "
						+ "  inner join dbo.Code as c on p.Regiment = c.CodeID "
						+ "  inner join dbo.Code as d on p.RegimCompany = d.CodeID "
						+ "  inner join dbo.Code as e on p.rank = e.CodeID "
						+ "  inner join dbo.Code as f on p.MissionType = f.CodeID"
						+ "  inner join dbo.Code as g on p.LeaderType = g.CodeID"
						+ " where p.regiment = '"+reg+"'";
			}else {
				sql="SELECT p.ServiceNumber"
						+ "      ,f.CodeName as MissionType"
						+ "      ,e.CodeName as Rank"
						+ "      ,p.Name"
						+ "      ,c.CodeName as Regiment"
						+ "      ,d.CodeName as RegimCompany"
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
						+ "  inner join dbo.MobileStatus as l on l.UserKey = p.MobileNumber "
						+ "  inner join dbo.Code as c on p.Regiment = c.CodeID "
						+ "  inner join dbo.Code as d on p.RegimCompany = d.CodeID "
						+ "  inner join dbo.Code as e on p.rank = e.CodeID "
						+ "  inner join dbo.Code as f on p.MissionType = f.CodeID"
						+ "  inner join dbo.Code as g on p.LeaderType = g.CodeID"
						+ " where p.regiment = '"+reg+"' and p.RegimCompany = '"+rc+"'";
			}
			
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String ServiceNumber = rs.getString("ServiceNumber");
				String MissionType = rs.getString("MissionType");
				String Rank = rs.getString("Rank");
				String Name = rs.getString("Name");
				String Regiment = rs.getString("Regiment");
				String RegimCompany = rs.getString("RegimCompany");
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

				personnelmanagement= new PersonnelManagement(ServiceNumber,MissionType,Rank,Name,Regiment,RegimCompany,MOS,Duty,HelpCare,BirthDate,JoinDate,PromotionDate,MovingDate,RetireDate,MobileNumber,MyPhoneNumber,ParentsNumber,Remark,Picture,Password,RegimPlatoon,RegimSquad,LeaderType,BloodType,Goout,Reserve01,Reserve02,Reserve03,Reserve04);
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

	
	public ArrayList<Beacons> getBeaconsList() {
		
		String sql = "";
		ArrayList<Beacons> beacons = new ArrayList<Beacons>();
		Beacons beacon = null;

		try {

			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
				sql="SELECT Uuid\r\n"
						+ "      ,Latitude\r\n"
						+ "      ,Longitude\r\n"
						+ "      ,d.CodeName as EquipType\r\n"
						+ "	  ,EquipType as EquipTypeCode\r\n"
						+ "      ,EquipId\r\n"
						+ "      ,ModelName\r\n"
						+ "      ,Manufacturer\r\n"
						+ "      ,a.CodeName as Regiment\r\n"
						+ "	  ,Regiment as RegimentCode\r\n"
						+ "      ,c.CodeName as RegimCompany\r\n"
						+ "	  ,RegimCompany as RegimCompanyCode\r\n"
						+ "      ,EquipLocation\r\n"
						+ "      ,RoomName\r\n"
						+ "      ,RoomNumber\r\n"
						+ "      ,b.Remark\r\n"
						+ "  FROM dbo.Beacons b\r\n"
						+ "  inner join dbo.code as a on a.CodeID = b.Regiment and a.CodeType ='Regiment'\r\n"
						+ "  inner join dbo.code as c on c.CodeID = b.RegimCompany and c.CodeType ='RegimCompany'\r\n"
						+ "  inner join dbo.code as d on d.CodeID = b.EquipType and d.CodeType ='EquipType'\r\n"
						+ "  inner join dbo.code as e on e.CodeID = b.EquipType and e.CodeType ='EquipType'\r\n"
						+ "  order by b.EquipId";
				con = getConn();				
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);	

				
				while(rs.next()) {
					String Uuid = rs.getString("Uuid");
					String Latitude = rs.getString("Latitude");
					String Longitude = rs.getString("Longitude");
					String EquipType = rs.getString("EquipType");
					String EquipTypeCode = rs.getString("EquipTypeCode");
					String EquipId = rs.getString("EquipId");
					String ModelName = rs.getString("ModelName");
					String Manufacturer = rs.getString("Manufacturer");
					String Regiment = rs.getString("Regiment");
					String RegimentCode = rs.getString("RegimentCode");
					String RegimCompany = rs.getString("RegimCompany");
					String RegimCompanyCode = rs.getString("RegimCompanyCode");
					String EquipLocation = rs.getString("EquipLocation");
					String RoomName = rs.getString("RoomName");
					String RoomNumber = rs.getString("RoomNumber");
					String Remark = rs.getString("Remark");

					beacon= new Beacons(Uuid,Latitude,Longitude,EquipType,EquipTypeCode,EquipId,ModelName,Manufacturer,Regiment,RegimentCode,RegimCompany,RegimCompanyCode,EquipLocation,RoomName,RoomNumber,Remark);
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
	


       
	
	
    
    
	
	public boolean PersonnelManagementDelete(String mobileNumber) {
		
		String sql = "";
	//	JSONArray jsonLocations = new JSONArray();
		boolean flag=false;
		
		try {
			con = getConn();				
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			sql="delete from dbo.PersonnelManagement where MobileNumber = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mobileNumber);
			flag=pstmt.execute();
			System.out.println(flag);			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(pstmt != null) pstmt.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}		
		
		return flag;
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
					+ "      ,p.RegimCompany "
					+ "      ,d.CodeName as RegimCompanyName "
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
					+ "      ,g.CodeName as LeaderType "
					+ "      ,p.BloodType "
					+ "      ,p.Goout "
					+ "      ,p.Reserve01 "
					+ "      ,p.Reserve02 "
					+ "      ,p.Reserve03 "
					+ "      ,p.Reserve04 "
					+ "  FROM dbo.PersonnelManagement p "
					+ "  inner join dbo.MobileStatus as l on l.UserKey = p.MobileNumber "
					+ "  inner join dbo.Code as c on p.Regiment = c.CodeID "
					+ "  inner join dbo.Code as d on p.RegimCompany = d.CodeID "
					+ "  inner join dbo.Code as e on p.rank = e.CodeID "
					+ "  inner join dbo.Code as f on p.MissionType = f.CodeID "
					+ "  inner join dbo.Code as g on p.LeaderType = g.CodeID "
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
				String Rank = rs.getString("Rank");
				String RankName = rs.getString("RankName");
				String Name = rs.getString("Name");
				String Regiment = rs.getString("Regiment");
				String RegimentName = rs.getString("RegimentName");
				String RegimCompany = rs.getString("RegimCompany");
				String RegimCompanyName = rs.getString("RegimCompanyName");
				String MOS = rs.getString("MOS");
				String Duty = rs.getString("Duty");
				String HelpCare = rs.getString("HelpCare");
				String BirthDate = searchDateConvert(rs.getString("BirthDate"),"yyyy-MM-dd");
				String JoinDate = searchDateConvert(rs.getString("JoinDate"),"yyyy-MM-dd");
				String PromotionDate = searchDateConvert(rs.getString("PromotionDate"),"yyyy-MM-dd");
				String MovingDate = searchDateConvert(rs.getString("MovingDate"),"yyyy-MM-dd");
				String RetireDate = searchDateConvert(rs.getString("RetireDate"),"yyyy-MM-dd");
				String MobileNumber = rs.getString("MobileNumber");
				String MyPhoneNumber = rs.getString("MyPhoneNumber");
				String ParentsNumber = rs.getString("ParentsNumber");
				String Remark = rs.getString("Remark");
				String Password =  rs.getString("Password");
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
				

				personnelmanagement= new PersonnelManagement(ServiceNumber,MissionType,MissionTypeName,Rank,RankName,Name,Regiment,RegimentName,RegimCompany,RegimCompanyName,MOS,Duty,HelpCare,BirthDate,JoinDate,PromotionDate,MovingDate,RetireDate,MobileNumber,MyPhoneNumber,ParentsNumber,Remark,Picture,Password,RegimPlatoon,RegimSquad,LeaderType,BloodType,Goout,Reserve01,Reserve02,Reserve03,Reserve04);
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
         System.out.println(e);
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
			if(reg.equals("전체") && rc.equals("전체") && ec.equals("전체") && ec.equals("전체")) {
				sql = "select m.MobileNumber, b.CodeName as Regiment,p.Regiment as RegimentCode, MobileType,m.Name,m.ServiceNumber, m.JoinDate, a.CodeName as RegimCompany, p.RegimCompany as RegimCompanyCode,c.CodeName as Rank, p.Rank as RankCode  "
						+ "from dbo.MobileManagement m "
						+ "	inner join dbo.PersonnelManagement p ON p.ServiceNumber = m.ServiceNumber "
						+ "	inner Join dbo.Code a ON p.RegimCompany = a.CodeID "
						+ "	inner Join dbo.Code b ON p.Regiment = b.CodeID  "
						+ "	inner Join dbo.Code c ON p.Rank = c.CodeID  "
						+ "	order by p.Regiment desc,p.RegimCompany desc, m.JoinDate desc; ";
					

			}else if(reg.equals("전체") && rc.equals("전체")) {
				sql = "select m.MobileNumber, b.CodeName as Regiment,p.Regiment as RegimentCode, MobileType,m.Name,m.ServiceNumber, m.JoinDate, a.CodeName as RegimCompany, p.RegimCompany as RegimCompanyCode,c.CodeName as Rank, p.Rank as RankCode  "
						+ "from dbo.MobileManagement m "
						+ "	inner join dbo.PersonnelManagement p ON p.ServiceNumber = m.ServiceNumber "
						+ "	inner Join dbo.Code a ON p.RegimCompany = a.CodeID "
						+ "	inner Join dbo.Code b ON p.Regiment = b.CodeID  "
						+ "	inner Join dbo.Code c ON p.Rank = c.CodeID  "
						+ " where m.MobileType ='"+ec+"'"
						+ "	order by p.Regiment desc,p.RegimCompany desc, m.JoinDate desc; ";
					

			}else if(rc.equals("전체") && ec.equals("전체")) {
				sql = "select m.MobileNumber, b.CodeName as Regiment,p.Regiment as RegimentCode, MobileType,m.Name,m.ServiceNumber, m.JoinDate, a.CodeName as RegimCompany, p.RegimCompany as RegimCompanyCode,c.CodeName as Rank, p.Rank as RankCode  "
						+ "from dbo.MobileManagement m "
						+ "	inner join dbo.PersonnelManagement p ON p.ServiceNumber = m.ServiceNumber "
						+ "	inner Join dbo.Code a ON p.RegimCompany = a.CodeID "
						+ "	inner Join dbo.Code b ON p.Regiment = b.CodeID  "
						+ "	inner Join dbo.Code c ON p.Rank = c.CodeID  "
						+ " where p.regiment = '"+reg+"'"
						+ "	order by p.Regiment desc,p.RegimCompany desc, m.JoinDate desc; ";
					

			}else if(reg.equals("전체")) {
				sql = "select m.MobileNumber, b.CodeName as Regiment,p.Regiment as RegimentCode, MobileType,m.Name,m.ServiceNumber, m.JoinDate, a.CodeName as RegimCompany, p.RegimCompany as RegimCompanyCode,c.CodeName as Rank, p.Rank as RankCode  "
						+ "from dbo.MobileManagement m "
						+ "	inner join dbo.PersonnelManagement p ON p.ServiceNumber = m.ServiceNumber "
						+ "	inner Join dbo.Code a ON p.RegimCompany = a.CodeID "
						+ "	inner Join dbo.Code b ON p.Regiment = b.CodeID  "
						+ "	inner Join dbo.Code c ON p.Rank = c.CodeID  "
						+ " where p.RegimCompany = '"+rc+"' and m.MobileType ='"+ec+"'"
						+ "	order by p.Regiment desc,p.RegimCompany desc, m.JoinDate desc; ";
			}else if(rc.equals("전체")) {
				sql = "select m.MobileNumber, b.CodeName as Regiment,p.Regiment as RegimentCode, MobileType,m.Name,m.ServiceNumber, m.JoinDate, a.CodeName as RegimCompany, p.RegimCompany as RegimCompanyCode,c.CodeName as Rank, p.Rank as RankCode  "
						+ "from dbo.MobileManagement m "
						+ "	inner join dbo.PersonnelManagement p ON p.ServiceNumber = m.ServiceNumber "
						+ "	inner Join dbo.Code a ON p.RegimCompany = a.CodeID "
						+ "	inner Join dbo.Code b ON p.Regiment = b.CodeID  "
						+ "	inner Join dbo.Code c ON p.Rank = c.CodeID  "
						+ " where p.regiment = '"+reg+"' and m.MobileType ='"+ec+"'"
						+ "	order by p.Regiment desc,p.RegimCompany desc, m.JoinDate desc; ";
			}else if(ec.equals("전체")) {
				sql = "select m.MobileNumber, b.CodeName as Regiment,p.Regiment as RegimentCode, MobileType,m.Name,m.ServiceNumber, m.JoinDate, a.CodeName as RegimCompany, p.RegimCompany as RegimCompanyCode,c.CodeName as Rank, p.Rank as RankCode  "
						+ "from dbo.MobileManagement m "
						+ "	inner join dbo.PersonnelManagement p ON p.ServiceNumber = m.ServiceNumber "
						+ "	inner Join dbo.Code a ON p.RegimCompany = a.CodeID "
						+ "	inner Join dbo.Code b ON p.Regiment = b.CodeID  "
						+ "	inner Join dbo.Code c ON p.Rank = c.CodeID  "
						+ " where p.regiment = '"+reg+"' and p.RegimCompany = '"+rc+"'"
						+ "	order by p.Regiment desc,p.RegimCompany desc, m.JoinDate desc; ";
			}else {
				sql = "select m.MobileNumber, b.CodeName as Regiment,p.Regiment as RegimentCode, MobileType,m.Name,m.ServiceNumber, m.JoinDate, a.CodeName as RegimCompany, p.RegimCompany as RegimCompanyCode,c.CodeName as Rank, p.Rank as RankCode  "
						+ "from dbo.MobileManagement m "
						+ "	inner join dbo.PersonnelManagement p ON p.ServiceNumber = m.ServiceNumber "
						+ "	inner Join dbo.Code a ON p.RegimCompany = a.CodeID "
						+ "	inner Join dbo.Code b ON p.Regiment = b.CodeID  "
						+ "	inner Join dbo.Code c ON p.Rank = c.CodeID  "
						+ " where p.regiment = '"+reg+"' and p.RegimCompany = '"+rc+"' and m.MobileType ='"+ec+"'"
						+ "	order by p.Regiment desc,p.RegimCompany desc, m.JoinDate desc; ";
			}
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String MobileNumber = phone(rs.getString("MobileNumber"));
				String Regiment = rs.getString("Regiment");
				String MobileType = rs.getString("MobileType");
				String Name = rs.getString("Name");
				String ServiceNumber = rs.getString("ServiceNumber");
				String JoinDate = searchDateConvert(rs.getString("JoinDate"),"yyyy-MM-dd");
				String RegimCompany = rs.getString("RegimCompany");
				String RegimCompanyCode = rs.getString("RegimCompanyCode");
				String RegimentCode = rs.getString("RegimentCode");
				String Rank = rs.getString("Rank");
				String RankCode = rs.getString("RankCode");



				mobile= new MobileEquip(MobileNumber,Regiment,Rank,Name,ServiceNumber,MobileType,JoinDate);
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
		
		String sql = "select c.latitude,c.longitude,c.r,a.CodeName as regiment  from dbo.Geofence c"
					+ " inner join dbo.Code a ON c.regiment=a.CodeID ";
	
		if(rc != "전체")
			sql = "select c.latitude,c.longitude,c.r,a.CodeName as regiment from dbo.Geofence c "
					+ " inner join dbo.Code a ON c.regiment=a.CodeID "
					+ " where regiment='"+rc+"'";
		
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
				String regiment = rs.getString("regiment");
				
				circle = new Circle(latitude, longitude, r,regiment);
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
			sql = "select top (50) l.*, p.* "
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
					String rank = rs.getString("rank");
					String regiment = rs.getString("regiment");
					String regimCompany = rs.getString("regimCompany");
					String isDevice = rs.getString("isDevice");
					String duty = rs.getString("duty");
					String userKey = rs.getString("UserKey");
					String latitude = rs.getString("Latitude");
					String longitude = rs.getString("longitude");
					String timestamp = format.format(rs.getTimestamp("Timestamp"));
					
					location = new Location(serviceNumber, userKey, name, rank, regiment,
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
				sql = "select top (50) l.*, p.* "
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
						String rank = rs.getString("rank");
						String regiment = rs.getString("regiment");
						String regimCompany = rs.getString("regimCompany");
						String isDevice = rs.getString("isDevice");
						String duty = rs.getString("duty");
						String userKey = rs.getString("UserKey");
						String latitude = rs.getString("Latitude");
						String longitude = rs.getString("longitude");
						String timestamp = format.format(rs.getTimestamp("Timestamp"));
						
						location = new Location(serviceNumber, userKey, name, rank, regiment,
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
				sql = "select top (50) l.*, p.* "
						+ "from dbo.Locations as l "
						+ "inner join dbo.PersonnelManagement as p "
						+ "on l.UserKey = p.MobileNumber "
						+ "where p.RegimCompany = ? "
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
						String rank = rs.getString("rank");
						String regiment = rs.getString("regiment");
						String regimCompany = rs.getString("regimCompany");
						String isDevice = rs.getString("isDevice");
						String duty = rs.getString("duty");
						String userKey = rs.getString("UserKey");
						String latitude = rs.getString("Latitude");
						String longitude = rs.getString("longitude");
						String timestamp = format.format(rs.getTimestamp("Timestamp"));
						
						location = new Location(serviceNumber, userKey, name, rank, regiment,
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
			sql = "select top (50) l.*, p.* "
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
					String rank = rs.getString("rank");
					String regiment = rs.getString("regiment");
					String regimCompany = rs.getString("regimCompany");
					String isDevice = rs.getString("isDevice");
					String duty = rs.getString("duty");
					String userKey = rs.getString("UserKey");
					String latitude = rs.getString("Latitude");
					String longitude = rs.getString("longitude");
					String timestamp = format.format(rs.getTimestamp("Timestamp"));
					
					location = new Location(serviceNumber, userKey, name, rank, regiment,
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
				sql = "select top (50) l.*, p.* "
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
						String rank = rs.getString("rank");
						String regiment = rs.getString("regiment");
						String regimCompany = rs.getString("regimCompany");
						String isDevice = rs.getString("isDevice");
						String duty = rs.getString("duty");
						String userKey = rs.getString("UserKey");
						String latitude = rs.getString("Latitude");
						String longitude = rs.getString("longitude");
						String timestamp = format.format(rs.getTimestamp("Timestamp"));
						
						location = new Location(serviceNumber, userKey, name, rank, regiment,
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
				sql = "select top (50) l.*, p.* "
						+ "from dbo.Locations as l "
						+ "inner join dbo.PersonnelManagement as p "
						+ "on l.UserKey = p.MobileNumber "
						+ "where l.isDevice = ? and p.Regiment = ? and p.RegimCompany = ? "
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
						String rank = rs.getString("rank");
						String regiment = rs.getString("regiment");
						String regimCompany = rs.getString("regimCompany");
						String isDevice = rs.getString("isDevice");
						String duty = rs.getString("duty");
						String userKey = rs.getString("UserKey");
						String latitude = rs.getString("Latitude");
						String longitude = rs.getString("longitude");
						String timestamp = format.format(rs.getTimestamp("Timestamp"));
						
						location = new Location(serviceNumber, userKey, name, rank, regiment,
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
			sql = "select top (50) l.*, p.* "
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
					String rank = rs.getString("rank");
					String regiment = rs.getString("regiment");
					String regimCompany = rs.getString("regimCompany");
					String isDevice = rs.getString("isDevice");
					String duty = rs.getString("duty");
					String userKey = rs.getString("UserKey");
					String latitude = rs.getString("Latitude");
					String longitude = rs.getString("longitude");
					String timestamp = format.format(rs.getTimestamp("Timestamp"));
					
					location = new Location(serviceNumber, userKey, name, rank, regiment,
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
				sql = "select top (50) l.*, p.* "
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
						String rank = rs.getString("rank");
						String regiment = rs.getString("regiment");
						String regimCompany = rs.getString("regimCompany");
						String isDevice = rs.getString("isDevice");
						String duty = rs.getString("duty");
						String userKey = rs.getString("UserKey");
						String latitude = rs.getString("Latitude");
						String longitude = rs.getString("longitude");
						String timestamp = format.format(rs.getTimestamp("Timestamp"));
						
						location = new Location(serviceNumber, userKey, name, rank, regiment,
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
				sql = "select top (50) l.*, p.* "
						+ "from dbo.MobileStatus as l "
						+ "inner join dbo.PersonnelManagement as p "
						+ "on l.UserKey = p.MobileNumber "
						+ "where p.RegimCompany = ? "
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
						String rank = rs.getString("rank");
						String regiment = rs.getString("regiment");
						String regimCompany = rs.getString("regimCompany");
						String isDevice = rs.getString("isDevice");
						String duty = rs.getString("duty");
						String userKey = rs.getString("UserKey");
						String latitude = rs.getString("Latitude");
						String longitude = rs.getString("longitude");
						String timestamp = format.format(rs.getTimestamp("Timestamp"));
						
						location = new Location(serviceNumber, userKey, name, rank, regiment,
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
			sql = "select top (50) l.*, p.* "
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
					String rank = rs.getString("rank");
					String regiment = rs.getString("regiment");
					String regimCompany = rs.getString("regimCompany");
					String isDevice = rs.getString("isDevice");
					String duty = rs.getString("duty");
					String userKey = rs.getString("UserKey");
					String latitude = rs.getString("Latitude");
					String longitude = rs.getString("longitude");
					String timestamp = format.format(rs.getTimestamp("Timestamp"));
					
					location = new Location(serviceNumber, userKey, name, rank, regiment,
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
				sql = "select top (50) l.*, p.* "
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
						String rank = rs.getString("rank");
						String regiment = rs.getString("regiment");
						String regimCompany = rs.getString("regimCompany");
						String isDevice = rs.getString("isDevice");
						String duty = rs.getString("duty");
						String userKey = rs.getString("UserKey");
						String latitude = rs.getString("Latitude");
						String longitude = rs.getString("longitude");
						String timestamp = format.format(rs.getTimestamp("Timestamp"));
						
						location = new Location(serviceNumber, userKey, name, rank, regiment,
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
				sql = "select top (50) l.*, p.* "
						+ "from dbo.MobileStatus as l "
						+ "inner join dbo.PersonnelManagement as p "
						+ "on l.UserKey = p.MobileNumber "
						+ "where l.isDevice = ? and p.Regiment = ? and p.RegimCompany = ? "
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
						String rank = rs.getString("rank");
						String regiment = rs.getString("regiment");
						String regimCompany = rs.getString("regimCompany");
						String isDevice = rs.getString("isDevice");
						String duty = rs.getString("duty");
						String userKey = rs.getString("UserKey");
						String latitude = rs.getString("Latitude");
						String longitude = rs.getString("longitude");
						String timestamp = format.format(rs.getTimestamp("Timestamp"));
						
						location = new Location(serviceNumber, userKey, name, rank, regiment,
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
	

	
public ArrayList<Location> getMobileStatus(String reg, String rc) {
		
		String sql = "select top (50) * from dbo.MobileStatus order by InputTime desc";
		Location location = null;
		ArrayList<Location> locations = new ArrayList<Location>();
	//	JSONArray jsonLocations = new JSONArray();
		
		if(reg.equals("전체") && rc.equals("전체")) {
			sql = "select l.*, p.ServiceNumber, p.Name, c.CodeName as Regiment, d.CodeName as RegimCompany, e.CodeName as Rank, p.Duty, (case when (l.HeartRate > 1 and (l.isDevice = 'wb' or l.isDevice ='wg')) then  concat('Y',l.BatteryPercent) ELSE concat('N',l.BatteryPercent) END) AS etc "
					+ "from dbo.MobileStatus as l "
					+ "inner join dbo.PersonnelManagement as p on l.UserKey = p.MobileNumber "
					+ "inner join dbo.Code as c on p.Regiment = c.CodeID "
					+ "inner join dbo.Code as d on p.RegimCompany = d.CodeID "
					+ "inner join dbo.Code as e on p.rank = e.CodeID "
					+ "order by Regiment, RegimCompany, Rank, p.Name ";
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				
				while(rs.next()) {
					
					String serviceNumber = rs.getString("serviceNumber");
					String name = rs.getString("name");
					String rank = rs.getString("rank");
					String regiment = rs.getString("regiment");
					String regimCompany = rs.getString("regimCompany");
					String isDevice = rs.getString("isDevice");
					String duty = rs.getString("duty");
					String userKey = rs.getString("UserKey");
					String latitude = rs.getString("Latitude");
					String longitude = rs.getString("longitude");
					String timestamp = format.format(rs.getTimestamp("Timestamp"));
					String etc = rs.getString("etc");

					location = new Location(serviceNumber, userKey, name, rank, regiment,
							 regimCompany, isDevice, duty, latitude, longitude, timestamp,etc);
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
		} else if(rc.equals("전체")) {
			
			sql = "select l.*, p.ServiceNumber, p.Name, c.CodeName as Regiment, d.CodeName as RegimCompany, e.CodeName as Rank, p.Duty, (case when (l.HeartRate > 1 and (l.isDevice = 'wb' or l.isDevice ='wg')) then  concat('Y',l.BatteryPercent) ELSE concat('N',l.BatteryPercent) END) AS etc "
					+ "from dbo.MobileStatus as l "
					+ "inner join dbo.PersonnelManagement as p on l.UserKey = p.MobileNumber "
					+ "inner join dbo.Code as c on p.Regiment = c.CodeID "
					+ "inner join dbo.Code as d on p.RegimCompany = d.CodeID "
					+ "inner join dbo.Code as e on p.rank = e.CodeID "
					+ "where p.Regiment = ? "
					+ "order by Regiment, RegimCompany, Rank, p.Name ";
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, reg);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					
					String serviceNumber = rs.getString("serviceNumber");
					String name = rs.getString("name");
					String rank = rs.getString("rank");
					String regiment = rs.getString("regiment");
					String regimCompany = rs.getString("regimCompany");
					String isDevice = rs.getString("isDevice");
					String duty = rs.getString("duty");
					String userKey = rs.getString("UserKey");
					String latitude = rs.getString("Latitude");
					String longitude = rs.getString("longitude");
					String timestamp = format.format(rs.getTimestamp("Timestamp"));
					String etc = rs.getString("etc");

					location = new Location(serviceNumber, userKey, name, rank, regiment,
							 regimCompany, isDevice, duty, latitude, longitude, timestamp,etc);
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
			sql = "select l.*, p.ServiceNumber, p.Name, c.CodeName as Regiment, d.CodeName as RegimCompany, e.CodeName as Rank, p.Duty, (case when (l.HeartRate > 1 and (l.isDevice = 'wb' or l.isDevice ='wg')) then  concat('Y',l.BatteryPercent) ELSE concat('N',l.BatteryPercent) END) AS etc "
					+ "from dbo.MobileStatus as l "
					+ "inner join dbo.PersonnelManagement as p on l.UserKey = p.MobileNumber "
					+ "inner join dbo.Code as c on p.Regiment = c.CodeID "
					+ "inner join dbo.Code as d on p.RegimCompany = d.CodeID "
					+ "inner join dbo.Code as e on p.rank = e.CodeID "
					+ "where p.Regiment = ? and p.RegimCompany = ? "
					+ "order by Regiment, RegimCompany, Rank, p.Name ";
			
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
					String rank = rs.getString("rank");
					String regiment = rs.getString("regiment");
					String regimCompany = rs.getString("regimCompany");
					String isDevice = rs.getString("isDevice");
					String duty = rs.getString("duty");
					String userKey = rs.getString("UserKey");
					String latitude = rs.getString("Latitude");
					String longitude = rs.getString("longitude");
					String timestamp = format.format(rs.getTimestamp("Timestamp"));
					String etc = rs.getString("etc");

					location = new Location(serviceNumber, userKey, name, rank, regiment,
							 regimCompany, isDevice, duty, latitude, longitude, timestamp,etc);
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

		String sql = "SELECT TOP(50) p.ServiceNumber, p.MobileNumber, p.Rank, p.Name, p.RegimCompany, p.Duty, l.Latitude, l.Longitude, l.Timestamp "
				+ "FROM PersonnelManagement AS p "
				+ "INNER JOIN Locations AS l "
				+ "ON p.MobileNumber = l.UserKey "
				+ "WHERE p.MobileNumber=? "
				+ "ORDER BY l.Timestamp DESC";
		Location location = null;
		ArrayList<Location> locations = new ArrayList<Location>();

		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, phoneNum);
			rs = pstmt.executeQuery();

			while(rs.next()) {
				String serviceNumber = rs.getString(1);
				String userKey = rs.getString(2);
				String rank = rs.getString(3);
				String name = rs.getString(4);
				String regiment = rs.getString(5);
				String duty = rs.getString(6);
				String latitude = rs.getString(7);
				String longitude = rs.getString(8);
				String timestamp = format.format(rs.getTimestamp(9));

				if(longitude == null || latitude == null) {
					
				} else {
					location = new Location(serviceNumber, userKey, name, rank, regiment, duty, latitude, longitude, timestamp);
					//	System.out.println(location.toString());
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
		
		String sql = "SELECT TOP(50) p.ServiceNumber, p.MobileNumber, p.Rank, p.Name, p.RegimCompany, p.Duty, l.Latitude, l.Longitude, l.Timestamp "
				+ "FROM PersonnelManagement AS p "
				+ "INNER JOIN Locations AS l "
				+ "ON p.MobileNumber = l.UserKey "
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
				String serviceNumber = rs.getString(1);
				String userKey = rs.getString(2);
				String rank = rs.getString(3);
				String name = rs.getString(4);
				String regiment = rs.getString(5);
				String duty = rs.getString(6);
				String latitude = rs.getString(7);
				String longitude = rs.getString(8);
				String timestamp = format.format(rs.getTimestamp(9));

				if(longitude == null || latitude == null) {
			
				} else {
					location = new Location(serviceNumber, userKey, name, rank, regiment, duty, latitude, longitude, timestamp);
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
		String sql = "SELECT p.ServiceNumber, p.MobileNumber, p.Rank, p.Name, p.Regiment, p.RegimCompany, p.duty, l.UserKey, l.IsDevice, l.Latitude, l.Longitude, l.Timestamp "
				+ "FROM PersonnelManagement p "
				+ "INNER JOIN MobileStatus l "
				+ "ON p.MobileNumber = l.UserKey "
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
				String rank = rs.getString("rank");
				String regiment = rs.getString("regiment");
				String regimCompany = rs.getString("regimCompany");
				String isDevice = rs.getString("isDevice");
				String duty = rs.getString("duty");
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));
								
				location = new Location(serviceNumber, userKey, name, rank, regiment,
						 regimCompany, isDevice, duty, latitude, longitude, timestamp);
				
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
		String sql = "SELECT p.ServiceNumber, p.MobileNumber, p.Rank, p.Name, p.Regiment, p.RegimCompany, p.Duty, l.UserKey, l.IsDevice, l.Latitude, l.Longitude, l.Timestamp "
				+ "FROM dbo.PersonnelManagement p "
				+ "INNER JOIN dbo.MobileStatus l ON p.MobileNumber = l.UserKey "
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
				String rank = rs.getString("rank");
				String regiment = rs.getString("regiment");
				String regimCompany = rs.getString("regimCompany");
				String isDevice = rs.getString("isDevice");
				String duty = rs.getString("duty");
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));
								
				location = new Location(serviceNumber, userKey, name, rank, regiment,
						 regimCompany, isDevice, duty, latitude, longitude, timestamp);

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
		
		String sqlTotalEquip = "select * from dbo.TotalEquip where Regiment ='RG-283'";
		
		EquipLocation equipLocationObject = null;
		ArrayList<EquipLocation> equipLocations = new ArrayList<EquipLocation>();
	//	JSONArray jsonLocations = new JSONArray();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(sqlTotalEquip);
			
			while(rs.next()) {
				
				String equipId = rs.getString("EquipId");
				String regiment = rs.getString("Regiment");
				String equipType = rs.getString("EquipType");
				String equipLocation = rs.getString("EquipLocation");
				String longitude = rs.getString("Longitude");
				String latitude = rs.getString("Latitude");
								
				//System.out.println(equipId);
				
				equipLocationObject = new EquipLocation(equipId, regiment, equipType, equipLocation, longitude, latitude);
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
			
			sql = "select equipId, b.CodeName as regiment, c.CodeName as equipType,equipLocation,longitude,latitude"
					+ " FROM dbo.TotalEquip a"
					+ " INNER JOIN dbo.Code b ON b.CodeID = a.regiment"
					+ " INNER JOIN dbo.Code c ON c.CodeID = a.equipType";
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				
				while(rs.next()) {
					
					String equipId = rs.getString("EquipId");
					String regiment = rs.getString("Regiment");
					String equipType = rs.getString("EquipType");
					String equipLocation = rs.getString("EquipLocation");
					String longitude = rs.getString("Longitude");
					String latitude = rs.getString("Latitude");
					
					equipLocationObject = new EquipLocation(equipId, regiment, equipType, equipLocation, longitude, latitude);
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
			
			sql = "select equipId, b.CodeName as regiment, c.CodeName as equipType,equipLocation,longitude,latitude"
					+ " FROM dbo.TotalEquip a"
					+ " INNER JOIN dbo.Code b ON b.CodeID = a.Regiment"
					+ " INNER JOIN dbo.Code c ON c.CodeID = a.EquipType"
					+ " where a.Regiment = ?";
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, reg);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					
					String equipId = rs.getString("EquipId");
					String regiment = rs.getString("Regiment");
					String equipType = rs.getString("EquipType");
					String equipLocation = rs.getString("EquipLocation");
					String longitude = rs.getString("Longitude");
					String latitude = rs.getString("Latitude");
					
					equipLocationObject = new EquipLocation(equipId, regiment, equipType, equipLocation, longitude, latitude);
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
			
			sql = "select equipId, b.CodeName as regiment, c.CodeName as equipType,equipLocation,longitude,latitude"
					+ " FROM dbo.TotalEquip a"
					+ " INNER JOIN dbo.Code b ON b.CodeID = a.regiment"
					+ " INNER JOIN dbo.Code c ON c.CodeID = a.equipType"
					+ " where a.Regiment = ? and a.EquipType = ?";
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, reg);
				pstmt.setString(2, et);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					
					String equipId = rs.getString("EquipId");
					String regiment = rs.getString("Regiment");
					String equipType = rs.getString("EquipType");
					String equipLocation = rs.getString("EquipLocation");
					String longitude = rs.getString("Longitude");
					String latitude = rs.getString("Latitude");
					
					equipLocationObject = new EquipLocation(equipId, regiment, equipType, equipLocation, longitude, latitude);
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
		
		String sql = "select * from dbo.TotalEquip where EquipId=?";
		ArrayList<EquipLocation> equipLocations = new ArrayList<EquipLocation>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, equipId);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				String regiment = rs.getString("Regiment");
				String equipType = rs.getString("EquipType");
				String equipLocation = rs.getString("EquipLocation");
				String longitude = rs.getString("Longitude");
				String latitude = rs.getString("Latitude");
								
				//System.out.println(equipId);
				
				EquipLocation e = new EquipLocation(equipId, regiment, equipType, equipLocation, longitude, latitude);
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
	
	//같은 EquipType & Location 장비 검색
	public ArrayList<EquipLocation> getSameTypeEquips(EquipLocation equip) {
		
		ArrayList<EquipLocation> equipLocations = new ArrayList<EquipLocation>();
		String sql = "select * from dbo.TotalEquip where EquipType=? and Regiment=?";
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, equip.getEquipType());
			pstmt.setString(2, equip.getRegiment());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				String equipId = rs.getString("EquipId");
				String regiment = rs.getString("Regiment");
				String equipType = rs.getString("EquipType");
				String equipLocation = rs.getString("EquipLocation");
				String longitude = rs.getString("Longitude");
				String latitude = rs.getString("Latitude");
								
				//System.out.println(equipId);
				
				EquipLocation e = new EquipLocation(equipId, regiment, equipType, equipLocation, longitude, latitude);
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
		String rankName = "";
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, codeType);
			pstmt.setString(2, codeId);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				rankName = rs.getString("CodeName");
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
	
		return rankName;		
		
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

		
		String sql = "select l.*, p.ServiceNumber, p.Name, c.CodeName as Regiment, d.CodeName as RegimCompany, e.CodeName as Rank, p.Duty, "
				+ " MissionType "
				+ "from dbo.MobileStatus as l "
				+ "inner join dbo.PersonnelManagement as p on l.UserKey = p.MobileNumber "
				+ "inner join dbo.Code as c on p.Regiment = c.CodeID "
				+ "inner join dbo.Code as d on p.RegimCompany = d.CodeID "
				+ "inner join dbo.Code as e on p.rank = e.CodeID "
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
				String rank = rs.getString("rank");
				String regiment = rs.getString("regiment");
				String regimCompany = rs.getString("regimCompany");
				String isDevice = rs.getString("isDevice");
				String duty = rs.getString("duty");
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));
				
				String EventId = Long.toString(tick);
				String EventDateTime = format1.format (System.currentTimeMillis());
				String MissionType = rs.getString("MissionType");
				String EquipID = "ESE";
				String EventType = "EVT-14";
				String ObjectType = "OBT-02";
				String EventRemark = "이탈 이벤트 발생, 상황접수";
				String Status = "EVS-01";
				String ActionStartDate = format2.format (System.currentTimeMillis());
				String ActionEndDate = ""; //rs.getString("ActionEndDate");
				String Actioncontents = ""; //rs.getString("Actioncontents");
				String ResultContents = ""; //rs.getString("ResultContents");
				String IsSendOK = "N";
				String GroupCode=getEmergencyGroupName(userKey);
				location = new Location(serviceNumber, userKey, name, rank, regiment,
							 regimCompany, isDevice, duty, latitude, longitude, timestamp, EventId, EventDateTime, MissionType, EquipID
						 , EventType, ObjectType, EventRemark, Status, ActionStartDate, ActionEndDate, Actioncontents, ResultContents, GroupCode, IsSendOK, userKey);
				
				//location = new Location(serviceNumber, userKey, name, rank, regiment,
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
	
	
	public ArrayList<Location> getMobileStatus(String pn){
		
		Location location = null;
		long TICKS_AT_EPOCH = 621355968000000000L; 
		long tick = (System.currentTimeMillis() + TimeZone.getDefault().getRawOffset()) * 10000 + TICKS_AT_EPOCH;
		
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss.SSS");

		System.out.println(tick );
		System.out.println( format1.format (System.currentTimeMillis()));

		
		String sql = "select l.*, p.ServiceNumber, p.Name, c.CodeName as Regiment, d.CodeName as RegimCompany, e.CodeName as Rank, p.Duty, "
				+ " MissionType "
				+ "from dbo.MobileStatus as l "
				+ "inner join dbo.PersonnelManagement as p on l.UserKey = p.MobileNumber "
				+ "inner join dbo.Code as c on p.Regiment = c.CodeID "
				+ "inner join dbo.Code as d on p.RegimCompany = d.CodeID "
				+ "inner join dbo.Code as e on p.rank = e.CodeID "
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
				String rank = rs.getString("rank");
				String regiment = rs.getString("regiment");
				String regimCompany = rs.getString("regimCompany");
				String isDevice = rs.getString("isDevice");
				String duty = rs.getString("duty");
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));
				
				String EventId = Long.toString(tick);
				String EventDateTime = format1.format (System.currentTimeMillis());
				String MissionType = rs.getString("MissionType");
				String EquipID = "ESE";
				String EventType = "EVT-14";
				String ObjectType = "OBT-02";
				String EventRemark = "이탈 이벤트 발생, 상황접수";
				String Status = "EVS-01";
				String ActionStartDate = format2.format (System.currentTimeMillis());
				String ActionEndDate = ""; //rs.getString("ActionEndDate");
				String Actioncontents = ""; //rs.getString("Actioncontents");
				String ResultContents = ""; //rs.getString("ResultContents");
				String IsSendOK = "N";
				String GroupCode=getEmergencyGroupName(userKey);
				location = new Location(serviceNumber, userKey, name, rank, regiment,
							 regimCompany, isDevice, duty, latitude, longitude, timestamp, EventId, EventDateTime, MissionType, EquipID
						 , EventType, ObjectType, EventRemark, Status, ActionStartDate, ActionEndDate, Actioncontents, ResultContents, GroupCode, IsSendOK, userKey);
				
				//location = new Location(serviceNumber, userKey, name, rank, regiment,
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
		
		String sql = "select l.*, p.ServiceNumber, p.Name, c.CodeName as Regiment, d.CodeName as RegimCompany, e.CodeName as Rank, p.Duty "
				+ "from dbo.MobileStatus as l "
				+ "inner join dbo.PersonnelManagement as p on l.UserKey = p.MobileNumber "
				+ "inner join dbo.Code as c on p.Regiment = c.CodeID "
				+ "inner join dbo.Code as d on p.RegimCompany = d.CodeID "
				+ "inner join dbo.Code as e on p.rank = e.CodeID "
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
				String rank = rs.getString("rank");
				String regiment = rs.getString("regiment");
				String regimCompany = rs.getString("regimCompany");
				String isDevice = rs.getString("isDevice");
				String duty = rs.getString("duty");
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));
				
				location = new Location(serviceNumber, userKey, name, rank, regiment,
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
		
		String sql = "select l.*, p.ServiceNumber, p.Name, c.CodeName as Regiment, d.CodeName as RegimCompany, e.CodeName as Rank, p.Duty "
				+ "from dbo.MobileStatus as l "
				+ "inner join dbo.PersonnelManagement as p on l.UserKey = p.MobileNumber "
				+ "inner join dbo.Code as c on p.Regiment = c.CodeID "
				+ "inner join dbo.Code as d on p.RegimCompany = d.CodeID "
				+ "inner join dbo.Code as e on p.rank = e.CodeID "
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
				String rank = rs.getString("rank");
				String regiment = rs.getString("regiment");
				String regimCompany = rs.getString("regimCompany");
				String isDevice = rs.getString("isDevice");
				String duty = rs.getString("duty");
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));
				
				location = new Location(serviceNumber, userKey, name, rank, regiment,
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
		
		String sql = "select l.*, p.ServiceNumber, p.Name, c.CodeName as Regiment, d.CodeName as RegimCompany, e.CodeName as Rank, p.Duty "
				+ "from dbo.MobileStatus as l "
				+ "inner join dbo.PersonnelManagement as p on l.UserKey = p.MobileNumber "
				+ "inner join dbo.Code as c on p.Regiment = c.CodeID "
				+ "inner join dbo.Code as d on p.RegimCompany = d.CodeID "
				+ "inner join dbo.Code as e on p.rank = e.CodeID "
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
				String rank = rs.getString("rank");
				String regiment = rs.getString("regiment");
				String regimCompany = rs.getString("regimCompany");
				String isDevice = rs.getString("isDevice");
				String duty = rs.getString("duty");
				String userKey = rs.getString("UserKey");
				String latitude = rs.getString("Latitude");
				String longitude = rs.getString("longitude");
				String timestamp = format.format(rs.getTimestamp("Timestamp"));
				
				location = new Location(serviceNumber, userKey, name, rank, regiment,
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
				String reg = rs.getString("Duty");
				
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
			ArrayList<String> regiments = new ArrayList<String>();
			
			try {
				con = getConn();
				System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				
				while(rs.next()) {
					String reg = rs.getString("CodeName");
					
					regiments.add(reg);
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
				try { if(rs != null) rs.close(); } catch(SQLException e) {}
				try { if(con != null) con.close(); } catch(SQLException e) {}
			}
			
			return regiments;		
			
		}
	
	public ArrayList<String> getRankReg(){
		
		String sql = "SELECT DISTINCT c.CodeName, c.CodeID "
				+ "FROM dbo.MobileStatus AS a "
				+ "INNER JOIN dbo.PersonnelManagement AS b ON a.UserKey = b.MobileNumber "
				+ "INNER JOIN dbo.Code AS c ON b.rank = c.CodeID "
				+ "ORDER BY c.CodeID";
		ArrayList<String> regiments = new ArrayList<String>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String reg = rs.getString("CodeName");
				
				regiments.add(reg);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(stmt != null) stmt.close(); } catch(SQLException e) {}
			try { if(rs != null) rs.close(); } catch(SQLException e) {}
			try { if(con != null) con.close(); } catch(SQLException e) {}
		}
		
		return regiments;		
		
	}
	
	public ArrayList<String> getMobileStatusRc(String reg){
		
		String sql = "SELECT DISTINCT d.CodeName, d.CodeID "
				+ "FROM dbo.MobileStatus AS a "
				+ "INNER JOIN dbo.PersonnelManagement AS b ON a.UserKey = b.MobileNumber "
				+ "INNER JOIN dbo.Code AS c ON b.Regiment = c.CodeID "
				+ "INNER JOIN dbo.Code AS d ON b.RegimCompany = d.CodeID "
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
				+ "INNER JOIN dbo.Code AS d ON b.RegimCompany = d.CodeID "
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
		
		if(reg.equals("전체")) {
			sql = "SELECT DISTINCT c.CodeName, c.CodeID "
					+ "	FROM dbo.FoodInventory AS a "
					+ "	INNER JOIN dbo.Code AS b ON a.Regiment = b.CodeID "
					+ "	INNER JOIN dbo.Code AS c ON a.Storehouse = c.CodeID "
					+ "	ORDER BY c.CodeID;";
		}
		
		ArrayList<String> rcs = new ArrayList<String>();
		rcs.add("전체");
		
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
	
	public ArrayList<String> getTotalEquipReg(){
		
		String sql = "SELECT DISTINCT b.CodeName, b.CodeID "
				+ "FROM dbo.TotalEquip AS a "
				+ "INNER JOIN dbo.Code AS b ON a.Regiment = b.CodeID "
				+ "ORDER BY b.CodeID";
		ArrayList<String> regiments = new ArrayList<String>();
		
		try {
			con = getConn();
			System.out.println("[" + format.format(new Timestamp(System.currentTimeMillis())) + "] " + "Connection Made");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				String reg = rs.getString("CodeName");
				
				regiments.add(reg);
				
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
		
		return regiments;		
		
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
				+ "INNER JOIN dbo.Code AS c ON a.Regiment = c.CodeID "
				+ "WHERE c.CodeName = ? "
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


