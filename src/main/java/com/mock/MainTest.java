package com.mock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.apache.http.client.utils.DateUtils;

public class MainTest {

	public static void main(String[] args) {
		long start = start = System.currentTimeMillis();
		System.out.println(start+ "ms");
		for (int i = 5000; i < 10000; i++) {
			Connection conn = MDBManager.getConnection();
			
			try {
				String sql = "insert into travelrecord(id,user,traveldate,fee,days) values (?,?,?,?,?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, i);
				pstmt.setString(2, "lily");
				pstmt.setString(3, DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
				pstmt.setDouble(4, Math.floor(Math.random() * 10000));
				pstmt.setInt(5, 1);
				pstmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				MDBManager.closeConnection(conn);
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(end+ "ms");
		System.out.println(((end - start) / 1000) + "s");
	}

}
