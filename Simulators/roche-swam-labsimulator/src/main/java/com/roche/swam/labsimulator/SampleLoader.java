package com.roche.swam.labsimulator;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.swam.labsimulator.mpx.bl.EnumSampleStatus;
import com.roche.swam.labsimulator.mpx.bl.Sample;
import com.roche.swam.labsimulator.mpx.bl.SampleRepository;
import com.roche.swam.labsimulator.util.SamplesListBean;

public class SampleLoader {

	@Autowired
	private static final Logger LOG = LoggerFactory.getLogger(SampleLoader.class);
	private SampleRepository samplesRepo;

	public SampleRepository getSamplesRepo() {
		return samplesRepo;
	}

	public void setSamplesRepo(SampleRepository samplesRepo) {
		this.samplesRepo = samplesRepo;
	}

	public static void main(String arg[]) {
		SampleLoader sl = new SampleLoader();
		sl.jsonToSamples();
	}

	public SampleRepository jsonToSamples() {
		
		this.samplesRepo = new SampleRepository();
		ObjectMapper mapper = new ObjectMapper();
		SamplesListBean samplesListBean = new SamplesListBean();
		try {
			samplesListBean=mapper.readValue(new File(MainApp.getFilepath(MainApp.getDeviceName())), SamplesListBean.class);
			for(Sample sample:samplesListBean.getSamples())
			{
				sample.setStatus(EnumSampleStatus.QUERYING);
				this.samplesRepo.add(sample);
			}
		} catch (IOException e) {
			
		}
		return this.samplesRepo;
	}
		
		/*JSONArray sampleArray = new JSONArray();
		JSONParser parser = new JSONParser();
		Object object = null;
		JSONObject samples = null, sampleFileObj = null;
		String serial;

		try (FileReader fileReader = new FileReader("C:\\SampleFile\\samples.json")) {

			object = parser.parse(fileReader);
			samples = (JSONObject) object;
			sampleArray = (JSONArray) samples.get("samples");
			for (int i = 0; i < sampleArray.size(); i++) {
				sampleFileObj = (JSONObject) sampleArray.get(i);
				Sample sample = new Sample();
				sample.setOrderId(sampleFileObj.get("orderId").toString());
				sample.setStatus(EnumSampleStatus.QUERYING);
				sample.setSampleId(sampleFileObj.get("sampleId").toString());
				serial = sampleFileObj.get("instrumentId").toString();
				sample.setInstrumentId(serial);
				sample.setOutputContainerId(sampleFileObj.get("outputContainerId").toString());
				sample.setOutputPosition(Integer.parseInt(sampleFileObj.get("outputPosition").toString()));
				sample.setSendingFacilityName(sampleFileObj.get("sendingFacilityName").toString());
				sample.setReceivingFacilityName(sampleFileObj.get("receivingFacilityName").toString());
				sample.setMessageControlId(sampleFileObj.get("messageControlId").toString());
				sample.setSendingApplicationName(sampleFileObj.get("sendingApplicationName").toString());
				sample.setReceivingApplicationName(sampleFileObj.get("receivingApplicationName").toString());
				sample.setProcessingId(sampleFileObj.get("ProcessingId").toString());
				sample.setVersionId(sampleFileObj.get("VersionId").toString());
				sample.setSampleType(sampleFileObj.get("sampleType").toString());
				sample.setCharacterSet(sampleFileObj.get("CharacterSet").toString());
				sample.setMessageQueryName(sampleFileObj.get("messageQueryName").toString());
				sample.setProtocolType(sampleFileObj.get("protocolType").toString());
				this.samplesRepo.add(sample);

			}
		} catch (IOException e) {
			LOG.error(e.getMessage());
		} catch (ParseException e1) {
			LOG.error(e1.getMessage());
		}
		Collection<Sample> samples1 = samplesRepo.getAllInState(null, EnumSampleStatus.QUERYING);
		return this.samplesRepo;
	}*/

}
