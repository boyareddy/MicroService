package com.roche.swam.labsimulator.mpx.bl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.roche.swam.labsimulator.util.Mp96SampleData;


@Repository
public class Mp96SampleRepository {

    private Map<String, Mp96SampleData> samples;

    public Mp96SampleRepository() {
        this.samples = new HashMap<>();
    }

    public Collection<Mp96SampleData> getAllInState(final String instId) {
        ArrayList<Mp96SampleData> col = new ArrayList<>();

        for (Mp96SampleData s : samples.values()) {
            	
            	
            
                /**
                 * A null indicates we want all samples in the specified state regardless
                 * of which instrument the sample is to or has been run on.  Otherwise only
                 * get the samples assigned to the specified instrument.
                 */
                if (instId != null && !instId.equalsIgnoreCase(s.getInstrumentSerialNo())) {
                    continue;
                }

                col.add(s);
        }

        return col;


    }

    public Mp96SampleData get(final String sampleId) {
        return this.samples.get(sampleId);
    }

    public void update(final Mp96SampleData sample) {
    	/**future scope--sonar bugfix*/
    }

    public void add(final Mp96SampleData sample) {
        this.samples.put(sample.getAccessioningId(), sample);
    }

    public Collection<Mp96SampleData> getAll(final String instId) {
        ArrayList<Mp96SampleData> col = new ArrayList<>();

        for (Mp96SampleData s : samples.values()) {
            /**
             * A null indicates we want all samples regardless of which instrument the sample
             * is to or has been run on.  Otherwise only get the samples assigned to the 
             * specified instrument.
             */
            if (instId != null && !instId.equalsIgnoreCase(s.getInstrumentSerialNo())) {
                continue;
            }

            col.add(s);
        }

        return col;
    }
}
