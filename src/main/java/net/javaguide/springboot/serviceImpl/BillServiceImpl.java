package net.javaguide.springboot.serviceImpl;

import java.io.FileOutputStream;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;
import net.javaguide.springboot.POJO.Bill;
import net.javaguide.springboot.constents.Constance;
import net.javaguide.springboot.dao.BillDao;
import net.javaguide.springboot.jwt.jwtFilter;
import net.javaguide.springboot.service.BillService;
import net.javaguide.springboot.utills.Utils;


@Slf4j
@Service
public class BillServiceImpl implements BillService {
	
	@Autowired
	jwtFilter jwtFilter;
	
	@Autowired
	BillDao billDao;

	@Override
	public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
		log.info("inside generateReport");
		try {
			String fileName;
			if (validaterRequestMap(requestMap)) {
				if (requestMap.containsKey("isGenerate")&& !(Boolean)requestMap.get("isGenerate")) {
					fileName=(String) requestMap.get("uuid");
				}else {
					fileName=Utils.getUUID();
					requestMap.put("uuid", fileName);
					insertBill(requestMap);
				}
				
				String data="Name :"+requestMap.get("name") +"\n"+"Contact Number :"+requestMap.get("contactNumber")+
						"\n"+"Email :"+requestMap.get("email")+"\n"+"Payment Method :"+requestMap.get("paymentMethod");
				Document document=new Document();
				PdfWriter.getInstance(document, new FileOutputStream(Constance.STORE_LOCATION+"\\"+fileName+".pdf"));
				
				document.open();
				setRectangleInPdf(document);
				
				Paragraph chunk=new Paragraph("PERSIST TECH",getFont("Header"));
				chunk.setAlignment(Element.ALIGN_CENTER);
				document.add(chunk);
				
				Paragraph paragraph=new Paragraph(data+"\n\n",getFont("Data"));
				document.add(paragraph);
				
				PdfPTable table= new PdfPTable(5);
				table.setWidthPercentage(100);
				addTableHeader(table);
				
				JSONArray JsonArray= Utils.getJsonArrayFromString((String)requestMap.get("productDetails"));
				for (int i = 0; i < JsonArray.length(); i++) {
					addTable(table,Utils.getMapFromJson(JsonArray.getString(i)));
					
					document.add(table);
					
					Paragraph footer=new Paragraph("Total : "+requestMap.get("totalAmount")+"\n"
							+"ThanYou for visiting please visit again!!",getFont("Data"));
					document.add(footer);
					document.close();
					return Utils.getResponseEntity("{\"uuid\":\""+fileName+"\"}",HttpStatus.OK);
				}
			}
			return Utils.getResponseEntity("require data not found .", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
	private void addTable(PdfPTable table, Map<String, Object> data) {
		log.info("inside addTable.");
		table.addCell((String)data.get("name"));
		table.addCell((String)data.get("category"));
		table.addCell((String)data.get("quantity"));
		table.addCell(Double.toString((Double)data.get("price")));
		table.addCell(Double.toString((Double)data.get("total")));

	}


	private void addTableHeader(PdfPTable table) {
		log.info("inside addTableHeader.");
		java.util.stream.Stream.of("Name","Category","quantity","Price","Subtotal")
		.forEach(ColumnTitle ->{
			PdfPCell header=new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(2);
			header.setPhrase(new Phrase(ColumnTitle));
			header.setBackgroundColor(BaseColor.YELLOW);
			header.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.setVerticalAlignment(Element.ALIGN_CENTER);
			table.addCell(header);
		});
		

	}


	private void setRectangleInPdf(Document document) throws DocumentException {
		log.info("inside setRectangleInPdf");
		com.itextpdf.text.Rectangle rectangle=new com.itextpdf.text.Rectangle(577, 825, 18, 15);
		rectangle.enableBorderSide(1);
		rectangle.enableBorderSide(2);
		rectangle.enableBorderSide(4);
		rectangle.enableBorderSide(8);
		rectangle.setBorderColor(BaseColor.BLACK);
		rectangle.setBorderWidth(1);
		document.add(rectangle);
		
		
		

	}


	private Font getFont(String type) {
		log.info("inside getFont");
		switch (type) {
		case "Header": 
			Font headerFont=FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
			headerFont.setStyle(Font.BOLD);
			return headerFont;
			
		case "Data":
			Font dataFont=FontFactory.getFont(FontFactory.TIMES_ROMAN,11,BaseColor.BLACK);
			dataFont.setStyle(Font.BOLD);
			return dataFont;
		default:
			return new Font();
		}

		
	}


	private void insertBill(Map<String, Object> requestMap) {
		try {
			Bill bill=new Bill();
			bill.setUuid((String) requestMap.get("uuid"));
			bill.setName((String)requestMap.get(""));
			bill.setEmail((String)requestMap.get("email"));
			bill.setContactNumber((String)requestMap.get("contactNumber"));
			bill.setPaymentMethod((String)requestMap.get("setPaymentMethod"));
			bill.setTotal((String)requestMap.get("total"));
			bill.setProductDetail((String)requestMap.get("productDetail"));
			bill.setCreatedBy(jwtFilter.getCurrentUser());
			billDao.save(bill);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	private boolean validaterRequestMap(Map<String, Object> requestMap) {
		return requestMap.containsKey("name")
				&&requestMap.containsKey("contactNumber")
				&&requestMap.containsKey("email")
				&&requestMap.containsKey("paymentMethod")
				&&requestMap.containsKey("productDetails")
				&&requestMap.containsKey("totalAmount");
	}
	

}
