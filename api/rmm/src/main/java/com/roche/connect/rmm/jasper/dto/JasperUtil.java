package com.roche.connect.rmm.jasper.dto;

import java.util.List;

import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.rmm.jasper.dto.LpplateBasedSamples;
import com.roche.connect.rmm.jasper.dto.SampleDTO;

public class JasperUtil {
	
	
	private JasperUtil() {
	}
	private static  HMTPLogger logger = HMTPLoggerFactory.getLogger(JasperUtil.class.getName());
	public static LpplateBasedSamples getLpPlateBasedSamples(List<SampleDTO> dtos){
		LpplateBasedSamples lpPlateBasedSamples=new LpplateBasedSamples();
		try{
		Object[] obj=dtos.toArray();
		
	int i=	obj.length;
	 switch (i) { 
	 
     case 1: 
    	 LpplateBasedSamples basedSamples1=new LpplateBasedSamples();
    	 basedSamples1.setPlateId1(((SampleDTO) obj[0]).getPlateId());
    	 basedSamples1.setPlateId1List(((SampleDTO) obj[0]).getListOfSampleDataDTO());
    	 lpPlateBasedSamples=basedSamples1;
         break; 
     case 2: 
    	 LpplateBasedSamples basedSamples2=new LpplateBasedSamples();
    	 basedSamples2.setPlateId1(((SampleDTO) obj[0]).getPlateId());
    	 basedSamples2.setPlateId1List(((SampleDTO) obj[0]).getListOfSampleDataDTO());
    	 basedSamples2.setPlateId2(((SampleDTO) obj[1]).getPlateId());
    	 basedSamples2.setPlateId2List(((SampleDTO) obj[1]).getListOfSampleDataDTO());
    	 lpPlateBasedSamples=basedSamples2;
         break; 
     case 3: 
    	 LpplateBasedSamples basedSamples3=new LpplateBasedSamples();
    	 basedSamples3.setPlateId1(((SampleDTO) obj[0]).getPlateId());
    	 basedSamples3.setPlateId1List(((SampleDTO) obj[0]).getListOfSampleDataDTO());
    	 basedSamples3.setPlateId2(((SampleDTO) obj[1]).getPlateId());
    	 basedSamples3.setPlateId2List(((SampleDTO) obj[1]).getListOfSampleDataDTO());
    	 basedSamples3.setPlateId3(((SampleDTO) obj[2]).getPlateId());
    	 basedSamples3.setPlateId3List(((SampleDTO) obj[2]).getListOfSampleDataDTO());
    	 lpPlateBasedSamples=basedSamples3;
         break; 
     case 4: 
    	 LpplateBasedSamples basedSamples4=new LpplateBasedSamples();
    	 basedSamples4.setPlateId1(((SampleDTO) obj[0]).getPlateId());
    	 basedSamples4.setPlateId1List(((SampleDTO) obj[0]).getListOfSampleDataDTO());
    	 basedSamples4.setPlateId2(((SampleDTO) obj[1]).getPlateId());
    	 basedSamples4.setPlateId2List(((SampleDTO) obj[1]).getListOfSampleDataDTO());
    	 basedSamples4.setPlateId3(((SampleDTO) obj[2]).getPlateId());
    	 basedSamples4.setPlateId3List(((SampleDTO) obj[2]).getListOfSampleDataDTO());
    	 basedSamples4.setPlateId4(((SampleDTO) obj[3]).getPlateId());
    	 basedSamples4.setPlateId4List(((SampleDTO) obj[3]).getListOfSampleDataDTO());
    	 lpPlateBasedSamples=basedSamples4;
         break; 
     case 5: 
    	 LpplateBasedSamples basedSamples5=new LpplateBasedSamples();
    	 basedSamples5.setPlateId1(((SampleDTO) obj[0]).getPlateId());
    	 basedSamples5.setPlateId1List(((SampleDTO) obj[0]).getListOfSampleDataDTO());
    	 basedSamples5.setPlateId2(((SampleDTO) obj[1]).getPlateId());
    	 basedSamples5.setPlateId2List(((SampleDTO) obj[1]).getListOfSampleDataDTO());
    	 basedSamples5.setPlateId3(((SampleDTO) obj[2]).getPlateId());
    	 basedSamples5.setPlateId3List(((SampleDTO) obj[2]).getListOfSampleDataDTO());
    	 basedSamples5.setPlateId4(((SampleDTO) obj[3]).getPlateId());
    	 basedSamples5.setPlateId4List(((SampleDTO) obj[3]).getListOfSampleDataDTO());
    	 basedSamples5.setPlateId5(((SampleDTO) obj[4]).getPlateId());
    	 basedSamples5.setPlateId5List(((SampleDTO) obj[4]).getListOfSampleDataDTO());
    	 lpPlateBasedSamples=basedSamples5;
         break; 
     case 6: 
    	 LpplateBasedSamples basedSamples6=new LpplateBasedSamples();
     	 basedSamples6.setPlateId1(((SampleDTO) obj[0]).getPlateId());
    	 basedSamples6.setPlateId1List(((SampleDTO) obj[0]).getListOfSampleDataDTO());
    	 basedSamples6.setPlateId2(((SampleDTO) obj[1]).getPlateId());
    	 basedSamples6.setPlateId2List(((SampleDTO) obj[1]).getListOfSampleDataDTO());
    	 basedSamples6.setPlateId3(((SampleDTO) obj[2]).getPlateId());
    	 basedSamples6.setPlateId3List(((SampleDTO) obj[2]).getListOfSampleDataDTO());
    	 basedSamples6.setPlateId4(((SampleDTO) obj[3]).getPlateId());
    	 basedSamples6.setPlateId4List(((SampleDTO) obj[3]).getListOfSampleDataDTO());
    	 basedSamples6.setPlateId5(((SampleDTO) obj[4]).getPlateId());
    	 basedSamples6.setPlateId5List(((SampleDTO) obj[4]).getListOfSampleDataDTO());
    	 basedSamples6.setPlateId6(((SampleDTO) obj[5]).getPlateId());
    	 basedSamples6.setPlateId6List(((SampleDTO) obj[5]).getListOfSampleDataDTO());
    	 lpPlateBasedSamples=basedSamples6;
         break; 
     case 7: 
    	 LpplateBasedSamples basedSamples7=new LpplateBasedSamples();
    	 basedSamples7.setPlateId1(((SampleDTO) obj[0]).getPlateId());
    	 basedSamples7.setPlateId1List(((SampleDTO) obj[0]).getListOfSampleDataDTO());
    	 basedSamples7.setPlateId2(((SampleDTO) obj[1]).getPlateId());
    	 basedSamples7.setPlateId2List(((SampleDTO) obj[1]).getListOfSampleDataDTO());
    	 basedSamples7.setPlateId3(((SampleDTO) obj[2]).getPlateId());
    	 basedSamples7.setPlateId3List(((SampleDTO) obj[2]).getListOfSampleDataDTO());
    	 basedSamples7.setPlateId4(((SampleDTO) obj[3]).getPlateId());
    	 basedSamples7.setPlateId4List(((SampleDTO) obj[3]).getListOfSampleDataDTO());
    	 basedSamples7.setPlateId5(((SampleDTO) obj[4]).getPlateId());
    	 basedSamples7.setPlateId5List(((SampleDTO) obj[4]).getListOfSampleDataDTO());
    	 basedSamples7.setPlateId6(((SampleDTO) obj[5]).getPlateId());
    	 basedSamples7.setPlateId6List(((SampleDTO) obj[5]).getListOfSampleDataDTO());
    	 basedSamples7.setPlateId7(((SampleDTO) obj[6]).getPlateId());
    	 basedSamples7.setPlateId7List(((SampleDTO) obj[6]).getListOfSampleDataDTO());
    	 lpPlateBasedSamples=basedSamples7;
         break; 
         
     case 8: 
    	 LpplateBasedSamples basedSamples8=new LpplateBasedSamples();
    	 basedSamples8.setPlateId1(((SampleDTO) obj[0]).getPlateId());
    	 basedSamples8.setPlateId1List(((SampleDTO) obj[0]).getListOfSampleDataDTO());
    	 basedSamples8.setPlateId2(((SampleDTO) obj[1]).getPlateId());
    	 basedSamples8.setPlateId2List(((SampleDTO) obj[1]).getListOfSampleDataDTO());
    	 basedSamples8.setPlateId3(((SampleDTO) obj[2]).getPlateId());
    	 basedSamples8.setPlateId3List(((SampleDTO) obj[2]).getListOfSampleDataDTO());
    	 basedSamples8.setPlateId4(((SampleDTO) obj[3]).getPlateId());
    	 basedSamples8.setPlateId4List(((SampleDTO) obj[3]).getListOfSampleDataDTO());
    	 basedSamples8.setPlateId5(((SampleDTO) obj[4]).getPlateId());
    	 basedSamples8.setPlateId5List(((SampleDTO) obj[4]).getListOfSampleDataDTO());
    	 basedSamples8.setPlateId6(((SampleDTO) obj[5]).getPlateId());
    	 basedSamples8.setPlateId6List(((SampleDTO) obj[5]).getListOfSampleDataDTO());
    	 basedSamples8.setPlateId7(((SampleDTO) obj[6]).getPlateId());
    	 basedSamples8.setPlateId7List(((SampleDTO) obj[6]).getListOfSampleDataDTO());
    	 basedSamples8.setPlateId8(((SampleDTO) obj[7]).getPlateId());
    	 basedSamples8.setPlateId8List(((SampleDTO) obj[7]).getListOfSampleDataDTO());
    	 lpPlateBasedSamples=basedSamples8;
         break; 
     case 9: 
    	 LpplateBasedSamples basedSamples9=new LpplateBasedSamples();

		 basedSamples9.setPlateId1(((SampleDTO) obj[0]).getPlateId());
    	 basedSamples9.setPlateId1List(((SampleDTO) obj[0]).getListOfSampleDataDTO());
    	 basedSamples9.setPlateId2(((SampleDTO) obj[1]).getPlateId());
    	 basedSamples9.setPlateId2List(((SampleDTO) obj[1]).getListOfSampleDataDTO());
    	 basedSamples9.setPlateId3(((SampleDTO) obj[2]).getPlateId());
    	 basedSamples9.setPlateId3List(((SampleDTO) obj[2]).getListOfSampleDataDTO());
    	 basedSamples9.setPlateId4(((SampleDTO) obj[3]).getPlateId());
    	 basedSamples9.setPlateId4List(((SampleDTO) obj[3]).getListOfSampleDataDTO());
    	 basedSamples9.setPlateId5(((SampleDTO) obj[4]).getPlateId());
    	 basedSamples9.setPlateId5List(((SampleDTO) obj[4]).getListOfSampleDataDTO());
    	 basedSamples9.setPlateId6(((SampleDTO) obj[5]).getPlateId());
    	 basedSamples9.setPlateId6List(((SampleDTO) obj[5]).getListOfSampleDataDTO());
    	 basedSamples9.setPlateId7(((SampleDTO) obj[6]).getPlateId());
    	 basedSamples9.setPlateId7List(((SampleDTO) obj[6]).getListOfSampleDataDTO());
    	 basedSamples9.setPlateId8(((SampleDTO) obj[7]).getPlateId());
    	 basedSamples9.setPlateId8List(((SampleDTO) obj[7]).getListOfSampleDataDTO());
    	 basedSamples9.setPlateId9(((SampleDTO) obj[8]).getPlateId());
    	 basedSamples9.setPlateId9List(((SampleDTO) obj[8]).getListOfSampleDataDTO());
    	 lpPlateBasedSamples=basedSamples9;
         break; 
     case 10: 
    	 LpplateBasedSamples basedSamples10=new LpplateBasedSamples();
    	 basedSamples10.setPlateId1(((SampleDTO) obj[0]).getPlateId());
    	 basedSamples10.setPlateId1List(((SampleDTO) obj[0]).getListOfSampleDataDTO());
    	 basedSamples10.setPlateId2(((SampleDTO) obj[1]).getPlateId());
    	 basedSamples10.setPlateId2List(((SampleDTO) obj[1]).getListOfSampleDataDTO());
    	 basedSamples10.setPlateId3(((SampleDTO) obj[2]).getPlateId());
    	 basedSamples10.setPlateId3List(((SampleDTO) obj[2]).getListOfSampleDataDTO());
    	 basedSamples10.setPlateId4(((SampleDTO) obj[3]).getPlateId());
    	 basedSamples10.setPlateId4List(((SampleDTO) obj[3]).getListOfSampleDataDTO());
    	 basedSamples10.setPlateId5(((SampleDTO) obj[4]).getPlateId());
    	 basedSamples10.setPlateId5List(((SampleDTO) obj[4]).getListOfSampleDataDTO());
    	 basedSamples10.setPlateId6(((SampleDTO) obj[5]).getPlateId());
    	 basedSamples10.setPlateId6List(((SampleDTO) obj[5]).getListOfSampleDataDTO());
    	 basedSamples10.setPlateId7(((SampleDTO) obj[6]).getPlateId());
    	 basedSamples10.setPlateId7List(((SampleDTO) obj[6]).getListOfSampleDataDTO());
    	 basedSamples10.setPlateId8(((SampleDTO) obj[7]).getPlateId());
    	 basedSamples10.setPlateId8List(((SampleDTO) obj[7]).getListOfSampleDataDTO());
    	 basedSamples10.setPlateId9(((SampleDTO) obj[8]).getPlateId());
    	 basedSamples10.setPlateId9List(((SampleDTO) obj[8]).getListOfSampleDataDTO());
    	 basedSamples10.setPlateId10(((SampleDTO) obj[9]).getPlateId());
    	 basedSamples10.setPlateId10List(((SampleDTO) obj[9]).getListOfSampleDataDTO());
    	 lpPlateBasedSamples=basedSamples10;
         break; 
     case 11: 
    	 LpplateBasedSamples basedSamples11=new LpplateBasedSamples();
    	 basedSamples11.setPlateId1(((SampleDTO) obj[0]).getPlateId());
    	 basedSamples11.setPlateId1List(((SampleDTO) obj[0]).getListOfSampleDataDTO());
    	 basedSamples11.setPlateId2(((SampleDTO) obj[1]).getPlateId());
    	 basedSamples11.setPlateId2List(((SampleDTO) obj[1]).getListOfSampleDataDTO());
    	 basedSamples11.setPlateId3(((SampleDTO) obj[2]).getPlateId());
    	 basedSamples11.setPlateId3List(((SampleDTO) obj[2]).getListOfSampleDataDTO());
    	 basedSamples11.setPlateId4(((SampleDTO) obj[3]).getPlateId());
    	 basedSamples11.setPlateId4List(((SampleDTO) obj[3]).getListOfSampleDataDTO());
    	 basedSamples11.setPlateId5(((SampleDTO) obj[4]).getPlateId());
    	 basedSamples11.setPlateId5List(((SampleDTO) obj[4]).getListOfSampleDataDTO());
    	 basedSamples11.setPlateId6(((SampleDTO) obj[5]).getPlateId());
    	 basedSamples11.setPlateId6List(((SampleDTO) obj[5]).getListOfSampleDataDTO());
    	 basedSamples11.setPlateId7(((SampleDTO) obj[6]).getPlateId());
    	 basedSamples11.setPlateId7List(((SampleDTO) obj[6]).getListOfSampleDataDTO());
    	 basedSamples11.setPlateId8(((SampleDTO) obj[7]).getPlateId());
    	 basedSamples11.setPlateId8List(((SampleDTO) obj[7]).getListOfSampleDataDTO());
    	 basedSamples11.setPlateId9(((SampleDTO) obj[8]).getPlateId());
    	 basedSamples11.setPlateId9List(((SampleDTO) obj[8]).getListOfSampleDataDTO());
    	 basedSamples11.setPlateId10(((SampleDTO) obj[9]).getPlateId());
    	 basedSamples11.setPlateId10List(((SampleDTO) obj[9]).getListOfSampleDataDTO());
    	 basedSamples11.setPlateId11(((SampleDTO) obj[10]).getPlateId());
    	 basedSamples11.setPlateId11List(((SampleDTO) obj[10]).getListOfSampleDataDTO());
    	 lpPlateBasedSamples=basedSamples11;
         break; 
     case 12: 
    	 LpplateBasedSamples basedSamples12=new LpplateBasedSamples();
    	 basedSamples12.setPlateId1(((SampleDTO) obj[0]).getPlateId());
    	 basedSamples12.setPlateId1List(((SampleDTO) obj[0]).getListOfSampleDataDTO());
    	 basedSamples12.setPlateId2(((SampleDTO) obj[1]).getPlateId());
    	 basedSamples12.setPlateId2List(((SampleDTO) obj[1]).getListOfSampleDataDTO());
    	 basedSamples12.setPlateId3(((SampleDTO) obj[2]).getPlateId());
    	 basedSamples12.setPlateId3List(((SampleDTO) obj[2]).getListOfSampleDataDTO());
    	 basedSamples12.setPlateId4(((SampleDTO) obj[3]).getPlateId());
    	 basedSamples12.setPlateId4List(((SampleDTO) obj[3]).getListOfSampleDataDTO());
    	 basedSamples12.setPlateId5(((SampleDTO) obj[4]).getPlateId());
    	 basedSamples12.setPlateId5List(((SampleDTO) obj[4]).getListOfSampleDataDTO());
    	 basedSamples12.setPlateId6(((SampleDTO) obj[5]).getPlateId());
    	 basedSamples12.setPlateId6List(((SampleDTO) obj[5]).getListOfSampleDataDTO());
    	 basedSamples12.setPlateId7(((SampleDTO) obj[6]).getPlateId());
    	 basedSamples12.setPlateId7List(((SampleDTO) obj[6]).getListOfSampleDataDTO());
    	 basedSamples12.setPlateId8(((SampleDTO) obj[7]).getPlateId());
    	 basedSamples12.setPlateId8List(((SampleDTO) obj[7]).getListOfSampleDataDTO());
    	 basedSamples12.setPlateId9(((SampleDTO) obj[8]).getPlateId());
    	 basedSamples12.setPlateId9List(((SampleDTO) obj[8]).getListOfSampleDataDTO());
    	 basedSamples12.setPlateId10(((SampleDTO) obj[9]).getPlateId());
    	 basedSamples12.setPlateId10List(((SampleDTO) obj[9]).getListOfSampleDataDTO());
    	 basedSamples12.setPlateId11(((SampleDTO) obj[10]).getPlateId());
    	 basedSamples12.setPlateId11List(((SampleDTO) obj[10]).getListOfSampleDataDTO());
    	 basedSamples12.setPlateId12(((SampleDTO) obj[11]).getPlateId());
    	 basedSamples12.setPlateId12List(((SampleDTO) obj[11]).getListOfSampleDataDTO());
    	 lpPlateBasedSamples=basedSamples12;
         break; 
     default: 
         
         break; 
     } }
		catch(Exception e){
			logger.error("getLpPlateBasedSamples()"+e.getMessage());
		}
     return lpPlateBasedSamples;
 } 
		
}
