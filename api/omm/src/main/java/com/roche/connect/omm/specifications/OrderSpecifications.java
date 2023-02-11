package com.roche.connect.omm.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.model.Patient;
import com.roche.connect.omm.model.PatientSamples;

public class OrderSpecifications {

	
	private OrderSpecifications() {
	}
	
	
	public static Specification<Order> getOrderResults(String status,String assayType, String createdDateTime,
			String updatedDateTime,String orderComments, String reqFieldMissingFlag,long domainId) {

		return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if(status!=null) {
				predicates.add(cb.equal(root.get("orderStatus"), "unassigned"));
			}
			if (assayType != null) {
				predicates.add(cb.equal(root.get("assayType"), assayType));
			}

			/*if (sampleType != null) {
				final Root<Patient> patientRoot = query.from(Patient.class);
				final Root<PatientSamples> patientSamplesRoot = patientRoot.from(PatientSamples.class);
//				predicates.add(cb.equal(root.get("sampleType"), sampleType));
				predicates.add(cb.equal(root.join("patient").in(arg0).get("patientSamples").get("sampleType"),sampleType));
			}
*/
			if (createdDateTime != null) {
				predicates.add(cb.equal(root.get("createdDateTime"), createdDateTime));
			}

			if (updatedDateTime != null) {
				predicates.add(cb.equal(root.get("updatedDateTime"), updatedDateTime));

			}

			if (orderComments != null) {
				predicates.add(cb.equal(root.get("orderComments"), orderComments));
			}

			if(reqFieldMissingFlag!=null) {
			predicates.add(cb.equal(root.get("reqFieldMissingFlag").as(Boolean.class), Boolean.parseBoolean(reqFieldMissingFlag)));
			}

			if(!(Long.valueOf(domainId)==null)) {
				predicates.add(cb.equal(root.get("company").get("id"), domainId));
			}			

			return cb.and(predicates.toArray(new Predicate[predicates.size()]));

		};
	}
}
