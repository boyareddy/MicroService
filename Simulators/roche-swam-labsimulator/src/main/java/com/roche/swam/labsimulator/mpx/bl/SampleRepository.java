package com.roche.swam.labsimulator.mpx.bl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;


@Repository
public class SampleRepository {

    private Map<String, Sample> samples;

    public SampleRepository() {
        this.samples = new HashMap<>();
    }

    public Collection<Sample> getAllInState(final String instId, final EnumSampleStatus status) {
        ArrayList<Sample> col = new ArrayList<>();

        for (Sample s : samples.values()) {
            if (s.getStatus() == status) {
            	
            	
            
                /**
                 * A null indicates we want all samples in the specified state regardless
                 * of which instrument the sample is to or has been run on.  Otherwise only
                 * get the samples assigned to the specified instrument.
                 */
                if (instId != null && !instId.equalsIgnoreCase(s.getInstrumentId())) {
                    continue;
                }

                col.add(s);
            }
        }

        return col;


    }

    public Sample get(final String sampleId) {
        return this.samples.get(sampleId);
    }

    public void update(final Sample sample) {
    	/**future scope--sonar bugfix*/
    }

    public void add(final Sample sample) {
        this.samples.put(sample.getSampleId(), sample);
    }

    public Collection<Sample> getAll(final String instId) {
        ArrayList<Sample> col = new ArrayList<>();

        for (Sample s : samples.values()) {
            /**
             * A null indicates we want all samples regardless of which instrument the sample
             * is to or has been run on.  Otherwise only get the samples assigned to the 
             * specified instrument.
             */
            if (instId != null && !instId.equalsIgnoreCase(s.getInstrumentId())) {
                continue;
            }

            col.add(s);
        }

        return col;
    }
}
