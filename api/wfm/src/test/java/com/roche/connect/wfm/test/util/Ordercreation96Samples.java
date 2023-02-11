package com.roche.connect.wfm.test.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;

import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.PatientDTO;

public class Ordercreation96Samples {
    public static Response postsamples(String url, Object object, String token, List<Object> providers)
        throws UnsupportedEncodingException {
        
        Builder builder = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), providers);
        if (token != null)
            builder.header("Cookie", "brownstoneauthcookie=" + token);
        
        return builder.post(Entity.entity(object, MediaType.APPLICATION_JSON));
    }
    
    public static Response post(String url, Entity entity, String token, List<Object> providers)
        throws UnsupportedEncodingException {
        
        Builder builder = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), providers);
        if (token != null)
            builder.header("Cookie", "brownstoneauthcookie=" + token);
        return builder.post(entity);
    }
    
	public static void main(String[] args) {

		//do {
			createOrder();

		//} while (canContinue());
	}
    
	public static boolean canContinue() {
		System.out.println("Want to break?? Or you will continue by creating Orders???");
		System.out.println("IF YOU WANT TO CONTINUE Type as 'YES' or 'TRUE'");
		System.out.println("IF YOU WANT TO DIS-CONTINUE Type as 'NO' or 'FALSE'");
		Scanner sc = new Scanner(System.in);
		String closescanner = sc.nextLine();
		if (StringUtils.isBlank(closescanner)) {
			System.exit(0);
		}
		sc.close();
		return (closescanner.equalsIgnoreCase("True") || closescanner.equalsIgnoreCase("Yes"));

	}
    private static void createOrder() {
		
		 System.out.println("Enter The number of ORDERS to be created:");
	        Scanner scanner = new Scanner(System.in);
	        String nooforders = scanner.nextLine();
	       if(StringUtils.isBlank(nooforders)) {
	           System.out.println(
	               "Number of ORDERS cannot be Blank or Empty");
	           System.exit(0);
	           
	       }
	        int orderNumbers=Integer.parseInt(nooforders);
	        if (orderNumbers > 96 || orderNumbers < 1) {
	            System.out.println(
	                "Maximum orders to be created for ORDER CREATION --<MP96> is 96. Please enter the correct values");
	            System.exit(0);
	        }
	        System.out.println("Please Enter 6 digit AccessioningID Number::");
	        Scanner scannerSecond = new Scanner(System.in);
	        String scannerAccessioingID = scanner.nextLine();
	        if (StringUtils.isBlank(scannerAccessioingID) || scannerAccessioingID.length() < 6) {
	            System.out
	                .println("Entered AccessioningID is either empty or Please enter 6 digit unique Number to proceed:");
	            System.out.println("PLease restart the Program");
	            System.exit(0);
	        }
	        
	        System.out.println("Please ContainerID Number to be created::");
	        Scanner scannerThird = new Scanner(System.in);
	        String ContainerID = scannerThird.nextLine();
	        if (StringUtils.isBlank(ContainerID)) {
	            System.out
	                .println("Entered ContainerID is  empty:");
	            System.out.println("PLease restart the Program");
	            System.exit(0);
	        }
	        
	       
	        scanner.close();
	        scannerSecond.close();
	        scannerThird.close();
	        List<OrderParentDTO> orderParentList = new ArrayList<OrderParentDTO>();;
	        try {
	            
	            String localhost = "http://localhost:96/omm";
	            String AUTHENTICATED_JSON = "/json";
	            String OMM_SSU_URL = "/rest/api/v1/order";
	            String OMM_CONTAINESAMPLES = "/rest/api/v1/containersamples";
	            
	            String url = localhost + AUTHENTICATED_JSON + OMM_SSU_URL;
	            int accessioningIdInt = Integer.parseInt(scannerAccessioingID);
	            
	            List<ContainerSamplesDTO> containerSamples = new CopyOnWriteArrayList<ContainerSamplesDTO>();
	            String token = SecurityToken.getToken();
	            
	            IntStream.range(0, orderNumbers).forEach(i->{
	            	 OrderParentDTO orderParentNew1 = null;
		                OrderDTO orderdto = new OrderDTO();
		                ContainerSamplesDTO containersample = new ContainerSamplesDTO();
		                try {
							orderParentNew1=getParentDTO();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		                orderdto = orderParentNew1.getOrder();
		                orderdto.setAccessioningId(Integer.toString(accessioningIdInt+i));
		                containersample.setAccessioningID(orderdto.getAccessioningId());
		                containersample.setContainerType("96 wellplate");
		                containersample.setContainerID(ContainerID);
		                containerSamples.add(containersample);
		                OrderParentDTO odParentDto = new OrderParentDTO();
		                odParentDto.setOrder(orderdto);
		                orderParentList.add(odParentDto);
		                orderParentNew1 = null;
	            });
	            System.out.println("Entered Orders::" + orderNumbers + "Order creation in OMM [ORDER TABLE] ready to start");
	            
	            orderParentList.stream().forEach(odd ->{
	            	try {
	            		Ordercreation96Samples.post(url, Entity.entity(odd, MediaType.APPLICATION_JSON), token, null);
	            		
	            	} catch (Exception e) {
	            		
	            		System.out.println("Error occured in " + orderNumbers + "Order creation");
	            	}
	            });
	            
	            
	            String s =
	                "A1,A2,A3,A4,A5,A6,A7,A8,A9,A10,A11,A12,B1,B2,B3,B4,B5,B6,B7,B8,B9,B10,B11,B12,C1,C2,C3,C4,C5,C6,C7,C8,C9,C10,C11,C12,"
	                    + "D1,D2,D3,D4,D5,D6,D7,D8,D9,D10,D11,D12,E1,E2,E3,E4,E5,E6,E7,E8,E9,E10,E11,E12,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,"
	                    + "G1,G2,G3,G4,G5,G6,G7,G8,G9,G10,G11,G12,H1,H2,H3,H4,H5,H6,H7,H8,H9,H10,H11,H12";
	            String[] san = s.split(",");
	            
	            IntStream.range(0, containerSamples.size()).forEach(i ->{
	            	ContainerSamplesDTO foo = containerSamples.get(i);
	            	foo.setPosition(san[i]);
	            });
	            
	            
	            
	            String urlcs = localhost + AUTHENTICATED_JSON + OMM_CONTAINESAMPLES;
	            try {
	                System.out.println(
	                    "Entered Orders:: " +orderNumbers +" Order creation in OMM [CONTAINER SAMPLES]ready to start");
	                Ordercreation96Samples.postsamples(urlcs, containerSamples, token, null);
	                
	            } catch (Exception e) {
	                
	                System.out.println("error in creating" + orderNumbers + "samples creation IN CONTAINER TABLE");
	            }
	            System.out.println("Orders created successfully <<>>Please view OMM TABLE:ORDER & CONTAINER SAMPLE");
	           
	            
	        } catch (Exception e) {
	            System.out.println("Exception occured in Ordercreation96Samples Class");
	            e.printStackTrace();
	        }
	}
	
    
    public static OrderParentDTO getParentDTO() throws ParseException {
    	
    	OrderParentDTO orderParentDTO=new OrderParentDTO();
    	OrderDTO orderDTO=new OrderDTO();
    	orderDTO.setAssayType("NIPTDPCR");
    	orderDTO.setSampleType( "Plasma");
    	orderDTO.setRetestSample(true);
    	orderDTO.setOrderComments("All information contained herein is, and remains the property of COMPANY");
    	
    	
    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	Date date = dateFormat.parse("08/01/2019");
    	long timefirst = date.getTime();
    	AssayDTO assayDTO=new AssayDTO();
    	assayDTO.setMaternalAge(4);
    	assayDTO.setGestationalAgeWeeks(7);
    	assayDTO.setGestationalAgeDays(6);
    	assayDTO.setEggDonor("Self");
    	assayDTO.setEggDonorAge(13);
    	assayDTO.setIvfStatus("Yes");
    	assayDTO.setFetusNumber("2");
    	assayDTO.setCollectionDate(new Timestamp(timefirst));
    	assayDTO.setReceivedDate(new Timestamp(timefirst));
    	
    	Map<String,Boolean> testoptionsList=new HashMap<>();
    	testoptionsList.put("Harmony Prenatal Test (T21, T18, T13)", true);
    	testoptionsList.put("Fetal Sex",true);
    	
    	assayDTO.setTestOptions(testoptionsList);
    	
    	orderDTO.setAssay(assayDTO);
    	
    	
    	PatientDTO patientDTO=new PatientDTO();
    	
    	/*patientDTO.setPatientLastName("FLETCHER");
    	patientDTO.setPatientFirstName("DUNCAN");
    	patientDTO.setPatientGender("Female");
    	patientDTO.setPatientMedicalRecNo("21321312312ABC");
    	patientDTO.setPatientDOB("1973-02-05T18:30:00.000Z");
    	patientDTO.setPatientContactNo("2132434342");
    	patientDTO.setTreatingDoctorName("Alessy Perry");
    	patientDTO.setTreatingDoctorContactNo("+213232344242");
    	patientDTO.setRefClinicianName("Mc Lanning");
    	patientDTO.setRefClinicianFaxNo("+213232344242");
    	patientDTO.setOtherClinicianName("Diagnostics");
    	patientDTO.setOtherClinicianFaxNo("+213232344242");
    	patientDTO.setRefClinicianClinicName("Pharma");*/
    	
    	orderDTO.setPatient(patientDTO);
    	
    	
    	orderParentDTO.setOrder(orderDTO);
    	return orderParentDTO;
    	
    }
}
